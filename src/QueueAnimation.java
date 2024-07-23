import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class QueueAnimation extends JFrame {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int CIRCLE_RADIUS = 30;

    private Queue<Integer> queue;
    private JButton startButton;
    private JSlider speedSlider;
    private Timer timer;
    private int circleX, circleY;
    private int speed;

    public QueueAnimation() {
        setTitle("Queue Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        queue = new LinkedList<>();
        generateRandomNumbers(30);

        startButton = new JButton("Start Animation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAnimation();
            }
        });

        speedSlider = new JSlider(1, 100, 50);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> speed = speedSlider.getValue());

        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        controlPanel.add(new JLabel("Animation Speed:"));
        controlPanel.add(speedSlider);

        add(controlPanel, BorderLayout.NORTH);
        add(new AnimationPanel(), BorderLayout.CENTER);
    }

    private void generateRandomNumbers(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            queue.offer(random.nextInt(100) + 1); // 生成1到100的隨機數字
        }
    }

    private void startAnimation() {
        timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!queue.isEmpty()) {
                    int number = queue.poll();
                    circleX = 0;
                    circleY = WINDOW_HEIGHT / 2;
                    moveCircle(number);
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void moveCircle(int number) {
        new Thread(() -> {
            while (circleX < WINDOW_WIDTH - CIRCLE_RADIUS) {
                circleX += 5; // 每次移動5個像素
                repaint();
                try {
                    // 將計算的時間轉換為long類型
                    long sleepTime = (long) (100 - speed * 0.9);
                    Thread.sleep(sleepTime); // 根據滑桿調整速度
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private class AnimationPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawString("Queue: " + queue, 10, 20);

            g.setColor(Color.RED);
            g.fillOval(circleX, circleY, CIRCLE_RADIUS, CIRCLE_RADIUS);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QueueAnimation().setVisible(true));
    }
}