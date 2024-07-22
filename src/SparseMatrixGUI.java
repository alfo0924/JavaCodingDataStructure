import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SparseMatrixGUI extends JFrame {

    private JTextField densityField; // 用於輸入稀疏矩陣的密度
    private JTextArea matrixArea, sparseMatrixArea, messageArea, timeArea; // 用於顯示矩陣和訊息的文本區域
    private JButton generateButton, compareTimeButton; // 按鈕用於生成矩陣和比較時間
    private int[][] matrix; // 二維陣列表示密集矩陣
    private ArrayList<int[]> sparseMatrix; // 用於存儲稀疏矩陣的列表

    public SparseMatrixGUI() {
        setTitle("D1204433 林俊傑 Sparse Matrix Operations"); // 設定視窗標題
        setSize(1000, 800); // 設定視窗大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設定關閉操作
        setLayout(new BorderLayout()); // 設定佈局

        // 創建輸入面板
        JPanel inputPanel = new JPanel(new FlowLayout());
        densityField = new JTextField(5); // 密度輸入框
        generateButton = new JButton("Generate and Compare"); // 生成和比較按鈕
        compareTimeButton = new JButton("Compare Times"); // 比較時間按鈕

        inputPanel.add(new JLabel("Density:")); // 添加密度標籤
        inputPanel.add(densityField); // 添加密度輸入框
        inputPanel.add(generateButton); // 添加生成按鈕
        inputPanel.add(compareTimeButton); // 添加比較按鈕

        // 創建文本區域以顯示矩陣和訊息
        matrixArea = new JTextArea(15, 30); // 顯示密集矩陣
        sparseMatrixArea = new JTextArea(15, 30); // 顯示稀疏矩陣
        messageArea = new JTextArea(3, 50); // 顯示訊息
        messageArea.setEditable(false); // 設定為不可編輯
        timeArea = new JTextArea(10, 50); // 顯示時間比較結果
        timeArea.setEditable(false); // 設定為不可編輯

        // 創建矩陣面板以顯示兩個文本區域
        JPanel matrixPanel = new JPanel(new GridLayout(1, 2));
        matrixPanel.add(new JScrollPane(matrixArea)); // 添加密集矩陣文本區域
        matrixPanel.add(new JScrollPane(sparseMatrixArea)); // 添加稀疏矩陣文本區域

        // 將面板添加到主視窗中
        add(inputPanel, BorderLayout.NORTH); // 上方添加輸入面板
        add(matrixPanel, BorderLayout.CENTER); // 中間添加矩陣面板
        add(new JScrollPane(messageArea), BorderLayout.SOUTH); // 下方添加訊息區域
        add(new JScrollPane(timeArea), BorderLayout.EAST); // 右側添加時間區域

        setupListeners(); // 設定事件監聽器
    }

    private void setupListeners() {
        // 設定按鈕的事件監聽器
        generateButton.addActionListener(e -> generateAndDisplayMatrix()); // 生成矩陣
        compareTimeButton.addActionListener(e -> compareTimes()); // 比較時間
    }

    private void generateAndDisplayMatrix() {
        try {
            double density = Double.parseDouble(densityField.getText()); // 讀取密度
            int size = 10; // 固定大小以進行演示
            matrix = new int[size][size]; // 初始化密集矩陣
            Random rand = new Random(); // 隨機數生成器

            // 生成密集矩陣
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rand.nextDouble() < density) { // 根據密度決定是否設置非零值
                        matrix[i][j] = rand.nextInt(10) + 1; // 非零值範圍在 1 到 10 之間
                    }
                }
            }

            sparseMatrix = convertToSparseFormat(matrix); // 將密集矩陣轉換為稀疏格式
            displayMatrix(matrixArea, matrix); // 顯示密集矩陣
            displaySparseMatrix(sparseMatrixArea, sparseMatrix); // 顯示稀疏矩陣
            messageArea.setText("Matrix generated with density: " + density); // 顯示生成的訊息
        } catch (NumberFormatException ex) {
            messageArea.setText("Invalid input for density"); // 處理無效的密度輸入
        }
    }

    private ArrayList<int[]> convertToSparseFormat(int[][] matrix) {
        ArrayList<int[]> sparseMatrix = new ArrayList<>(); // 初始化稀疏矩陣列表
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) { // 如果矩陣元素不為零，則添加到稀疏矩陣
                    sparseMatrix.add(new int[]{i, j, matrix[i][j]}); // 存儲行、列及值
                }
            }
        }
        return sparseMatrix; // 返回稀疏矩陣
    }

    private void displayMatrix(JTextArea area, int[][] matrix) {
        StringBuilder sb = new StringBuilder(); // 用於構建顯示字符串
        for (int[] row : matrix) {
            for (int val : row) {
                sb.append(String.format("%4d", val)); // 格式化顯示矩陣元素
            }
            sb.append("\n"); // 換行
        }
        area.setText(sb.toString()); // 設定文本區域的內容
    }

    private void displaySparseMatrix(JTextArea area, ArrayList<int[]> sparseMatrix) {
        StringBuilder sb = new StringBuilder(); // 用於構建顯示字符串
        for (int[] entry : sparseMatrix) {
            sb.append(String.format("(%d, %d, %d)\n", entry[0], entry[1], entry[2])); // 格式化顯示稀疏矩陣元素
        }
        area.setText(sb.toString()); // 設定文本區域的內容
    }

    private void compareTimes() {
        int[] sizes = {10, 100, 1000, 3000}; // 不同的矩陣大小
        double density = 0.01; // 固定密度

        StringBuilder results = new StringBuilder(); // 用於存儲時間比較結果
        for (int size : sizes) {
            // 生成密集矩陣
            long denseStartTime = System.nanoTime(); // 開始計時
            int[][] denseMatrix = new int[size][size]; // 初始化密集矩陣
            Random rand = new Random(); // 隨機數生成器

            // 生成密集矩陣
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rand.nextDouble() < density) { // 根據密度決定是否設置非零值
                        denseMatrix[i][j] = rand.nextInt(10) + 1; // 非零值範圍在 1 到 10 之間
                    }
                }
            }
            long denseEndTime = System.nanoTime(); // 結束計時
            double denseDuration = (denseEndTime - denseStartTime) / 1000000.0; // 計算密集矩陣生成時間

            long sparseStartTime = System.nanoTime(); // 開始計時
            ArrayList<int[]> sparseMatrix = convertToSparseFormat(denseMatrix); // 轉換為稀疏矩陣
            long sparseEndTime = System.nanoTime(); // 結束計時
            double sparseDuration = (sparseEndTime - sparseStartTime) / 1000000.0; // 計算稀疏矩陣生成時間

            // 比較矩陣轉置的時間
            long denseTransposeStartTime = System.nanoTime();
            int[][] denseTranspose = transposeMatrix(denseMatrix);
            long denseTransposeEndTime = System.nanoTime();
            double denseTransposeDuration = (denseTransposeEndTime - denseTransposeStartTime) / 1000000.0;

            long sparseTransposeStartTime = System.nanoTime();
            ArrayList<int[]> sparseTranspose = transposeMatrix(sparseMatrix);
            long sparseTransposeEndTime = System.nanoTime();
            double sparseTransposeDuration = (sparseTransposeEndTime - sparseTransposeStartTime) / 1000000.0;

            // 比較矩陣加法的時間
            long denseAdditionStartTime = System.nanoTime();
            int[][] denseResult = addMatrices(denseMatrix, denseMatrix);
            long denseAdditionEndTime = System.nanoTime();
            double denseAdditionDuration = (denseAdditionEndTime - denseAdditionStartTime) / 1000000.0;

            long sparseAdditionStartTime = System.nanoTime();
            ArrayList<int[]> sparseResult = addMatrices(sparseMatrix, sparseMatrix);
            long sparseAdditionEndTime = System.nanoTime();
            double sparseAdditionDuration = (sparseAdditionEndTime - sparseAdditionStartTime) / 1000000.0;

            // 將結果添加到結果字符串中
            results.append(String.format("Size: %d%n", size));
            results.append(String.format("Dense Matrix Time: %.2f ms, Sparse Matrix Time: %.2f ms%n", denseDuration, sparseDuration));
            results.append(String.format("Dense Matrix Transpose Time: %.2f ms, Sparse Matrix Transpose Time: %.2f ms%n", denseTransposeDuration, sparseTransposeDuration));
            results.append(String.format("Dense Matrix Addition Time: %.2f ms, Sparse Matrix Addition Time: %.2f ms%n%n", denseAdditionDuration, sparseAdditionDuration));
        }
        timeArea.setText(results.toString()); // 顯示時間比較結果
    }

    private int[][] transposeMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transpose = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }

    private ArrayList<int[]> transposeMatrix(ArrayList<int[]> sparseMatrix) {
        ArrayList<int[]> transpose = new ArrayList<>();
        for (int[] entry : sparseMatrix) {
            transpose.add(new int[]{entry[1], entry[0], entry[2]});
        }
        return transpose;
    }

    private int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    private ArrayList<int[]> addMatrices(ArrayList<int[]> sparseMatrix1, ArrayList<int[]> sparseMatrix2) {
        ArrayList<int[]> result = new ArrayList<>(sparseMatrix1);
        for (int[] entry : sparseMatrix2) {
            boolean found = false;
            for (int[] resultEntry : result) {
                if (resultEntry[0] == entry[0] && resultEntry[1] == entry[1]) {
                    resultEntry[2] += entry[2];
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(new int[]{entry[0], entry[1], entry[2]});
            }
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SparseMatrixGUI().setVisible(true)); // 啟動 GUI
    }
}