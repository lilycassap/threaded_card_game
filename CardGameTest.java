import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CardGameTest {

    private CardDeck cardDeck1, cardDeck2;
    private ArrayList<CardDeck> deckList;

    @Before
    public void setUp() {
        // Create 2 decks for testing
        deckList = new ArrayList<>();
        cardDeck1 = new CardDeck(1);
        cardDeck2 = new CardDeck(2);
        deckList.add(cardDeck1);
        deckList.add(cardDeck2);
    }

    @Test
    public void testReadPackFile() {
        System.out.println("readPackFile");

        // Check for an existing pack file with valid card values
        ArrayList<Integer> exp = new ArrayList<>(Arrays.asList(1, 3, 4, 3, 5, 4, 2, 7, 6, 5, 3, 6, 6, 1, 8, 3, 1, 4, 8, 4, 3, 7, 6, 2, 7, 2, 8, 7, 2, 5, 7, 5));
        ArrayList<Integer> result = CardGame.readPackFile("packfile.txt");
        assertEquals(exp, result);

        // Check for existing pack file with string instead of integer in card values
        exp = new ArrayList<Integer>();
        result = CardGame.readPackFile("badpackfile.txt");
        assertEquals(exp, result);

        // Check for a non-existent pack file
        result = CardGame.readPackFile("testfile.txt");
        assertEquals(exp, result);
    }

    @Test
    public void testCheckInteger() {
        System.out.println("checkInteger");

        // Check 0 condition branches
        Boolean result = CardGame.checkInteger("0", 0);
        assertEquals(true, result);
        result = CardGame.checkInteger("-1", 0);
        assertEquals(false, result);

        // Check 1 condition branches
        result = CardGame.checkInteger("1", 1);
        assertEquals(true, result);
        result = CardGame.checkInteger("0", 1);
        assertEquals(false, result);

        // Check invalid non-integers are caught
        result = CardGame.checkInteger("hello", 1);
        assertEquals(false, result);
    }

    @Test
    public void testCreatePlayers() {
        System.out.println("createPlayers");

        // Create 2 players
        ArrayList<Player> playerList = CardGame.createPlayers(2, deckList);
        Player player1 = playerList.get(0);
        Player player2 = playerList.get(1);

        // Check player numbers are correct
        assertEquals(1, player1.getNumber());
        assertEquals(2, player2.getNumber());

        // Check left decks
        assertEquals(cardDeck1, player1.getDrawDeck());
        assertEquals(cardDeck2, player2.getDrawDeck());

        // Check right decks
        assertEquals(cardDeck1, player2.getDiscardDeck());
        assertEquals(cardDeck2, player1.getDiscardDeck());
    }
}
