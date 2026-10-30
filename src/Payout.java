package Blackjack;
public class Payout {

    private Game game;

    private int money = 100;
    private int userBet;
    private int winnings;
    private int totalWinnings;

    public void getBet(){
        System.out.println("Current Balance: $" + getMoney());
        if(getMoney() == 0) {
            System.out.println("No money left, sorry!");
            System.exit(1);
        }
        System.out.print("Enter amount you would like to bet: ");
        userBet = Game.scanner.nextInt();
        Game.scanner.nextLine();
        if(getMoney() == 0) {
            System.out.println("No money left, sorry!");
            System.exit(1);
        }
        while(getUserBet() > getMoney()){
            System.out.println("Insufficient funds.");
            System.out.println("Current Balance: $" + getMoney());
            System.out.print("Enter amount you would like to bet: ");
            userBet = Game.scanner.nextInt();
            Game.scanner.nextLine();
        }
        while(getUserBet() <= 0) {
            System.out.println("Cannot bet $0 or less, please try again.");
            System.out.println("Current Balance: $" + getMoney());
            System.out.print("Enter amount you would like to bet: ");
            userBet = Game.scanner.nextInt();
            Game.scanner.nextLine();
        }
    }

    public void subtractBet(){
        money -= getUserBet();
    }

    public void getPayout(int userBet) {
        switch (game.getUserWins()) {
            case "Blackjack" -> {
                winnings = (int) Math.ceil(userBet * 2.5);
                money += winnings;
                System.out.println("You won $" + winnings + "!");
                System.out.println("Congratulations!");
            }
            case "Tie" -> {
                if(game.doubleDown == 'y') {
                    winnings = userBet;
                    money += winnings * 2;
                    System.out.println("Push, Your balance remains the same.");
                }
                else {
                    winnings = userBet;
                    money += winnings;
                    System.out.println("Push, Your balance remains the same.");
                }
            }
            case "Loss" -> {
                if(game.doubleDown == 'y') {
                    System.out.println("You lost $" + (userBet * 2));
                    System.out.println("Sorry :(");
                }
                else {
                    System.out.println("You lost $" + userBet);
                    System.out.println("Sorry :(");
                }

            }
            case "Win" -> {
                if(game.doubleDown == 'y') {
                    winnings = userBet * 2;
                    money += winnings * 2;
                    System.out.println("You won $" + winnings + "!");
                    System.out.println("Congratulations!");
                }
                else {
                    winnings = userBet;
                    money += winnings * 2;
                    System.out.println("You won $" + winnings + "!");
                    System.out.println("Congratulations!");
                }

            }
        }
    }

    public void getSplitPayout(int userBet) {
        for(int i = 0; i < game.getUserWinsSplit().size(); i++) {
            winnings = 0;
            switch (game.getUserWinsSplit().get(i)) {
                case "Win" -> {
                    if(i == 2) {
                        if(game.getUserWinsSplit().get(0).equals("y")) {
                            winnings = userBet * 2;
                            totalWinnings += winnings;
                            money += winnings * 2;
                            System.out.println("You won $" + winnings + " profit on Hand " + (i - 1) + ".");
                        }
                        // below is correct for no double
                        else {
                            winnings = userBet;
                            totalWinnings += winnings;
                            money += winnings * 2;
                            System.out.println("You won $" + winnings + " profit on Hand " + (i - 1) + ".");
                        }
                    }
                    else if (i == 3) {
                        if (game.getUserWinsSplit().get(1).equals("y")) {
                            winnings = userBet * 2;
                            totalWinnings += winnings;
                            money += winnings * 2;
                            System.out.println("You won $" + winnings + " profit on Hand " + (i - 1) + ".");
                        }
                            // below is correct for no double
                            else {
                                winnings = userBet;
                                totalWinnings += winnings;
                                money += winnings * 2;
                                System.out.println("You won $" + winnings + " profit on Hand " + (i - 1) + ".");
                            }
                        }
                }
                case "Loss" -> {
                    if (i == 2) {
                        if (game.getUserWinsSplit().get(0).equals("y")) {
                            winnings = userBet * 2;
                            totalWinnings -= winnings;
                            System.out.println("Sorry, you lost $" + winnings + " on Hand " + (i - 1) + ".");
                        }
                        // correct logic
                        else {
                            winnings = userBet;
                            totalWinnings -= winnings;
                            System.out.println("Sorry, you lost $" + winnings + " on Hand " + (i - 1) + ".");
                        }
                    }
                    else if (i == 3) {
                        if (game.getUserWinsSplit().get(1).equals("y")) {
                            winnings = userBet * 2;
                            totalWinnings -= winnings;
                            System.out.println("Sorry, you lost $" + winnings + " on Hand " + (i - 1) + ".");
                        } else {
                            winnings = userBet;
                            totalWinnings -= winnings;
                            System.out.println("Sorry, you lost $" + winnings + " on Hand " + (i - 1) + ".");
                        }
                    }
                }
                case "Tie" -> {
                    if(i == 2) {
                        if(game.getUserWinsSplit().get(0).equals("y")) {
                            winnings = userBet;
                            money += winnings * 2;
                            System.out.println("Push, You tied on Hand " + (i - 1) + ".");
                        }
                        else {
                            // correct logic for no double
                            winnings = userBet;
                            money += winnings;
                            System.out.println("Push, You tied on Hand " + (i - 1) + ".");
                        }
                    }
                    else if(i == 3) {
                        if(game.getUserWinsSplit().get(1).equals("y")) {
                            winnings = userBet;
                            money += winnings * 2;
                            System.out.println("Push, You tied on Hand " + (i - 1) + ".");
                        }
                        else {
                            // correct logic
                            winnings = userBet;
                            money += winnings;
                            System.out.println("Push, You tied on Hand " + (i - 1) + ".");
                        }
                    }
                }
            }
        }
        if(totalWinnings > 0){
            System.out.println("------------------------------------------------------");
            System.out.println("You won $" + totalWinnings + " across these two hands!");
            System.out.println("------------------------------------------------------");
        }
        else if(totalWinnings == 0){
            System.out.println("---------------");
            System.out.println("You broke even.");
            System.out.println("---------------");

        }
        else {
            System.out.println("----------------------------------------------------------");
            System.out.println("You lost $" + totalWinnings + " across these two hands. :(");
            System.out.println("----------------------------------------------------------");
        }
        System.out.println();
    }

    public boolean checkSplit(){
        boolean canSplit;
        if((getMoney() >= getUserBet())) {
            canSplit = true;
        }
        else{
            canSplit = false;
        }
        return canSplit;
    }

    public boolean checkDoubleMoneyMaybeThisOneWorks() {
        boolean canDouble;
        if(money >= getUserBet()) {
            canDouble = true;
        }
        else {
            canDouble = false;
        }
        return canDouble;
    }

    void resetPayout(){
        winnings = 0;
        totalWinnings = 0;
        userBet = 0;
        int losings = 0;
        int totalLosings = 0;
    }

    int getUserBet(){
        return userBet;
    }

    int getMoney(){
        return money;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
