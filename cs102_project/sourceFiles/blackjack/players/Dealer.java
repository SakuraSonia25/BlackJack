package blackjack.players;

import blackjack.items.*;

public class Dealer extends Person{

    //Constructor - Write it explicity
    public Dealer(String name, int cashAmt, Hand hand) {
        super(name, cashAmt, hand);

    }

    //Method to challenge
    //Prompting done in menu, this method will be called afterwards
    public char challenge(Player opponent) {

        //Get hand of opponent
        // System.out.println(opponent);
        // System.out.println(this.toString());

        //Calculate result
        /*
            - else if, any hand bust (>21) -- dealer wins (does not matter if dealer > 21)
            - else, compare value
            if player less than 21 && player less than dealer -- dealer wins
            if player less than 21 && player more than dealer -- dealer loses
        */

        //NEED TO CHECK IF IT IS BETTER/CLEANER TO HAVE getHandScore METHOD IN PERSON INSTEAD
        //OF HAND CLASS
       
        Hand hand = this.getHandFromPerson();
        Hand opponentHand = opponent.getHandFromPerson();
        int opponentHandScore = opponentHand.getHandScore();
        int handScore = hand.getHandScore();

        // if( opponentHandScore > 21) {
        //     return true;
        // } else if(opponentHandScore < 21 && opponentHandScore < handScore) {
        //     return true;
        // } else{
        //     return false;
        // }

        // dealer got luckyhand
        if (hand.checkBlackjack() || hand.checkDragon() || hand.checkTriple7()){
            // dealer win
            if (!(opponentHand.checkBlackjack() || opponentHand.checkDragon() || opponentHand.checkTriple7()) || opponentHand.checkBurst()) {
                return 'w';
            }
        } else {
            // dealer lose
            if ((opponentHand.checkBlackjack() || opponentHand.checkDragon() || opponentHand.checkTriple7()) || opponentHandScore < handScore) {
                return 'l';
            } 
            // dealer win
            else if (opponentHand.checkBurst() && !hand.checkBurst()) {
                return 'w';
            }
        }
        
        return 'd';
    }
    
}
