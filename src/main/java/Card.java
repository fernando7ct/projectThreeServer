// Card.java
public class Card implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    char suit;
    int value;

    // Constructor to initialize card suit and value
    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // Getter for suit
    public char getSuit() {
        return suit;
    }

    // Getter for value
    public int getValue() {
        return value;
    }
}