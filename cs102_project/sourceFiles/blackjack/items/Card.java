package blackjack.items;

public class Card {
    // private static final int maxValue = 1;
    // private static final int minValue = 11;

    private String value;
    private String suit;
    private int score;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;

        int cardScore = 0;
        if (value.equals("Jack") || value.equals("Queen") || value.equals("King")) {
            cardScore += 10;
        } else if (value.equals("Ace")) {
            cardScore += 11;
        } else {
            cardScore += Integer.parseInt(value);
        }
        this.score = cardScore;
    }
     
    public String getCardSuit() {
        return this.suit;
    }
    
    public String getCardValue() {
        return this.value;
    }
    
    public int getCardScore() {
        return this.score;
    }    
    
    //After getting player's decision, update total hand accordingly for Ace card
    public void updateAce(int playerDecision) {
        String cardValue = this.value;
        if (cardValue.equals("Ace")) {
            this.score = playerDecision;
        }
    }
    
    @Override
    public String toString() {
        return "Card [value=" + value + ", suit=" + suit + ", score=" + score + "]";
    }

    // // Dealer's side
    // public int drawAce(int totalHand, int totalHandScore) {
        //     if (totalHand == 2) {
            //         if (totalHandScore + 11 <= 21) {
    //             totalHandScore += 11;
    //         } 
    //         else if (totalHandScore + 10 <= 21) {
    //             totalHandScore += 10;
    //         }
    //         else {
    //             totalHandScore += 1; // Ace counts as 1
    //         }
    //     } else if (numCards == 3) {
    //         if (totalHandScore + 10 <= 21) {
    //             totalHandScore += 10;
    //         } else {
    //             totalHandScore += 1; // Ace counts as 1
    //         }
    //     }
    //     else {
    //         return totalHandScore += 1;
    //     }
    // }
}