import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TurkishBlockGame extends JFrame {
    private static final int GRID_SIZE = 10;
    private static final int BLOCK_SIZE = 30;
    private static final int GAME_SPEED = 500; // in milliseconds

    private JPanel gamePanel;
    private boolean[][] grid;
    private Timer gameTimer;

    public TurkishBlockGame() {
        setTitle("Turkish Block Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(GRID_SIZE * BLOCK_SIZE, GRID_SIZE * BLOCK_SIZE));
        add(gamePanel, BorderLayout.CENTER);

        grid = new boolean[GRID_SIZE][GRID_SIZE];
        initializeGrid();

        gameTimer = new Timer(GAME_SPEED, e -> updateGrid());
        gameTimer.start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGrid() {
        Random random = new Random();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = random.nextBoolean();
            }
        }
    }

    private void updateGrid() {
        boolean[][] newGrid = new boolean[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int neighbors = countNeighbors(i, j);
                if (grid[i][j]) {
                    newGrid[i][j] = neighbors == 2 || neighbors == 3;
                } else {
                    newGrid[i][j] = neighbors == 3;
                }
            }
        }

        grid = newGrid;
        gamePanel.repaint();
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE && grid[newX][newY]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TurkishBlockGame::new);
    }
}