package Connect4MVC.View;

import Connect4MVC.Controller.Connect4Controller;
import Connect4MVC.Model.Connect4Field;
import Connect4MVC.Model.PlayerInterface;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 *
 * Connect4View
 *
 * @author Arjun Dhuliya
 *
 */
public class Connect4View extends JFrame {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 480;
    private static final boolean ALLOW_RESIZE = false;
    private static final int BOARD_ROWS = 9;
    private static final int BOARD_COLUMNS = 25;
    private static final char[][] board = new char[BOARD_ROWS][BOARD_COLUMNS];
    private final Connect4Controller connect4Controller;
    private ArrayList<BoardCell> boardCells;
    private ArrayList<ColumnDownArrow> arrowBtn;
    private final JLabel gameResult = new JLabel("",SwingConstants.CENTER);

    /***
     * Connect4View Constructor
     * @param connect4Controller object of controller to let view talk with controller when required
     */
    public Connect4View(Connect4Controller connect4Controller) {

        this.connect4Controller = connect4Controller;
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Connect 4");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(ALLOW_RESIZE);
        setLocationRelativeTo(null);

        // Create User Input panel
        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new GridLayout(3, 1));
        userInputPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        JLabel menuTextField = new JLabel("Select the Mode: ", SwingConstants.CENTER);
        JButton playerVsPlayer = new JButton("Player Vs Player");
        playerVsPlayer.setBackground(Color.blue);
        playerVsPlayer.setForeground(Color.blue);
        playerVsPlayer.setOpaque(true);
        playerVsPlayer.setSize(200, 50);
        JButton playerVsComputer = new JButton("Player Vs Computer");
        playerVsComputer.setSize(200, 50);
        playerVsComputer.setBackground(Color.blue);
        playerVsComputer.setForeground(Color.blue);
        playerVsComputer.setOpaque(true);

        userInputPanel.add(menuTextField);
        userInputPanel.add(playerVsPlayer);
        userInputPanel.add(playerVsComputer);

        this.getContentPane().add(userInputPanel);

