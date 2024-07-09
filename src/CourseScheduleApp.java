import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.TransferHandler;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.Map;

public class CourseScheduleApp extends JFrame {
    private static final String[] COLUMN_HEADERS = {"節次", "星期一", "星期二", "星期三", "星期四", "星期五"};
    private static final String[] CLASS_NAMES = {"", "計算機概論", "離散數學", "資料結構", "資料庫理論", "上機實習"};
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 6;
    private JTable table;
    private DefaultTableModel model;
    private JLabel statusLabel;
    private JList<String> courseList;
    private Map<String, Integer> classMap;
    private int[][] courseNumbers;
    private boolean displayCourseNames = false; // Flag to toggle between code and name

    public CourseScheduleApp() {
        setTitle("課表");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        classMap = new HashMap<>();
        classMap.put("計算機概論", 1);
        classMap.put("離散數學", 2);
        classMap.put("資料結構", 3);
        classMap.put("資料庫理論", 4);
        classMap.put("上機實習", 5);

        courseNumbers = new int[NUM_ROWS][NUM_COLS - 1];

        model = new DefaultTableModel(NUM_ROWS, NUM_COLS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        model.setColumnIdentifiers(COLUMN_HEADERS);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        for (int i = 1; i < NUM_COLS; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(150);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < NUM_COLS; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            table.setValueAt(i + 1, i, 0);
        }

        table.setDragEnabled(true);
        table.setDropMode(DropMode.ON_OR_INSERT_ROWS);
        table.setTransferHandler(new TableTransferHandler());

        JButton btnSave = new JButton("保存");
        btnSave.addActionListener(e -> saveSchedule());

        JButton btnConvert = new JButton("轉換為課程名稱");
        btnConvert.addActionListener(e -> toggleDisplay());

        statusLabel = new JLabel(getClassNumberRelationshipText());

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 1; i < CLASS_NAMES.length; i++) {
            listModel.addElement(CLASS_NAMES[i]);
        }

        courseList = new JList<>(listModel);
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setDragEnabled(true);
        courseList.setTransferHandler(new ListTransferHandler());

        JScrollPane listScrollPane = new JScrollPane(courseList);
        listScrollPane.setPreferredSize(new Dimension(150, 0));
        add(listScrollPane, BorderLayout.EAST);

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnSave);
        panelButtons.add(btnConvert);
        add(panelButtons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private class TableTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JTable table = (JTable) c;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if (col > 0) {
                String className = (String) table.getValueAt(row, col);
                if (!displayCourseNames) {
                    return new StringSelection(String.valueOf(courseNumbers[row][col - 1]));
                } else {
                    return new StringSelection(className);
                }
            }
            return null;
        }

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
                    if (!displayCourseNames) {
                        int courseIndex = Integer.parseInt(data);
                        courseNumbers[row][col - 1] = courseIndex;
                        table.setValueAt(String.valueOf(courseIndex), row, col);
                    } else {
                        String className = data;
                        if (classMap.containsKey(className)) {
                            int classIndex = classMap.get(className);
                            courseNumbers[row][col - 1] = classIndex;
                            table.setValueAt(className, row, col);
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private class ListTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JList list = (JList) c;
            int index = list.getSelectedIndex();
            if (index >= 0) {
                return new StringSelection((String) list.getModel().getElementAt(index));
            }
            return null;
        }

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
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                int index = dl.getIndex();
                DefaultListModel<String> model = (DefaultListModel<String>) courseList.getModel();
                model.add(index, data);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private String getClassNumberRelationshipText() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : classMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
        }
        return sb.toString();
    }

    private void saveSchedule() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 1; col < NUM_COLS; col++) {
                Object value = table.getValueAt(row, col);
                if (value != null && value instanceof String) {
                    courseNumbers[row][col - 1] = classMap.get(value);
                }
            }
        }
        JOptionPane.showMessageDialog(this, "課程表已保存");
    }

    private void toggleDisplay() {
        displayCourseNames = !displayCourseNames;
        if (displayCourseNames) {
            convertToCourseNames();
        } else {
            convertToCourseCodes();
        }
    }

    private void convertToCourseNames() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 1; col < NUM_COLS; col++) {
                int courseNumber = courseNumbers[row][col - 1];
                if (courseNumber != 0) {
                    for (Map.Entry<String, Integer> entry : classMap.entrySet()) {
                        if (entry.getValue() == courseNumber) {
                            table.setValueAt(entry.getKey(), row, col);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void convertToCourseCodes() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 1; col < NUM_COLS; col++) {
                int courseNumber = courseNumbers[row][col - 1];
                if (courseNumber != 0) {
                    table.setValueAt(String.valueOf(courseNumber), row, col);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseScheduleApp().setVisible(true));
    }
}
