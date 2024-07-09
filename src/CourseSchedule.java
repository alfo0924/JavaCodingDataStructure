import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class CourseSchedule extends JFrame {
    private JButton convertButton, saveButton;
    private JPanel coursePanel;
    private JLabel[][] courseLabels;
    private int[][] courseSchedule;

    // 課程編號與名稱的關係
    private String[] courseNames = {"", "計算機概論", "離散數學", "資料結構", "資料庫理論", "上機實習"};

    public CourseSchedule() {
        setTitle("課表");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 建立課表面板
        coursePanel = new JPanel(new GridLayout(7, 6, 10, 10)); // 7行，6列
        courseLabels = new JLabel[6][6]; // 6行，6列
        courseSchedule = new int[6][5]; // 6行，5列

        // 初始化課表
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 && j == 0) {
                    courseLabels[i][j] = new JLabel("節次", SwingConstants.CENTER);
                } else if (i == 0) {
                    courseLabels[i][j] = new JLabel("星期" + j, SwingConstants.CENTER);
                } else if (j == 0) {
                    courseLabels[i][j] = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                } else {
                    courseLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                    courseLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    courseLabels[i][j].setBackground(Color.WHITE);
                    courseLabels[i][j].setOpaque(true);
                    courseLabels[i][j].setTransferHandler(new CourseTransferHandler());
                }
                coursePanel.add(courseLabels[i][j]);
            }
        }

        // 建立轉換按鈕
        convertButton = new JButton("轉換");
        convertButton.addActionListener(e -> convertSchedule());

        // 建立保存按鈕
        saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveSchedule());

        // 建立課程說明面板
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 設定邊界
        infoPanel.add(new JLabel("課程編號與名稱:"));
        for (int i = 1; i <= 5; i++) {
            infoPanel.add(new JLabel(i + ". " + courseNames[i]));
        }

        // 建立整體佈局
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.add(coursePanel, BorderLayout.CENTER);
        mainPanel.add(convertButton, BorderLayout.SOUTH);
        mainPanel.add(saveButton, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void convertSchedule() {
        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                try {
                    int course = Integer.parseInt(courseLabels[i][j].getText());
                    if (course >= 1 && course <= 5) {
                        courseLabels[i][j].setText(courseNames[course]);
                        courseSchedule[i - 1][j - 1] = course;
                    }
                } catch (NumberFormatException e) {
                    // If not a valid number, do nothing
                }
            }
        }
    }

    private void saveSchedule() {
        System.out.println("課表內容:");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(courseSchedule[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CourseSchedule frame = new CourseSchedule();
            frame.setVisible(true);
        });
    }

    private class CourseTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                return new StringSelection(label.getText());
            }
            return null;
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            try {
                String text = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                JLabel label = (JLabel) support.getComponent();
                label.setText(text);
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                return false;
            }
        }
    }
}
