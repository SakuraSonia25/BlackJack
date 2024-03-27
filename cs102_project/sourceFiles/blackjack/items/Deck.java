package blackjack.items;

import java.util.*;

import blackjack.players.*;

public class Deck {
    private List<Card> deck;

    public Deck() {
        this.makeDeck();
        System.out.println("Deck created!");
        Collections.shuffle(deck);
        System.out.println("Deck has been shuffled!");
    }

    public void makeDeck() {
        this.deck = new ArrayList<>();
        String[] suits = {"Diamond", "Heart", "Club", "Spade"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    
        for (String suit : suits) {
            for (String value : values) {
                Card cardToAdd = new Card(suit, value);
                deck.add(cardToAdd);
            }
        }
    }

    public List<Card> getDeck() {
        return this.deck;
    }

    // Deal 1 Card
    public Card dealCards() {
        Card toDeal = deck.get(0);
        deck.remove(0);

        return toDeal;
    }
   
}