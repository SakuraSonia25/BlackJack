package blackjack.processes;

import java.util.*;
import java.util.stream.Collectors;

import blackjack.players.*;
import blackjack.items.*;
import blackjack.menus.*;

public class Game {

    //Constants for Colours
    public static final String ANSI_RESET = "\u001B[0m"; //It goes to normal
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // Attributes
    private List<Player> players;
    private Dealer dealer;

    // Constructor
    public Game(List<Player> players, Dealer dealer) {
        this.players = players;
        this.dealer = dealer;
    }

    // Getter Methods
    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    // Setter Methods
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    // Main Playing Methods
    public Round startRound() {

        Deck d = new Deck();
        int betAmt = 500; // Default bet amt: Human players can change it

        // Setting Bet Amt and hand to all players
        for (Player p : this.getPlayers()) {
            if (!(p instanceof BotPlayer)) {
                System.out.println("Your cash amount: $" + p.getCashAmt());
                betAmt = this.getBetAmount(p);

            } else {
                if (betAmt > p.getCashAmt()) {
                    betAmt = p.getCashAmt();
                }
            }
            p.setBetAmt(betAmt);
            p.setHand(new Hand(d));

        }
        this.dealer.setHand(new Hand(d));
        
        // inform human the cards have been distributed
        System.out.println("2 cards have been distributed to each player.");

        // create new round
        Round r = new Round(d, getPlayers(), getDealer());

        // check lucky cards
        boolean dealerLuck = this.dealer.getHandFromPerson().checkSpecialHands();
        if (!dealerLuck) {
            // implement each turn
            for (Player p : getPlayers()) {
                r.playerTurn(p);
            }
            r.dealerTurn();
        } else {
            RoundDisplay.displayTurnResult(r.getTypeOfHand(this.dealer), this.dealer);
        }
        return r;
    }

    public void endRound(Round r) {

        if (!r.isAllPlayersChallenged()) { // if not all players are challenged
            // Ensure to challenge remaining players
            System.out.println("You auto challenges the rest of the players!"); // inform human

            r.dealerChallenge(this.players);

            // display challenge result
            RoundDisplay.displayChallengeResult(this.players, this.dealer);
        }
        // display round result
        RoundDisplay.displayRoundResult(this.players, this.dealer);
    }

    // Sub Game Methods
    public int getBetAmount(Player p) {
        int betAmt = 0; // Store bet Amt

        // Prompt player for bet amt
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter bet amt or 'A' for all-in: ");
        String answer = sc.next();

        // if player input is not A or a valid int value
        while ((!isInteger(answer) && !answer.equals("A")) || (isInteger(answer)
                && (Integer.parseInt(answer) <= 0 || Integer.parseInt(answer) > p.getCashAmt()))) {
            // Err msg
            System.out.println("Please enter a number more than 0 and less then your cash value or A.");

            // Reprompt
            System.out.print("Enter bet amt or 'A' for all-in: ");
            answer = sc.next();
        }

        // After receiving a valid player bet value
        if (answer.equals("A")) {
            // All-in
            betAmt = p.getCashAmt();
            System.out.println("Bet Amt: " + p.getCashAmt());
        } else {
            // Set bet amt
            betAmt = Integer.parseInt(answer);
        }

        return betAmt;
    }

    public TreeMap<String, Integer> getOverallGameResults() {
        TreeMap<String, Integer> results = new TreeMap<>();
        for (Person player : getPlayers()) {
            results.put(player.getName(), player.getCashAmt());
        }
        results.put(dealer.getName(), dealer.getCashAmt());
        return results;
    }

    public void displayOverallGameResults(Person human) {

        // get game result
        TreeMap<String, Integer> gameResultMap = getOverallGameResults();

        // get a sorted version (highest cash -> lowest cash)
        List<String> collect = gameResultMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(entry -> entry.getKey()).collect(Collectors.toList());

        // Display Game Result Message
        System.out.println(ANSI_CYAN);
        System.out.println("============================");
        System.out.println("||  GAME RESULT! \\(>o<)/ ||");
        System.out.println("============================");
        System.out.print(ANSI_RESET);
        int i = 1;
        for (String name : collect) {
            if (human.getName().equals(name)) {
                System.out.println(i + ". YOU " + "(balance: $" + gameResultMap.get(name) + ")");
            } else{
                System.out.println(i + ". " + name + " (balance: $" + gameResultMap.get(name) + ")");
            }
            i++;
        }

        // display human result
        if (human.getCashAmt() > dealer.getCashAmt()) {
            System.out.println("\n"+ ANSI_GREEN + "WOW HUAT AH YOU WON" + ANSI_RESET);
        } else if (human.getCashAmt() < dealer.getCashAmt()){
            System.out.println("\n"+ ANSI_RED + "AIYO you lost leh :( it's okay!" + ANSI_RESET);
        } else {
            System.out.println("\n"+ ANSI_CYAN + "Draw ah... Aya better than losing :/" + ANSI_RESET);
        }
        
        System.out.println(ANSI_CYAN + "----- END GAME -----" + ANSI_RESET);
    }

    public void removeBankruptPlayers() {
        Iterator<Player> iterator = getPlayers().iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (isHumanBankrupt(player)) {
                iterator.remove();
            }
        }
    }

    // Boolean Methods
    public boolean isDealerBankrupt() {
        return getDealer().getCashAmt() <= 0;
    }

    public static boolean isHumanBankrupt(Player player) {
        if (player instanceof Player && player.getCashAmt() <= 0) {
            return true;
        }
        return false;
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // public void terminateGame(String endGame) { //assume gets input whether
    // player wants to end game
    // if (isDealerBankrupt() || endGame.equals("yes")) {
    // //output overall results
    // displayOverallGameResults();
    // //dont know how to exit
    // }
    // }
}
