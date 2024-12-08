// ThreeCardLogic.java
import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {

    // Evaluate the hand and return its type as an integer
    public static int evalHand(ArrayList<Card> hand) {
        // Sort the hand by value for easier evaluation
        Collections.sort(hand, (c1, c2) -> Integer.compare(c1.value, c2.value));

        boolean isFlush = hand.get(0).suit == hand.get(1).suit && hand.get(1).suit == hand.get(2).suit;
        boolean isStraight = hand.get(2).value == hand.get(1).value + 1 && hand.get(1).value == hand.get(0).value + 1;
        boolean isThreeOfAKind = hand.get(0).value == hand.get(1).value && hand.get(1).value == hand.get(2).value;
        boolean isPair = hand.get(0).value == hand.get(1).value || hand.get(1).value == hand.get(2).value || hand.get(0).value == hand.get(2).value;

        if (isFlush && isStraight) return 1; // Straight Flush
        else if (isThreeOfAKind) return 2; // Three of a Kind
        else if (isStraight) return 3; // Straight
        else if (isFlush) return 4; // Flush
        else if (isPair) return 5; // Pair
        else return 0; // High Card
    }

    // Calculate Pair Plus winnings based on the hand type
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int handValue = evalHand(hand);
        switch (handValue) {
            case 1: return bet * 40;  // Straight Flush
            case 2: return bet * 30;  // Three of a Kind
            case 3: return bet * 6;   // Straight
            case 4: return bet * 3;   // Flush
            case 5: return bet * 1;   // Pair
            default: return 0;        // High Card (no winnings)
        }
    }

    // Compare dealer's and player's hands to determine the winner
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerValue = evalHand(dealer);
        int playerValue = evalHand(player);

        // Compare hand types first (Lower value is better)
        if (playerValue < dealerValue) {  // Player has a better hand
            return 2;  // Player wins
        } else if (dealerValue < playerValue) { // Dealer has a better hand
            return 1;  // Dealer wins
        } else {
            // Same hand type, compare card values
            return resolveTie(dealer, player);
        }
    }

    // Resolve tie by comparing individual card values
    private static int resolveTie(ArrayList<Card> dealer, ArrayList<Card> player) {
        // Sort both hands by card value in descending order
        Collections.sort(dealer, (c1, c2) -> Integer.compare(c2.value, c1.value));
        Collections.sort(player, (c1, c2) -> Integer.compare(c2.value, c1.value));

        // Compare the highest cards one by one
        for (int i = 0; i < dealer.size(); i++) {
            if (player.get(i).value > dealer.get(i).value) {
                return 2;  // Player wins
            } else if (dealer.get(i).value > player.get(i).value) {
                return 1;  // Dealer wins
            }
        }

        // If all cards are the same value, it's a tie
        return 0;
    }
}