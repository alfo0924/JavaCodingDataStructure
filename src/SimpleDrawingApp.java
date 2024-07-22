import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class SimpleDrawingApp extends JFrame {
    private Stack<Shape> shapes = new Stack<>();
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;

    public SimpleDrawingApp() {
        setTitle("簡單繪圖應用");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Shape shape : shapes) {
                    g.setColor(shape.color);
                    ((Graphics2D) g).setStroke(new BasicStroke(shape.thickness));
                    g.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
                }
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shapes.push(new Shape(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, currentThickness));
                repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Shape currentShape = shapes.peek();
                currentShape.x2 = e.getX();
                currentShape.y2 = e.getY();
                repaint();
            }
        });

        JButton undoButton = new JButton("復原");
        undoButton.addActionListener(e -> {
            if (!shapes.isEmpty()) {
                shapes.pop();
                repaint();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(undoButton);

        setLayout(new BorderLayout());
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class Shape {
        int x1, y1, x2, y2;
        Color color;
        int thickness;

        Shape(int x1, int y1, int x2, int y2, Color color, int thickness) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.thickness = thickness;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleDrawingApp().setVisible(true));
    }
}