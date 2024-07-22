import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class SimpleDrawingApp extends JFrame {
    private Stack<Shape> shapes = new Stack<>();
    private Stack<Shape> redoStack = new Stack<>();
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private static final int MAX_UNDO = 5;

    public SimpleDrawingApp() {
        setTitle("簡單繪圖應用");
        setSize(640, 480);
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
                addShape(new Shape(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, currentThickness));
                redoStack.clear();
                repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!shapes.isEmpty()) {
                    Shape currentShape = shapes.peek();
                    currentShape.x2 = e.getX();
                    currentShape.y2 = e.getY();
                    repaint();
                }
            }
        });

        JButton undoButton = new JButton("復原 (Undo)");
        undoButton.addActionListener(e -> undo());

        JButton redoButton = new JButton("重做 (Redo)");
        redoButton.addActionListener(e -> redo());

        JButton colorButton = new JButton("選擇顏色");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "選擇顏色", currentColor);
            if (newColor != null) {
                currentColor = newColor;
            }
        });

        JLabel thicknessLabel = new JLabel("筆刷粗細:");
        JSlider thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 2);
        thicknessSlider.addChangeListener(e -> {
            currentThickness = thicknessSlider.getValue();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        controlPanel.add(colorButton);
        controlPanel.add(thicknessLabel);
        controlPanel.add(thicknessSlider);

        setLayout(new BorderLayout());
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void addShape(Shape shape) {
        shapes.push(shape);
        if (shapes.size() > MAX_UNDO) {
            shapes.remove(0);
        }
    }

    private void undo() {
        if (!shapes.isEmpty()) {
            Shape removedShape = shapes.pop();
            redoStack.push(removedShape);
            repaint();
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            Shape redoShape = redoStack.pop();
            addShape(redoShape);
            repaint();
        }
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