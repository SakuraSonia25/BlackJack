package blackjack.items;

import java.util.*;

public class Hand {
    // Constants For Card Icons
    private final char heart = '\u0003';
    private final char diamond = '\u0004';
    private final char club = '\u0005';
    private final char spade = '\u0006';
    
    // Attribute
    private ArrayList<Card> hand;

    // Constructor
    public Hand(Deck deck) {
        // create arraylist to store cards
        ArrayList<Card> playerHand = new ArrayList<>();

        // loop through hand
        for (int i = 0; i < 2; i++) {
            // add a random card to hand
            
            Card c = deck.dealCards();
            playerHand.add(c);
        }
        
        this.hand = playerHand;
    }

    // Getter Methods
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public int getHandSize() {
        ArrayList<Card> handList = this.hand;
        return handList.size();
    }

    public int getHandScore() {
        ArrayList<Card> aList = this.hand;

        int aListSize = aList.size();
        int totalHandScore = 0;

        for (int i = 0; i < aListSize; i++) {
            Card card = aList.get(i);
            totalHandScore += card.getCardScore();
        }

        return totalHandScore;
    }
   
    // Setter and Update Methods
    public void addCard(Card... cardList) {
        for (Card c : cardList) {
            this.hand.add(c);
        }
    }

    public void clear() {
        this.hand.clear();
    }

    // Boolean Methods 
    public boolean checkBlackjack() {
        if (this.getHandScore() == 21) {
            return true;
        }
        
        return false;
    }

    public boolean checkDoubleAs() {
        // handle double Ace
        if (this.getHandScore() == 22 && this.getHandSize() == 2) {
            return true;
        }

        return false;
    }
    
    public boolean checkDragon() { // call this when player has drawn the 5th card
        // Added the 5 cards check
        if (this.getHand().size() == 5 && this.getHandScore() <= 21) {
            return true;
        }
        return false;
    }

    public boolean checkTriple7() {
        ArrayList<Card> triple7List = this.hand;
        int triple7ListSize = triple7List.size();
        int triple7ListScore = this.getHandScore();

        int triple7Flag = 0;
        if (triple7ListSize == 3 && triple7ListScore == 21) {
            for (int i = 0; i < 3; i++) {
                Card test = triple7List.get(i);
                if (test.getCardValue().equals("7")) {
                    triple7Flag++;
                }
            }
        }

        if (triple7Flag == 3) {
            return true;
        }
        return false;
    }

    public boolean checkSpecialHands() {
        if (this.checkBlackjack() || this.checkDragon() || this.checkTriple7() || checkDoubleAs()) {
            return true;
        }
        return false;
    }

    public boolean checkForAcesCard() {
        for (Card c : this.hand) {
            if (c.getCardValue().equals("Ace")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBurst() {
        return this.getHandScore() > 21;
    }

    // Display Hand Methods
    public String displayCloseHand() {
        return String.format("(%d Cards)", this.hand.size());
    }

    public String displayOpenHand() {
        StringBuilder content = new StringBuilder();
    
        // Loop through each card in the hand
        for (Card c : this.hand) {
            // Construct the top border of the card
            content.append("\t_____");
        }
        content.append("\n");
    
        // Loop through each card in the hand
        for (Card c : this.hand) {
            // Construct the rank line of the card
            content.append("\t|");
            content.append(String.format("%-3s", c.getCardValue().charAt(0) == '1'? c.getCardValue(): c.getCardValue().charAt(0))); // Left-align the rank
            content.append("| ");
    
        }
        content.append("\n");
    
        // Loop through each card in the hand
        for (Card c : this.hand) {

            // Construct the suit line of the card
            content.append("\t| ");
            char suitSymbol;
            switch (c.getCardSuit()) {
                case "Diamond":
                    suitSymbol = diamond;
                    break;
                case "Club":
                    suitSymbol = club;
                    break;
                case "Heart":
                    suitSymbol = heart;
                    break;
                case "Spade":
                    suitSymbol = spade;
                    break;
                default:
                    suitSymbol = '?';
                    break;
            }
            content.append(suitSymbol);
            content.append(" | ");
        }
        content.append("\n");
    
        // Loop through each card in the hand
        for (Card c : this.hand) {
            // Construct the bottom border of the card
            content.append("\t|___| ");
        }
        content.append("\n");
    
        content.append("\ntotal score: " + this.getHandScore());
    
        return content.toString();
    }

    // @Override
    // public String toString() {
    //     return "Hand [hand=" + hand + "]";
    // }
}
