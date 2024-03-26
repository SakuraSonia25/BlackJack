package blackjack.processes;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

// PACKAGES ISSUE
import blackjack.items.*;
import blackjack.players.*;
import blackjack.menus.*;;

public class Round {

    // To Print the icons
    public static PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
            true);
    private Scanner sc = new Scanner(System.in);

    /* ENCODINGS */
    private static Map<Character, String> luckyHandMap = new HashMap<>() {
        {
            put('n', "< 12");
            put('a', "DOUBLE As");
            put('j', "BLACKJACK");
            put('s', "777");
            put('d', "DRAGON");
            put('b', "BUST");
        }
    };
    private static Map<Character, String> resultMap = new HashMap<>() {
        {
            put('w', "WIN");
            put('l', "LOSE");
            put('d', "DRAW");
            put('c', "notChallenge");
            put('D', "dealer");
        }
    };
    private static Map<Character, String> statusMap = new HashMap<>() {
        {
            put('w', "Waiting");
            put('t', "Thinking");
            put('d', "Done!");
        }
    };

    /* ATTRIBUTES */
    private Deck deck;
    private List<Player> players;
    private Dealer dealer;

    // Store Players Results // [Player : {result, lucky, status}]
    private static Map<Person, char[]> playersResult = new HashMap<>();

    // Constructor
    public Round(Deck deck, List<Player> players, Dealer dealer) {

        this.deck = deck;
        this.players = players;
        this.dealer = dealer;

        for (Player p : players) {
            playersResult.put(p, new char[] { 'c', 'n', 'w' });
        }
        playersResult.put(dealer, new char[] { 'D', 'n', 'w' });
    }

    /* TURN METHODS */
    // Player Turn
    public void playerTurn(Player player) {
        // set player status to thinking
        setPlayerStatus(player, 't');

        // store turn outcome
        char turnOutcome = checkForLuckyHand(player); // default: check player hand for lucky hand

        // if player hand is not lucky or burst
        if (turnOutcome == 'n') {
            // if player is a bot
            if (player instanceof BotPlayer) {

                BotPlayer botPlayer = (BotPlayer) player; // class cast

                // bot thinking
                System.out.println("Player " + botPlayer.getName() + " is thinking...");

                // pausing to imitate thinking
                pausing();

                // bot actions
                char botAction;
                do {
                    // bot player deciding action
                    botAction = botPlayer.determineAction();

                    // if bot player decided to hit
                    if (botAction == 'h') {
                        turnOutcome = hit(player); // hit a card
                        System.out.println("-- " + player.getName() + " draws a card"); // display action to human
                    } else {
                        turnOutcome = stand(player); // stand
                        System.out.println("-- " + player.getName() + " stands"); // display action to human
                    }

                } while (turnOutcome == 'n' && botAction == 'h');

                // bot done
                System.out.println("Player " + player.getName() + "'s turn ends!\n");

            }
            // else, player is human
            else {
                // prompt player for action
                turnOutcome = promptHumanPlayer(player);
            }
        }

        // update player luckyhand outcome and status
        setPlayerLucky(player, turnOutcome);
        setPlayerStatus(player, 'd'); // set to done

        // display turn outcome
        displayTurnResult(turnOutcome, player);

    }

    // Dealer Turn - idk how to describe this method
    public void dealerTurn() {
        // set dealer status to thinking
        setPlayerStatus(this.dealer, 't');

        char turnOutcome = 'n'; // default: n (<21)

        // if dealer is a bot
        if (this.dealer instanceof BotDealer) {

            BotDealer botDealer = (BotDealer) this.dealer; // class cast

            // dealer thinking
            System.out.println("Dealer Thinking...\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // bot actions
            char botAction;
            do {
                botAction = botDealer.determineAction();

                if (botAction == 'h') {
                    turnOutcome = hit(this.dealer);
                } else {
                    botDealerChallenge(botDealer);
                }

            } while (turnOutcome == 'n' && botAction == 'h');

            // if dealer burst or luckyhand after hitting
            if (botAction == 'h') {
                // auto challenge
                botDealerChallenge(botDealer);
                setPlayerLucky(this.dealer, turnOutcome);
            }

            // dealer done
            System.out.println("Dealer is done!\n");

        }
        // else dealer is human
        else {
            promptHumanDealer();
        }

        // set player luckyhand outcome and status to done
        setPlayerStatus(this.dealer, 'd');

    }

    /* PROMPTING METHODS */
    public List<Player> chooseOpponents() {
        // store oppenents
        List<Player> oppenents = new ArrayList<>();
        // track selection status
        boolean stillSelecting = true; // default true
        // store dealer hand
        Hand dealerHand = this.dealer.getHandFromPerson();

        // Prompt user to select opponent
        displayChallengeOptions(dealerHand);
        int choice = checkForInputException(sc);
        while (stillSelecting) {
            // stop selecing if choice is -1
            if (choice == -1) {
                // if some players were chosen, end the selection
                if (oppenents.size() != 0) {
                    stillSelecting = false;
                    break;
                }
                // else, return err msg (or shld I do warning msg)
                System.out.println("Please select at least 1 player to challenge.");
            }

            // select all remaining players to challenge TBC
            else if (choice == 0) {
                // loop thr the players list and add players not challenged into the oppenents
                // list
                for (Player p : this.players) {
                    if (getPlayerResult(p) == 'c') {
                        oppenents.add(p);
                    }
                }
                return oppenents;
            }

            // continue selecting
            // invalid choice
            else if (((choice - 1) < 0 || (choice - 1) >= this.players.size())
                    || getPlayerResult(this.players.get(choice - 1)) != 'c') {
                // err msg
                System.out.println("Please select the corresponding index of the player you want to choose.\n");
            } else { // valid choice

                Player chosenPlayer = this.players.get(choice - 1);

                // if player is alrdy chosen
                if (oppenents.contains(chosenPlayer)) {
                    // err msg
                    System.out.println("Player " + chosenPlayer.getName() + " is selected already.");
                }
                // else, if all is gd, add chosen player to list
                else {
                    // add chosen player to list
                    oppenents.add(chosenPlayer);
                    System.out.println("-- Added " + chosenPlayer.getName());

                    // if all players are selected, end selection
                    if (oppenents.size() == getNumOfUnchallengedPlayers()) {
                        stillSelecting = false;
                        break;
                    }

                }
            }
            // reprompt question
            System.out.print("Please enter the player's index (or -1 to stop selecting): ");
            choice = checkForInputException(sc);
        }

        return oppenents;
    }

    public char promptHumanPlayer(Player player) {

        char turnOutcome = 'n';
        int choice;
        do {
            displayPlayerTurnOptions(player.getHandFromPerson());
            choice = checkForInputException(sc);

            switch (choice) {
                case 1: // hit
                    turnOutcome = hit(player);
                    break;
                case 2: // stand
                    turnOutcome = stand(player);
                    break;
                default:
                    System.out.println("Please enter a choice between 1 and 2.\n");
            }

        } while ((choice != 1 && choice != 2) || (choice == 1 && turnOutcome == 'n'));

        return turnOutcome;
    }

    public void promptHumanDealer() {
        char turnOutcome = 'n';
        boolean challenging = !isAllPlayersChallenged();
        int choice;
        do {
            displayDealerTurnOptions(this.dealer.getHandFromPerson());
            choice = checkForInputException(sc);

            switch (choice) {
                case 1:
                    turnOutcome = hit(this.dealer);
                    break;
                case 2:
                    turnOutcome = stand(this.dealer);
                    if (turnOutcome == 'n') {
                        List<Player> oppenents = chooseOpponents();
                        dealerChallenge(oppenents);
                        challenging = !isAllPlayersChallenged();   
                    }
                    System.out.println("c=" + challenging + ", to=" + turnOutcome);
                    break;
                default:
                    System.out.println("Please enter a choice between 1 and 2.\n");
            }

        } while ((choice != 1 && choice != 2) || (turnOutcome == 'n' && challenging));

        // auto challenge
        if (turnOutcome != 'n' && challenging) {
            if (choice == 1) {
                displayTurnResult(turnOutcome, this.dealer);
            }  
            setPlayerLucky(this.dealer, turnOutcome);
            List<Player> oppenents = this.players.stream().filter(p -> getPlayerResult(p) == 'c')
                    .collect(Collectors.toList());
            dealerChallenge(oppenents);
        }

    }

    /* ACTION METHODS */
    public char hit(Person p) {
        // draw cards
        this.deck.dealCardsToPerson(p, 1);
        return checkForLuckyHand(p);
    }

    public char stand(Person p) {

        Hand pHand = p.getHandFromPerson();

        while (pHand.getHandScore() < 16) { // default 16 is minStart (NEED TO THINK OF HOW TO PASS IN)
            // inform player about the auto hit
            System.out.println("Your handscore is below 16. Auto hit a card!");

            // hit a card
            char turnOucome = hit(p);
            setPlayerLucky(p, turnOucome);
            pHand = p.getHandFromPerson();

            // inform player about outcome
            System.out.println("-- your current handscore is " + pHand.getHandScore() + "("
                    + (getPlayerLucky(p) == 'n' ? "" : luckyHandMap.get(getPlayerLucky(p))) + ")");
            System.out.println(pHand.displayOpenHand());
        }

        return checkForLuckyHand(p);
    }

    public void botDealerChallenge(BotDealer botDealer) {

        Hand dealerHand = this.dealer.getHandFromPerson();

        // hit if hand score < 16
        while (dealerHand.getHandScore() < 16) {
            hit(this.dealer);
            dealerHand = this.dealer.getHandFromPerson();
        }
        // track which player is still not challenged
        List<Player> toChallenge = new ArrayList<>(players);

        // while there are still players to challenge
        while (toChallenge.size() > 0) {
            // bot choose opponent
            Player opp = botDealer.determineChallenge(toChallenge);
            System.out.println("Dealer challenging " + opp.getName());

            // bot challenge opponent
            char challengeResult = this.dealer.challenge(opp);

            // set opponent result
            if (challengeResult == 'w') {
                setPlayerResult(opp, 'l');
            } else if (challengeResult == 'l') {
                setPlayerResult(opp, 'w');
            } else {
                setPlayerResult(opp, challengeResult);
            }

            // display challenge result
            System.out.println("-- dealer " + resultMap.get(challengeResult) + "\n");

            // remove opponent from tochallengelist
            toChallenge.remove(opp);
        }

        // update cash amt
        updateCashAmt(this.players);

        // End Challenge Method
    }

    public void dealerChallenge(List<Player> oppenents) {
        // challenging oppenents
        for (Player opp : oppenents) {
            char challengeResult = this.dealer.challenge(opp);

            // set opponent result
            if (challengeResult == 'w') {
                setPlayerResult(opp, 'l');
            } else if (challengeResult == 'l') {
                setPlayerResult(opp, 'w');
            } else {
                setPlayerResult(opp, challengeResult);
            }
        }

        // update cash amt
        updateCashAmt(oppenents);

        // display challenge Result
        displayChallengeResult(oppenents);
    }

    /* CALCULATION METHODS */
    public void updateCashAmt(List<Player> playerList) {
        for (Player p : playerList) {
            int playerBetAmt = p.getBetAmt();
            // LOSE CASE - player pay dealer
            if (getPlayerResult(p) == 'l') {
                p.setCashAmt(p.getCashAmt() - playerBetAmt);
                this.dealer.setCashAmt(this.dealer.getCashAmt() + playerBetAmt);
            }
            // WIN CASE - dealer pay player
            if (getPlayerResult(p) == 'w') {
                p.setCashAmt(p.getCashAmt() + playerBetAmt);
                this.dealer.setCashAmt(this.dealer.getCashAmt() - playerBetAmt);
            }
        }

    }

    /* DISPLAY METHODS */
    public void displayPlayerTurnOptions(Hand playerHand) {

        String handStr = "Your Hand: \n" + playerHand.displayOpenHand();
        // System.out.println(handStr.indexOf("_\n") + "vs" + handStr.length());
        displayHeading("Your Turn (Player)", '=', handStr.indexOf("_\n"));
        displayTable(handStr.indexOf("_\n"));
        displayHoriLine('-', handStr.indexOf("_\n"), '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStr.indexOf("_\n"), '\n');
        System.out.println("1. Hit");
        System.out.println("2. Stand");
        System.out.print("Please enter your choice: ");
    }

    public void displayDealerTurnOptions(Hand dealerHand) {

        String handStr = "Your Hand: \n" + dealerHand.displayOpenHand();

        displayHeading("Your Turn (Dealer)", '=', handStr.indexOf('\n'));
        displayRemainingTable(handStr.indexOf("_\n"));
        displayHoriLine('-', handStr.indexOf("_\n"), '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStr.indexOf("_\n"), '\n');
        System.out.println("1. Hit");
        System.out.println("2. Challenge");
        System.out.print("Please enter your choice: ");
    }

    public void displayChallengeOptions(Hand dealerHand) {

        String handStr = "Your Hand: \n" + dealerHand.displayOpenHand();

        displayHeading("Pick your Opponent(s)", '=', handStr.length());
        System.out.println("0. All Players at table");
        displayRemainingTable(handStr.length());
        displayHoriLine('-', handStr.length(), '\n');
        System.out.println(handStr);
        displayHoriLine('-', handStr.length(), '\n');
        System.out.print("Please enter the player's index (or -1 to stop selecting): ");
    }

    public void displayTable(int headingLength) {
        displayHeading("table", '-', headingLength);
        // Print Dealer
        if (dealer instanceof BotDealer) {
            System.out.println("1. Dealer " + this.dealer.getHandFromPerson().displayCloseHand());
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

    public void displayRemainingTable(int headingLength) {
        displayHeading("table", '-', headingLength);
        // // Print Players
        for (int i = 0; i < players.size(); i++) {
            if (getPlayerResult(players.get(i)) == 'c') {
                System.out.println((i+1) + ". " + players.get(i).getName() + " " + players.get(i).getHandFromPerson().displayCloseHand());
            }
        }
    }

    public void displayRoundResult() {
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
                System.out.println(this.dealer.getHandFromPerson().displayOpenHand());
                System.out.println("Your Hand:");
                System.out.println(p.getHandFromPerson().displayOpenHand());
                System.out.println("current cash amount: " + p.getCashAmt());
            }
            // if human is dealer
            if ((p instanceof Dealer) && !(p instanceof BotDealer)) {
                // display challenge result of all players + dealer cash amt
                int i = 1;
                for (Player player : players) {
                    System.out
                            .println(i + "." + player.getName() + " (" + resultMap.get(getPlayerResult(player)) + ")");
                    i++;
                }
                System.out.println("\nYour current cash amount: " + p.getCashAmt());
            }

        }
        System.out.println("");
    }

    public void displayChallengeResult(List<Player> oppenents) {
        System.out.println("\n=== Challenge Result ===");
        int i = 1;
        for (Player opp : oppenents) {
            System.out.println(i + "." + opp.getName() + " " + resultMap.get(getPlayerResult(opp)));
            System.out.println(opp.getHandFromPerson().displayOpenHand());
            i++;
        }
        System.out.println("Your current cash amount: " + this.dealer.getCashAmt());
        promptEnterKey(); // prompt player to press enter key to proceed
    }

    public void displayTurnResult(char turnOutcome, Person player) {
        if (turnOutcome != 'n') {
            if (player instanceof BotPlayer) {
                System.out.println(player.getName() + " " + (turnOutcome != 'b' ? "GOT" : "")
                        + luckyHandMap.get(turnOutcome) + "!\n");
            } else {
                displayHeading("Your Hand", '=', 25);
                System.out.println(player.getHandFromPerson().displayOpenHand());
                System.out.println("\nYOU " + (turnOutcome != 'b' ? "GOT" : "") + luckyHandMap.get(turnOutcome) + "!");
                promptEnterKey(); // prompt player to press enter key to proceed
            }
        }
    }

    /* HELPER METHODS */
    public void promptEnterKey() {
        // Scanner sc = new Scanner(System.in);
        System.out.print("Press \"ENTER\" to continue...");
        try {
            sc.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayHoriLine(char chara, int noOfChara, char endChara) {
        System.out.print(("" + chara).repeat(noOfChara) + endChara);
    }

    public void displayHeading(String title, char chara, int length) {
        int charlength = (length - title.length() - 2) / 2;

        String heading = ("" + chara).repeat(charlength) + " " + title + " " + ("" + chara).repeat(charlength);
        System.out.println(heading);

    }

    /* HELPER METHODS */
    // Check for BJ // {true - hv, false - no hv} // MAY DEL
    public boolean hvStartingLuckyHand(Person p) {
        Hand hand = p.getHandFromPerson();
        if (hand.checkBlackjack()) {
            System.out.println(p.getName() + " got BLACKJACK!");
            return true;
        }
        return false;
    }

    public char checkForLuckyHand(Person p) {

        Hand pHand = p.getHandFromPerson();

        if (pHand.checkBurst()) {
            return 'b';
        } else if (pHand.checkTriple7()) {
            return 's';
        } else if (pHand.checkDragon()) {
            return 'd';
        }

        return 'n';

    }

    // Check if all players were challenged
    public boolean isAllPlayersChallenged() {
        for (Player p : this.players) {
            if (getPlayerResult(p) == 'c') {
                return false;
            }
        }
        return true;
    }

    // Get number of unchallenged players
    public int getNumOfUnchallengedPlayers() {
        int count = 0;

        for (Person p : playersResult.keySet()) {
            if (getPlayerResult(p) == 'c') {
                count++;
            }
        }
        return count;
    }

    // Set Player Status
    public void setPlayerStatus(Person p, char status) {
        char[] info = playersResult.get(p);
        info[2] = status;
        playersResult.put(p, info);
    }

    // Set Player Result
    public void setPlayerResult(Person p, char result) {
        char[] info = playersResult.get(p);
        info[0] = result;
        playersResult.put(p, info);
    }

    // Set Player LuckyHand
    public void setPlayerLucky(Person p, char lucky) {
        char[] info = playersResult.get(p);
        info[1] = lucky;
        playersResult.put(p, info);
    }

    // Getter methods
    public char getPlayerStatus(Person p) {
        char[] info = playersResult.get(p);
        return info[2];
    }

    // Set Player Result
    public char getPlayerResult(Person p) {
        char[] info = playersResult.get(p);
        return info[0];
    }

    // Set Player LuckyHand
    public char getPlayerLucky(Person p) {
        char[] info = playersResult.get(p);
        return info[1];
    }

    public int checkForInputException(Scanner sc) {
        try {
            int choice = sc.nextInt();
            sc.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("inside InputMismatchException");
            sc.nextLine();
            return -2;
        }

    }

    public void pausing() {
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
