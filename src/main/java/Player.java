// Player.java
import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    int anteBet;
    int playBet;
    int pairPlusBet;
    int totalWinnings;

    // No-argument constructor initializes player state
    public Player() {
        this.hand = new ArrayList<>();
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.totalWinnings = 100; // Starting balance
    }

    private boolean anteAlreadyDeducted = false;

    public boolean isAnteAlreadyDeducted() {
        return anteAlreadyDeducted;
    }

    public void setAnteAlreadyDeducted(boolean anteAlreadyDeducted) {
        this.anteAlreadyDeducted = anteAlreadyDeducted;
    }

    // Getter and setter for anteBet
    public int getAnteBet() {
        return anteBet;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    // Getter and setter for playBet
    public int getPlayBet() {
        return playBet;
    }

    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    // Getter and setter for pairPlusBet
    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    // Getter and setter for totalWinnings
    public int getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(int totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    // Getter for hand
    public ArrayList<Card> getHand() {
        return hand;
    }

    // Method to reset bets and optionally total winnings for a new round
    public void resetBets() {
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.anteAlreadyDeducted = false;
    }

    // Method to reset only Pair Plus bet
    public void resetPairBets() {
        this.pairPlusBet = 0;
    }
}