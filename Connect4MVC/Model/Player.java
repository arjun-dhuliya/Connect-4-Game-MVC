package Connect4MVC.Model;

/*
 * Player.java
 *
 *
 * Version 1.0
 *
 *
 */

/**
 * Player class represents a human Player playing the game.
 *
 * @author Arjun Dhuliya
 */
public class Player implements PlayerInterface {

    final private String name;
    final private char gamePiece;

    /***
     * Constructor which initialize connect4FieldInterface, name of player and game piece
     *
     * @param name:      name of player
     * @param gamePiece: game piece player will use
     */
    public Player(String name, char gamePiece) {
        this.name = name;
        this.gamePiece = gamePiece;
    }

    /***
     * returns the gamePiece for this player
     *
     * @return char game piece which this player is using for the game.
     */
    @Override
    public char getGamePiece() {
        return this.gamePiece;
    }

    /***
     * returns the name for this player
     *
     * @return string name which this player is using for the game.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /***
     * Ask userInput for column and return it.
     *
     * @return integer column number, on which the game piece is to be inserted.
     */
    @Override
    public int nextMove() {
        return -1;
//        Scanner input = new Scanner(System.in);
//        System.out.print(this.getName() + ", which column you wanna drop your piece -> " + this.getGamePiece() +
//                ": ");
//        return input.nextInt();
    }
}
