package Connect4MVC.Model;
/*
 * Connect4MVC.Model.Connect4Field.java
 *
 *
 * Version 1.0
 *
 *
 */

import java.util.Objects;

/**
 * Connect4MVC.Model.Connect4Field Class implements Connect4MVC.Model.Connect4FieldInterface and controls game play
 * * Structure taken from RIT CS661 HW4
 *
 * @author Arjun Dhuliya
 */
public class Connect4Field implements Connect4FieldInterface {

    public final static char BLANK_SYMBOL = '_';
    public final static char INVALID_SYMBOL = ' ';
    final static int ROWS_COUNT = 9;
    final static int COLUMN_COUNT = 25;
    static final char[][] board = new char[ROWS_COUNT][COLUMN_COUNT];
    public static PlayerInterface[] thePlayers = new PlayerInterface[2];
    public final int[][] cellsToHighlight = new int[4][2];
    public int lastMoveColumnIndex = 0;
    private int lastMoveRowIndex = 0;
    private char currentGamePiece;

    public Connect4Field() {
        initBoard();
    }

    /***
     * checkIfPiecedCanBeDroppedIn, checks if piece can be dropped at column
     *
     * @param column, is an integer value representing the column
     * @return boolean true or false
     */
    @Override
    public boolean checkIfPiecedCanBeDroppedIn(int column) {
        return !(column <= 0 || column > board[0].length) && Objects.equals(board[0][column - 1], BLANK_SYMBOL);
    }

    /***
     * dropPieces, drops a game piece at the column
     *
     * @param column,   is an integer value representing the column
     * @param gamePiece , char representing the game piece to be dropped
     */
    @Override
    public void dropPieces(int column, char gamePiece) {

        int columnIndex = column - 1;

        if (checkIfPiecedCanBeDroppedIn(column)) {
            for (int i = 0; i < board.length; i++) {
                if (i + 1 > board.length - 1 || board[i + 1][columnIndex] != BLANK_SYMBOL
                        || board[i + 1][columnIndex] == INVALID_SYMBOL) {
                    board[i][columnIndex] = gamePiece;
                    lastMoveRowIndex = i;
                    lastMoveColumnIndex = columnIndex;
                    this.currentGamePiece = gamePiece;
                    break;
                }
            }
        } else {
            System.out.println("Cant drop in column " + column);
        }
    }

    /***
     * didLastMoveWin, checks if the last move won
     *
     * @return boolean, true or false
     */
    @Override
    public boolean didLastMoveWin() {
        boolean success = false;
        for (int directionCode = 0; directionCode < 4; directionCode++) {
            if (success)
                break;
            cellsToHighlight[0][0] = lastMoveColumnIndex;
            cellsToHighlight[0][1] = lastMoveRowIndex;
            int count = 1;
            for (int j = 0; j < 2; j++) {
                int lastY = lastMoveRowIndex;
                int lastX = lastMoveColumnIndex;
                if (success)
                    break;
                do {
                    if (count >= 4) {
                        success = true;
                        break;
                    }
                    int[] lastXY = changeXnYWithDirection(lastX, lastY, directionCode, j);
                    lastX = lastXY[0];
                    lastY = lastXY[1];
                    if (lastY >= board.length || lastY < 0 || lastX < 0 || lastX >= board[0].length)
                        break;
                    else if (board[lastY][lastX] == this.currentGamePiece) {
                        cellsToHighlight[count][0] = lastX;
                        cellsToHighlight[count][1] = lastY;
                        count++;
                    }
                } while (board[lastY][lastX] == this.currentGamePiece);
            }
        }
        return success;
    }


    /***
     * change XnY With Direction, changes the current X and Y value, according to direction code
     *
     * @param lastX          last value of X that is to be changed according to direction.
     * @param lastY          last value of Y that is to be changed according to direction.
     * @param directionCode  direction code is code for direction 0= horizontal,1=vertical,2= N-E to S-W,3 N-W to S-E
     * @param iterationIndex 0 for first round and 1 for end round.
     * @return returns an int[] having X in 0th index and y at 1th index
     */
    private int[] changeXnYWithDirection(int lastX, int lastY, int directionCode, int iterationIndex) {
        switch (directionCode) {
            case 0:                         // horizontal East - west line ->
                if (iterationIndex == 0)
                    lastX++;
                else
                    lastX--;
                break;
            case 1:                         // vertical North - South line  |
                if (iterationIndex == 0)
                    lastY++;
                else
                    lastY--;
                break;
            case 2:                         // Diagonal N-E to S-W /
                if (iterationIndex == 0) {
                    lastX++;
                    lastY++;
                } else {
                    lastX--;
                    lastY--;
                }
                break;
            case 3:                         // Diagonal N-W to S-E \
                if (iterationIndex == 0) {
                    lastX--;
                    lastY++;
                } else {
                    lastX++;
                    lastY--;
                }
                break;
        }
        return new int[]{lastX, lastY};
    }


    /***
     * isItaDraw, checks if game is drawn.
     *
     * @return boolean true or false.
     */
    @Override
    public boolean isItaDraw() {
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == BLANK_SYMBOL)
                return false;
        }
        return true;
    }

    /***
     * init, initializes  the players array
     *
     * @param playerA, object of Connect4MVC.Model.PlayerInterface
     * @param playerB, object of Connect4MVC.Model.PlayerInterface
     */
    @Override
    public void init(PlayerInterface playerA, PlayerInterface playerB) {
        thePlayers[0] = playerA;
        thePlayers[1] = playerB;
    }

    /***
     * toString, prints the current state of Board.
     *
     * @return game board in String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (char[] aBoard : board) {
            for (char anABoard : aBoard) {

                sb.append(anABoard);
            }
            sb.append('\n');

        }
        return sb.toString();
    }

    /***
     * points to the column last player inserted his game piece.
     *
     * @param column integer value representing the column
     */
    private void pointColumn(int column) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < column; i++)
            sb.append("  ");
        sb.append("V");
        System.out.println(sb);

    }

    /***
     * initBoard, initializes the game board for the first time.
     */
    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j < i || j > (board[i].length - i - 1))
                    board[i][j] = ' ';
                else
                    board[i][j] = BLANK_SYMBOL;
            }
        }

    }

    /***
     * playTheGame, starts the gamePlay between two players, and handles game events.
     */
    @Override
    public void playTheGame() {
        int column;
        //System.out.println(this);
        boolean gameIsOver = false;
        int moveCount = 0;
        System.out.println(this);
        do {
            for (int index = 0; index < 2; index++) {
                if (isItaDraw()) {
                    System.out.println("Draw");
                    gameIsOver = true;
                    break;
                } else {
                    column = thePlayers[index].nextMove();
                    System.out.println(thePlayers[index].getName() + " Dropped "
                            + thePlayers[index].getGamePiece() + " at " + column);

                    if (checkIfPiecedCanBeDroppedIn(column)) {
                        dropPieces(column, thePlayers[index].getGamePiece());
                        pointColumn(column);
                        System.out.println(this);
                    }
                    moveCount++;
                    if (didLastMoveWin()) {
                        gameIsOver = true;
                        pointColumn(column);
                        System.out.println(this);
                        System.out.println("\n*****The winner is: " + thePlayers[index].getName() + "******");
                        System.out.println("\n*****Total Moves: " + moveCount + "  ******");
                        break;
                    }
                }
            }
        } while (!gameIsOver);
    }
}
