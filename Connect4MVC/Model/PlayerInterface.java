package Connect4MVC.Model;/*
 * Connect4MVC.Model.PlayerInterface.java
 *
 *
 * Version 1.0
 *
 *
 */

/**
 * Connect4MVC.Model.PlayerInterface
 */
public interface PlayerInterface {

    // this is how your constructor has to be
    // Player(Connect4MVC.Model.Connect4FieldInterface theField, String name, char currentGamePiece)
    char getGamePiece();

    String getName();

    int nextMove();

}
