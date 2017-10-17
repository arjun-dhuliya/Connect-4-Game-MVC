/*
 *
 * Connect4Controller
 *
 * @author Arjun Dhuliya
 *
 */
package Connect4MVC.Controller;

import Connect4MVC.Model.Connect4Field;
import Connect4MVC.Model.Player;
import Connect4MVC.Model.PlayerInterface;
import Connect4MVC.Model.SimulatedPlayer;
import Connect4MVC.View.Connect4View;

import java.util.Random;

/*
 *
 * Connect4View
 *
 * @author Arjun Dhuliya
 * @author Nihar Vanjara
 *
 */
public class Connect4Controller {
    private final char[] colorCodes = {'r', 'b', 'g', 'y', 'l', 'p', 'o', 'm', 'c'};
    private final Random rand = new Random();
    private Connect4View connect4View;
    private Connect4Field connect4Field;
    private PlayerInterface currentPlayer;

    public Connect4Controller() {

    }

    public void start() {
        connect4View = new Connect4View(this);
        connect4Field = new Connect4Field();
        Connect4Field.thePlayers = new PlayerInterface[2];
        char colorCode = colorCodes[rand.nextInt(colorCodes.length)];
        Connect4Field.thePlayers[0] = new Player("Player1", colorCode);
        currentPlayer = Connect4Field.thePlayers[0];
    }

    public void createPlayerB(char playerTypeCode) {
        char colorCode;
        do {
            colorCode = colorCodes[rand.nextInt(colorCodes.length)];
        } while (colorCode == Connect4Field.thePlayers[0].getGamePiece());

        if (Character.toLowerCase(playerTypeCode) == 'c') {

            Connect4Field.thePlayers[1] = new SimulatedPlayer(connect4Field, "Computer1:", colorCode);
        } else if (Character.toLowerCase(playerTypeCode) == 'p') {
            Connect4Field.thePlayers[1] = new Player("Player2", colorCode);
        }
    }

    /**
     * it notifies colNum
     *
     * @param colNumString Column Number as a string input
     */
    public void notifyColumn(String colNumString) {
        try {
            int columnNumber = Integer.parseInt(colNumString);
            if (connect4Field.checkIfPiecedCanBeDroppedIn(columnNumber)) {
                connect4Field.dropPieces(columnNumber, currentPlayer.getGamePiece());
                connect4View.updateCell(connect4Field.toString());
            }
            if (connect4Field.didLastMoveWin()) {
                int[][] cellsToHighlight = connect4Field.cellsToHighlight;
                connect4View.highlightWinningCells(currentPlayer, cellsToHighlight, connect4Field.toString().split("\n"));
            } else if (connect4Field.isItaDraw()) {
                int[][] cellsToHighlight = new int[0][2];
                cellsToHighlight[0][0] = connect4Field.lastMoveColumnIndex;
                cellsToHighlight[0][1] = connect4Field.lastMoveColumnIndex;
                connect4View.highlightWinningCells(currentPlayer, cellsToHighlight, connect4Field.toString().split("\n"));
            } else {
                currentPlayer = (currentPlayer == Connect4Field.thePlayers[0]) ? Connect4Field.thePlayers[1] :
                        Connect4Field.thePlayers[0];
                if (currentPlayer instanceof SimulatedPlayer) {
                    notifyColumn(Integer.toString(currentPlayer.nextMove()));
                }
            }

        } catch (Exception exp) {
            String msg = exp.getMessage();
            System.out.println(msg);
            System.exit(1);
        }
    }
}
