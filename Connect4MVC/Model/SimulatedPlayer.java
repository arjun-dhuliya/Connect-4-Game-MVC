package Connect4MVC.Model;

import java.util.Objects;
import java.util.Random;

/*
 * SimulatedPlayer.java
 *
 *
 * Version 1.0
 *
 *
 */

/**
 * SimulatedPlayer class represents a Computer Player playing the game.
 *
 * @author Arjun Dhuliya
 * @author Nihar Vanjara
 */
public class SimulatedPlayer implements PlayerInterface {

    final private Connect4FieldInterface connect4FieldInterface;
    final private String name;
    private final Random random = new Random();
    private final char gamePiece;
    private int lastMoveRowIndex = 0;
    private int lastMoveColumnIndex = 0;

    /***
     * 3 parametrized constructor, initializes connect4FieldInterface,name and gamePiece.
     *
     * @param theField   interface obj.
     * @param name,      name of the player in this game
     * @param gamePiece, char gamePiece, represents the game piece this instance uses.
     */
    public SimulatedPlayer(Connect4FieldInterface theField, String name, char gamePiece) {
        this.connect4FieldInterface = theField;
        this.name = name;
        this.gamePiece = gamePiece;
    }

    /***
     * shuffleArray shuffles the array.
     *
     * @param directionCodes array to be shuffled.
     */
    private static void shuffleArray(int[] directionCodes) {
        Random random = new Random();
        for (int i = directionCodes.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = directionCodes[index];
            directionCodes[index] = directionCodes[i];
            directionCodes[i] = a;
        }
    }

    /***
     * getOpponentGamePiece, fetches the opponent game piece.
     *
     * @return game piece of opponent
     */
    private char getOpponentGamePiece() {
        char opponentGamePiece = this.gamePiece;
        for (PlayerInterface player : Connect4Field.thePlayers) {
            if (!Objects.equals(player.getName(), this.getName()) && player.getGamePiece() != this.getGamePiece()) {
                opponentGamePiece = player.getGamePiece();
                break;
            }
        }
        return opponentGamePiece;
    }

    /***
     * get player's game piece
     *
     * @return game piece of this player
     */
    @Override
    public char getGamePiece() {
        return this.gamePiece;
    }

    /***
     * returns the name of this player
     *
     * @return name of the player.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /***
     * buildWeightArray, creates a 2D array for weight (maximum consecutive game pieces)
     * where row 1 is indexes(game piece to be dropped) and row 2 weight as defined above.
     *
     * @param copyOfBoard new copy of current game board state.
     * @param weightArray array which will store result
     * @param gamePiece   game piece to be matched.
     * @return int[] where int[0] stores the column index and int[1] stores the value i.e. weight.
     */
    private int[] buildWeightArray(char[][] copyOfBoard, int[][] weightArray, char gamePiece) {
        int[] bestMove = new int[2];                                    // where index 0 stores the index and 1 weight
        for (int columnIndex = 0; columnIndex < Connect4Field.COLUMN_COUNT; columnIndex++) { //loop to calc all column count
            duplicateCurrentState(copyOfBoard);
            dropPieces(columnIndex + 1, copyOfBoard, gamePiece);
            weightArray[0][columnIndex] = columnIndex;
            weightArray[1][columnIndex] = weightOfMove(copyOfBoard, gamePiece);
            if (bestMove[1] < weightArray[1][columnIndex]) {           //row 1 has indexes and row 2 has weight for it
                bestMove[1] = weightArray[1][columnIndex];
                bestMove[0] = columnIndex;
            }
        }
        return bestMove;
    }

