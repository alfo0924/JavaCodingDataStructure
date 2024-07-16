import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SparseMatrixGUI extends JFrame {

    private int matrixSize;
    private double sparsityPercentage;
    private int[][] sparseMatrix1;
    private int[][] sparseMatrix2;
    private int[][] resultMatrix;

    private JTable matrixTable1;
    private JTable matrixTable2;
    private JTable resultTable;
    private JButton generateButton;
    private JButton toggleViewButton;
    private JButton generateSecondMatrixButton;
    private JButton addMatrixButton;
    private JButton subtractMatrixButton;
    private JButton multiplyMatrixButton;
    private JButton multiplySparseMatrixButton;
    private JButton transposeMatrixButton;
    private JButton transposeFirstMatrixButton;
    private JTextField sizeTextField;
    private JLabel sparsityLabel;
    private JTextField sparsityTextField;
    private JLabel messageLabel;
    private JLabel timeLabel;
    private boolean isSparseView;

    public SparseMatrixGUI() {
        setTitle("Sparse Matrix Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        JLabel sizeLabel = new JLabel("Enter matrix size:");
        sizeTextField = new JTextField(5);
        generateButton = new JButton("Generate Matrix");

        sparsityLabel = new JLabel("Enter sparsity percentage (0-1):");
        sparsityTextField = new JTextField(5);

        toggleViewButton = new JButton("Toggle View");
        toggleViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMatrixView();
            }
        });

        generateSecondMatrixButton = new JButton("Generate Second Matrix");
        generateSecondMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSecondMatrix();
            }
        });

        addMatrixButton = new JButton("Add Matrices");
        addMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMatrices();
            }
        });

        subtractMatrixButton = new JButton("Subtract Matrices");
        subtractMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subtractMatrices();
            }
        });

        multiplyMatrixButton = new JButton("Multiply Matrices");
        multiplyMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiplyMatrices();
            }
        });

        multiplySparseMatrixButton = new JButton("Multiply Sparse Matrices");
        multiplySparseMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiplySparseMatrices();
            }
        });

        transposeMatrixButton = new JButton("Transpose Second Matrix");
        transposeMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transposeSecondMatrix();
            }
        });

        transposeFirstMatrixButton = new JButton("Transpose First Matrix");
        transposeFirstMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transposeFirstMatrix();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMatrix();
            }
        });

        controlPanel.add(sizeLabel);
        controlPanel.add(sizeTextField);
        controlPanel.add(sparsityLabel);
        controlPanel.add(sparsityTextField);
        controlPanel.add(generateButton);
        controlPanel.add(toggleViewButton);
        controlPanel.add(generateSecondMatrixButton);
        controlPanel.add(addMatrixButton);
        controlPanel.add(subtractMatrixButton);
        controlPanel.add(multiplyMatrixButton);
        controlPanel.add(multiplySparseMatrixButton);
        controlPanel.add(transposeMatrixButton);
        controlPanel.add(transposeFirstMatrixButton);

        messageLabel = new JLabel(" ");
        controlPanel.add(messageLabel);

        timeLabel = new JLabel("Execution Time: ");
        controlPanel.add(timeLabel);

        JPanel matrixPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        matrixTable1 = new JTable();
        matrixTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane1 = new JScrollPane(matrixTable1);
        matrixPanel.add(scrollPane1);

        matrixTable2 = new JTable();
        matrixTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane2 = new JScrollPane(matrixTable2);
        matrixPanel.add(scrollPane2);

        resultTable = new JTable();
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane3 = new JScrollPane(resultTable);
        matrixPanel.add(scrollPane3);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(matrixPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);

        isSparseView = false;
    }

    private void generateMatrix() {
        try {
            matrixSize = Integer.parseInt(sizeTextField.getText());
            if (matrixSize <= 0) {
                showMessage("Matrix size must be greater than zero.");
                return;
            }

            sparsityPercentage = Double.parseDouble(sparsityTextField.getText());
            if (sparsityPercentage < 0 || sparsityPercentage > 1) {
                showMessage("Sparsity percentage must be between 0 and 1.");
                return;
            }

            sparseMatrix1 = new int[matrixSize][matrixSize];

            Random random = new Random();

            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    double randomValue = random.nextDouble();
                    if (randomValue > sparsityPercentage) {
                        sparseMatrix1[i][j] = random.nextInt(10);
                    } else {
                        sparseMatrix1[i][j] = 0;
                    }
                }
            }

            updateMatrixDisplay(matrixTable1, sparseMatrix1);

        } catch (NumberFormatException e) {
            showMessage("Invalid input. Please enter valid numbers.");
        }
    }

    private void generateSecondMatrix() {
        try {
            if (matrixSize <= 0) {
                showMessage("Matrix size must be greater than zero.");
                return;
            }

            sparsityPercentage = Double.parseDouble(sparsityTextField.getText());
            if (sparsityPercentage < 0 || sparsityPercentage > 1) {
                showMessage("Sparsity percentage must be between 0 and 1.");
                return;
            }

            sparseMatrix2 = new int[matrixSize][matrixSize];

            Random random = new Random();

            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    double randomValue = random.nextDouble();
                    if (randomValue > sparsityPercentage) {
                        sparseMatrix2[i][j] = random.nextInt(10);
                    } else {
                        sparseMatrix2[i][j] = 0;
                    }
                }
            }

            updateMatrixDisplay(matrixTable2, sparseMatrix2);

        } catch (NumberFormatException e) {
            showMessage("Invalid input. Please enter valid numbers.");
        }
    }

    private void addMatrices() {
        if (!checkMatrixSizes()) {
            showMessage("Matrix sizes are incompatible for addition.");
            return;
        }

        long startTime = System.nanoTime();
        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                resultMatrix[i][j] = sparseMatrix1[i][j] + sparseMatrix2[i][j];
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(resultTable, resultMatrix);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private void subtractMatrices() {
        if (!checkMatrixSizes()) {
            showMessage("Matrix sizes are incompatible for subtraction.");
            return;
        }

        long startTime = System.nanoTime();
        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                resultMatrix[i][j] = sparseMatrix1[i][j] - sparseMatrix2[i][j];
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(resultTable, resultMatrix);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private void multiplyMatrices() {
        if (!checkMatrixMultiplication()) {
            showMessage("Matrix sizes are incompatible for multiplication.");
            return;
        }

        long startTime = System.nanoTime();
        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += sparseMatrix1[i][k] * sparseMatrix2[k][j];
                }
                resultMatrix[i][j] = sum;
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(resultTable, resultMatrix);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private void multiplySparseMatrices() {
        if (!checkMatrixMultiplication()) {
            showMessage("Matrix sizes are incompatible for multiplication.");
            return;
        }

        long startTime = System.nanoTime();
        int[][] result = sparseMatrixMultiplication(sparseMatrix1, sparseMatrix2);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(resultTable, result);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private int[][] sparseMatrixMultiplication(int[][] matrix1, int[][] matrix2) {
        int size = matrix1.length;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int sum = 0;
                for (int k = 0; k < size; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    private void transposeFirstMatrix() {
        long startTime = System.nanoTime();
        sparseMatrix1 = fastTranspose(sparseMatrix1);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(matrixTable1, sparseMatrix1);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private void transposeSecondMatrix() {
        long startTime = System.nanoTime();
        sparseMatrix2 = fastTranspose(sparseMatrix2);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        updateMatrixDisplay(matrixTable2, sparseMatrix2);
        timeLabel.setText("Execution Time: " + duration + " ns");
    }

    private int[][] fastTranspose(int[][] matrix) {
        int size = matrix.length;
        int[][] transpose = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }

        return transpose;
    }

    private boolean checkMatrixSizes() {
        return sparseMatrix1 != null && sparseMatrix2 != null && sparseMatrix1.length == matrixSize && sparseMatrix2.length == matrixSize;
    }

    private boolean checkMatrixMultiplication() {
        return sparseMatrix1 != null && sparseMatrix2 != null && sparseMatrix1.length == matrixSize && sparseMatrix2.length == matrixSize;
    }

    private void updateMatrixDisplay(JTable table, int[][] matrix) {
        int size = matrix.length;
        DefaultTableModel model = new DefaultTableModel(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                model.setValueAt(matrix[i][j], i, j);
            }
        }
        table.setModel(model);
    }

    private void toggleMatrixView() {
        if (!isSparseView) {
            updateMatrixDisplay(matrixTable1, sparseMatrix1);
            updateMatrixDisplay(matrixTable2, sparseMatrix2);
            isSparseView = true;
        } else {
            updateMatrixDisplay(matrixTable1, sparseMatrix1);
            updateMatrixDisplay(matrixTable2, sparseMatrix2);
            isSparseView = false;
        }
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SparseMatrixGUI();
            }
        });
    }
}