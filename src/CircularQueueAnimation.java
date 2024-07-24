import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class CircularQueueAnimation extends JFrame {
    private Queue<Integer> queue;
    private int size = 5; // 環狀Queue的大小
    private int speed = 1000; // 動畫速度（毫秒）
    private Timer timer;
    private int front = 0;
    private int nextValue = 1; // 用於生成順序數字

    private JPanel queuePanel;
    private JButton enqueueButton;
    private JButton dequeueButton;
    private JSlider speedSlider;

    public CircularQueueAnimation() {
        queue = new LinkedList<>();
        initializeUI();
        startAnimation();
    }

    private void initializeUI() {
        setTitle("Circular Queue Animation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        queuePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawQueue(g);
            }
        };

        enqueueButton = new JButton("Enqueue");
        dequeueButton = new JButton("Dequeue");
        speedSlider = new JSlider(100, 2000, speed);
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        enqueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enqueue();
            }
        });

        dequeueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dequeue();
            }
        });

        speedSlider.addChangeListener(e -> {
            speed = speedSlider.getValue();
            timer.setDelay(speed);
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(enqueueButton);
        controlPanel.add(dequeueButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);

        add(queuePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startAnimation() {
        timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                front = (front + 1) % size;
                queuePanel.repaint();
            }
        });
        timer.start();
    }

    private void enqueue() {
        if (queue.size() < size) {
            queue.offer(nextValue); // 加入順序數字
            nextValue = (nextValue % size) + 1; // 更新下一個數字
        }
        queuePanel.repaint();
    }

    private void dequeue() {
        if (!queue.isEmpty()) {
            queue.poll();
        }
        queuePanel.repaint();
    }

    private void drawQueue(Graphics g) {
        int width = queuePanel.getWidth();
        int height = queuePanel.getHeight();
        int radius = Math.min(width, height) / 3;
        int centerX = width / 2;
        int centerY = height / 2;

        // 繪製環狀Queue
        for (int i = 0; i < size; i++) {
            int angle = (360 / size) * i;
            int x = (int) (centerX + radius * Math.cos(Math.toRadians(angle)));
            int y = (int) (centerY + radius * Math.sin(Math.toRadians(angle)));

            if (i < queue.size()) {
                g.setColor(Color.BLUE);
                g.fillOval(x - 20, y - 20, 40, 40);
                g.setColor(Color.WHITE);
                g.drawString(queue.toArray()[i].toString(), x - 10, y + 5);
            } else {
                g.setColor(Color.GRAY);
                g.fillOval(x - 20, y - 20, 40, 40);
            }
        }

        // 繪製前端指示
        if (!queue.isEmpty()) {
            int frontAngle = (360 / size) * front;
            int frontX = (int) (centerX + (radius + 30) * Math.cos(Math.toRadians(frontAngle)));
            int frontY = (int) (centerY + (radius + 30) * Math.sin(Math.toRadians(frontAngle)));
            g.setColor(Color.RED);
            g.drawLine(centerX, centerY, frontX, frontY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CircularQueueAnimation animation = new CircularQueueAnimation();
            animation.setVisible(true);
        });
    }
}