    /***
     * decides the next move the computer player should make.
     *
     * @return column number
     */
    @Override
    public int nextMove() {
        char opponentPiece = getOpponentGamePiece();
        int[][] attackWeight = new int[2][Connect4Field.COLUMN_COUNT];   //row 1 has indexes and row 2 has weight for it
        int[][] defenseWeight = new int[2][Connect4Field.COLUMN_COUNT];  //row 1 has indexes and row 2 has weight for it
        int[] bestAttackMove;                              // where index 0 stores the index and 1 weight
        int[] bestDefenseMove;                             // where index 0 stores the index and 1 weight
        char[][] copyOfBoard = new char[Connect4Field.ROWS_COUNT][Connect4Field.COLUMN_COUNT];

        bestAttackMove = buildWeightArray(copyOfBoard, attackWeight, this.gamePiece);
//        for (int[] a : attackWeight) {
//            for (int num : a) {
//                System.out.print("\t" + num);
//            }
//            System.out.println();
//        }

        //System.out.println("best attack was : column " + (bestAttackMove[0] + 1) + " count: " + bestAttackMove[1]);

        if (bestAttackMove[1] >= 4) {                                     // where index 0 stores the index and 1 weight
            System.out.println("attack>4");
            return bestAttackMove[0] + 1;
        }

        bestDefenseMove = buildWeightArray(copyOfBoard, defenseWeight, opponentPiece); //passing opponent game piece

//        for (int[] a : defenseWeight) {
//            for (int num : a) {
//                System.out.print("\t" + num);
//            }
//            System.out.println();
//        }

        //System.out.println("best defense was : column " + (bestDefenseMove[0] + 1) + " count: " + bestDefenseMove[1]);

        if (bestDefenseMove[1] >= 4) {                                     // where index 0 stores the index and 1 weight
            //System.out.println("defense>4");
            return bestDefenseMove[0] + 1;
        }


        if (bestDefenseMove[1] >= 3) {                                 //including test opponent game piece drop, count >= 2
            if (connect4FieldInterface.checkIfPiecedCanBeDroppedIn(bestDefenseMove[0] + 1)) {
                char[][] carbonBoard = new char[Connect4Field.ROWS_COUNT][Connect4Field.COLUMN_COUNT];
                duplicateCurrentState(carbonBoard);
                dropPieces(bestDefenseMove[0] + 1, carbonBoard, this.gamePiece);
                if (predictWeightOfMove(carbonBoard, opponentPiece) < 4) {
                    //System.out.println("defense: our move don't make us loose");
                    return bestDefenseMove[0] + 1;
                } else {
                    sortArrays(defenseWeight[0], defenseWeight[1]);
                    int i = 1;
                    while (i < defenseWeight[1].length && defenseWeight[1][i] >= 3) {
                        if (connect4FieldInterface.checkIfPiecedCanBeDroppedIn(i + 1)) {
                            //System.out.println("defense: Next best cause our move make us loose");
                            return i + 1;
                        } else
                            i++;
                    }
                }
            }
        } else if (bestAttackMove[1] > 1) {                               //including test own game piece drop, count >= 2
            if (connect4FieldInterface.checkIfPiecedCanBeDroppedIn(bestAttackMove[0] + 1)) {
                char[][] carbonBoard = new char[Connect4Field.ROWS_COUNT][Connect4Field.COLUMN_COUNT];
                duplicateCurrentState(carbonBoard);
                dropPieces(bestAttackMove[0] + 1, carbonBoard, this.gamePiece);
                if (predictWeightOfMove(carbonBoard, opponentPiece) < 4) {
                    //System.out.println("attack: our move don't make us loose");
                    return bestAttackMove[0] + 1;
                } else {
                    sortArrays(attackWeight[0], attackWeight[1]);
                    int i = 1;
                    while (i < attackWeight[1].length && attackWeight[1][i] >= 2) {
                        if (connect4FieldInterface.checkIfPiecedCanBeDroppedIn(i + 1)) {
                            //System.out.println("Attack: Next best cause our move make us loose");
                            return i + 1;
                        } else
                            i++;
                    }
                }
            } else {
                sortArrays(attackWeight[0], attackWeight[1]);
                int i = 1;
                while (i < attackWeight[1].length && attackWeight[1][i] >= 1) {
                    if (connect4FieldInterface.checkIfPiecedCanBeDroppedIn(i + 1)) {
                        //System.out.println("Attack: Next best cause our move make us loose");
                        return i + 1;
                    } else
                        i++;
                }
            }
        }
        int col;
        do {
            col = Math.abs(random.nextInt() % copyOfBoard[0].length);
        } while (!connect4FieldInterface.checkIfPiecedCanBeDroppedIn(col + 1));
        return col;
    }

