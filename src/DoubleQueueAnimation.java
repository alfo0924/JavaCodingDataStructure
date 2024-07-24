import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class DoubleQueueAnimation extends JFrame {
    private Queue<Integer> queue1;
    private Queue<Integer> queue2;
    private int size = 5; // 每個Queue的大小
    private int speed = 1000; // 動畫速度（毫秒）
    private Timer timer;
    private int front1 = 0;
    private int front2 = 0;
    private int nextValue1 = 1; // 用於Queue1的順序數字
    private int nextValue2 = 1; // 用於Queue2的順序數字

    private JPanel queuePanel;
    private JButton enqueueButton1;
    private JButton enqueueButton2;
    private JButton dequeueButton1;
    private JButton dequeueButton2;
    private JSlider speedSlider;

    public DoubleQueueAnimation() {
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
        initializeUI();
        startAnimation();
    }

    private void initializeUI() {
        setTitle("Double Queue Animation");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        queuePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawQueues(g);
            }
        };

        enqueueButton1 = new JButton("Enqueue Queue 1");
        enqueueButton2 = new JButton("Enqueue Queue 2");
        dequeueButton1 = new JButton("Dequeue Queue 1");
        dequeueButton2 = new JButton("Dequeue Queue 2");
        speedSlider = new JSlider(100, 2000, speed);
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        enqueueButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enqueue(queue1, nextValue1);
                nextValue1 = (nextValue1 % 50) + 1; // 更新下一個數字
            }
        });

        enqueueButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enqueue(queue2, nextValue2);
                nextValue2 = (nextValue2 % 50) + 1; // 更新下一個數字
            }
        });

        dequeueButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dequeue(queue1);
            }
        });

        dequeueButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dequeue(queue2);
            }
        });

        speedSlider.addChangeListener(e -> {
            speed = speedSlider.getValue();
            timer.setDelay(speed);
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(enqueueButton1);
        controlPanel.add(dequeueButton1);
        controlPanel.add(enqueueButton2);
        controlPanel.add(dequeueButton2);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);

        add(queuePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startAnimation() {
        timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                front1 = (front1 + 1) % size;
                front2 = (front2 + 1) % size;
                queuePanel.repaint();
            }
        });
        timer.start();
    }

    private void enqueue(Queue<Integer> queue, int value) {
        if (queue.size() < size) {
            queue.offer(value);
        }
        queuePanel.repaint();
    }

    private void dequeue(Queue<Integer> queue) {
        if (!queue.isEmpty()) {
            queue.poll();
        }
        queuePanel.repaint();
    }

    private void drawQueues(Graphics g) {
        int width = queuePanel.getWidth();
        int height = queuePanel.getHeight();
        int radius = Math.min(width, height) / 4;
        int centerX1 = width / 3;
        int centerY1 = height / 2;
        int centerX2 = 2 * width / 3;
        int centerY2 = height / 2;

        // 繪製Queue 1
        drawQueue(g, queue1, centerX1, centerY1, front1, radius, Color.BLUE);

        // 繪製Queue 2
        drawQueue(g, queue2, centerX2, centerY2, front2, radius, Color.GREEN);
    }

    private void drawQueue(Graphics g, Queue<Integer> queue, int centerX, int centerY, int front, int radius, Color color) {
        for (int i = 0; i < size; i++) {
            int angle = (360 / size) * i;
            int x = (int) (centerX + radius * Math.cos(Math.toRadians(angle)));
            int y = (int) (centerY + radius * Math.sin(Math.toRadians(angle)));

            if (i < queue.size()) {
                g.setColor(color);
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
            DoubleQueueAnimation animation = new DoubleQueueAnimation();
            animation.setVisible(true);
        });
    }
}