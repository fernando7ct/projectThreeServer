// MyTest.java
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class MyTest {

	@Test
	@DisplayName("Test Card constructor and getters")
	void testCard() {
		Card card = new Card('H', 12); // Queen of Hearts
		assertEquals('H', card.getSuit(), "Suit should be 'H'");
		assertEquals(12, card.getValue(), "Value should be 12");
	}

	@Test
	@DisplayName("Test Deck initialization and size")
	void testDeckInitialization() {
		Deck deck = new Deck();
		assertEquals(52, deck.size(), "Deck should contain 52 cards after initialization");
	}

	@Test
	@DisplayName("Test Deck shuffling")
	void testDeckShuffling() {
		Deck deck1 = new Deck();
		Deck deck2 = new Deck();
		deck2.newDeck(); // Reshuffle deck2

		// It's possible but highly unlikely that two shuffles result in the same order
		boolean isDifferent = false;
		for (int i = 0; i < deck1.size(); i++) {
			if (deck1.get(i).getValue() != deck2.get(i).getValue() ||
					deck1.get(i).getSuit() != deck2.get(i).getSuit()) {
				isDifferent = true;
				break;
			}
		}
		assertTrue(isDifferent, "Two shuffled decks should have different order");
	}

	@Test
	@DisplayName("Test Player initial state")
	void testPlayerInitialState() {
		Player player = new Player();
		assertEquals(100, player.getTotalWinnings(), "Initial total winnings should be 100");
		assertEquals(0, player.getAnteBet(), "Initial Ante bet should be 0");
		assertEquals(0, player.getPlayBet(), "Initial Play bet should be 0");
		assertEquals(0, player.getPairPlusBet(), "Initial Pair Plus bet should be 0");
		assertTrue(player.getHand().isEmpty(), "Initial hand should be empty");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for Straight Flush")
	void testEvalHandStraightFlush() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('H', 10));
		hand.add(new Card('H', 11));
		hand.add(new Card('H', 12)); // 10, J, Q of Hearts
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(1, result, "Hand should be evaluated as Straight Flush");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for Three of a Kind")
	void testEvalHandThreeOfAKind() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('D', 9));
		hand.add(new Card('H', 9));
		hand.add(new Card('S', 9)); // Three 9s
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(2, result, "Hand should be evaluated as Three of a Kind");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for Straight")
	void testEvalHandStraight() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('C', 7));
		hand.add(new Card('D', 8));
		hand.add(new Card('H', 9)); // 7, 8, 9
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(3, result, "Hand should be evaluated as Straight");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for Flush")
	void testEvalHandFlush() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('S', 2));
		hand.add(new Card('S', 5));
		hand.add(new Card('S', 9)); // All Spades
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(4, result, "Hand should be evaluated as Flush");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for Pair")
	void testEvalHandPair() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('C', 4));
		hand.add(new Card('D', 4));
		hand.add(new Card('H', 7)); // Pair of 4s
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(5, result, "Hand should be evaluated as Pair");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalHand for High Card")
	void testEvalHandHighCard() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('C', 2));
		hand.add(new Card('D', 5));
		hand.add(new Card('H', 9)); // No combination
		int result = ThreeCardLogic.evalHand(hand);
		assertEquals(0, result, "Hand should be evaluated as High Card");
	}

	@Test
	@DisplayName("Test ThreeCardLogic evalPPWinnings for different hands")
	void testEvalPPWinnings() {
		ArrayList<Card> straightFlush = new ArrayList<>();
		straightFlush.add(new Card('H', 10));
		straightFlush.add(new Card('H', 11));
		straightFlush.add(new Card('H', 12)); // Straight Flush
		assertEquals(400, ThreeCardLogic.evalPPWinnings(straightFlush, 10), "Straight Flush should pay 40x");

		ArrayList<Card> threeOfAKind = new ArrayList<>();
		threeOfAKind.add(new Card('D', 9));
		threeOfAKind.add(new Card('H', 9));
		threeOfAKind.add(new Card('S', 9)); // Three of a Kind
		assertEquals(300, ThreeCardLogic.evalPPWinnings(threeOfAKind, 10), "Three of a Kind should pay 30x");

		ArrayList<Card> straight = new ArrayList<>();
		straight.add(new Card('C', 7));
		straight.add(new Card('D', 8));
		straight.add(new Card('H', 9)); // Straight
		assertEquals(60, ThreeCardLogic.evalPPWinnings(straight, 10), "Straight should pay 6x");

		ArrayList<Card> flush = new ArrayList<>();
		flush.add(new Card('S', 2));
		flush.add(new Card('S', 5));
		flush.add(new Card('S', 9)); // Flush
		assertEquals(30, ThreeCardLogic.evalPPWinnings(flush, 10), "Flush should pay 3x");

		ArrayList<Card> pair = new ArrayList<>();
		pair.add(new Card('C', 4));
		pair.add(new Card('D', 4));
		pair.add(new Card('H', 7)); // Pair
		assertEquals(10, ThreeCardLogic.evalPPWinnings(pair, 10), "Pair should pay 1x");

		ArrayList<Card> highCard = new ArrayList<>();
		highCard.add(new Card('C', 2));
		highCard.add(new Card('D', 5));
		highCard.add(new Card('H', 9)); // High Card
		assertEquals(0, ThreeCardLogic.evalPPWinnings(highCard, 10), "High Card should pay 0");
	}

	@Test
	@DisplayName("Test ThreeCardLogic compareHands where dealer wins")
	void testCompareHandsDealerWins() {
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card('C', 10));
		dealerHand.add(new Card('D', 11));
		dealerHand.add(new Card('H', 12)); // Straight Flush

		ArrayList<Card> playerHand = new ArrayList<>();
		playerHand.add(new Card('H', 2));
		playerHand.add(new Card('H', 5));
		playerHand.add(new Card('H', 9)); // Flush

		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(1, result, "Dealer should win with a better hand");
	}

	@Test
	@DisplayName("Test ThreeCardLogic compareHands where it's a tie")
	void testCompareHandsTie() {
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card('C', 10));
		dealerHand.add(new Card('D', 11));
		dealerHand.add(new Card('H', 12)); // Straight Flush

		ArrayList<Card> playerHand = new ArrayList<>();
		playerHand.add(new Card('S', 10));
		playerHand.add(new Card('H', 11));
		playerHand.add(new Card('D', 12)); // Straight Flush

		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(0, result, "Hands should be tied");
	}

	@Test
	@DisplayName("Test Player resetBets method")
	void testPlayerResetBets() {
		Player player = new Player();
		player.setAnteBet(10);
		player.setPlayBet(10);
		player.setPairPlusBet(10);
		player.setTotalWinnings(70);

		player.resetBets();

		assertEquals(0, player.getAnteBet(), "Ante bet should be reset to 0");
		assertEquals(0, player.getPlayBet(), "Play bet should be reset to 0");
		assertEquals(0, player.getPairPlusBet(), "Pair Plus bet should be reset to 0");
		assertEquals(70, player.getTotalWinnings(), "Total winnings should be not reset to initial value");
	}

	@Test
	@DisplayName("Test Deck contains unique cards after initialization")
	void testDeckUniqueCards() {
		Deck deck = new Deck();
		Set<String> uniqueCards = new HashSet<>();
		for (Card card : deck) {
			String identifier = "" + card.getSuit() + card.getValue();
			uniqueCards.add(identifier);
		}
		assertEquals(52, uniqueCards.size(), "Deck should contain 52 unique cards");
	}
}