# Chinese Blackjack (Banluck) Game

## Description
This is a console implementation of the card game, Banluck. 
4 players will be playing the game, including the human player. The rest will be computer bots.
The human player is allowed to choose if he/she wants to take the role of the dealer or a player.

## Installation
1. Clone or download this repository to your local machine
3. Navigate to the project directory.
4. Run the compile.bat file in the terminal
5. Run the run.bat file in the terminal to start the game

## Rules
1. The players’ aim is to attain a total value as close to 21, but not over, with the cards in their hands. 
2. Alternatively, they should aim for lucky starting hands:
    a) Blackjack: When the player draws an Ace together with either a King, Queen or Jack --> Winning returns 2x
    b) 2 Ace: Player draws 2 Ace --> Winning returns 3x
3. Other lucky cards:
    a) Dragon: When the total value of 5 cards on Hand is below 21 --> Winning returns 2x
    b) Triple 7: When all the 3 cards on Hand all have the value of 7 --> Winning returns 7x
4. For a player to win, they only need to beat the Dealer. 
5. Card Value:
    a)The cards 2-10 have a value respective to their number
    b)The Aces have a value of 1, 10, or 11 when there are 2 cards on Hand; 1 or 10 when there are 3 cards on Hand; 1 when there are 4 or more cards on Hand
    c)The picture cards have a value of 10.

## Gameflow
1. The "Start Menu" prompts the human player for an input:
    a) Start game
    b) Read rules
    c) End program
2. When human player chooses to start the game, he/she will be prompted to enter a name
3. The "Game Menu" allows the player to:
    a) Play as Player
    b) Player as Dealer

4. Player is required to enter an initial cash amount
5. When the round starts, the Player will enter a bet amount for that round
6. The Player's hand will be displayed
7. If the Dealer has lucky starting hands (Blackjack, 2 Ace), the Dealer will be considered to have won and the round ends immediately.
8. If Player or the Dealer do not have any lucky starting hands (Blackjack, 2 Ace), the Player can choose to Hit (continue drawing a card) or Stand, when he/she has reached the minimum total value of 16, or is satisfied with the currrent total
8. After all players and the dealer has drawn their cards, the dealer will challenge each player. To challenge, the Dealer will ask a Player to reveal their hand, and whoever that has a value closer to 21 between the two, wins

<Result>
9. If the Dealer wins, the bet will be taken. If the Dealer loses, then the Player will be paid the amount he/she bet. If it is a draw, both parties get to keep their money
10. Human player is prompted if he/she wants to continue with the game. If yes, a new round starts. If no, the player will be brought back to the "Start Menu"
11. The game wil end when the human player does not want to continue with the game, or when he/she is bankrupt, or when the Dealer for the game is bankrupt

## Directories
|-- cs102_project \n
  |-- classes\n
  |-- sourceFiles\n
    |-- blackjack
      |-- items
        |-- Card.java
        |-- Deck.java
        |-- Hand.java
      |-- menus
        |-- Menu.java
        |-- RoundDisplay.java
      |-- players
        |-- BotDealer.java
        |-- BotDealer.java
        |-- BotDealer.java
        |-- BotDealer.java
        |-- BotDealer.java
      |-- processes
        |-- Game.java
        |-- Round.java
  |-- compile.bat
  |-- run.bat  

## UML
https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&page-id=JNRQ3Mruw2Z1RfvKf6q3#G1z208zJpMzq8ww5uSWisVAwfuPigA1K7M

## Classes
1.package items
    Card.java: Represents a playing card in the Banluck game, storing the methods and properties related to a card, such as the card value, suit and integer value. 
    Deck.java: Represents the card deck in the Banluck game, storing all the playing cards in the deck. The Deck creates and shuffles the cards, before assigning them to individual players and the dealer
    Hand.java: Represent a Person's hand. Stores the cards drawn to the Player's hand, while managing the total value and size of cards on hand. Hand.java also checks for special cards on hand and situations in which tht hand value exceeds 21.

2. package menus
    Menu.java: Facilitates user interaction with the game through providing options for starting and ending the program. Overall, it handles the progression of the game rounds.

3. package players
    BotDealer.java: Subclass of Dealer.java and represents a Bot Dealer in the game. Implements the logic for BotDealers to make the decision on players to challenge
    BotPlayer.java: Subclass of Player.java and represents a Bot Player in the game. Implements the logic for BotPlayers to decide on "Hit" or "Stand"
    Dealer.java: Subclass of Person.java and represents a Dealer in the game. Contains functionalities to simulate the actions of a dealer, including challenging players and determining the outcome of challenges.
    Person.java: Represents a person playing the game. This class encapsulates the attributes and behaviors common to both players and dealers, including managing their hands and providing information about players.
    Player.java: Subclass of Person.java and represents players in the game. The class manages the bet amount that is placed by each player for every round

4. package processes
    Game.java: Represents a single Banluck game. The class manages the gameplay of a blackjack game, including starting and ending individual rounds, player actions, and overall game results.
    Round.java: Represents a single round in the game. The class implements the specifics of the actions that occur in each player's turn and dealer's turn, while managing the overall round results

5. Blackjack.java: Serves as the entry point for the blackjack game. It initializes a Menu object and invokes the main options of the game.

## Credits

