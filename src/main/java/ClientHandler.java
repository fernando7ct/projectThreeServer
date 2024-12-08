// ClientHandler.java
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private Player player;
    private Dealer dealer;
    private boolean active = true;

    // Constructor to initialize ClientHandler with socket and server reference
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.player = new Player();
        this.dealer = new Dealer();
    }

    @Override
    public void run() {
        boolean joined = false;
        try {
            // Initialize output stream first to prevent blocking
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            sendMessage("Connected. Place your bets.");
            server.addEvent("Client joined: " + socket.getRemoteSocketAddress());
            joined = true;
            PokerInfo request;
            // Continuously listen for client requests
            while (active && (request = (PokerInfo) in.readObject()) != null) {
                handleRequest(request);
            }

        } catch (Exception e) {
            // Client disconnected or error occurred
        } finally {
            closeConnection();
            server.removeClient(this);
            if (joined) {
                server.addEvent("Client left: " + socket.getRemoteSocketAddress());
            }
        }
    }

    // Handle different types of client requests based on action
    private void handleRequest(PokerInfo request) {
        switch (request.getAction()) {
            case CONNECT:
                // Already handled in run()
                break;
            case PLACE_BET:
                handlePlaceBet(request);
                break;
            case DEAL:
                handleDeal();
                break;
            case PLAY:
                handlePlay();
                break;
            case FOLD:
                handleFold();
                break;
            case EXIT:
                active = false;
                break;
            case FRESH_START:
                player.resetBets();
                player.totalWinnings = 100; // Reset to initial balance, adjust if needed
                sendMessage("Fresh start! Place your bets.");
                server.addEvent("Client " + socket.getRemoteSocketAddress() + " did a fresh start.");
                break;
            case NEW_GAME:
                // Start a new game round: reset bets and prompt for new bets
                player.resetBets();
                sendMessage("New game started. Place your bets.");
                server.addEvent("Client " + socket.getRemoteSocketAddress() + " started a new game.");
                sendMessage("Balance: $" + player.getTotalWinnings());
                break;
            default:
                sendUpdate("Unknown action.");
        }
    }

    // Handle bet placement from client
    private void handlePlaceBet(PokerInfo request) {
        int ante = request.getAnteBetRequest();
        int pp = request.getPairPlusBetRequest();

        // Validate Ante bet range
        if (ante > 0 && (ante < 5 || ante > 25)) {
            sendUpdate("Ante must be between $5 and $25.");
            return;
        }

        // Validate Pair Plus bet range
        if (pp > 0 && (pp < 5 || pp > 25)) {
            sendUpdate("Pair Plus must be between $5 and $25.");
            return;
        }

        int totalBet = ante + pp;
        // Check if player has enough balance
        if (player.getTotalWinnings() < totalBet) {
            sendUpdate("Not enough balance for bets.");
            return;
        }

        // Deduct bets from player's total winnings
        player.setAnteBet(ante);
        player.setPairPlusBet(pp);
        player.setTotalWinnings(player.getTotalWinnings() - totalBet);

        sendMessage("Bet placed. Click DEAL to continue.");
        server.addEvent("Client " + socket.getRemoteSocketAddress() + " placed bets: Ante = $" + ante + ", Pair Plus = $" + pp);
    }

    // Handle DEAL action: deal cards to player and dealer
    private void handleDeal() {
        player.hand = dealer.dealHand();
        dealer.dealersHand = dealer.dealHand();
        sendUpdateInfo("Cards dealt. Choose to Play or Fold.");
        server.addEvent("Client " + socket.getRemoteSocketAddress() + " was dealt cards.");
    }

    // Handle PLAY action: proceed with the game
    private void handlePlay() {
        if (player.getAnteBet() == 0) {
            sendUpdate("No Ante bet, cannot play.");
            return;
        }

        int playBet = player.getAnteBet();
        if (player.getTotalWinnings() < playBet) {
            sendUpdate("Not enough balance for play bet.");
            return;
        }

        // Deduct play bet from player's total winnings
        player.setPlayBet(playBet);
        player.setTotalWinnings(player.getTotalWinnings() - playBet);
        server.addEvent("Client " + socket.getRemoteSocketAddress() + " chose to Play with Bet = $" + playBet);
        evaluateHandsAndSendResults();
    }

    // Handle FOLD action: player decides to fold
    private void handleFold() {
        player.resetBets();
        sendRoundResults("You folded.", 0);
        server.addEvent("Client " + socket.getRemoteSocketAddress() + " folded.");
    }

    // Evaluate hands and determine game results
    private void evaluateHandsAndSendResults() {
        boolean dealerQualifies = dealerQualifies(dealer.dealersHand);
        int pairPlusWinnings = 0;
        if (player.getPairPlusBet() > 0) {
            pairPlusWinnings = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlusBet());
            player.setTotalWinnings(player.getTotalWinnings() + pairPlusWinnings);
        }

        String resultMessage;
        int roundWinnings = 0;

        if (!dealerQualifies) {
            // Dealer does not qualify: Ante and Play bets are returned
            player.setTotalWinnings(player.getTotalWinnings() + player.getPlayBet() + player.getAnteBet());
            resultMessage = "Dealer does not qualify. Ante and Play returned.";
            roundWinnings = player.getPlayBet() + player.getAnteBet() + pairPlusWinnings
                    - (player.getAnteBet() + player.getPairPlusBet() + player.getPlayBet());
        } else {
            int outcome = ThreeCardLogic.compareHands(dealer.dealersHand, player.getHand());
            if (outcome == 2) { // Player wins
                int winnings = (player.getAnteBet() + player.getPlayBet()) * 2;
                player.setTotalWinnings(player.getTotalWinnings() + winnings);
                resultMessage = "You beat the dealer!";
                roundWinnings = winnings + pairPlusWinnings
                        - (player.getAnteBet() + player.getPairPlusBet() + player.getPlayBet());
            } else if (outcome == 1) { // Dealer wins
                resultMessage = "Dealer wins. You lose Ante and Play.";
                roundWinnings = pairPlusWinnings
                        - (player.getAnteBet() + player.getPairPlusBet() + player.getPlayBet());
            } else {
                // Tie: Ante and Play bets are returned
                player.setTotalWinnings(player.getTotalWinnings() + player.getAnteBet() + player.getPlayBet());
                resultMessage = "It's a tie. Ante and Play returned.";
                roundWinnings = (player.getAnteBet() + player.getPlayBet()) + pairPlusWinnings
                        - (player.getAnteBet() + player.getPairPlusBet() + player.getPlayBet());
            }
        }

        // Reset bets for the next round
        player.resetBets();
        sendRoundResults(resultMessage, roundWinnings);

        // Determine the outcome message for server events
        String endResult;
        if (roundWinnings > 0) {
            endResult = "won $" + roundWinnings;
            player.setTotalWinnings(player.totalWinnings + roundWinnings);
        } else if (roundWinnings < 0) {
            endResult = "lost $" + Math.abs(roundWinnings);
            player.setTotalWinnings(player.totalWinnings - roundWinnings);
        } else {
            endResult = "neither lost nor won anything";
        }

        server.addEvent("Client " + socket.getRemoteSocketAddress() + "'s game ended, they " + endResult + ".");
    }

    // Check if dealer qualifies based on their hand
    private boolean dealerQualifies(ArrayList<Card> hand) {
        for (Card card : hand) {
            if (card.getValue() >= 12) { // Queen or higher
                return true;
            }
        }
        return false;
    }

    // Send round results to the client
    private void sendRoundResults(String msg, int roundWinnings) {
        PokerInfo info = new PokerInfo();
        info.setAction(PokerInfo.Action.RESULTS);
        info.setMessage(msg);
        info.setRoundWinnings(roundWinnings);
        info.setPlayerBalance(player.getTotalWinnings());
        info.setDealerHand(dealer.dealersHand);
        sendInfo(info);
    }

    // Send a general message to the client
    private void sendMessage(String message) {
        PokerInfo info = new PokerInfo();
        info.setAction(PokerInfo.Action.UPDATE);
        info.setMessage(message);
        info.setPlayerBalance(player.getTotalWinnings());
        info.setPlayerAnte(player.getAnteBet());
        info.setPlayerPairPlus(player.getPairPlusBet());
        info.setPlayerPlay(player.getPlayBet());
        info.setPlayerHand(player.getHand());
        sendInfo(info);
    }

    // Helper method to send update messages
    private void sendUpdate(String msg) {
        sendMessage(msg);
    }

    // Send update information, hiding dealer's cards
    private void sendUpdateInfo(String msg) {
        PokerInfo info = new PokerInfo();
        info.setAction(PokerInfo.Action.UPDATE);
        info.setMessage(msg);
        info.setPlayerBalance(player.getTotalWinnings());
        info.setPlayerHand(player.getHand());
        info.setDealerHand(new ArrayList<>()); // Empty dealer hand
        info.setDealerCardsRevealed(false); // Explicitly hide dealer's cards
        sendInfo(info);
    }

    // Serialize and send PokerInfo object to the client
    private void sendInfo(PokerInfo info) {
        try {
            out.writeObject(info);
            out.flush();
        } catch (IOException e) {
            active = false; // Terminate connection on failure
        }
    }

    // Close client connection gracefully
    public void closeConnection() {
        active = false;
        try {
            socket.close();
        } catch (Exception e) {}
    }
}