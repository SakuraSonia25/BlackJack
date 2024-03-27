package blackjack.players;

import blackjack.items.*;

public class Player extends Person {
    
    //Attributes
    private int betAmt;

    //Constructor 
    public Player(String name, int cashAmt, Hand hand, int betAmt) {
        super(name, cashAmt, hand);
        this.betAmt = betAmt;
    }

    public int getBetAmt() {
        return betAmt;
    }
    
    public void setBetAmt(int betAmt) {
        this.betAmt = betAmt;
    }
}
