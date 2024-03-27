package blackjack.players;

import java.util.*;
import java.io.*;

import blackjack.items.*;

public class BotPlayer extends Player {

    public BotPlayer(String name, int cashAmt, Hand hand, int betAmt) {
        super(name, cashAmt, hand, betAmt);
    }

    public char determineAction() {

        // Introduce a delay of 1.5 seconds before bot does each action
        try {
            Thread.sleep(1500);
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
        int aceCount = 0; //counter for aces in hand

        for (Card c : super.getHandFromPerson().getHand()) {
            if (c.getCardValue().equals("Ace")) {
                aceCount++;
            }
        }

        // check if bot has Ace and no double ace
        boolean hasAce = super.getHandFromPerson().checkForAcesCard() && aceCount == 1;

        // update the value of Ace
        if (hasAce) {
            handScore = handScore - 11 + chooseAceValue();
        }

        // bot will be more aggressive as it has the flexibility of Ace
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

    // Method to choose Ace value
    public int chooseAceValue() {
        int handScore = super.getHandScore();
        int handValueWith11 = handScore;
        int handValueWith10 = handScore - 1;
        int handValueWith1 = handScore - 10;

        if (handValueWith11 <= 21) {
            return 11;
        } else if (handValueWith1 <= 21) {
            return 1;
        } else {

            // If both cause the hands to bust, choose 10
            return 10;
        }
    }

        // Method to pull random reaction for bot
    public String getPlayerRandomReaction() {
        List<String> reactions = new ArrayList<>();

        String filePath = "src/blackjack/players/player.txt";

        try (Scanner sc = new Scanner(new File(filePath))) {
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                reactions.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int index = random.nextInt(reactions.size());
        return reactions.get(index);
    }
}
