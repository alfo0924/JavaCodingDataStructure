import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int GRID_SIZE = 20;
    private static final int BLOCK_SIZE = 25;
    private static final int GAME_SPEED = 100; // in milliseconds

    private JPanel gamePanel;
    private ArrayList<Point> snake;
    private Point food;
    private Direction direction;
    private boolean gameOver;
    private Timer gameTimer;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public SnakeGame() {
        setTitle("Snake Game");
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

        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = Direction.RIGHT;
        gameOver = false;

        generateFood();

        gameTimer = new Timer(GAME_SPEED, new GameTimerListener());
        gameTimer.start();

        addKeyListener(new GameKeyListener());
        setFocusable(true);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generateFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void updateGame() {
        if (!gameOver) {
            Point head = snake.get(0);
            int newX = head.x;
            int newY = head.y;

            switch (direction) {
                case UP:
                    newY--;
                    break;
                case DOWN:
                    newY++;
                    break;
                case LEFT:
                    newX--;
                    break;
                case RIGHT:
                    newX++;
                    break;
            }

            if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE || snake.contains(new Point(newX, newY))) {
                gameOver = true;
            } else {
                snake.add(0, new Point(newX, newY));

                if (newX == food.x && newY == food.y) {
                    generateFood();
                } else {
                    snake.remove(snake.size() - 1);
                }
            }
        }
        gamePanel.repaint();
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GRID_SIZE * BLOCK_SIZE, GRID_SIZE * BLOCK_SIZE);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * BLOCK_SIZE, food.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over", GRID_SIZE * BLOCK_SIZE / 2 - 50, GRID_SIZE * BLOCK_SIZE / 2);
        }
    }

    private class GameTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateGame();
        }
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}