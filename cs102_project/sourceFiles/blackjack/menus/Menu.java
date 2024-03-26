package blackjack.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import blackjack.processes.*;
import blackjack.players.*;

public class Menu {

    static final Scanner scanner = new Scanner(System.in);

    public void displayMain() {
        System.out.println("==== Start Menu ====");
        System.out.println("1. Start Game");
        System.out.println("2. Read Rules");
        System.out.println("3. End Program");
        System.out.print("Please enter your choice:");
    }

    public void displayRules() {
        try {
            Thread.sleep(300);
            System.out.print("\nPreparing Rules.");
            Thread.sleep(400);
            System.out.print(".");
            Thread.sleep(400);
            System.out.println(".\n");
            Thread.sleep(600);
            System.out.println("==== RULES ====");
            System.out.println("1. Blah Blah Blah");
            System.out.println("2. Blah Blah Blah");
            System.out.println("3. Blah Blah Blah");
            System.out.println("4. Blah Blah Blah");
            System.out.println("5. Blah Blah Blah");
            System.out.println("6. Blah Blah Blah");
            System.out.println("7. Blah Blah Blah");
            System.out.println("8. Blah Blah Blah");

            System.out.print("\nPress Enter to go back to Menu: ");
            scanner.nextLine();
            System.out.println("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void MainOptions() {
        int choice;
        do {
            displayMain();

            while (!scanner.hasNextInt()) {
                System.out.println("\nPlease enter a valid choice between 1 and 3.\n");
                scanner.nextLine(); // Consume invalid input
                displayMain();
            }

            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    GameOptions();
                    break;
                case 2:
                    displayRules();
                    break;
                case 3:
                    System.out.println("\nThank you for playing, see you again!");
                    break;
                default:
                    System.out.println("\nPlease enter a valid choice between 1 and 3.\n");
            }
        } while (choice != 3);
        scanner.close();
        // VERY IMPORTANT TO ONLY CLOSE SCANNER HERE
    }

    public void displayGame() {
        System.out.println("\n==== Game Menu ====");
        System.out.println("1. Play as Player");
        System.out.println("2. Play as Dealer");
        System.out.println("3. Back");
        System.out.print("Please enter your choice:");
    }

    public void GameOptions() {
        System.out.print("\nEnter your display name: ");
        String name = scanner.nextLine();
        int choice;
        do {
            displayGame();

            while (!scanner.hasNextInt()) {
                System.out.println("\nPlease enter a valid choice between 1 and 3.");
                scanner.nextLine(); // Consume invalid input
                displayGame();
            }

            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    startPlayerGame(name);
                    break;
                case 2:
                    startDealerGame(name);
                    break;
                case 3:
                    System.out.println();
                    break;
                default:
                    System.out.println("\nPlease enter a valid choice between 1 and 3.");
            }
        } while (choice < 1 || choice > 3);
    }

