package blackjack.menus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import blackjack.items.*;
import blackjack.players.*;

public class RoundDisplay {

    // Constants for Colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // To Print the icons
    public static PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8),true);
    private static Scanner sc = new Scanner(System.in);

    /* ENCODINGS */
    public static Map<Character, String> luckyHandMap = new HashMap<>() {
        {
            put('n', "< 12");
            put('a', "DOUBLE As");
            put('j', "BLACKJACK");
            put('s', "777");
            put('d', "DRAGON");
            put('b', "BUST");
        }
    };
    public static Map<Character, String> resultMap = new HashMap<>() {
        {
            put('w', "WIN");
            put('l', "LOSE");
            put('d', "DRAW");
            put('c', "notChallenge");
            put('D', "dealer");
        }
    };
    public static Map<Character, String> statusMap = new HashMap<>() {
        {
            put('w', "Waiting");
            put('t', "Thinking");
            put('d', "Done!");
        }
    };

    // Store Players Results // [Player : {result, lucky, status}]
    public static Map<Person, char[]> playersResult = new HashMap<>();

    // Rest Result Map
    public static void resetMap() {
        playersResult = new HashMap<>();
    }

    // Setter Methods
    public static void setPlayerStatus(Person p, char status) {
        char[] info = playersResult.get(p);
        info[2] = status;
        playersResult.put(p, info);
    }

    public static void setPlayerResult(Person p, char result) {
        char[] info = playersResult.get(p);
        info[0] = result;
        playersResult.put(p, info);
    }

    public static void setPlayerLucky(Person p, char lucky) {
        char[] info = playersResult.get(p);
        info[1] = lucky;
        playersResult.put(p, info);
    }

    // Getter Methods
    public static char getPlayerStatus(Person p) {
        char[] info = playersResult.get(p);
        return info[2];
    }

    public static char getPlayerResult(Person p) {
        char[] info = playersResult.get(p);
        return info[0];
    }

    public static char getPlayerLucky(Person p) {
        char[] info = playersResult.get(p);
        return info[1];
    }

    // Display Option Menus Methods
    // Player Option Menu
    public static void displayPlayerTurnOptions(Hand playerHand, Dealer dealer, List<Player> players) {

        String handStr = "Your Hand: \n" + ANSI_GREEN + playerHand.displayOpenHand() + ANSI_RESET;
        int handStrlen = handStr.indexOf("_\n") + 1;
        // heading
        System.out.println("");
        displayHeading("Your Turn (Player)", '=', handStrlen);
        // table
        displayTable(handStrlen, dealer, players);
        // player hand
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        // options
        System.out.println("1. Hit");
        System.out.println("2. Stand");
        System.out.print("Please enter your choice: ");
    }
    // Dealer Option Menu
    public static void displayDealerTurnOptions(Hand dealerHand, List<Player> players) {

        String handStr = "Your Hand: \n" + ANSI_GREEN + dealerHand.displayOpenHand() + ANSI_RESET;
        int handStrlen = handStr.indexOf("_\n") + 1;
        // heading
        System.out.println("");
        displayHeading("Your Turn (Dealer)", '=', handStrlen);
        // table
        displayRemainingTable(handStrlen, players);
        // dealer hand
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        // options
        System.out.println("1. Hit");
        System.out.println("2. Challenge");
        System.out.print("Please enter your choice: ");
    }
    // Dealer - Choose Opponent Option Menu
    public static void displayChallengeOptions(Hand dealerHand, List<Player> players) {

        String handStr = "Your Hand: \n" + ANSI_GREEN + dealerHand.displayOpenHand() + ANSI_RESET;
        int handStrlen = handStr.indexOf("_\n") + 1;
        // heading
        System.out.println("");
        displayHeading("Pick your Opponent(s)", '=', handStrlen);
        // options / table
        System.out.println("0. All Players at table");
        displayRemainingTable(handStrlen, players);
        // dealer hand
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        System.out.print("Please enter the player's index (or -1 to stop selecting): ");
    }
    
    // Display Tables
    // display all players on table
    public static void displayTable(int headingLength, Dealer dealer, List<Player> players) {
        displayHeading("table", '-', headingLength);
        // Print Dealer
        if (dealer instanceof BotDealer) {
            System.out.println("1. " + dealer.getName() + " " + dealer.getHandFromPerson().displayCloseHand());
        }
        // Print Players
        int i = 2;
        for (Player p : players) {

            if (p instanceof BotPlayer) {
                System.out.println(i + ". " + p.getName() + " " + p.getHandFromPerson().displayCloseHand());
                i++;
            }
        }
    }
    // display only remaining non challenged players on table
    public static void displayRemainingTable(int headingLength, List<Player> players) {
        // heading
        displayHeading("table", '-', headingLength);
        // display remaining unchallenged players
        for (int i = 0; i < players.size(); i++) {
            if (getPlayerResult(players.get(i)) == 'c') {
                System.out.println((i + 1) + ". " + players.get(i).getName() + " " + players.get(i).getHandFromPerson().displayCloseHand());
            }
        }
    }

    // Display Results
    // Round Result
    public static void displayRoundResult(List<Player> players, Dealer dealer) {

        Person p = findHuman(); // Find who is human

        // Heading
        System.out.println(ANSI_CYAN);
        System.out.println("=======================");
        System.out.println("||  ROUND ENDED! ^o^ ||");
        System.out.println("=======================");
        System.out.print(ANSI_RESET);

        // Print Round Sumaary
        System.out.println("Round Summary: ");
        int i = 1;
        // display dealer if human is not dealer
        if (p != dealer) {
            System.out.println(ANSI_CYAN + i + ". " + dealer.getName() + " (Balance: $" + dealer.getCashAmt()+ ")" + ANSI_RESET); 
            i++;
        }
        // display players
        for (Player player : players) {
            if (p == player) { // compare addresses
                System.out.println(i + ". YOU " + resultMap.get(getPlayerResult(player))); 
            } else {
                System.out.println(
                        i + ". " + player.getName() + " " + resultMap.get(getPlayerResult(player)) + " (Balance: $" + player.getCashAmt()+ ")");
            }
            i++;
        }

        // if human is player
        if ((p instanceof Player) && !(p instanceof BotPlayer)) {           
            // player result
            System.out.println("\nResult: YOU " + resultMap.get(getPlayerResult(p)) + " THE ROUND!");
            // dealer hand
            System.out.println("Dealer Hand:");
            System.out.println(ANSI_GREEN + dealer.getHandFromPerson().displayOpenHand() + ANSI_RESET);
            System.out.println();
            // player hand
            System.out.println("Your Hand:");
            System.out.println(ANSI_GREEN + p.getHandFromPerson().displayOpenHand() + ANSI_RESET);
            // player cash amt
            System.out.println(String.format("Your current cash amount: %s",(p.getCashAmt() <= 0 ? ANSI_RED + "$" + p.getCashAmt() + "(Bankrupt)" + ANSI_RESET : "$" + p.getCashAmt())));
        }
        // if human is dealer
        if ((p instanceof Dealer) && !(p instanceof BotDealer)) {
            // dealer cash amt
            System.out.println(String.format("\nYour current cash amount: %s",(p.getCashAmt() <= 0 ? ANSI_RED + "$" + p.getCashAmt() + "(Bankrupt)" + ANSI_RESET : "$" + p.getCashAmt())));
        }
        
        promptEnterKey(); // prompt player to press enter key to proceed
    }

    // Challenge Result
    public static void displayChallengeResult(List<Player> oppenents, Dealer dealer) {
        // heading
        System.out.println(ANSI_CYAN + "\n=== Challenge Result ===" + ANSI_RESET);
        // display all oppenent and their result
        int i = 1;
        for (Player opp : oppenents) {
            System.out.println(i + "." + opp.getName() + " " + resultMap.get(getPlayerResult(opp)));
            System.out.println(ANSI_GREEN + opp.getHandFromPerson().displayOpenHand() + ANSI_RESET);
            i++;
        }
        // display dealer cash amount
        System.out.println("Your current cash amount: $" + dealer.getCashAmt());

        promptEnterKey(); // prompt player to press enter key to proceed
    }

    // Turn Result
    public static void displayTurnResult(char turnOutcome, Person player) {

        // if player is bot
        if (player instanceof BotPlayer || player instanceof BotDealer) {
            // if player is a bot dealer
            if (player instanceof BotDealer) {
                // hand burst or got lucky hand
                if (turnOutcome != 'n') {
                    // print hand outcome + hand  
                    System.out.println("\n" + player.getName() + (turnOutcome != 'b' ? " GOT " + luckyHandMap.get(turnOutcome) + "!": luckyHandMap.get(turnOutcome) + "!"));
                    displayHandStr(player);
                } 
                // normal hand
                else {
                    System.out.println(player.getName() + " is done!\n");
                }
                
            } 
            // player is a bot player
            else {
                System.out.println("Player " + player.getName() + "'s turn ends!\n");
            }
        }
        // else, playe ris human
        else {
            if (turnOutcome != 'n') {
                System.out.println(ANSI_CYAN + "\n OMGGG YOU " + (turnOutcome != 'b' ? "GOT " + luckyHandMap.get(turnOutcome) + "!(~ ' 3 ')~": luckyHandMap.get(turnOutcome) + "! ( T _ T )" + ANSI_RESET));
                displayHandStr(player);
                promptEnterKey(); // prompt player to press enter key to proceed
            } else {
                System.out.println(ANSI_CYAN + "Your turn ends!\n" + ANSI_RESET);
            }

        }
    }

    /* Helper Methods */

    public static void displayHandStr(Person p) { 
        // using the hand str to calculate length of heading
        String handStr = "Your Hand: \n" + p.getHandFromPerson().displayOpenHand(); 
        int handStrlen = handStr.indexOf("_\n") + 1;

        displayHeading("Your Hand", '-', handStrlen);
        System.out.println(ANSI_GREEN + p.getHandFromPerson().displayOpenHand() + ANSI_RESET);
    }

    public static void promptEnterKey() {
        // Scanner sc = new Scanner(System.in);
        System.out.print(ANSI_CYAN + "Press \"ENTER\" to continue...\n" + ANSI_RESET);
        try {
            sc.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayHoriLine(char chara, int noOfChara, char endChara) {
        System.out.print(("" + chara).repeat(noOfChara) + endChara);
    }

    public static void displayHeading(String title, char chara, int length) {
        int charlength = (length - title.length()) / 2;
        String heading = ANSI_CYAN + ("" + chara).repeat(charlength) + " " + title + " "
                + ("" + chara).repeat(charlength) + ANSI_RESET;
        System.out.println(heading);

    }

    public static Person findHuman() { 

        for (Person p : playersResult.keySet()) {
            if (((p instanceof Player) && !(p instanceof BotPlayer)) || ((p instanceof Dealer) && !(p instanceof BotDealer))) {
                return p;
            }
        }

        return null;
    }

}
