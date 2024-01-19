import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class PackTest {
    private Pack pack;
    private Card card, card2;

    private Player player1, player2;
    private CardDeck cardDeck1, cardDeck2;

    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<CardDeck> cardDeckList = new ArrayList<>();

    @Before
    public void setUp() {
        // Create a deck containing card 1
        pack = new Pack();
        for (int i=1; i<=10; i++) {
            card = new Card(i);
            pack.addCard(card);
        }

        // Create a card to test adding
        card2 = new Card(11);

        // Create decks
        cardDeck1 = new CardDeck(1);
        cardDeck2 = new CardDeck(2);
        cardDeckList.add(cardDeck1);
        cardDeckList.add(cardDeck2);

        // Create players
        player1 = new Player(1, cardDeck1, cardDeck2);
        player2 = new Player(2, cardDeck2, cardDeck1);
        playerList.add(player1);
        playerList.add(player2);
    }

    @Test
    public void testAddCard() {
        System.out.println("addCard");
        pack.addCard(card2);
        // Check card 2 has been added to deck
        assertTrue(pack.getPack().contains(card2));
    }

    @Test
    public void testRemoveCard() {
        System.out.println("removeCard");
        Card removedCard = pack.getPack().get(0);
        pack.removeCard(removedCard);
        // Check drawn card 1 is no longer in deck
        assertFalse(pack.getPack().contains(removedCard));
    }

    @Test
    public void testGetSize() {
        System.out.println("getSize");
        int result = pack.getSize();
        // Check deck size is correct
        assertEquals(10, result);
    }

    @Test
    public void testDistributeCards() {
        System.out.println("distributeCards");
        pack.distributeCards(playerList, cardDeckList);

        // Check that the pack is now empty
        assertEquals(0, pack.getSize());

        // Check that the players have 4 cards in their hand
        assertEquals(4, player1.getHand().size());
        assertEquals(4, player2.getHand().size());

        // Check that the decks have 1 card left each
        assertEquals(1, cardDeck1.getSize());
        assertEquals(1, cardDeck2.getSize());

        // Check the round-robin distribution of player hand
        assertEquals(1, player1.getHand().get(0).getValue());
        assertEquals(2, player2.getHand().get(0).getValue());

        // Check the round-robin distribution of decks
        assertEquals(9, cardDeck1.getDeck().get(0).getValue());
        assertEquals(10, cardDeck2.getDeck().get(0).getValue());
    }
}
