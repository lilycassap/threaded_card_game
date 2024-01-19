import org.junit.*;
import static org.junit.Assert.*;

public class CardTest {
    private Card card;

    @Before
    public void setUp() {
        card = new Card(1);
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        assertEquals(1, card.getValue());
    }

}
