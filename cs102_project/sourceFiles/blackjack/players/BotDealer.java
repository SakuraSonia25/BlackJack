package blackjack.players;

import blackjack.items.*;

import java.util.ArrayList;
import java.util.List;

public class BotDealer extends Dealer {

    public BotDealer(String name, int cashAmt, Hand hand) {
        super(name, cashAmt, hand);
    }

    // for dealer to choose when to hit or challenge
    public boolean determineHit() {

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

    // for dealer to choose who to challenge
    public Player determineChallenge(List<Player> listOfPlayers) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Player playerToChallenge = choosePlayerToChallenge(listOfPlayers);

        return playerToChallenge;

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

    private boolean handlePair() {
        ArrayList<Card> hand = super.getHandFromPerson().getHand();

        if (hand.get(0).getCardValue().equals("Ace")) {
            return false; // Always stand if the pair is Ace
        } else if (hand.get(0).getCardValue().equals("2") || hand.get(0).getCardValue().equals("3")){
            return true;
        } else {
            return super.getHandScore() < 17;
        }
    }

    private boolean handleSoftHand() {
        int handScore = super.getHandScore();

        // bot will be more aggressive as it has the flexibility of the Ace
        return handScore < 19;
    }

    private boolean handleHardHand() {
        int handScore = super.getHandScore();

        // bot will be more conservative as it has no Ace
        return handScore < 16;
    }

    // Method to choose which player to challenge
    private Player choosePlayerToChallenge(List<Player> players) {
        
        Player selectedPlayer = players.getFirst();

        if (determineHit() == true) {
            if (isSoftHand()) {
                selectedPlayer = findPlayerWithMostCards(players);
            } else {
                selectedPlayer = findPlayerWithLeastCards(players);
            }
        } else {
            if (isSoftHand()) {
                selectedPlayer = findPlayerWithLeastCards(players);
            } else {
                selectedPlayer = findPlayerWithMostCards(players);
            }
        }

        return selectedPlayer;
    }

    // Method to find the player with the least cards
    private Player findPlayerWithLeastCards(List<Player> players) {
        Player leastCardsPlayer = players.getFirst();
        for (Player current : players) {
            int size = current.getHandSize();
            if(size < leastCardsPlayer.getHandSize()) {
                leastCardsPlayer = current;
            }
        }
    
        return leastCardsPlayer;
    }

    // Method to find the player with the most cards
    private Player findPlayerWithMostCards(List<Player> players) {

        Player mostCardsPlayer = players.getFirst();
        for (Player current : players) {
            int size = current.getHandSize();
            if(size > mostCardsPlayer.getHandSize()) {
                mostCardsPlayer = current;
            }
        }

        return mostCardsPlayer;
    }
}