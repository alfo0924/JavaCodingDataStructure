import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class QueueAnimation extends JFrame {
    private Queue<Integer> queue = new LinkedList<>();
    private JPanel animationPanel;
    private JTextField inputField;
    private JSlider speedSlider;
    private JButton enqueueButton, dequeueButton, startButton;
    private Timer timer;
    private int animationStep = 0;

    public QueueAnimation() {
        setTitle("Queue Animation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        animationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawQueue(g);
            }
        };
        add(animationPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        inputField = new JTextField(5);
        enqueueButton = new JButton("Enqueue");
        dequeueButton = new JButton("Dequeue");
        startButton = new JButton("Start Animation");
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        controlPanel.add(new JLabel("Input:"));
        controlPanel.add(inputField);
        controlPanel.add(enqueueButton);
        controlPanel.add(dequeueButton);
        controlPanel.add(startButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);

        add(controlPanel, BorderLayout.SOUTH);

        enqueueButton.addActionListener(e -> enqueue());
        dequeueButton.addActionListener(e -> dequeue());
        startButton.addActionListener(e -> startAnimation());

        timer = new Timer(1000 / speedSlider.getValue(), e -> animate());
    }

    private void enqueue() {
        try {
            int value = Integer.parseInt(inputField.getText());
            queue.offer(value);
            inputField.setText("");
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
    }

    private void dequeue() {
        if (!queue.isEmpty()) {
            queue.poll();
            repaint();
        }
    }

    private void startAnimation() {
        if (!timer.isRunning()) {
            animationStep = 0;
            timer.start();
            startButton.setText("Stop Animation");
        } else {
            timer.stop();
            startButton.setText("Start Animation");
        }
    }

    private void animate() {
        animationStep++;
        if (animationStep > queue.size()) {
            animationStep = 0;
        }
        repaint();
        timer.setDelay(1000 / speedSlider.getValue());
    }

    private void drawQueue(Graphics g) {
        int x = 50;
        int y = 150;
        int width = 50;
        int height = 50;

        for (Integer value : queue) {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
            g.setColor(Color.WHITE);
            g.drawString(value.toString(), x + 20, y + 30);
            x += width + 10;
        }

        if (!queue.isEmpty() && animationStep > 0) {
            g.setColor(Color.RED);
            g.drawRect(50 + (animationStep - 1) * (width + 10), y - 10, width, height + 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QueueAnimation().setVisible(true));
    }
}