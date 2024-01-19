import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable {
    private final int number;
    private ArrayList<Card> hand = new ArrayList<>();
    private CardDeck drawDeck;
    private CardDeck discardDeck;
    private ArrayList<Thread> threadList;
    private volatile boolean hasWon = false;

    /**
     * returns the players number
     * @return number
     */
    public int getNumber() {return number;}

    public ArrayList<Card> getHand() {return hand;}

    /**
     * craete a player
     * @param number the player number
     * @param drawDeck the deck the player should draw from
     * @param discardDeck the deck the player should discard from
     */
    public Player (int number, CardDeck drawDeck, CardDeck discardDeck) {
        this.number = number;
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
    }

    /**
     * add the card to the list of cards in hand
     * @param card card to be added
     */
    public void addToHand(Card card) {
        hand.add(card);
    }


    /**
     * get output line for file
     * @return the hand for the file outputs
     */
    public String displayHand() {
        String output = "";
        for (Card card:hand) {
            output += card.getValue() + " ";
        }
        return output;
    }


    private Object lock = new Object();


    /**
     * draws a card from the draw deck and adds to the players hand and file output
     * keeps trying to draw a card until either a card is successfully drawn or it has tried 200 times
     * @return boolean of whether a card has been successfully drawn from the draw deck
     */
    public boolean drawCard() {

        Boolean drawn = false;
        Boolean failed = false;
        int countloop = 0;

        // Continue attempting to draw until the draw deck has a card to draw
        while (!drawn && !failed) {
            countloop += 1;

            // Check deck is not empty
            if (drawDeck.getSize() != 0) {
                synchronized (lock) {
                    // Draw a card from draw deck and add it to player hand
                    Card card = drawDeck.drawCard();
                    if (card != null) {;
                        hand.add(card);
                    }

                    drawn = true;

                    // Update file output
                    fileOutput += "player " + number + " draws a " + card.getValue() + " from deck " + drawDeck.getNumber() + "\n";
                }
            }
            // Exit if stuck while waiting to draw a card from the deck
            if (countloop == 200) {
                failed = true;
            }
        }
        return failed;

    }

    /**
     * removes a card from the players hand and adds to the discard deck and file output
     * creates a list of cards that aren't the players preferred denomination and selects one
     */
    public void discardCard() {
        Card card;

        ArrayList<Card> validCards = new ArrayList<>();
        for (Card myCard : hand) {
            // Add all cards that are not of its preferred denomination to list of valid cards
            if (myCard.getValue() != number) {
                validCards.add(myCard);
            }
        }

        Random rand = new Random();

        if (validCards.size() == 1) {
            // If only one card of non preferred denomination then it can only be that card
            card = validCards.get(0);
        } else {
            // Pick a random valid card to remove from the list
            card = validCards.get(rand.nextInt(validCards.size() - 1));
        }

        synchronized (lock) {
            hand.remove(card);
            discardDeck.addToDeck(card);
        }

        // Update the file output
        fileOutput += "player " + number + " discards a " + card.getValue() + " to deck " + discardDeck.getNumber() + "\n";

    }

    /**
     * check if all the cards in the hand are of the same value
     * @return boolean for winning hand
     */
    public Boolean checkWin() {
        for (Card card : hand) {
            // Check all card values are the same
            if (!(card.getValue() == hand.get(0).getValue())) {
                return false;
            }
        }
        return true;
    }

    //list of all players threads
    public void setThreadList(ArrayList<Thread> threadList) {
        this.threadList = threadList;
    }

    public String fileOutput = "";

    //static for all players to see and interact with
    public static Player winningPlayer = null;


    /**
     * The threading player logic
     * while loop for checking if the player has won themselves each time they draw and discard a card
     * if player has won set the static method winningPlayer to themselves and interrupt the other players
     */
    public void run() {

        try {
            // Repeat as long as there are no winners
            while (!hasWon) {

                boolean failed = drawCard();
                // Only discard if the player managed to draw successfully
                if (!failed) {
                    discardCard();

                    // Check if won
                    hasWon = checkWin();

                    // Update file contents with current draw/discard cycle
                    fileOutput += "player " + number + " current hand is " + displayHand() + "\n";
                }

                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }

            // Set self as winning player if won
            winningPlayer = this;

            // Interrupt all other threads once there is a winner
            for(Thread thread : threadList) {
                thread.interrupt();
            }

        } catch (InterruptedException e) {
        }
    }

    /**
     * output stored messages from changing hand and draw/discard values
     * output appropriate messages based on if the player is the winner or not
     * @param winner object of the winning player - check if player == winning player
     */
    public  void finalOutputs(Player winner) {
        try {
            // Always wrap FileWriter in BufferedWriter
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("player" + number + "_output.txt", true));

            if (winner == this) {

                bufferedWriter.write(fileOutput);

                bufferedWriter.write("player " + number + " wins");
                bufferedWriter.newLine();

                bufferedWriter.write("player " + number + " exits");
                bufferedWriter.newLine();

                bufferedWriter.write("player " + number + " final hand: " + displayHand());
                bufferedWriter.newLine();

                bufferedWriter.close();// Always close it
            } else {
                bufferedWriter.write(fileOutput);

                bufferedWriter.write("player " + winner.number + " has informed player " + number + " that player " + winner.number + " has won");
                bufferedWriter.newLine();

                bufferedWriter.write("player " + number + " exits");
                bufferedWriter.newLine();

                bufferedWriter.write("player " + number + " hand: " + displayHand());
                bufferedWriter.newLine();

                bufferedWriter.close();// Always close it
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * For testing purposes
     * @return return ArrayList of cards
     */
    public CardDeck getDrawDeck() { return drawDeck; }

    /**
     * For testing purposes
     * @return return ArrayList of cards
     */
    public CardDeck getDiscardDeck() { return discardDeck; }


}
