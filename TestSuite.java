import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PlayerTest.class,
        PackTest.class,
        CardTest.class,
        CardDeckTest.class,
        CardGameTest.class
})

public class TestSuite {
    // This class can be empty
    // It's used only as a holder for the above annotations
}