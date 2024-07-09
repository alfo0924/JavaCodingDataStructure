import javax.swing.*;
import java.awt.*;

public class TetrisGame extends JFrame {

    JLabel statusbar;

    public TetrisGame() {
        initUI();
    }

    private void initUI() {
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        var board = new Tetris(this);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var game = new TetrisGame();
            game.setVisible(true);
        });
    }
}