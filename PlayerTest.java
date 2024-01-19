import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
public class PlayerTest {
    private Player player, winningPlayer;
    private CardDeck cardDeck1, cardDeck2;
    private Card card, card1, card2, card3, card4;

    // First card in the draw deck
    private Card card5;

    @Before
    public void setUp() {
        cardDeck1 = new CardDeck(1);
        cardDeck2 = new CardDeck(2);
        player = new Player(1, cardDeck1,cardDeck2);

        // Create player hand - 4 cards with 3 preferred cards (1)
        card1 = new Card(1);
        player.addToHand(card1);

        card2 = new Card(1);
        player.addToHand(card2);

        card3 = new Card(1);
        player.addToHand(card3);

        card4 = new Card(2);
        player.addToHand(card4);

        // Create draw deck
        card5 = new Card(5);
        cardDeck1.addToDeck(card5);
        card = new Card(7);
        cardDeck1.addToDeck(card);

        // Create discard deck
        card = new Card(6);
        cardDeck2.addToDeck(card);
        card = new Card(8);
        cardDeck2.addToDeck(card);

        // Create a winning player with 4 of the same card
        winningPlayer = new Player(2, cardDeck1, cardDeck2);
        for (int i = 0; i<4; i++) {
            card = new Card(30);
            winningPlayer.addToHand(card);
        }
    }

    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        assertEquals(1, player.getNumber());
    }

    @Test
    public void testGetHand() {
        System.out.println("getHand");

        ArrayList<Card> exp = new ArrayList<>();
        exp.add(card1);
        exp.add(card2);
        exp.add(card3);
        exp.add(card4);

        assertEquals(exp, player.getHand());
    }

    @Test
    public void testAddToHand() {
        System.out.println("addToHand");
        card = new Card(20);
        player.addToHand(card);
        // Check card has been added to hand
        assertTrue(player.getHand().contains(card));
    }

    @Test
    public void testDisplayHand() {
        System.out.println("displayHand");
        assertEquals("1 1 1 2 ", player.displayHand());
    }

    @Test
    public void testCheckWin() {
        System.out.println("checkWin");
        // Check that the players correctly identify whether they have won or not
        assertEquals(true, winningPlayer.checkWin());
        assertEquals(false, player.checkWin());
    }

    @Test
    public void testDrawCard() {
        System.out.println("drawCard");

        // Before drawing - check contents of player hand and deck
        assertFalse(player.getHand().contains(card5));
        assertTrue(cardDeck1.getDeck().contains(card5));
        assertEquals(4, player.getHand().size());
        assertEquals(2, cardDeck1.getDeck().size());

        // Draw - check that the draw does not return a fail
        boolean failed = player.drawCard();
        assertFalse(failed);

        // After drawing - check that it picks up card 5 as this is the first card of the deck, and check the list lengths
        assertTrue(player.getHand().contains(card5));
        assertFalse(cardDeck1.getDeck().contains(card5));
        assertEquals(5, player.getHand().size());
        assertEquals(1, cardDeck1.getDeck().size());

        // Test for an empty deck by drawing 2 more times
        player.drawCard();
        failed = player.drawCard();

        // Check that the second draw failed
        assertTrue(failed);

        // Check that players hand size only increased by one as the second draw failed
        assertEquals(6, player.getHand().size());
    }

    @Test
    public void testDiscardCard() {
        System.out.println("discardCard");

        // Check contents of player hand and deck before discarding
        assertTrue(player.getHand().contains(card4));
        assertFalse(cardDeck2.getDeck().contains(card4));
        assertEquals(4, player.getHand().size());

        // Discard
        player.discardCard();

        // Check contents of player hand and deck after discarding
        assertFalse(player.getHand().contains(card4));
        assertTrue(cardDeck2.getDeck().contains(card4));
        assertEquals(3, player.getHand().size());
    }
}
