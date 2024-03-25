package blackjack.players;

import blackjack.items.*;
import java.util.ArrayList;

public class BotPlayer extends Player {

    // private int betAmt;

    public BotPlayer(String name, int cashAmt, Hand hand, int betAmt) {
        super(name, cashAmt, hand, betAmt);
        // this.betAmt = betAmt;
    }

    public char determineAction() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Adjust bot's decision-making based on hand type
        if (isPair()) {
            return handlePair();

        } else if (isSoftHand()) {
            return handleSoftHand();

        } else {
            return handleHardHand();
        }
    }

    private boolean isPair() {
        ArrayList<Card> hand = super.getHandFromPerson().getHand();

        return hand.size() == 2 && hand.get(0).getCardValue().equals(hand.get(1).getCardValue());
    }

    private boolean isSoftHand() {
        ArrayList<Card> hand = super.getHandFromPerson().getHand();
        int numberofAces = 0;
        int totalValue = 0;
        boolean hasAce = false;

        for (Card card : hand) {
            if (card.getCardValue().equals("Ace")) {
                numberofAces++;
                hasAce = true;
            }
        }

        while (numberofAces > 0 && totalValue <= 11) {
            totalValue += 10;
            numberofAces--;
        }

        return hasAce && totalValue <= 21;
        
    }

    private char handlePair() {
        ArrayList<Card> hand = super.getHandFromPerson().getHand();

        if (hand.get(0).getCardValue().equals("Ace")) {
            return 's'; // Always stand if the pair is Ace
        } else {
            return 'h';
        }
    }

    private char handleSoftHand() {
        int handScore = super.getHandScore();

        // bot will be more aggressive as it has the flexibility of the Ace
        if (handScore < 19) {
            return 'h';
        } else {
            return 's';
        }
    }

    private char handleHardHand() {
        int handScore = super.getHandScore();

        // bot will be more conservative as it has no Ace
        if (handScore >= 16) {
            return 's';
        } else {
            return 'h';
        }
    }
}
