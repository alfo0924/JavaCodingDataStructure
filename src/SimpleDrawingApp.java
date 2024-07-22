import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleDrawingApp extends JFrame {
    private List<Shape> shapes = new ArrayList<>(); // 儲存已繪製的形狀
    private List<Shape> undoBuffer = new ArrayList<>(); // 儲存撤銷操作的形狀
    private Color currentColor = Color.BLACK; // 當前選擇的顏色
    private int currentThickness = 2; // 當前選擇的筆刷粗細
    private static final int MAX_UNDO_REDO = 5; // 最大撤銷/重做次數

    public SimpleDrawingApp() {
        setTitle("D1204433林俊傑 簡單繪圖應用"); // 設定視窗標題
        setSize(640, 480); // 設定視窗大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設定關閉操作

        JPanel drawingPanel = new JPanel() { // 繪圖面板
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Shape shape : shapes) { // 繪製所有已儲存的形狀
                    g.setColor(shape.color);
                    ((Graphics2D) g).setStroke(new BasicStroke(shape.thickness));
                    g.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
                }
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shapes.add(new Shape(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, currentThickness)); // 新增形狀
                undoBuffer.clear(); // 清空撤銷緩衝區
                repaint(); // 重繪面板
                updateButtons(); // 更新按鈕狀態
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!shapes.isEmpty()) {
                    Shape currentShape = shapes.get(shapes.size() - 1); // 獲取最新的形狀
                    currentShape.x2 = e.getX(); // 更新形狀的結束點
                    currentShape.y2 = e.getY();
                    repaint(); // 重繪面板
                }
            }
        });

        JButton undoButton = new JButton("復原 (Undo)"); // 撤銷按鈕
        undoButton.addActionListener(e -> undo()); // 設定撤銷操作

        JButton redoButton = new JButton("重做 (Redo)"); // 重做按鈕
        redoButton.addActionListener(e -> redo()); // 設定重做操作

        JButton colorButton = new JButton("選擇顏色"); // 選擇顏色按鈕
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "選擇顏色", currentColor); // 顯示顏色選擇器
            if (newColor != null) {
                currentColor = newColor; // 更新當前顏色
            }
        });

        JLabel thicknessLabel = new JLabel("筆刷粗細:"); // 筆刷粗細標籤
        JSlider thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 2); // 筆刷粗細滑桿
        thicknessSlider.addChangeListener(e -> {
            currentThickness = thicknessSlider.getValue(); // 更新當前筆刷粗細
        });

        JPanel controlPanel = new JPanel(); // 控制面板
        controlPanel.add(undoButton); // 添加撤銷按鈕
        controlPanel.add(redoButton); // 添加重做按鈕
        controlPanel.add(colorButton); // 添加選擇顏色按鈕
        controlPanel.add(thicknessLabel); // 添加筆刷粗細標籤
        controlPanel.add(thicknessSlider); // 添加筆刷粗細滑桿

        setLayout(new BorderLayout()); // 設定佈局
        add(drawingPanel, BorderLayout.CENTER); // 添加繪圖面板
        add(controlPanel, BorderLayout.SOUTH); // 添加控制面板

        updateButtons(); // 更新按鈕狀態
    }

    private void undo() {
        if (!shapes.isEmpty() && undoBuffer.size() < MAX_UNDO_REDO) {
            Shape removedShape = shapes.remove(shapes.size() - 1); // 移除最後一個形狀
            undoBuffer.add(0, removedShape); // 將形狀添加到撤銷緩衝區
            repaint(); // 重繪面板
            updateButtons(); // 更新按鈕狀態
            System.out.println("執行 Undo 操作 - 撤銷了一個形狀: (" +
                    removedShape.x1 + "," + removedShape.y1 + ") 到 (" +
                    removedShape.x2 + "," + removedShape.y2 + ")");
        } else {
            System.out.println("無法執行 Undo 操作 - 已達到最大撤銷次數或沒有可撤銷的操作");
        }
    }

    private void redo() {
        if (!undoBuffer.isEmpty()) {
            Shape redoShape = undoBuffer.remove(0); // 從撤銷緩衝區移除形狀
            shapes.add(redoShape); // 將形狀添加到形狀列表
            repaint(); // 重繪面板
            updateButtons(); // 更新按鈕狀態
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
                            button.setEnabled(shapes.size() > 0 && undoBuffer.size() < MAX_UNDO_REDO); // 啟用/禁用撤銷按鈕
                        } else if (button.getText().equals("重做 (Redo)")) {
                            button.setEnabled(!undoBuffer.isEmpty()); // 啟用/禁用重做按鈕
                        }
                    }
                }
            }
        }
    }

    private class Shape { // 形狀類別
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
        SwingUtilities.invokeLater(() -> new SimpleDrawingApp().setVisible(true)); // 啟動 GUI
    }
}