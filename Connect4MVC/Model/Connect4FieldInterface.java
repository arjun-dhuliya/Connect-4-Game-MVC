package Connect4MVC.Model;
/*
 * Connect4MVC.Model.Connect4FieldInterface.java
 *
 *
 * Version 1.0
 *
 *
 */

/**
 * Connect4MVC.Model.Connect4FieldInterface interface
 */
interface Connect4FieldInterface {

    boolean checkIfPiecedCanBeDroppedIn(int column);

    void dropPieces(int column, char gamePiece);

    boolean didLastMoveWin();

    boolean isItaDraw();

    void init(PlayerInterface playerA, PlayerInterface playerB);

    String toString();

    void playTheGame();

}
