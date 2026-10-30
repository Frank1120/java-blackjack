package Blackjack;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Game game = new Game();
        Payout payout = new Payout();

        game.setPayout(payout);
        payout.setGame(game);

        char playAgain = 'y';
        while(playAgain == 'y') {
            payout.getBet();
            game.playGame();
            System.out.print("Would you like to play again? (y/n): ");
            playAgain = scanner.nextLine().toLowerCase().charAt(0);

            switch(playAgain) {
                case 'y', 'n' -> {}
                default ->  {
                    System.out.println("Incorrect input, please try again.");
                    while(playAgain != 'y' && playAgain != 'n') {
                        System.out.print("Would you like to play again? (y/n): ");
                        playAgain = scanner.nextLine().toLowerCase().charAt(0);
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Your final balance was $" + payout.getMoney());
        System.out.println("Thank you for playing! Have a nice day.");
    }
}