    public void startPlayerGame(String name) {
        // Values you get from mainManu
        int cashAmt = 5000;
        int numOfPlayers = 4;
        boolean isDealer = false;
        
        // Generate players and dealer 
        List<Player> playerList = new ArrayList<>();
        Dealer d = new BotDealer("DealerBot", cashAmt, null);

        if (isDealer) {
            d = new Dealer(name, cashAmt, null);  //set cards to null first
        }
        else {
            Player currentPlayer = new Player(name, cashAmt, null, 0);
            playerList.add(currentPlayer);
        }

        //add other players to playerList
        for (int i = 1; i < numOfPlayers; i++) {
            //create player bots
            BotPlayer playerBot = new BotPlayer("Bot " + i, cashAmt, null, 50);  
            playerList.add(playerBot);
        }

        //create game
        Game g = new Game(playerList, d);
        g.setDealer(d);
        g.setPlayers(playerList);
        int roundNo = 1;

        //implement game
        do {
            System.out.println("===== ROUND " + roundNo + "/10 =====");
            Round r = g.startRound();
            g.endRound(r);

            //Check if the players are bankrupt
            for (Player p : playerList) {
                if (!(p instanceof BotPlayer) && g.isHumanBankrupt(p)) {
                    //end game
                    System.out.println("You Lost the Game!");
                    System.out.println("----- END GAME -----");
                    System.out.println();
                    this.MainOptions();
                }
            }

            g.removeBankruptPlayers();

            //check if dealer is bankrupt
            if (g.isDealerBankrupt()) {
                System.out.println("Congratulations! You Won the Game!");
                System.out.println("----- END GAME -----");
                System.out.println();
                this.MainOptions(); // End the game after printing the result
            }

            

            /* =========================================================== *
             * Prompt user whether they want to continue to the next round *
             * or end the Game prematurely (Game ends automatically at the *
             * 10th round)                                                 *
             * =========================================================== */ 
            

            String userInput = "";
            boolean validInput = false;
            while (!validInput && roundNo < 10) {
                try {
                    System.out.print("Would you like to continue? (y/n): ");
                    userInput = scanner.nextLine().toLowerCase().trim();
                    if (userInput.equals("y") || userInput.equals("n")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException("Invalid input. Please enter 'y' or 'n'\n");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (userInput.equals("n")) {
                break; // Exit the loop if user chooses not to continue
            }

            roundNo++;

        } while (roundNo < 10);
    }
    

    
    public void startDealerGame(String name) {
        // Values you get from mainManu
        int cashAmt = 5000;
        int numOfPlayers = 4;
        boolean isDealer = true;
        
        // Generate players and dealer 
        List<Player> playerList = new ArrayList<>();
        Dealer d = new BotDealer("DealerBot", cashAmt, null);

        if (isDealer) {
            d = new Dealer(name, cashAmt, null);  //set cards to null first
        }
        else {
            Player currentPlayer = new Player(name, cashAmt, null, 0);
            numOfPlayers -= 1;
            playerList.add(currentPlayer);
        }

        //add other players to playerList
        for (int i = 0; i < numOfPlayers; i++) {
            //create player bots
            BotPlayer playerBot = new BotPlayer("Bot " + i, cashAmt, null, 50);  
            playerList.add(playerBot);
        }

        //create game
        Game g = new Game(playerList, d);
        g.setDealer(d);
        g.setPlayers(playerList);
        int roundNo = 1;

        //implement game

        do {
            System.out.println("===== ROUND " + roundNo + "/10 =====");
            Round r = g.startRound();
            g.endRound(r);

            // Check for bankrupt players
            g.removeBankruptPlayers();
            
            if (playerList.size() == 0) {
                System.out.println("Congratulations! You Won the Game!");
                System.out.println("----- END GAME -----");
                System.out.println();
                this.MainOptions();
            }

            //check if dealer is bankrupt
            if (g.isDealerBankrupt()) {
                System.out.println("You Lost the Game!");
                System.out.println("----- END GAME -----");
                System.out.println();
                this.MainOptions(); 
            }

            /* =========================================================== *
                * Prompt user whether they want to continue to the next round *
                * or end the Game prematurely (Game ends automatically at the *
                * 10th round)                                                 *
                * =========================================================== */ 
            

            String userInput = "";
            boolean validInput = false;
            while (!validInput && roundNo < 10) {
                try {
                    System.out.print("Would you like to continue? (y/n): ");
                    userInput = scanner.nextLine().toLowerCase().trim(); // Convert input to lowercase for case-insensitive comparison
                    if (userInput.equals("y") || userInput.equals("n")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException("Invalid input. Please enter 'y' or 'n'\n");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (userInput.equals("n")) {
                break; // Exit the loop if user chooses not to continue
            }

            roundNo++;

        } while (roundNo < 10);
    }
}

        
        
        // String choice = "no";
        // for (int i = 0; i < 10; i++) {
        //     while ( !(choice.equals("yes") || g.isDealerBankrupt()) ) {
        //         Round r = g.startRound();
        //         g.endRound(r);

        //         //ask player if continue
        //         System.out.print("Do you want to end the game? (yes/no)");
        //         choice = scanner.nextLine();
        //     }
        // }
        // if (choice.equals("yes")) {
        //     g.terminateGame(choice);
        // }

    //     int startingCashAmt = 500; // Customizable when time allows

    //     List<Player> players = new ArrayList<>();

    //     Player humanPlayer = new Player(name, startingCashAmt);
    //     // Tell durga to make constructor without hand parameter

    //     players.add(humanPlayer);
    //     players.add(new BotPlayer("Bot Player 1", startingCashAmt));
    //     players.add(new BotPlayer("Bot Player 2", startingCashAmt));
    //     players.add(new BotPlayer("Bot Player 3", startingCashAmt));
        
    //     Game game = new Game(players, new BotDealer("Dealer", startingCashAmt));
    //     Deck deck = new Deck();
    //     int roundNo = 1;

    //     do {
    //         System.out.println("===== ROUND " + roundNo + "/10 =====");
    //         Round round = new Round(deck);
    //         game.startRound();

    //         /* =========================================================== *
    //          * Prompt user whether they want to continue to the next round *
    //          * or end the Game prematurely (Game ends automatically at the *
    //          * 10th round)                                                 *
    //          * =========================================================== */ 
            
    //         System.out.println("ROUND HAPPENING THINGAMGI");

    //         String userInput = "";
    //         boolean validInput = false;
    //         while (!validInput && roundNo < 10) {
    //             try {
    //                 System.out.print("Would you like to continue? (y/n): ");
    //                 userInput = scanner.nextLine().toLowerCase().trim(); // Convert input to lowercase for case-insensitive comparison
    //                 if (userInput.equals("y") || userInput.equals("n")) {
    //                     validInput = true;
    //                 } else {
    //                     throw new IllegalArgumentException("Invalid input. Please enter 'y' or 'n'\n");
    //                 }
    //             } catch (IllegalArgumentException e) {
    //                 System.out.println(e.getMessage());
    //             }
    //         }
            
    //         if (userInput.equals("n")) {
    //             break; // Exit the loop if user chooses not to continue
    //         }

    //         roundNo++;

    //     } while (roundNo < 10);

    //     game.displayOverallGameResult();
    // }

//     public void startDealerGame(String name) {
//         int startingCashAmt = 500; // Customizable when time allows

//         List<Player> players = new ArrayList<>();

//         Player humanPlayer = new Player(name, startingCashAmt);
//         // Tell durga to make constructor without hand parameter

//         players.add(humanPlayer);
//         players.add(new BotPlayer("Bot Player 1", startingCashAmt));
//         players.add(new BotPlayer("Bot Player 2", startingCashAmt));
//         players.add(new BotPlayer("Bot Player 3", startingCashAmt));
        
//         Game game = new Game(players, new Dealer("Dealer", startingCashAmt));
//         Deck deck = new Deck();
//         int roundNo = 1;

//         do {
//             System.out.println("===== ROUND " + roundNo + "/10 =====");
//             // Round round = new Round(deck);
//             // game.startRound();
//             // // Shuffle cards in deck
//             // // Give out cards from deck
//             // // Set hands of players
//             // // Check for lucky hands

//             // List<Player> luckyPlayers = new ArrayList<>();
//             // luckyPlayers = round.getAllLuckyHandPlayers();

//             // if (game.isDealerBankrupt()) {
//             //     // Print something?
//             //     break;
//             // }

//             // round.playerTurn();

//             // round.displayRoundStatus();

//             // round.computeResults();

//             // round.displayPlayerResult();

//             // round.removeBankruptPlayers();

//             // if (game.isHumanBankrupt()) {
//             //     // Print something?
//             //     break;
//             // }

//             /* =========================================================== *
//              * Prompt user whether they want to continue to the next round *
//              * or end the Game prematurely (Game ends automatically at the *
//              * 10th round)                                                 *
//              * =========================================================== */ 
            
//             System.out.println("ROUND HAPPENING THINGAMGI");

//             String userInput = "";
//             boolean validInput = false;
//             while (!validInput && roundNo < 10) {
//                 try {
//                     System.out.print("Would you like to continue? (y/n): ");
//                     userInput = scanner.nextLine().toLowerCase().trim(); // Convert input to lowercase for case-insensitive comparison
//                     if (userInput.equals("y") || userInput.equals("n")) {
//                         validInput = true;
//                     } else {
//                         throw new IllegalArgumentException("Invalid input. Please enter 'y' or 'n'\n");
//                     }
//                 } catch (IllegalArgumentException e) {
//                     System.out.println(e.getMessage());
//                 }
//             }
            
//             if (userInput.equals("n")) {
//                 break; // Exit the loop if user chooses not to continue
//             }

//             roundNo++;

//         } while (roundNo < 10);

//         game.displayOverallGameResult();
//     }
// }