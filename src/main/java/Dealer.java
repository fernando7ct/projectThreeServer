// Dealer.java
import java.util.ArrayList;

public class Dealer {
    Deck theDeck;
    ArrayList<Card> dealersHand;

    // Constructor initializes a new deck and dealer's hand
    public Dealer() {
        theDeck = new Deck();
        dealersHand = new ArrayList<>();
    }

    // Deal a hand of three cards to the dealer
    public ArrayList<Card> dealHand() {
        // Reshuffle only if less than 35 cards are available
        if (theDeck.size() < 35) {
            theDeck.newDeck();
        }
        dealersHand.clear();
        for (int i = 0; i < 3; i++) {
            dealersHand.add(theDeck.remove(0));
        }
        return new ArrayList<>(dealersHand); // Return a copy to prevent external modification
    }
}