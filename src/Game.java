package Blackjack;

import java.util.ArrayList;
import java.util.Scanner;


public class Game {
    static Scanner scanner = new Scanner(System.in);
    Deck deck = new Deck();
    private Payout payout;
    ArrayList<Integer> userCardValues = new ArrayList<>();
    ArrayList<Integer> dealerCardValues = new ArrayList<>();
    ArrayList<Card> userCards = new ArrayList<>();
    ArrayList<Card> dealerCards = new ArrayList<>();
    int userTotal = 0;
    int dealerTotal = 0;
    boolean done = false;
    int userAceCount = 0;
    int dealerAceCount = 0;
    int[] userResults;
    int[] dealerResults;
    private String userWins = "";
    char split = 'n';
    char doubleDown = 'n';
    boolean canSplit;
    ArrayList<Card> splitHand = new ArrayList<>();
    ArrayList<Integer> splitHandValues = new ArrayList<>();
    int splitHandTotal;
    int splitAceCount;
    ArrayList<String> userWinsSplit = new ArrayList<>();
    ArrayList<Integer> splitHandTotalArray = new ArrayList<>();
    boolean dealerBJ = false;
    boolean canDoubleCardCheck = false;
    boolean canDoubleMoneyCheck = false;

    void playGame() {
        deck.generateDeck();
        payout.subtractBet();
        for (int i = 0; i < 2; i++) {
            userResults = userCardLogic(userCardValues, userCards, userTotal, userAceCount);
            userTotal = userResults[0];
            userAceCount = userResults[1];
            dealerResults = dealerCardLogic(dealerCardValues, dealerCards, dealerTotal, dealerAceCount);
            dealerTotal = dealerResults[0];
            dealerAceCount = dealerResults[1];
        }

        System.out.println("Welcome to Blackjack!");
        System.out.print("Your cards: " + userCards + " (Total: " + (userTotal) + ")\n");
        System.out.println("Dealer's face-up card: " + (dealerCards.getFirst()));


        if(userCards.get(0).toString().contains("A") && userCards.get(1).toString().contains("A") || userCards.get(0).toString().charAt(0) == userCards.get(1).toString().charAt(0)) {
                if(dealerTotal == 21 && dealerCards.size() == 2) {
                    dealerBJ = true;
                }
                canSplit = payout.checkSplit();
                if(canSplit && !dealerBJ) {
                    askForSplit();
                }
            }
        if(dealerTotal == 21 && dealerCards.size() == 2) {
            dealerBJ = true;
        }

        askForDouble();

        while(userTotal < 21 && dealerTotal < 21 && !done && doubleDown == 'n' && split == 'n'){
            userGameLogic();
        }

        if(split == 'n'){
            userWins = getResults();
            payout.getPayout(payout.getUserBet());
        }

        if(split == 'y'){
            payout.getSplitPayout(payout.getUserBet());
        }
        resetGame();
        payout.resetPayout();
    }

    int[] userCardLogic(ArrayList<Integer> userCardValues, ArrayList<Card> userCards, int userTotal, int userAceCount) {
        for (int i = 0; i < 1; i++) {
            Card card = deck.drawCard();
            userCards.add(card);
            if ((card.toString().contains("J") || (card.toString().contains("Q")) || (card.toString().contains("K")))) {
                userCardValues.add(10);
                userTotal += 10;
            } else if (card.toString().contains("A")) {
                userCardValues.add(11);
                userTotal += 11;
                userAceCount++;
            } else if(card.toString().contains("10")) {
                userCardValues.add(10);
                userTotal += 10;
            } else {
                int cardValue = Integer.parseInt(card.toString().substring(0, 1));
                userCardValues.add(cardValue);
                userTotal += cardValue;
            }
        }
        if(userCards.size() == 2 && userTotal == 22){
            userCardValues.set(0, 1);
            userTotal -= 10;
            userAceCount--;
        }
        while(userTotal > 21 && userAceCount > 0){
            for(int i = 0; i < userCardValues.size(); i++) {
                if(userCardValues.get(i) == 11) {
                    userTotal -= 10;
                    userAceCount--;
                    userCardValues.set(i, 1);
                    break;
                }
            }
        }
        if(userCards.size() > 2){
            System.out.println("You drew a " + userCards.getLast() + " (Total: " + userTotal + ")");
        }
        return new int[] {userTotal, userAceCount};
    }

