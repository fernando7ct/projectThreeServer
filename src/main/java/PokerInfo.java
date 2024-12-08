// PokerInfo.java
import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // Define possible actions that clients can perform
    public enum Action {
        CONNECT,
        PLACE_BET,
        DEAL,
        PLAY,
        FOLD,
        EXIT,
        UPDATE,
        RESULTS,
        FRESH_START,
        NEW_GAME
    }

    private Action action;
    private String message;
    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private int playerBalance;
    private int playerAnte;
    private int playerPairPlus;
    private int playerPlay;
    private boolean dealerCardsRevealed;
    private int anteBetRequest;
    private int pairPlusBetRequest;
    private int roundWinnings;

    // Getters and Setters
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(ArrayList<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(int playerBalance) {
        this.playerBalance = playerBalance;
    }

    public int getPlayerAnte() {
        return playerAnte;
    }

    public void setPlayerAnte(int playerAnte) {
        this.playerAnte = playerAnte;
    }

    public int getPlayerPairPlus() {
        return playerPairPlus;
    }

    public void setPlayerPairPlus(int playerPairPlus) {
        this.playerPairPlus = playerPairPlus;
    }

    public int getPlayerPlay() {
        return playerPlay;
    }

    public void setPlayerPlay(int playerPlay) {
        this.playerPlay = playerPlay;
    }

    public boolean isDealerCardsRevealed() {
        return dealerCardsRevealed;
    }

    public void setDealerCardsRevealed(boolean dealerCardsRevealed) {
        this.dealerCardsRevealed = dealerCardsRevealed;
    }

    public int getAnteBetRequest() {
        return anteBetRequest;
    }

    public void setAnteBetRequest(int anteBetRequest) {
        this.anteBetRequest = anteBetRequest;
    }

    public int getPairPlusBetRequest() {
        return pairPlusBetRequest;
    }

    public void setPairPlusBetRequest(int pairPlusBetRequest) {
        this.pairPlusBetRequest = pairPlusBetRequest;
    }

    public int getRoundWinnings() {
        return roundWinnings;
    }

    public void setRoundWinnings(int roundWinnings) {
        this.roundWinnings = roundWinnings;
    }
}