import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    /**
     * Open the given file, check each line to ensure it is a valid card value, and place these values into an array list
     * @param filename name of file to be opened
     * @return array list of integer card values in pack file
     */
    public static ArrayList<Integer> readPackFile(String filename) {
        ArrayList<Integer> packValues = new ArrayList<Integer>();
        BufferedReader reader = null;

        try {
            // Open the chosen file
            reader = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = reader.readLine()) != null) {
                // Read the line, check if it is a non-negative integer and add to list of pack values
                if (checkInteger(line, 0)) {
                    packValues.add(Integer.parseInt(line));
                } else {
                    throw new IOException("Invalid pack values.");
                }
            }
        } catch(IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                // Attempt to close the file
                if (reader != null) reader.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        // If file reading failed, an empty array list will be returned
        return packValues;
    }

    /**
     * Checks whether string input by user or value in pack file is a valid integer or not
     * @param s input string to be checked
     * @param condition the minimum value that the integer can be, i.e. 0 for card values or 1 for player amount
     * @return boolean depending on whether input is a valid integer or not
     */
    public static boolean checkInteger(String s, Integer condition) {
        // A non integer will be caught by this try
        try {
            // Turn string value into an integer and check if it is greater than the minimum
            return Integer.parseInt(s) >= condition;
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
    }

    /**
     * Take a user input for the number of players in the game and check that a valid integer is given
     * @return number of players as an integer
     * @throws IOException - if player number entered is not a valid integer
     */
    public static Integer inputPlayerAmount() throws IOException {
        // Take user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the number of players:");
        String playerAmount = scanner.next();
        // Check input it is an integer greater than 1 and add to list of pack values
        if (checkInteger(playerAmount, 1)) {
            return Integer.parseInt(playerAmount);
        } else {
            throw new IOException("Invalid player number.");
        }
    }

    /**
     * Take a user input for the name of the pack file
     * @return name of file
     * @throws IOException
     */
    public static String inputFileName() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter location of pack to load:");
        String packFile = scanner.next();
        scanner.close();
        return packFile;
    }

    /**
     * Take the list of card values in the pack and check that it is valid for the given number of players, and
     * take the boolean valid to check whether any of the previous inputs were invalid, and stop the game as needed
     * @param packList - integer card values read from the pack file
     * @param playerNum - number of players input by the user
     * @param valid - boolean for whether the previous user inputs were valid
     */
    public static void checkInputValidity(ArrayList<Integer> packList, int playerNum, boolean valid) {
        if (packList.size() != 8*playerNum || !valid) {
            System.out.println("Invalid pack length.");
            // Terminate the game if inputs were invalid
            System.exit(-1);
        }
    }

    /**
     * Given a number of players, create the player objects and assign them the correct draw and discard decks
     * using the given deck list
     * @param playerNum - integer number of players in the game
     * @param deckList - list of deck objects
     * @return - list of created player objects
     */
    public static ArrayList<Player> createPlayers(int playerNum, ArrayList<CardDeck> deckList) {
        ArrayList<Player> playerList = new ArrayList<>();
        CardDeck drawDeck, discardDeck;
        // Create correct number of players
        for (int i=1; i <= playerNum; i++) {
            drawDeck = null;
            discardDeck = null;
            for (CardDeck deck : deckList) {
                // Player draws from the left deck
                if (deck.getNumber() == i) {
                    drawDeck = deck;
                // Player discards to the right deck
                } else if (deck.getNumber() == (i % playerNum) + 1) {
                    discardDeck = deck;
                }
            }
            // Create the player and add it to the player list
            Player myPlayer = new Player(i, drawDeck, discardDeck);
            playerList.add(myPlayer);
        }
        return playerList;
    }

    /**
     * Go through list of threads repeatedly until they are all dead
     * @param threads - list of running threads
     */
    public static void checkFinished(ArrayList<Thread> threads) {
        boolean finished = false;
        // Repeat until all threads have finished running
        while (!finished) {
            boolean allDone = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    allDone = false;
                }
            }
            if (allDone) {
                finished = true;
            }
        }
    }

    /**
     * Output the final deck contents to the deck files
     * @param deckList - List of deck objects
     */
    public static void displayDeckFinal(ArrayList<CardDeck>deckList) {
        try {
            for (CardDeck decks : deckList) {
                // Output to each deck file
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("deck" + decks.getNumber() + "_output.txt"));
                bufferedWriter.write("deck " + decks.getNumber() + " contents: " + decks.displayDeck());
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Given the list of players, add their starting hand to their file
     * @param playerList - list of player objects
     */
    public static void initialHandState(ArrayList<Player>playerList) {
        try {
            for (Player player : playerList) {
                // Output to each player file
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("player" + player.getNumber() + "_output.txt"));
                bufferedWriter.write("player " + player.getNumber() + " initial hand " + player.displayHand());
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Main game procedure
     * @param args
     */
    public static void main(String[] args) {

        Integer playerNum = 0;
        boolean valid = true;

        // Take user input for number of players
        try {
            playerNum = inputPlayerAmount();
        } catch (IOException ex) {
            System.out.println("Invalid player number");
            valid = false;
        }

        ArrayList<Integer> packList = new ArrayList<>();

        // Take user input for pack file
        try {
            String packFile = inputFileName();
            packList = readPackFile(packFile);
        } catch (IOException ex) {
            System.out.println("Couldn't find file");
            valid = false;
        }

        // Check whether the given inputs for pack file and player number are valid
        checkInputValidity(packList,playerNum,valid);

        // Import the card numbers from the pack file as cards and add to pack object for distribution to decks and hands
        Pack pack = new Pack();
        for (int i = 0; i < packList.size(); i++) {
            Card card = new Card(packList.get(i));
            pack.addCard(card);
        }

        // Deck creating
        ArrayList<CardDeck> deckList = new ArrayList<CardDeck>();
        for (int i=1; i <= playerNum; i++) {
            CardDeck myDeck = new CardDeck(i);
            deckList.add(myDeck);
        }

        // Player creating
        ArrayList<Player> playerList = createPlayers(playerNum, deckList);

        // Pack distribution
        pack.distributeCards(playerList, deckList);

        // Get initial hand of each player and output to corresponding file
        initialHandState(playerList);

        // Check if any initial hands are a winning hand
        Player winner = null;
        for (Player player:playerList) {
            if (player.checkWin()) {
                winner = player;
            }
        }

        // If no winning hand start threading and game logic
        if (winner == null) {
            ArrayList<Thread> myThreads = new ArrayList<>();

            // Start the threading of the game for players
            for (int i = 0; i < playerNum; i++) {
                Player myPlayer = playerList.get(i);
                Thread myThreadedPlayer = new Thread(myPlayer);
                myThreadedPlayer.start();

                // Add to list of running threads
                myThreads.add(myThreadedPlayer);
            }

            // Set the thread list for each player
            for (Player player : playerList) {
                player.setThreadList(myThreads);
            }

            // Do not continue running program until all threads have stopped running
            checkFinished(myThreads);

            // Output the winner to the terminal
            System.out.println("Player " + Player.winningPlayer.getNumber() + " has won");

            // Output the results of the game to each player file
            for (Player player:playerList) {
                player.finalOutputs(Player.winningPlayer);
            }

        // If a player had a winning hand, do not start game, skip to printing winner and outputting results
        } else {
            System.out.println("Player " + winner.getNumber() + " has won");
            for (Player player:playerList) {
                player.finalOutputs(winner);
            }
        }

        // Display the final hand of each deck after the game ends
        displayDeckFinal(deckList);
    }
}