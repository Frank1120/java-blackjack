package Blackjack;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    Random random = new Random();

    ArrayList<Card> allCards = new ArrayList<>();
    private final String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private final String[] suits = {"♠", "♥", "♦", "♣"};

    void generateDeck() {
        for (String rank : getRanks()) {
            for (String suit : getSuits()) {
                allCards.add(new Card(rank, suit));
            }
        }
    }

    String[] getRanks(){
        return ranks;
    }

    String[] getSuits(){
        return suits;
    }

    Card drawCard(){
        Card card = allCards.get(random.nextInt(allCards.size()));
        allCards.remove(card);

        return card;
    }
}
