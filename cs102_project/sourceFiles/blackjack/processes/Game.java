package blackjack.processes;

import java.util.*;

import blackjack.players.*;
import blackjack.items.*;

public class Game {

    private List<Player> players;  
    private Dealer dealer;

    public Game(List<Player> players, Dealer dealer) {
        this.players = players;
        this.dealer = dealer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    // public void startRound() {
    public Round startRound() {
        Deck d = new Deck();
        int betAmt = 500;
        for (Player p : this.getPlayers()) {
            if (!(p instanceof BotPlayer)) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter bet amt: ");
                betAmt = sc.nextInt();

                while (betAmt > 5000 || betAmt > p.getCashAmt()) {
                    System.out.println("Too high. Enter bet amt: ");
                    betAmt = sc.nextInt();
                }
            }
            else {
                if (betAmt > p.getCashAmt()) {
                    betAmt = p.getCashAmt();
                }
            }
            p.setBetAmt(betAmt);
            p.setHand(new Hand(d));
           
        }
        this.dealer.setHand(new Hand(d)); 

        // create new round
        Round r = new Round(d, getPlayers(), getDealer());
        
        //check lucky cards
        boolean dealerLuck = r.hvStartingLuckyHand(getDealer());
        if (!dealerLuck) {
            //implement each turn
            for (Player p : getPlayers()) {
                r.playerTurn(p);
            }
            r.dealerTurn();
        }
        return r;
    }

    public void endRound(Round r) {

       if (!r.isAllPlayersChallenged()) {
            r.dealerChallengeAll();
       }

       r.displayRoundResult();
    }

    public Map<String, Integer> displayOverallGameResults() {
        Map<String, Integer> results = new HashMap<>();
        for (Person player : getPlayers()) {
            results.put(player.getName(), player.getCashAmt());
        }
        return results;
    }

    public boolean isDealerBankrupt() {
        return getDealer().getCashAmt() <= 0;
    }

    public static boolean isHumanBankrupt(Player player) {
        if (player instanceof Player && player.getCashAmt() <= 0) {
            return true;
        }
        return false;
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

    // public void terminateGame(String endGame) {  //assume gets input whether player wants to end game
    //     if (isDealerBankrupt() || endGame.equals("yes")) {
    //         //output overall results
    //         displayOverallGameResults();
    //         //dont know how to exit
    //     }
    // }
}