    /***
     * Sorts the two arrays on weight (values in this case).
     *
     * @param indexes array of indexes
     * @param values  array of values.
     */
    private void sortArrays(int[] indexes, int[] values) {
        int i, j;
        int newValue;
        int newIndex;
        for (i = 1; i < indexes.length; i++) {
            newValue = values[i];
            newIndex = indexes[i];
            j = i;
            while (j > 0 && values[j - 1] > newValue) {
                values[j] = values[j - 1];
                indexes[j] = indexes[j - 1];
                j--;
            }
            values[j] = newValue;
            indexes[j] = newIndex;
        }
    }

    /***
     * creates copy of Current state of board
     *
     * @param copyOfBoard the array which will store the resultant.
     */
    private void duplicateCurrentState(char[][] copyOfBoard) {
        for (int rowIndex = 0; rowIndex < Connect4Field.board.length; rowIndex++) {
            System.arraycopy(Connect4Field.board[rowIndex], 0, copyOfBoard[rowIndex], 0, Connect4Field.board[0].length);
        }
    }

    /***
     * drops the game piece,
     *
     * @param column,    integer
     * @param board,     2D char[][] board on which demo drop to be done.
     * @param gamePiece, char game piece to be dropped.
     */
    private void dropPieces(int column, char[][] board, char gamePiece) {

        int columnIndex = column - 1;

        for (int i = 0; i < board.length; i++) {
            if (i + 1 > board.length - 1 || board[i + 1][columnIndex] != Connect4Field.BLANK_SYMBOL) {
                board[i][columnIndex] = gamePiece;
                lastMoveRowIndex = i;
                lastMoveColumnIndex = columnIndex;
                break;
            }
        }
    }

    /***
     * change XnY with Direction, changes the X and Y values accordingly to direction code.
     *
     * @param lastX,         last X value of ur 2D array
     * @param lastY,         last Y value of ur 2D array
     * @param directionCode, int[] directionCode all 4 directions
     * @param iterationIndex (0 represents top to down, 1 represent bottom to top.
     * @return array of two int element where int[0] is X - co ordinate and int[1] is Y co ordinate
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
     * predicts next weight
     *
     * @param board            board
     * @param currentGamePiece opponent game piece
     * @return int for weight
     */
    private int predictWeightOfMove(char[][] board, char currentGamePiece) {
        int bestCount = 0;
        int count;                  //count is zero to start.
        int[] directionCodes = {0, 1, 2, 3};
        shuffleArray(directionCodes);
        for (int x = 0; x < board.length; x++)
            for (int y = 0; y < board[0].length; y++) {
                for (int directionCode : directionCodes) {
                    count = 0;
                    for (int j = 0; j < 2; j++) {
                        int lastY = y;
                        int lastX = x;
                        do {
                            int[] lastXY = changeXnYWithDirection(lastX, lastY, directionCode, j);
                            lastX = lastXY[0];
                            lastY = lastXY[1];
                            if (lastY >= board.length || lastY < 0 || lastX < 0 || lastX >= board[0].length)
                                break;
                            else if (board[lastY][lastX] == currentGamePiece)
                                count++;
                            else {
                                if (bestCount < count)
                                    bestCount = count;
                            }
                        } while (board[lastY][lastX] == currentGamePiece);
                    }
                }
            }
        return bestCount;
    }

    /***
     * weightOfMove
     *
     * @param board            2D board
     * @param currentGamePiece game piece to be tested.
     * @return int weight of move
     */
    private int weightOfMove(char[][] board, char currentGamePiece) {
        int bestCount = 0;
        int[] directionCodes = {0, 1, 2, 3};
        shuffleArray(directionCodes);
        for (int directionCode : directionCodes) {
            int countForThisDirectionCode = 1;
            for (int j = 0; j < 2; j++) {
                int lastY = lastMoveRowIndex;
                int lastX = lastMoveColumnIndex;
                do {
                    int[] lastXY = changeXnYWithDirection(lastX, lastY, directionCode, j);
                    lastX = lastXY[0];
                    lastY = lastXY[1];
                    if (lastY >= board.length || lastY < 0 || lastX < 0 || lastX >= board[0].length)
                        break;
                    else if (board[lastY][lastX] == currentGamePiece)
                        countForThisDirectionCode++;
                } while (board[lastY][lastX] == currentGamePiece);
            }

            if (countForThisDirectionCode > bestCount) {
                bestCount = countForThisDirectionCode;
            }
        }
//            }
        return bestCount;
    }

}