    void dealerGameLogic(){
        System.out.println("The dealer reveals their hidden card: " + dealerCards.getLast() + " (Total: " + dealerTotal + ")");
        while (dealerTotal < 17) {
            dealerResults = dealerCardLogic(dealerCardValues, dealerCards, dealerTotal, dealerAceCount);
            dealerTotal = dealerResults[0];
            dealerAceCount = dealerResults[1];
            System.out.println("Dealer drew a " + dealerCards.getLast() + " (Total: " + dealerTotal + ")");
        }
    }

    void userGameLogic(){
        char userInput = '\n';
        System.out.print("Do you want to (H)it or (S)tay?: ");
        userInput = scanner.next().toUpperCase().charAt(0);
        switch (userInput) {
            case 'H' -> {
                userResults = userCardLogic(userCardValues, userCards, userTotal, userAceCount);
                userTotal = userResults[0];
                userAceCount = userResults[1];
                if(userTotal == 21 && userCards.size() > 2) {
                    dealerGameLogic();
                }
                if(doubleDown == 'y') {
                    done = true;
                    dealerGameLogic();
                }
            }
            case 'S' -> {
                dealerGameLogic();
                done = true;
            }
            default -> System.out.println("Incorrect input, please try again.");
        }
        scanner.nextLine();
    }

    ArrayList<String> splitLogic() {
        for (int i = 0; i < 2; i++) {
            resetSplitLogic();
            System.out.println("----------------");
            System.out.println("Hand " + (i + 1));
            System.out.println("----------------");
            splitHand.add(userCards.get(i));
            splitHandValues.add(userCardValues.get(i));
            splitHandTotal = userCardValues.get(i);

            if(splitHandTotal == 11 && splitHand.get(0).toString().contains("A") && i == 1) {
                splitHandTotal -= 10;
                splitHandValues.set(0, 1);
            }

            if (splitHand.size() == 1) {
                userResults = splitCardLogic(splitHand, splitHandValues, splitAceCount);
                splitHandTotal += userResults[0];
                splitAceCount += userResults[1];
                if(splitHand.get(1).toString().contains("A")) {
                    splitAceCount--;
                }
            }

            if(splitHand.getFirst().toString().contains("A") && splitHandTotal != 22) {
                splitAceCount++;
                splitHandValues.set(i, 11);
                splitHandTotal += 10;
            }

            if(splitHand.size() == 2 && splitHandTotal == 22){
                splitHandValues.set(0, 1);
                splitHandTotal -= 10;
                splitAceCount--;
            }

            System.out.println("Your second card for hand " + (i + 1) + " is " + splitHand.getLast() + " (Total: " + splitHandTotal + ")");

            askForDoubleInSplit();

            if(splitHand.getFirst().toString().contains("A")) {
                done = true;
                if(i == 1) {
                    dealerGameLogic();
                }
            }

            while (splitHandTotal < 21 && !done) {
                char userInput = '\n';
                System.out.print("Do you want to (H)it or (S)tay?: ");
                userInput = scanner.next().toUpperCase().charAt(0);
                switch (userInput) {
                    case 'H' -> {
                        if(doubleDown == 'y') {
                            done = true;
                        }
                        userResults = splitCardLogic(splitHand, splitHandValues, splitHandTotal);
                        splitHandTotal = userResults[0];
                        splitAceCount = userResults[1];
                        if (splitHandTotal == 21 && splitHand.size() > 2 && i == 1) {
                            dealerGameLogic();
                        }
                        if (splitHandTotal > 21 && i == 1) {
                            dealerGameLogic();
                            done = true;
                        }

                        if(i == 1 && doubleDown == 'y' && splitHandTotal != 21) {
                            dealerGameLogic();
                            done = true;
                        }
                    }
                    case 'S' -> {
                        if (i == 1) {
                            dealerGameLogic();
                        }
                        done = true;

                    }

                    default -> System.out.println("Incorrect input, please try again.");
                }
                scanner.nextLine();
            }

            splitHandTotalArray.add(splitHandTotal);
            if(i == 1) {
                for(int j = 0; j < splitHandTotalArray.size(); j++) {
                    splitHandTotal = splitHandTotalArray.get(j);
                    userWinsSplit = getSplitResults();
                }
            }
        }
        return userWinsSplit;
    }

    boolean doubleCheckCards(){
        if((userTotal == 9 && split == 'n' || userTotal == 10 && split == 'n' || userTotal == 11 && split == 'n') || splitHandTotal == 9
                || splitHandTotal == 10 || splitHandTotal == 11 && splitHand.size() <= 2) {
                canDoubleCardCheck = true;
        }
        else {
            canDoubleCardCheck = false;
        }
        return canDoubleCardCheck;
    }

