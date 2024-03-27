package blackjack.processes;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

import blackjack.items.*;
import blackjack.players.*;
import blackjack.menus.*;

public class Round {

    // To Print the icons
    public static PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
            true);
    private Scanner sc = new Scanner(System.in);

    // Constants for Colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    /* ATTRIBUTES */
    private Deck deck;
    private List<Player> players;
    private Dealer dealer;

    // Constructor
    public Round(Deck deck, List<Player> players, Dealer dealer) {

        this.deck = deck;
        this.players = players;
        this.dealer = dealer;

        RoundDisplay.resetMap();

        for (Player p : players) {
            RoundDisplay.playersResult.put(p, new char[] { 'c', 'n', 'w' });
        }
        RoundDisplay.playersResult.put(dealer, new char[] { 'D', 'n', 'w' });
    }

    /* TURN METHODS */
    // Player Turn
    public void playerTurn(Player player) {
        // set player status to thinking
        RoundDisplay.setPlayerStatus(player, 't');

        // store turn outcome
        char turnOutcome = getTypeOfHand(player); // default: check player hand for lucky hand

        // if player is a bot
        if (player instanceof BotPlayer) {

            BotPlayer botPlayer = (BotPlayer) player; // class cast

            // bot thinking
            System.out.println("Player " + botPlayer.getName() + " is thinking...");

            // pausing to imitate thinking
            pausing(250);

            // if player hand is not lucky or burst, allow bot to do actions like hit and
            // stand
            if (turnOutcome == 'n') {
                // bot randomised reactions
                System.out.println(
                        botPlayer.getName() + " says " + ANSI_CYAN + botPlayer.getPlayerRandomReaction() + ANSI_RESET);

                // pausing to imitate thinking
                pausing(1000);

                // dealing with any aces in initial hand
                // if no double As and got ace card
                if (turnOutcome != 'a' && player.getHandFromPerson().checkForAcesCard()) {
                    // loop through the player hand for Ace cards
                    for (Card c : player.getHandFromPerson().getHand()) {

                        if (c.getCardValue().equals("Ace")) {
                            int value = botPlayer.chooseAceValue();
                            c.updateAce(value);
                        }
                    }
                }

                // bot actions
                char botAction;
                do {
                    // prompt bot for action
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
            }

        }
        // else, player is human
        else {
            // if player hand is not lucky or burst, prompt human for action
            if (turnOutcome == 'n') {
                // if no double As and got ace card
                if (turnOutcome != 'a' && player.getHandFromPerson().checkForAcesCard()) {
                    // loop through the player hand for Ace cards
                    for (Card c : player.getHandFromPerson().getHand()) {
                        // Each Aces card, prompt player for value
                        if (c.getCardValue().equals("Ace")) {
                            int value = promptForAceValue(player);

                            c.updateAce(value);
                            System.out.println(ANSI_CYAN + "Ace's value updated! Your new total handscore is "
                                    + player.getHandScore() + "." + ANSI_RESET);
                        }
                    }
                }
                // prompt player for action (hit or stand)
                turnOutcome = promptHumanPlayer(player);
            }

        }

        /* Reaching here means player STAND, GOT LUCKYHAND, or BURST */

        // update player luckyhand outcome and status
        RoundDisplay.setPlayerLucky(player, turnOutcome);
        RoundDisplay.setPlayerStatus(player, 'd'); // set to done

        // display turn outcome
        RoundDisplay.displayTurnResult(turnOutcome, player);
    }

    // Dealer Turn
    public void dealerTurn() {
        // set dealer status to thinking
        RoundDisplay.setPlayerStatus(this.dealer, 't');

        // store turn outcome
        char turnOutcome = getTypeOfHand(this.dealer); // default: check player hand for lucky hand

        // if dealer is a bot
        if (this.dealer instanceof BotDealer) {

            BotDealer botDealer = (BotDealer) this.dealer; // class cast

            // dealer thinking
            System.out.println(this.dealer.getName() + " is thinking...");

            // pausing to imitate thinking
            pausing(250);

            // dealer randomised reactions
            System.out.println(
                    this.dealer.getName() + " says " + ANSI_CYAN + botDealer.getDealerRandomReaction() + ANSI_RESET);

            pausing(250);

            // deals with initial aces in dealer's hand
            // if no double As and got ace card
            if (turnOutcome != 'a' && botDealer.getHandFromPerson().checkForAcesCard()) {

                // loop through the player hand for Ace cards
                for (Card c : botDealer.getHandFromPerson().getHand()) {

                    if (c.getCardValue().equals("Ace")) {
                        int value = botDealer.chooseAceValue();
                        c.updateAce(value);
                    }
                }
            }

            boolean choice = botDealer.determineHit();
            do {
                choice = botDealer.determineHit();
                String[] dealerNameArr = this.dealer.getName().split(" ");
                if (choice) {
                    System.out.println("-- " + dealerNameArr[dealerNameArr.length - 1] + " draws a card");
                    turnOutcome = hit(this.dealer);
                    pausing(250);
                } else {
                    botDealerChallenge(botDealer);
                }
            } while (turnOutcome == 'n' && choice == true);

            // if dealer burst or luckyhand after hitting
            if (choice == true) {
                // auto challenge
                botDealerChallenge(botDealer);
            }

            // dealer done
            RoundDisplay.displayTurnResult(turnOutcome, this.dealer);

        }
        // else dealer is human
        else {
            // if no double As and got ace card
            if (turnOutcome != 'a' && this.dealer.getHandFromPerson().checkForAcesCard()) {
                // loop through the player hand for Ace cards
                for (Card c : this.dealer.getHandFromPerson().getHand()) {
                    // Each Aces card, prompt player for value
                    if (c.getCardValue().equals("Ace")) {
                        int value = promptForAceValue(this.dealer);

                        c.updateAce(value);
                        System.out.println(ANSI_CYAN + "Ace's value updated! Your new total handscore is "
                                + this.dealer.getHandScore() + "." + ANSI_RESET);
                    }
                }
            }

            // prompt dealer for action
            promptHumanDealer();
            // method ends when all players are challenged
        }

        // set player luckyhand outcome and status to done
        RoundDisplay.setPlayerStatus(this.dealer, 'd');

    }

    /* PROMPTING METHODS */
    public int checkForInputException(Scanner sc) {
        try {
            int choice = sc.nextInt();
            sc.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            // System.out.println("Caught an InputMismatchException.");
            sc.nextLine();
            return -2;
        }

    }

    public List<Player> chooseOpponents() {
        // store oppenents
        List<Player> oppenents = new ArrayList<>();
        // track selection status
        boolean stillSelecting = true; // default true
        // store dealer hand
        Hand dealerHand = this.dealer.getHandFromPerson();

        // Prompt user to select opponent
        RoundDisplay.displayChallengeOptions(dealerHand, this.players);

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
                System.out.print(
                        "You haven't pick an opponent yet. Are you sure you want to end oppenent selection? (y/n) ");
                String input = sc.nextLine();
                input = input.toLowerCase().trim();
                if (input.equals("y") || input.equals("yes")) {
                    stillSelecting = false;
                    break;
                }

            }

            // select all remaining players to challenge TBC
            else if (choice == 0) {
                // loop thr the players list and add players not challenged into the oppenents list
                for (Player p : this.players) {
                    if (RoundDisplay.getPlayerResult(p) == 'c') {
                        oppenents.add(p);
                    }
                }
                return oppenents;
            }

            // continue selecting
            // invalid choice
            else if (((choice - 1) < 0 || (choice - 1) >= this.players.size())
                    || RoundDisplay.getPlayerResult(this.players.get(choice - 1)) != 'c') {
                // err msg
                System.out.println(ANSI_RED
                        + "Please select the corresponding index of the player you want to choose.\n" + ANSI_RESET);
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
                    System.out.println("-- Added " + chosenPlayer.getName() + " as opponenet");

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

        System.out.println("-- Oppenents Selection Ends --");
        return oppenents;
    }

    public char promptHumanPlayer(Player player) {

        char turnOutcome = 'n'; // Assume turn outcome is nothing

        // prompt player for action (hit or stand)
        int choice;
        do {
            RoundDisplay.displayPlayerTurnOptions(player.getHandFromPerson(), this.dealer, this.players);
            choice = checkForInputException(sc);

            switch (choice) {
                case 1: // hit
                    turnOutcome = hit(player);
                    break;
                case 2: // stand
                    turnOutcome = stand(player);
                    break;
                default: // err msg
                    System.out.println(ANSI_RED + "Please enter a choice between 1 and 2.\n" + ANSI_RESET);
            }

        } while ((choice != 1 && choice != 2) || (choice == 1 && turnOutcome == 'n'));

        // return the outcome from the actions
        return turnOutcome;
    }

    public void promptHumanDealer() {

        char turnOutcome = 'n'; // Assume turn outcome is nothing

        boolean challenging = !isAllPlayersChallenged(); // tracks if dealer need to continue challenging

        // prompt dealer for action (hit or challenge)
        int choice;
        do {
            RoundDisplay.displayDealerTurnOptions(this.dealer.getHandFromPerson(), this.players);
            choice = checkForInputException(sc);

            switch (choice) {
                case 1: // hit
                    turnOutcome = hit(this.dealer);
                    break;
                case 2: // challenge

                    turnOutcome = stand(this.dealer); // auto hit if handscore < 16 and return dealer hand state

                    // if hand no burst or don't have lucky hand
                    if (turnOutcome == 'n') {
                        // choose players to challenge
                        List<Player> oppenents = chooseOpponents();

                        // challenge chosen players
                        dealerChallenge(oppenents);

                        // display challenge Result
                        RoundDisplay.displayChallengeResult(oppenents, this.dealer);
                        challenging = !isAllPlayersChallenged(); // update if its necessary to continue challenging
                    }
                    break;
                default: // err msg
                    System.out.println(ANSI_RED + "Please enter a choice between 1 and 2.\n" + ANSI_RESET);
            }

        } while ((choice != 1 && choice != 2) || (turnOutcome == 'n' && challenging));

        // auto challenge - hand burst or got lucky hand
        if (turnOutcome != 'n' && challenging) {

            // return turn result
            RoundDisplay.displayTurnResult(turnOutcome, this.dealer);

            // update player lucky hand
            RoundDisplay.setPlayerLucky(this.dealer, turnOutcome);

            // auto challenge remaining players
            System.out.println("You auto challenges the rest of the players!"); // inform human
            List<Player> oppenents = this.players.stream().filter(p -> RoundDisplay.getPlayerResult(p) == 'c')
                    .collect(Collectors.toList());
            dealerChallenge(oppenents);

            // display challenge result
            RoundDisplay.displayChallengeResult(oppenents, this.dealer);
        }

        // End of Human Dealer Actions
    }

    public int promptForAceValue(Person p) {
        int value = 0;
        do {
            System.out.println("\nYou got an Ace Card!");
            RoundDisplay.displayHandStr(p, true);
            System.out.print("Enter your desired value for the card ");

            //specific value of card
            if (p.getHandSize() == 2) {
                System.out.print("(1 or 10 or 11): ");
                value = checkForInputException(sc);
                if (value != 1 && value != 10 && value != 11) {
                    System.out.println(ANSI_RED + "Please enter a value of 1, 10 or 11" + ANSI_RESET);
                }
            }
            else if (p.getHandSize() == 3) {
                System.out.println("(1 or 10): ");
                value = checkForInputException(sc);
                if (value != 1 && value != 10) {
                    System.out.println(ANSI_RED + "Please enter a value of 1 or 10" + ANSI_RESET);
                }
            }

        } while (value != 1 && value != 10 && value != 11);

        return value;
    }

    /* ACTION METHODS */
    public char hit(Person p) {
        // draw cards
        Card card = this.deck.dealCards();
        p.getHandFromPerson().addCard(card);

        // if p is a human
        if (!(p instanceof BotDealer || p instanceof BotPlayer)) {
            // if p drew a Ace card
            if (card.getCardValue().equals("Ace")) {
                // prompt p for Ace card value
                int value = promptForAceValue(p);

                card.updateAce(value);
                System.out.println("Ace's value updated! Your new total handscore is " + p.getHandScore() + ".");

            }
            // display card drew
            System.out.println("Card you drew:");
            System.out.println(ANSI_GREEN + card.displayCard() + ANSI_RESET);
        }
        // p is bot player
        else if (p instanceof BotPlayer) { 
            // check for any Aces, and update value
            if(card.getCardValue().equals("Ace")) {
                BotPlayer botP = (BotPlayer) p;
                List<Card> hand = botP.getHandFromPerson().getHand();
                // we iterate from the back to ensure only the newest Ace is updated
                for (int i = hand.size() - 1; i >= 0; i--) {
                    Card c = hand.get(i);
                    int value = botP.chooseAceValue();
                    c.updateAce(value);

                    // break to prevent older Ace values from being updated
                    break;
                }
            }

        // p is botdealer
        } else {
            // check for any Aces, and update value
            if(card.getCardValue().equals("Ace")) {
                BotDealer botP = (BotDealer) p;
                List<Card> hand = botP.getHandFromPerson().getHand();
                // we iterate from the back to ensure only the newest Ace is updated
                for (int i = hand.size() - 1; i >= 0; i--) {
                    Card c = hand.get(i);
                    int value = botP.chooseAceValue();
                    c.updateAce(value);

                    // break to prevent older Ace values from being updated
                    break;
                }
            }
            
        }

        return getTypeOfHand(p);
    }

    public char stand(Person p) {

        Hand pHand = p.getHandFromPerson();

        while (pHand.getHandScore() < 16) { // default 16 is minStart (NEED TO THINK OF HOW TO PASS IN)
            // inform player about the auto hit
            System.out.println("Your handscore is below 16. Auto hit a card!");

            // hit a card
            char turnOucome = hit(p);
            RoundDisplay.setPlayerLucky(p, turnOucome);
            pHand = p.getHandFromPerson();

        }

        return getTypeOfHand(p);
    }

    public void botDealerChallenge(BotDealer botDealer) {

        Hand dealerHand = this.dealer.getHandFromPerson();

        // hit if hand score < 16
        while (dealerHand.getHandScore() < 16) {
            hit(this.dealer);
            dealerHand = this.dealer.getHandFromPerson();
        }

        System.out.println(ANSI_CYAN + "\n" + botDealer.getName() + " is going to challenge now MUHAHAHAHA" + ANSI_RESET);

        // track which player is still not challenged
        List<Player> toChallenge = new ArrayList<>(players);

        String[] dealerNameArr = botDealer.getName().split(" ");

        // while there are still players to challenge
        while (toChallenge.size() > 0) {
            // bot choose opponent
            Player opp = botDealer.determineChallenge(toChallenge);
            System.out.println(dealerNameArr[dealerNameArr.length - 1] + " is challenging " + opp.getName());

            // bot challenge opponent
            char challengeResult = this.dealer.challenge(opp);

            // set opponent result
            if (challengeResult == 'w') {
                RoundDisplay.setPlayerResult(opp, 'l');
            } else if (challengeResult == 'l') {
                RoundDisplay.setPlayerResult(opp, 'w');
            } else {
                RoundDisplay.setPlayerResult(opp, challengeResult);
            }

            // display challenge result
            System.out.println("-- " + dealerNameArr[dealerNameArr.length - 1] + " "
                    + RoundDisplay.resultMap.get(challengeResult) + "\n");

            // remove opponent from tochallengelist
            toChallenge.remove(opp);
        }
        // update cash amt
        updateCashAmt(this.players);
        // End Challenge Method

    }

    public void dealerChallenge(List<Player> oppenents) {
        // if no oppenents, end method
        if (oppenents == null || oppenents.size() == 0) {
            return;
        }
        // challenging oppenents
        for (Player opp : oppenents) {
            char challengeResult = this.dealer.challenge(opp);

            // set opponent result
            if (challengeResult == 'w') {
                RoundDisplay.setPlayerResult(opp, 'l');
            } else if (challengeResult == 'l') {
                RoundDisplay.setPlayerResult(opp, 'w');
            } else {
                RoundDisplay.setPlayerResult(opp, challengeResult);
            }
        }

        // update cash amt
        updateCashAmt(oppenents);

    }

    /* CALCULATION METHODS */
    public void updateCashAmt(List<Player> playerList) {
        for (Player p : playerList) {
            int playerBetAmt = p.getBetAmt();

            //update for special hands
            if (this.getTypeOfHand(p) == 'j' || this.getTypeOfHand(p) == 'd') {
                playerBetAmt *= 2;
            }
            else if (this.getTypeOfHand(p) == 'a') {
                playerBetAmt *= 3;
            }
            else if (this.getTypeOfHand(p) == 's') {
                playerBetAmt *= 7;
            }

            // LOSE CASE - player pay dealer
            if (RoundDisplay.getPlayerResult(p) == 'l') {
                p.setCashAmt(p.getCashAmt() - playerBetAmt);
                this.dealer.setCashAmt(this.dealer.getCashAmt() + playerBetAmt);
            }
            // WIN CASE - dealer pay player
            if (RoundDisplay.getPlayerResult(p) == 'w') {
                p.setCashAmt(p.getCashAmt() + playerBetAmt);
                this.dealer.setCashAmt(this.dealer.getCashAmt() - playerBetAmt);
            }
        }
    }

    /* HELPER METHODS */
    // Check if all players were challenged
    public boolean isAllPlayersChallenged() {
        for (Player p : this.players) {
            if (RoundDisplay.getPlayerResult(p) == 'c') {
                return false;
            }
        }
        return true;
    }

    // Get number of unchallenged players
    public int getNumOfUnchallengedPlayers() {
        int count = 0;

        for (Person p : RoundDisplay.playersResult.keySet()) {
            if (RoundDisplay.getPlayerResult(p) == 'c') {
                count++;
            }
        }
        return count;
    }

    public char getTypeOfHand(Person p) {

        Hand pHand = p.getHandFromPerson();

        if (pHand.checkBurst()) {
            return 'b';
        } else if (pHand.checkDragon()) {
            return 'd';
        } else if (pHand.checkTriple7()) {
            return 's';
        } else if (pHand.checkDoubleAs()) {
            return 'a';
        } else if (pHand.checkBlackjack()) {
            return 'j';
        }

        return 'n';

    }

    public void pausing(int noOfmillisec) {
        try {
            Thread.sleep(noOfmillisec); // 1000 milliseconds = 1 seconds

        } catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
