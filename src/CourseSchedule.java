import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.table.*;

public class CourseSchedule extends JFrame {
    private JTable table;
    private String[][] scheduleData;
    private int[][] numberSchedule;
    private String[] courses = {"", "計算機概論", "離散數學", "資料結構", "資料庫理論", "上機實習"};
    private JButton saveButton, convertButton;
    private JList<String> courseList;

    public CourseSchedule() {
        setTitle("課表");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"節次", "星期一", "星期二", "星期三", "星期四", "星期五"};
        scheduleData = new String[6][6];
        numberSchedule = new int[6][5];

        for (int i = 0; i < 6; i++) {
            scheduleData[i][0] = String.valueOf(i + 1);
        }

        table = new JTable(scheduleData, columnNames);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.LIGHT_GRAY);
                return c;
            }
        });
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return false;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("保存");
        convertButton = new JButton("轉換");
        buttonPanel.add(saveButton);
        buttonPanel.add(convertButton);
        add(buttonPanel, BorderLayout.SOUTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 1; i < courses.length; i++) {
            listModel.addElement(i + ". " + courses[i]);
        }
        courseList = new JList<>(listModel);
        courseList.setDragEnabled(true);
        JScrollPane listScrollPane = new JScrollPane(courseList);
        listScrollPane.setPreferredSize(new Dimension(150, 0));
        add(listScrollPane, BorderLayout.EAST);

        saveButton.addActionListener(e -> saveSchedule());
        convertButton.addActionListener(e -> convertSchedule());

        setSize(750, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        courseList.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringSelection(courseList.getSelectedValue());
            }
        });

        table.setDropMode(DropMode.ON_OR_INSERT);
        table.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                try {
                    String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
                    int row = dl.getRow();
                    int col = dl.getColumn();
                    if (col > 0) {
                        String courseNumber = data.split("\\.")[0];
                        int number = Integer.parseInt(courseNumber);
                        table.setValueAt(courseNumber, row, col);
                        numberSchedule[row][col-1] = number;  // 更新 numberSchedule 
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    private void saveSchedule() {
        System.out.println("Current schedule:");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(numberSchedule[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void convertSchedule() {
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                String cellValue = (String) table.getValueAt(i, j);
                try {
                    int courseNumber = Integer.parseInt(cellValue);
                    if (courseNumber >= 1 && courseNumber < courses.length) {
                        numberSchedule[i][j-1] = courseNumber;  // 更新 numberSchedule 
                        table.setValueAt(courses[courseNumber], i, j);
                    }
                } catch (NumberFormatException e) {
                    // 如果不是數字，嘗試將課程名稱轉換為編號 
                    for (int k = 1; k < courses.length; k++) {
                        if (courses[k].equals(cellValue)) {
                            numberSchedule[i][j-1] = k;
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CourseSchedule::new);
    }
}