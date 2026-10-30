package Blackjack;

import java.util.ArrayList;

public class Card {
    private final String suit;
    private final String rank;


    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }

    public String toString(){
        return suit + rank;
    }
}
