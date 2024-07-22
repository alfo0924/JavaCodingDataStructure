import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleDrawingApp extends JFrame {
    private List<Shape> shapes = new ArrayList<>();
    private List<Shape> undoBuffer = new ArrayList<>();
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private static final int MAX_UNDO_REDO = 5;

    public SimpleDrawingApp() {
        setTitle("D1204433林俊傑 簡單繪圖應用");
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
                shapes.add(new Shape(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, currentThickness));
                undoBuffer.clear();
                repaint();
                updateButtons();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!shapes.isEmpty()) {
                    Shape currentShape = shapes.get(shapes.size() - 1);
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

        updateButtons();
    }

    private void undo() {
        if (!shapes.isEmpty() && undoBuffer.size() < MAX_UNDO_REDO) {
            Shape removedShape = shapes.remove(shapes.size() - 1);
            undoBuffer.add(0, removedShape);
            repaint();
            updateButtons();
            System.out.println("執行 Undo 操作 - 撤銷了一個形狀: (" +
                    removedShape.x1 + "," + removedShape.y1 + ") 到 (" +
                    removedShape.x2 + "," + removedShape.y2 + ")");
        } else {
            System.out.println("無法執行 Undo 操作 - 已達到最大撤銷次數或沒有可撤銷的操作");
        }
    }

    private void redo() {
        if (!undoBuffer.isEmpty()) {
            Shape redoShape = undoBuffer.remove(0);
            shapes.add(redoShape);
            repaint();
            updateButtons();
            System.out.println("執行 Redo 操作 - 重做了一個形狀: (" +
                    redoShape.x1 + "," + redoShape.y1 + ") 到 (" +
                    redoShape.x2 + "," + redoShape.y2 + ")");
        } else {
            System.out.println("無法執行 Redo 操作 - 沒有可重做的操作");
        }
    }

    private void updateButtons() {
        Container contentPane = getContentPane();
        Component[] components = contentPane.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component c : panel.getComponents()) {
                    if (c instanceof JButton) {
                        JButton button = (JButton) c;
                        if (button.getText().equals("復原 (Undo)")) {
                            button.setEnabled(shapes.size() > 0 && undoBuffer.size() < MAX_UNDO_REDO);
                        } else if (button.getText().equals("重做 (Redo)")) {
                            button.setEnabled(!undoBuffer.isEmpty());
                        }
                    }
                }
            }
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