    int[] splitCardLogic(ArrayList<Card> splitHand, ArrayList<Integer> splitHandValues, int splitHandTotal) {
        for (int i = 0; i < 1; i++) {
            Card card = deck.drawCard();
                splitHand.add(card);
                if ((card.toString().contains("J") || (card.toString().contains("Q")) || (card.toString().contains("K")))) {
                    splitHandValues.add(10);
                    splitHandTotal += 10;
                } else if (card.toString().contains("A")) {
                    splitHandValues.add(11);
                    splitHandTotal += 11;
                    splitAceCount++;
                } else if(card.toString().contains("10")) {
                    splitHandValues.add(10);
                    splitHandTotal += 10;
                } else {
                    int cardValue = Integer.parseInt(card.toString().substring(0, 1));
                    splitHandValues.add(cardValue);
                    splitHandTotal += cardValue;
                }
            }
            if(splitHand.size() == 2 && splitHandTotal == 22){
                splitHandValues.set(0, 1);
                splitHandTotal -= 10;
                splitAceCount--;
            }

            while(splitHandTotal > 21 && splitAceCount > 0){
                for(int i = 0; i < splitHandValues.size(); i++) {
                    if(splitHandValues.get(i) == 11) {
                        splitHandTotal -= 10;
                        splitAceCount--;
                        splitHandValues.set(i, 1);
                        break;
                    }
                }
            }
            if(splitHand.size() > 2) {
                System.out.println("You drew a " + splitHand.getLast() + " (Total: " + splitHandTotal + ")");
            }
            return new int[] {splitHandTotal, splitAceCount};
        }

    int[] dealerCardLogic(ArrayList<Integer> dealerCardValues, ArrayList<Card> dealerCards, int dealerTotal, int dealerAceCount) {
        for (int i = 0; i < 1; i++) {
            Card card = deck.drawCard();
            deck.allCards.remove(card);
            dealerCards.add(card);
            if ((card.toString().contains("J") || (card.toString().contains("Q")) || (card.toString().contains("K")))) {
                dealerCardValues.add(10);
                dealerTotal += 10;
            } else if (card.toString().contains("A")) {
                dealerCardValues.add(11);
                dealerTotal += 11;
                dealerAceCount++;
            } else if (card.toString().contains("10")) {
                dealerCardValues.add(10);
                dealerTotal += 10;
            } else {
                int cardValue = Integer.parseInt(card.toString().substring(0, 1));
                dealerCardValues.add(cardValue);
                dealerTotal += cardValue;
            }
            if (dealerCards.size() == 2 && dealerTotal == 22) {
                dealerCardValues.set(0, 1);
                dealerTotal -= 10;
                dealerAceCount--;
            }
            if (dealerTotal > 21 && dealerAceCount > 0) {
                for (int j = 0; j < dealerCardValues.size(); j++) {
                    if (dealerCardValues.get(j) == 11) {
                        dealerCardValues.set(j, 1);
                        dealerTotal -= 10;
                        dealerAceCount--;
                        break;
                    }
                }
            }
        }
        return new int[]{dealerTotal, dealerAceCount};
    }

    String getResults() {
        String userWins;
        if(dealerTotal <= 21 && dealerTotal == userTotal) {
            System.out.println("Push! You tie.");
            userWins = "Tie";
            return userWins;
        } else if(userTotal == 21 && userCardValues.size() == 2) {
            System.out.println("Blackjack! You win.");
            userWins = "Blackjack";
            return userWins;
        } else if(dealerTotal == 21 && dealerCardValues.size() == 2) {
            System.out.println("The dealer reveals their hidden card: " + dealerCards.getLast() + " (Total: " + dealerTotal + ")");
            System.out.println("Dealer Blackjack, you lose. :(");
            userWins = "Loss";
            return userWins;
        } else if(dealerTotal > 21) {
            System.out.println("Dealer busts! You win!");
            userWins = "Win";
            return userWins;
        } else if(userTotal > 21) {
            System.out.println("You bust! You lose!");
            userWins = "Loss";
            return userWins;
        } else if(userTotal > dealerTotal) {
            System.out.println("You win!");
            userWins = "Win";
            return userWins;
        } else {
            System.out.println("You lose!");
            userWins = "Loss";
            return userWins;
        }
    }

