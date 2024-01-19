import org.junit.*;
import static org.junit.Assert.*;

public class CardDeckTest {

    private CardDeck cardDeck;
    private Card card1, card2, card3, card4;

    @Before
    public void setUp() {
        // Create a deck containing card 1
        cardDeck = new CardDeck(1);
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        card4 = new Card(4);
        cardDeck.addToDeck(card1);
        cardDeck.addToDeck(card2);
        cardDeck.addToDeck(card3);
    }

    @Test
    public void testAddToDeck() {
        System.out.println("addToDeck");
        cardDeck.addToDeck(card4);
        // Check card 2 has been added to deck
        assertTrue(cardDeck.getDeck().contains(card4));
    }

    @Test
    public void testDrawCard() {
        System.out.println("drawCard");
        Card drawnCard = cardDeck.drawCard();
        // Check correct drawn card 1 is returned
        assertEquals(card1, drawnCard);
        // Check drawn card 1 is no longer in deck
        assertFalse(cardDeck.getDeck().contains(card1));
    }

    @Test
    public void testDisplayDeck() {
        System.out.println("displayDeck");
        String result = cardDeck.displayDeck();
        // Check deck displays correctly
        assertEquals("1 2 3 ", result);
    }

    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        int result = cardDeck.getNumber();
        // Check deck number is correct
        assertEquals(1, result);
    }

    @Test
    public void testGetSize() {
        System.out.println("getSize");
        int result = cardDeck.getSize();
        // Check deck displays correctly
        assertEquals(3, result);
    }
}
