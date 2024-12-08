// Deck.java
import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {

    // Constructor initializes a new deck
    public Deck() {
        newDeck();
    }

    // Create a new shuffled deck of 52 cards
    public void newDeck() {
        this.clear();
        char[] suits = {'C', 'D', 'S', 'H'}; // Clubs, Diamonds, Spades, Hearts
        for (char suit : suits) {
            for (int value = 2; value <= 14; value++) { // 2-10, J=11, Q=12, K=13, A=14
                this.add(new Card(suit, value));
            }
        }
        Collections.shuffle(this); // Shuffle the deck
    }
}