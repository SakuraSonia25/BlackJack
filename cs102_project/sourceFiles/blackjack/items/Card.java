package blackjack.items;

public class Card {

    // Constants For Card Icons
    private final char heart = '\u0003';
    private final char diamond = '\u0004';
    private final char club = '\u0005';
    private final char spade = '\u0006';

    // Attributes
    private String value;
    private String suit;
    private int score;

    // Constructor
    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;

        if (value.equals("Jack") || value.equals("Queen") || value.equals("King")) {
            this.score = 10;
        } else if (value.equals("Ace")) {
            this.score = 11;
        } else {
            this.score = Integer.parseInt(value);
        }
    }
     
    // Getter Methods
    public String getCardSuit() {
        return this.suit;
    }
    
    public String getCardValue() {
        return this.value;
    }
    
    public int getCardScore() {
        return this.score;
    }    
    
    // Update Ace card score after getting player input
    public void updateAce(int playerDecision) {
        String cardValue = this.value;
        if (cardValue.equals("Ace")) {
            this.score = playerDecision;
        }
    }
    
    // Display Card + score its adding to hand
    public String displayCard() {
        StringBuilder content = new StringBuilder();
    
        // Construct the top border of the card
        content.append("\t_____");

        content.append("\n");
    
        // Construct the rank line of the card
        content.append("\t|");
        content.append(String.format("%-3s", this.getCardValue().charAt(0) == '1'? this.getCardValue(): this.getCardValue().charAt(0))); // Left-align the rank
        content.append("| ");

        content.append("\n");
    
        // Construct the suit line of the card
        content.append("\t| ");
        char suitSymbol;
        switch (this.getCardSuit()) {
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

        content.append("\n");

        // Construct the bottom border of the card
        content.append("\t|___| ");

        int totalScoreAdded = 0;
    
        content.append("\n");
    
        content.append("\nadded " + this.getCardScore() + " to your hand");
    
        return content.toString();
    }

    // @Override
    // public String toString() {
    //     return "Card [value=" + value + ", suit=" + suit + ", score=" + score + "]";
    // }

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