    ArrayList<String> getSplitResults() {
        if(dealerTotal <= 21 && dealerTotal == splitHandTotal) {
            userWinsSplit.add("Tie");
            return userWinsSplit;
        } else if(dealerTotal == 21 && dealerCardValues.size() == 2) {
            userWinsSplit.add("Loss");
            return userWinsSplit;
        }  else if(splitHandTotal > 21) {
            userWinsSplit.add("Loss");
            return userWinsSplit;
        } else if(dealerTotal > 21) {
            userWinsSplit.add("Win");
            return userWinsSplit;
        } else if(splitHandTotal > dealerTotal) {
            userWinsSplit.add("Win");
            return userWinsSplit;
        } else {
            userWinsSplit.add("Loss");
            return userWinsSplit;
        }
    }

    void askForSplit(){
        do {
            System.out.print("Would you like to split? (y/n): ");
            split = scanner.nextLine().toLowerCase().charAt(0);
            canDoubleMoneyCheck = payout.checkDoubleMoneyMaybeThisOneWorks();

            if(split == 'y'){
                payout.subtractBet();
                while(splitHandTotal < 21 && dealerTotal < 21 && !done && split == 'y') {
                    userWinsSplit = splitLogic();
                }
                done = true;
            } else if(split == 'n'){
                System.out.println("You will not split this hand.");
                done = true;
            } else {
                System.out.println("Incorrect input.");
            }
        } while(split != 'n' && !done);
        done = false;
    }

    void askForDouble() {
        canDoubleCardCheck = doubleCheckCards();
        canDoubleMoneyCheck = payout.checkDoubleMoneyMaybeThisOneWorks();
        if (canDoubleCardCheck && canDoubleMoneyCheck && split == 'n' && !dealerBJ) {

            do {
                System.out.print("Would you like to double? (y/n): ");
                doubleDown = scanner.nextLine().toLowerCase().charAt(0);
                if (doubleDown == 'y' && split == 'n') {
                    payout.subtractBet();
                    while (userTotal < 21 && dealerTotal < 21 && !done) {
                        userGameLogic();
                    }
                    done = true;
                } else if (doubleDown == 'n') {
                    System.out.println("You will not double this hand");
                    done = true;
                } else {
                    System.out.println("Incorrect Input.");
                }
            } while (doubleDown != 'n' && !done);
            done = false;
        }
    }

    void askForDoubleInSplit(){
        canDoubleCardCheck = doubleCheckCards();
        canDoubleMoneyCheck = payout.checkDoubleMoneyMaybeThisOneWorks();
        if (canDoubleCardCheck && canDoubleMoneyCheck) {

            do {
                System.out.print("Would you like to double? (y/n): ");
                doubleDown = scanner.nextLine().toLowerCase().charAt(0);
                if (doubleDown == 'y') {
                    userWinsSplit.add("y");
                    payout.subtractBet();
                    done = true;
                } else if (doubleDown == 'n') {
                    userWinsSplit.add("n");
                    System.out.println("You will not double this hand");
                    done = true;
                } else {
                    System.out.println("Incorrect Input.");
                }
            } while (doubleDown != 'n' && !done);
            done = false;
        }
        else {
            userWinsSplit.add("n");
        }
    }

    void resetGame(){
        deck.allCards.removeAll(deck.allCards);
        dealerTotal = 0;
        userTotal = 0;
        dealerCards.removeAll(dealerCards);
        userCards.removeAll(userCards);
        userCardValues.removeAll(userCardValues);
        dealerCardValues.removeAll(dealerCardValues);
        userAceCount = 0;
        dealerAceCount = 0;
        done = false;
        split = 'n';
        splitHandTotal = 0;
        splitAceCount = 0;
        splitHand.removeAll(splitHand);
        splitHandValues.removeAll(splitHandValues);
        userWinsSplit.removeAll(userWinsSplit);
        userWins = "";
        canSplit = false;
        splitHandTotalArray.removeAll(splitHandTotalArray);
        canDoubleCardCheck = false;
        canDoubleMoneyCheck = false;
        dealerBJ = false;
    }

    void resetSplitLogic() {
        splitHandTotal = 0;
        splitAceCount = 0;
        done = false;
        splitHand.removeAll(splitHand);
        splitHandValues.removeAll(splitHandValues);
        doubleDown = 'n';
    }

    String getUserWins(){
        return userWins;
    }

    ArrayList<String> getUserWinsSplit(){
        return userWinsSplit;
    }

    public void setPayout(Payout payout){
        this.payout = payout;
    }
}