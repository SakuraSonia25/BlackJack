package blackjack.players;

import blackjack.items.*;
import java.util.*;

public class Person {

    //Atributes
    private String name;
    private int cashAmt;
    private Hand hand;   //Need import statement

    //Constructor
    public Person(String name, int cashAmt, Hand hand) {
        this.name = name;
        this.cashAmt = cashAmt;
        this.hand = hand;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(int cashAmt) {
        this.cashAmt = cashAmt;
    }

    public Hand getHandFromPerson() {
        return hand;
    }

    public int getHandScore() {
        ArrayList<Card> aList = getHandFromPerson().getHand();

        int aListSize = aList.size();
        int totalHandScore = 0;

        for (int i = 0; i < aListSize; i++) {
            Card card = aList.get(i);
            totalHandScore += card.getCardScore();
        }

        return totalHandScore;
    }

    public int getHandSize() {
        ArrayList<Card> handList = getHandFromPerson().getHand();
        return handList.size();
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    //Check for hit
    //Check for stand
    //Safer to put in Round


    // Method to clear hand
    public void clearHand() {
       this.hand.clear();
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    }
}