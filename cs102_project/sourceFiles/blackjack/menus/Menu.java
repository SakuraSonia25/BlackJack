package blackjack.menus;

import java.util.*;

import blackjack.processes.*;
import blackjack.players.*;

public class Menu {

    //Constants for Colours
    public static final String ANSI_RESET = "\u001B[0m"; //It goes to normal
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    static final Scanner scanner = new Scanner(System.in);

    //Display the main menu of the game
    public void displayMain() {
        // System.out.println("\033[H\033[2J"); //This code clears the terminal
        System.out.println(ANSI_CYAN + "==== Start Menu ====" + ANSI_RESET);
        System.out.println("1. Start Game");
        System.out.println("2. Read Rules");
        System.out.println("3. End Program");
        System.out.print("Please enter your choice:");

    }

    //To display the rules of the games
    public void displayRules() {
        try {
            Thread.sleep(300);
            System.out.print("\nPreparing Rules.");
            Thread.sleep(400);
            System.out.print(".");
            Thread.sleep(400);
            System.out.println(".\n");
            Thread.sleep(600);
            System.out.println(ANSI_CYAN + "==== RULES ====" + ANSI_RESET);

            //NOT THE MOST EFFICIENT WAY OF PRINTING
            System.out.println("Aim: Attain a total sum of card values, as close to 21, but not over");
            System.out.println("How can one win the game?\n");
            
            System.out.println("1. By getting a lucky starting hand. These lucky hands are:");
            System.out.println("\t a) Blackjack: When the player draws an Ace together with either a King, Queen or Jack");
            System.out.println("\t b) 2 Ace: Player draws 2 Ace");
            System.out.println("Note: If dealer also has a lucky hand, the round will end in a draw\n");
            
            System.out.println("2.By winning the dealer without getting a bust");
            System.out.println("\t a)Bust: Occurs when total sum of card values exceeds 21");
            System.out.println("\t b)No bust: If sum is more than dealer's sum of card values, you win!");
            System.out.println("Note: If both you and dealer has a bust, game ends in a draw\n");
            
            System.out.println("Details of Card Values:");
            System.out.println("\t a)The cards 2-10 have a value respective to their number");
            System.out.println("\t b)The Aces have a value of 1, 10, or 11 when there are 2 cards on Hand");
            System.out.println("\t 1 or 10 when there are 3 cards on Hand; 1 when there are 4 or more cards on Hand");
            System.out.println("\t c)The picture cards have a value of 10\n");
            
            System.out.println("Other possible lucky cards:");
            System.out.println("\t a) Dragon: When the total value of 5 cards on Hand is below 21");
            System.out.println("\t b) Triple 7: When all the 3 cards on Hand all have the value of 7");
            
            System.out.print(ANSI_CYAN + "\nPress Enter to go back to Menu: " + ANSI_RESET);
        
            scanner.nextLine();
            System.out.println("\n");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //The output of each option(1,2 3) that user chooses
    public void MainOptions() {
        int choice;
        do {

            displayMain();

            while (!scanner.hasNextInt()) {
                System.out.println(ANSI_RED + "\nPlease enter a valid choice between 1 and 3.\n" + ANSI_RESET);
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
                    System.out.println("\033[H\033[2J"); //This code clears the terminal
                    break;
                case 3:
                    System.out.println(ANSI_CYAN + "\nThank you for playing, see you again!" + ANSI_RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(ANSI_RED + "\nPlease enter a valid choice between 1 and 3.\n" + ANSI_RESET);
            }

        } while (choice != 3);
        // scanner.close();
        // VERY IMPORTANT TO ONLY CLOSE SCANNER HERE
    }

    //Game menu displayed after human chooses option 1
    public void displayGame() {
        // System.out.println("\033[H\033[2J"); //Code to clear terminal from start to where cursor is now
        System.out.println(ANSI_CYAN + "==== Game Menu ====" + ANSI_RESET);
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

                System.out.println(ANSI_RED + "\nPlease enter a valid choice between 1 and 3.\n" + ANSI_RESET);

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
                System.out.println(ANSI_RED + "\nPlease enter a valid choice between 1 and 3.\n" + ANSI_RESET);
            }
        } while (choice < 1 || choice > 3);
    }

    public void startPlayerGame(String name) {
        // Values you get from mainManu
        int cashAmt = 5000; //fixed
        int numOfPlayers = 4; //fixed
        boolean isDealer = false;
        
        // Generate players and dealer 
        List<Player> playerList = new ArrayList<>();
        Dealer d = new BotDealer("Yeow Leong the BOSS", cashAmt, null);

        if (isDealer) {
            d = new Dealer(name, cashAmt, null);  //set cards to null first
        }
        else {
            Player currentPlayer = new Player(name, cashAmt, null, 0);
            playerList.add(currentPlayer);
        }

        //Names for bots
        List<String> botNames = new ArrayList<>();
        botNames.add("Christie");
        botNames.add("King Yeh");
        botNames.add("Zhiyuan");
        botNames.add("Lay Foo");

        //add other players to playerList
        for (int i = 1; i < numOfPlayers; i++) {
            //create player bots
            BotPlayer playerBot = new BotPlayer(botNames.get(i), cashAmt, null, 50);  
            playerList.add(playerBot);
        }

        //create game
        Game g = new Game(playerList, d);
        g.setDealer(d);
        g.setPlayers(playerList);
        int roundNo = 1;

        //implement game
        do {

            // System.out.println("\033[H\033[2J"); //Clear terminal
            System.out.println(ANSI_CYAN + "===== ROUND " + roundNo + "/10 =====" + ANSI_RESET);
            Round r = g.startRound();
            g.endRound(r);

            //Check if the players are bankrupt
            for (Player p : playerList) {
                if (!(p instanceof BotPlayer) && g.isHumanBankrupt(p)) {
                    //end game
                    // display overall game result
                    g.displayOverallGameResults(RoundDisplay.findHuman());
                    System.out.println();
                    this.MainOptions();
                }
            }

            g.removeBankruptPlayers();

            //check if dealer is bankrupt
            if (g.isDealerBankrupt()) {
                // display overall game result
                g.displayOverallGameResults(RoundDisplay.findHuman());
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
            while (!validInput && roundNo <= 10) {
                try {
                    System.out.print("Would you like to continue? (y/n): ");
                    userInput = scanner.nextLine().toLowerCase().trim();
                    if (userInput.equals("y") || userInput.equals("n")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(ANSI_RED + "Invalid input. Please enter 'y' or 'n'\n" + ANSI_RESET);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (userInput.equals("n")) {
                break; // Exit the loop if user chooses not to continue
            }

            roundNo++;

        } while (roundNo <= 10);

        // display overall game result
        g.displayOverallGameResults(RoundDisplay.findHuman());
        System.out.println();
    }

    public void startDealerGame(String name) {
        // Values you get from mainManu
        int cashAmt = 5000;
        int numOfPlayers = 4;
        boolean isDealer = true;
        
        // Generate players and dealer 
        List<Player> playerList = new ArrayList<>();
        Dealer d = new BotDealer("Yeow Leong the BOSS", cashAmt, null);

        if (isDealer) {
            d = new Dealer(name, cashAmt, null);  //set cards to null first
        }
        else {
            Player currentPlayer = new Player(name, cashAmt, null, 0);
            numOfPlayers -= 1;
            playerList.add(currentPlayer);
        }

        //Names for bots
        List<String> botNames = new ArrayList<>();
        botNames.add("Christie");
        botNames.add("King Yeh");
        botNames.add("Zhiyuan");
        botNames.add("Lay Foo");

        //add other players to playerList
        for (int i = 1; i < numOfPlayers; i++) {
            //create player bots
            BotPlayer playerBot = new BotPlayer(botNames.get(i), cashAmt, null, 50);  
            playerList.add(playerBot);
        }

        //create game
        Game g = new Game(playerList, d);
        g.setDealer(d);
        g.setPlayers(playerList);
        int roundNo = 1;

        //implement game

        do {
            // System.out.println("\033[H\033[2J"); //Clear the terminal
            System.out.println(ANSI_CYAN + "\n===== ROUND " + roundNo + "/10 =====" + ANSI_RESET);
            Round r = g.startRound();
            g.endRound(r);

            // Check for bankrupt players
            g.removeBankruptPlayers();
            
            if (playerList.size() == 0) {
                // display overall game result
                g.displayOverallGameResults(RoundDisplay.findHuman());
                System.out.println();
                this.MainOptions();
            }

            //check if dealer is bankrupt
            if (g.isDealerBankrupt()) {
                // display overall game result
                g.displayOverallGameResults(RoundDisplay.findHuman());
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

                        throw new IllegalArgumentException(ANSI_RED + "Invalid input. Please enter 'y' or 'n'\n" + ANSI_RESET);

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

        // display overall game result
        g.displayOverallGameResults(RoundDisplay.findHuman());
        System.out.println();

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