package blackjack.items;

import java.util.*;

public class Deck {
    // Attribute
    private List<Card> deck;

    // Constructor
    public Deck() {
        this.makeDeck();
        System.out.println("Deck created!");
        Collections.shuffle(deck);
        System.out.println("Deck has been shuffled!\n");
    }

    // Create a deck of cards
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

    // Getter Method
    public List<Card> getDeck() {
        return this.deck;
    }

    // Deal out 1 card
    public Card dealCards() {
        Card toDeal = deck.get(0);
        deck.remove(0);

        return toDeal;
    }
   
}