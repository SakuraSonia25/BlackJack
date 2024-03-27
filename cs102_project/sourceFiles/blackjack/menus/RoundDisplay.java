package blackjack.menus;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import blackjack.items.Hand;
import blackjack.players.BotDealer;
import blackjack.players.BotPlayer;
import blackjack.players.Dealer;
import blackjack.players.Person;
import blackjack.players.Player;

public class RoundDisplay {

    // To Print the icons
    public static PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
            true);
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

    // Set Player Status
    public static void setPlayerStatus(Person p, char status) {
        char[] info = playersResult.get(p);
        info[2] = status;
        playersResult.put(p, info);
    }

    // Set Player Result
    public static void setPlayerResult(Person p, char result) {
        char[] info = playersResult.get(p);
        info[0] = result;
        playersResult.put(p, info);
    }

    // Set Player LuckyHand
    public static void setPlayerLucky(Person p, char lucky) {
        char[] info = playersResult.get(p);
        info[1] = lucky;
        playersResult.put(p, info);
    }

    // Getter methods
    public static char getPlayerStatus(Person p) {
        char[] info = playersResult.get(p);
        return info[2];
    }

    // Set Player Result
    public static char getPlayerResult(Person p) {
        char[] info = playersResult.get(p);
        return info[0];
    }

    // Set Player LuckyHand
    public static char getPlayerLucky(Person p) {
        char[] info = playersResult.get(p);
        return info[1];
    }

    /* DISPLAY METHODS */
    public static void displayPlayerTurnOptions(Hand playerHand, Dealer dealer, List<Player> players) {

        String handStr = "Your Hand: \n" + playerHand.displayOpenHand();
        int handStrlen = handStr.indexOf("_\n") + 1;

        System.out.println("");
        displayHeading("Your Turn (Player)", '=', handStrlen);
        displayTable(handStrlen, dealer, players);
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        System.out.println("1. Hit");
        System.out.println("2. Stand");
        System.out.print("Please enter your choice: ");
    }

    public static void displayDealerTurnOptions(Hand dealerHand, List<Player> players) {

        String handStr = "Your Hand: \n" + dealerHand.displayOpenHand();
        int handStrlen = handStr.indexOf("_\n") + 1;

        System.out.println("");
        displayHeading("Your Turn (Dealer)", '=', handStrlen);
        displayRemainingTable(handStrlen, players);
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        System.out.println("1. Hit");
        System.out.println("2. Challenge");
        System.out.print("Please enter your choice: ");
    }

    public static void displayChallengeOptions(Hand dealerHand, List<Player> players) {

        String handStr = "Your Hand: \n" + dealerHand.displayOpenHand();
        int handStrlen = handStr.indexOf("_\n") + 1;

        System.out.println("");
        displayHeading("Pick your Opponent(s)", '=', handStrlen);
        System.out.println("0. All Players at table");
        displayRemainingTable(handStrlen, players);
        displayHoriLine('-', handStrlen, '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStrlen, '\n');
        System.out.print("Please enter the player's index (or -1 to stop selecting): ");
    }

    public static void displayTable(int headingLength, Dealer dealer, List<Player> players) {
        displayHeading("table", '-', headingLength);
        // Print Dealer
        if (dealer instanceof BotDealer) {
            System.out.println("1. Dealer " + dealer.getHandFromPerson().displayCloseHand());
        }
        // // Print Players
        int i = 1;
        for (Player p : players) {
            if (p instanceof BotPlayer) {
                System.out.println(i + ". " + p.getName() + " " + p.getHandFromPerson().displayCloseHand());
                i++;
            }
        }
    }

    public static void displayRemainingTable(int headingLength, List<Player> players) {
        displayHeading("table", '-', headingLength);
        // // Print Players
        for (int i = 0; i < players.size(); i++) {
            if (getPlayerResult(players.get(i)) == 'c') {
                System.out.println((i + 1) + ". " + players.get(i).getName() + " "
                        + players.get(i).getHandFromPerson().displayCloseHand());
            }
        }
    }

    public static void displayRoundResult(List<Player> players, Dealer dealer) {
        System.out.println("");
        System.out.println("=======================");
        System.out.println("||  ROUND ENDED! ^o^ ||");
        System.out.println("=======================");

        // Find Human
        for (Person p : playersResult.keySet()) {
            // if human is player
            if ((p instanceof Player) && !(p instanceof BotPlayer)) {
                // display player result + cash amt
                System.out.println("YOU " + resultMap.get(getPlayerResult(p)) + "!");
                System.out.println("Dealer Hand:");
                System.out.println(dealer.getHandFromPerson().displayOpenHand());
                System.out.println("Your Hand:");
                System.out.println(p.getHandFromPerson().displayOpenHand());
                System.out.println("Your current cash amount: " + p.getCashAmt());
                System.out.println("Dealer cash amount: " + dealer.getCashAmt());
            }
            // if human is dealer
            if ((p instanceof Dealer) && !(p instanceof BotDealer)) {
                // display challenge result of all players + dealer cash amt
                int i = 1;
                for (Player player : players) {
                    System.out.println(i + "." + player.getName() + " (" + resultMap.get(getPlayerResult(player)) + ")");
                    i++;
                }
                System.out.println("\nYour current cash amount: " + p.getCashAmt());
            }

        }
        System.out.println("");
    }

    public static void displayChallengeResult(List<Player> oppenents, Dealer dealer) {
        System.out.println("\n=== Challenge Result ===");
        int i = 1;
        for (Player opp : oppenents) {
            System.out.println(i + "." + opp.getName() + " " + resultMap.get(getPlayerResult(opp)));
            System.out.println(opp.getHandFromPerson().displayOpenHand());
            i++;
        }
        System.out.println("Your current cash amount: " + dealer.getCashAmt());
        promptEnterKey(); // prompt player to press enter key to proceed
    }

    public static void displayTurnResult(char turnOutcome, Person player) {
        System.out.print("\n");
        if (turnOutcome != 'n') {
            if (player instanceof BotPlayer) {
                System.out.println(player.getName() + " " + (turnOutcome != 'b' ? "GOT" : "")
                        + luckyHandMap.get(turnOutcome) + "!\n");
            } else {
                System.out.println("YOU " + (turnOutcome != 'b' ? "GOT " + luckyHandMap.get(turnOutcome) +"!(~ ' 3 ')~"  : luckyHandMap.get(turnOutcome)+ "! ( T _ T )"));
                displayHandStr(player);
                promptEnterKey(); // prompt player to press enter key to proceed
            }
        } else {
            if (player instanceof BotPlayer) {
                System.out.println("Player " + player.getName() + "'s turn ends!\n");
            } else {
                System.out.println("Your turn ends!\n");
            }
        }
    }

    public static void displayHandStr(Person p) {
        String handStr = "Your Hand: \n" + p.getHandFromPerson().displayOpenHand();
        int handStrlen = handStr.indexOf("_\n") + 1;
        displayHeading("Your Hand", '-', handStrlen);
        System.out.println(p.getHandFromPerson().displayOpenHand());
    }

    /* HELPER METHODS */
    public static void promptEnterKey() {
        // Scanner sc = new Scanner(System.in);
        System.out.print("Press \"ENTER\" to continue...");
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
        String heading = ("" + chara).repeat(charlength) + " " + title + " " + ("" + chara).repeat(charlength);
        System.out.println(heading);

    }

    

}
