package blackjack.players;

import blackjack.items.*;

public class Dealer extends Person{

    //Constructor
    public Dealer(String name, int cashAmt, Hand hand) {
        super(name, cashAmt, hand);

    }

    //Method to challenge
    //Prompting done in menu, this method will be called afterwards
    public char challenge(Player opponent) {
       
        Hand hand = this.getHandFromPerson();
        Hand opponentHand = opponent.getHandFromPerson();
        int opponentHandScore = opponentHand.getHandScore();
        int handScore = hand.getHandScore();

        // Compare luckyhand
        if (hand.checkSpecialHands() && !(opponentHand.checkSpecialHands())){
            return 'w';
        } 
        else if (hand.checkSpecialHands() && opponentHand.checkSpecialHands()) {
            return 'd';
        }
        else {
            if (opponentHand.checkSpecialHands()) {
                return 'l';
            } 
        }

        //check normal cards
        if (handScore == opponentHandScore || (hand.checkBurst() && opponentHand.checkBurst())) {
            return 'd';
        }
        else if (handScore > opponentHandScore && !hand.checkBurst()) {
            return 'w';
        }
        else if (opponentHand.checkBurst()) {
            return 'w'; 
        }
        return 'l';
    }
    
}
