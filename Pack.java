import java.util.ArrayList;

public class Pack {
    private ArrayList<Card> cards = new ArrayList<>();

    /**
     * Called from CardGame - adds the cards from the pack file to a list to be distributed
     * @param card current card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Remove the given card from the card list
     * @param card - card to remove
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Get the current number of cards in the card list
     * @return length of card list
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Distribute the cards to the hands and then the remaining to the decks in a round-robin fashion
     * @param playerList - the list of player objects in the game
     * @param deckList - the list of deck objects
     */
    public void distributeCards(ArrayList<Player>playerList, ArrayList<CardDeck>deckList) {
        // Repeat 4 times to give 4 cards to each player hand
        for (int i=0; i < 4; i++) {
            for (Player player: playerList) {
                // Remove the first card from the pack and add to player x's hand
                player.addToHand(cards.get(0));
                removeCard(cards.get(0));
            }
        }

        // Repeat until the pack is empty
        while (getSize() != 0) {
            // Distribute to the rest of the decks
            for (CardDeck deck: deckList) {
                // Remove the first card from the pack and add to the deck
                deck.addToDeck(cards.get(0));
                removeCard(cards.get(0));
            }
        }
    }

    /**
     * Get the contents of the pack for testing purposes
     * @return the ArrayList of cards
     */
    public ArrayList<Card> getPack() {
        return cards;
    }
}
