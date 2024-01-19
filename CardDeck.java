import java.util.ArrayList;

public class CardDeck {
    private final int number;
    private ArrayList<Card> deck = new ArrayList<>();

    /**
     * Create new CardDeck object
     * @param number - number of deck
     */
    public CardDeck (int number) {
        this.number = number;
    }

    /**
     * Get number of deck
     * @return deck number
     */
    public int getNumber() { return number; }

    /**
     * Add a new card to the deck list
     * @param card - card object to be added
     */
    public void addToDeck(Card card) {
        deck.add(card);
    }

    /**
     * Select the first card of the deck list and remove it
     * @return first card in the list
     */
    public Card drawCard() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    /**
     * Convert list of card objects in deck to a readable string of values for file outputs
     * @return string of card values in deck
     */
    public String displayDeck() {
        String output = "";
        for (Card card:deck) {
            output += card.getValue() + " ";
        }
        return output;
    }

    /**
     * Get number of cards in deck
     * @return deck list length
     */
    public int getSize() { return deck.size(); }

    /**
     * Get the contents of the deck list for testing purposes
     * @return the ArrayList of cards
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }
}