        JPanel gamePlayPanel = new JPanel();
        //gamePlayPanel.setLayout(new GridLayout(2, 1));  //two rows wen gamepiece info
        gamePlayPanel.setLayout(new GridLayout(1, 1));
        gamePlayPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        JPanel boardPanel = new JPanel();                   //connect 4 game board
        boardPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); //rest 200 for the player game piece color
        boardPanel.setLayout(new GridLayout(BOARD_ROWS + 1, BOARD_COLUMNS));

        arrowBtn = new ArrayList<>();
        for (int i = 0; i < BOARD_COLUMNS; i++) {
            ColumnDownArrow b = new ColumnDownArrow();
            b.setName(Integer.toString(i + 1));
            b.addActionListener(e -> connect4Controller.notifyColumn(((BasicArrowButton) e.getSource()).getName()));
            b.setOpaque(false);
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setOpaque(false);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setOpaque(true);
                }
            });
            arrowBtn.add(b);
            boardPanel.add(arrowBtn.get(i));
        }

        getInitBoard();                     //initial board;
        boardCells = new ArrayList<>();
        for (char[] anInitBoard : board) {
            for (char anAnInitBoard : anInitBoard) {
                if (anAnInitBoard == ' ') {
                    BoardCell blank = new BoardCell();
                    blank.isBlank = true;
                    boardCells.add(blank);
                    boardPanel.add(new JPanel());
                } else if (anAnInitBoard == '_') {
                    BoardCell bCell = new BoardCell();
                    boardCells.add(bCell);
                    boardPanel.add(bCell);
                }
            }
        }
        gamePlayPanel.add(boardPanel);

        JPanel gamePeiceInfo = new JPanel(new GridLayout(3,2));
        gamePeiceInfo.setSize(100,100);

        JLabel player1label = new JLabel("Player 1 : ",SwingConstants.RIGHT);
        GamePieceInfo gamePiece1 = new GamePieceInfo();
        JLabel player2label = new JLabel("", SwingConstants.RIGHT);
        GamePieceInfo gamePiece2 = new GamePieceInfo();

        gamePeiceInfo.add(player1label);
        gamePeiceInfo.add(gamePiece1);
        gamePeiceInfo.add(player2label);
        gamePeiceInfo.add(gamePiece2);
        gamePeiceInfo.add(gameResult);

        //gamePlayPanel.add(gamePeiceInfo);

        playerVsComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Controller.createPlayerB('c');
                Color colorToSet = charToColor(Connect4Field.thePlayers[0].getGamePiece());
                gamePiece1.color = colorToSet;
                gamePiece1.repaint();
                player2label.setText("Computer 1: ");
                player2label.setName("c");                  //c is for computer and p for player.
                colorToSet = charToColor(Connect4Field.thePlayers[1].getGamePiece());
                gamePiece2.color = colorToSet;
                gamePiece2.repaint();

                setPanel(gamePlayPanel);

            }
        });

        playerVsPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Controller.createPlayerB('p');
                Color colorToSet = charToColor(Connect4Field.thePlayers[0].getGamePiece());
                gamePiece1.color = colorToSet;
                gamePiece1.repaint();
                player2label.setText("Player 2: ");
                player2label.setName("p");                  //c is for computer and p for player.
                colorToSet = charToColor(Connect4Field.thePlayers[1].getGamePiece());
                gamePiece2.color = colorToSet;
                gamePiece2.repaint();

                setPanel(gamePlayPanel);

            }
        });

    }

    private void setPanel(JPanel panel) {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.removeAll();
        contentPane.add(panel);
        contentPane.revalidate();
        contentPane.repaint();
    }

    /***
     * initBoard, initializes the game board for the first time.
     */
    private void getInitBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j < i || j > (board[i].length - i - 1))
                    board[i][j] = ' ';
                else
                    board[i][j] = '_';
            }
        }
    }

    public void updateCell(String board) {
        String[] lines = board.split("\n");
        for (int row = 0; row < lines.length; row++) {
            for (int column = 0; column < lines[0].length(); column++) {
                if (lines[row].charAt(column) != Connect4Field.BLANK_SYMBOL && lines[row].charAt(column) !=
                        Connect4Field.INVALID_SYMBOL &&
                        !boardCells.get(row * BOARD_COLUMNS + column).isFilled) {
                    BoardCell b = boardCells.get(row * BOARD_COLUMNS + column);
                    char colorCode = lines[row].charAt(column);
                    b.ovalColor = charToColor(colorCode);
                    b.isFilled = true;
                    b.repaint();

                }
            }

        }

    }

    public void highlightWinningCells(PlayerInterface currentPlayer, int[][] c, String[] board) {

        gameResult.setText(currentPlayer.getName()+ " Won!!!!");

        for (BasicArrowButton b : arrowBtn) {
            b.setVisible(false);
        }

        Color originalColor = charToColor(board[c[0][1]].charAt(c[0][0]));
        Color winn = Color.green.darker();
        // And From your main() method or any other method
        Timer timer = new Timer(500, e -> {
            Color toSetColor;
            if (boardCells.get(c[0][1] * BOARD_COLUMNS + c[0][0]).ovalColor == originalColor)
                toSetColor = winn;
            else
                toSetColor = originalColor;

            for (int[] cI : c) {
//            for (int i = 0; i < 4; i++) {
                BoardCell cell = boardCells.get(cI[1] * BOARD_COLUMNS + cI[0]);  //calculate index wth row and column
                cell.ovalColor = toSetColor;
                cell.repaint();
            }

        });
        timer.start();
    }

    private static Color charToColor(char ch) {
        ch = Character.toLowerCase(ch);
        return ch == 'r' ? Color.red.darker() : ch == 'b' ? Color.blue.darker() : ch == 'g' ? Color.green
                : ch == 'y' ? Color.yellow.darker() : ch == 'p' ? Color.pink.darker() : ch == 'o' ?
                Color.orange.darker() : ch == 'm' ? Color.magenta.darker() : Color.cyan.darker();
    }

    class ColumnDownArrow extends BasicArrowButton {
        public ColumnDownArrow() {
            super(BasicArrowButton.SOUTH);
        }
    }

    class GamePieceInfo extends JComponent {
        public Color color;
        public void paint(Graphics g) {
            g.setColor(color);
            g.fillRect(0,0,40,40);
        }
    }

    class BoardCell extends JButton {

        public boolean isFilled = false;
        public boolean isBlank = false;
        public Color ovalColor = Color.RED;

        public Dimension getPreferredSize() {
            return (new Dimension(40, 40));
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

//        super.paintComponent(g);
            if (!isBlank) {
                if (!isFilled) {
                    g2.setColor(Color.white);
                    g2.fillOval(getHorizontalAlignment(), getVerticalAlignment(), getWidth(), getHeight());
                } else {
                    g2.setColor(ovalColor);
                    g2.fillOval(getHorizontalAlignment(), getVerticalAlignment(), getWidth(), getHeight());
                    g2.setColor(Color.white);
                    g2.drawOval(getHorizontalAlignment(), getVerticalAlignment(), getWidth(), getHeight());
                }
            }
        }
    }
}

