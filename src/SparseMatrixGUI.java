import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SparseMatrixGUI extends JFrame {

    private int matrixSize;
    private double sparsityPercentage; // New variable to hold sparsity percentage
    private int[][] sparseMatrix1; // First matrix
    private int[][] sparseMatrix2; // Second matrix
    private int[][] resultMatrix; // Result matrix for addition or subtraction

    private JTable matrixTable1; // Table for first matrix
    private JTable matrixTable2; // Table for second matrix
    private JTable resultTable; // Table for result matrix
    private JButton generateButton;
    private JButton toggleViewButton; // Button to toggle view
    private JButton generateSecondMatrixButton; // Button to generate second matrix
    private JButton addMatrixButton; // Button to add matrices
    private JButton subtractMatrixButton; // Button to subtract matrices
    private JButton multiplyMatrixButton; // Button to multiply matrices traditionally
    private JButton multiplySparseMatrixButton; // Button to multiply matrices using sparse matrix method
    private JButton transposeMatrixButton; // Button to transpose second matrix
    private JTextField sizeTextField;
    private JLabel sparsityLabel;
    private JTextField sparsityTextField;
    private JLabel messageLabel; // Label to display messages
    private boolean isSparseView; // Flag to track current view mode

    public SparseMatrixGUI() {
        setTitle("Sparse Matrix Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(2, 6, 5, 5)); // Two rows for buttons

        JLabel sizeLabel = new JLabel("Enter matrix size:");
        sizeTextField = new JTextField(5);
        generateButton = new JButton("Generate Matrix");

        // New components for sparsity percentage
        sparsityLabel = new JLabel("Enter sparsity percentage (0-1):");
        sparsityTextField = new JTextField(5);

        // Button to toggle view mode
        toggleViewButton = new JButton("Toggle View");
        toggleViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMatrixView();
            }
        });

        // Button to generate second matrix
        generateSecondMatrixButton = new JButton("Generate Second Matrix");
        generateSecondMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSecondMatrix();
            }
        });

        // Button to add matrices
        addMatrixButton = new JButton("Add Matrices");
        addMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMatrices();
            }
        });

        // Button to subtract matrices
        subtractMatrixButton = new JButton("Subtract Matrices");
        subtractMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subtractMatrices();
            }
        });

        // Button to multiply matrices traditionally
        multiplyMatrixButton = new JButton("Multiply Matrices");
        multiplyMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiplyMatrices();
            }
        });

        // Button to multiply matrices using sparse matrix method
        multiplySparseMatrixButton = new JButton("Multiply Sparse Matrices");
        multiplySparseMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiplySparseMatrices();
            }
        });

        // Button to transpose second matrix
        transposeMatrixButton = new JButton("Transpose Second Matrix");
        transposeMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transposeSecondMatrix();
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
        controlPanel.add(transposeMatrixButton); // Add transpose button

        messageLabel = new JLabel(" "); // Initial message label
        controlPanel.add(messageLabel); // Add message label to control panel

        JPanel matrixPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Panel to hold all matrix tables

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

        // Default view mode is dense matrix
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

            // Generate sparse matrix 1
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    // Generate random number between 0 and 1
                    double randomValue = random.nextDouble();
                    if (randomValue > sparsityPercentage) {
                        // If randomValue is greater than sparsityPercentage, set to random number between 0 and 9
                        sparseMatrix1[i][j] = random.nextInt(10);
                    } else {
                        // Otherwise, set to 0
                        sparseMatrix1[i][j] = 0;
                    }
                }
            }

            // Update the table model to display matrix 1 based on current view mode
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

            // Generate sparse matrix 2 with same sparsity percentage as matrix 1
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    // Generate random number between 0 and 1
                    double randomValue = random.nextDouble();
                    if (randomValue > sparsityPercentage) {
                        // If randomValue is greater than sparsityPercentage, set to random number between 0 and 9
                        sparseMatrix2[i][j] = random.nextInt(10);
                    } else {
                        // Otherwise, set to 0
                        sparseMatrix2[i][j] = 0;
                    }
                }
            }

            // Update the table model to display matrix 2 based on current view mode
            updateMatrixDisplay(matrixTable2, sparseMatrix2);

        } catch (NumberFormatException e) {
            showMessage("Invalid input. Please enter valid numbers.");
        }
    }

    private void addMatrices() {
        if (!checkMatrixSizes()) {
            showMessage("Matrix sizes are not compatible for addition.");
            return;
        }

        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                resultMatrix[i][j] = sparseMatrix1[i][j] + sparseMatrix2[i][j];
            }
        }

        updateMatrixDisplay(resultTable, resultMatrix);
    }

    private void subtractMatrices() {
        if (!checkMatrixSizes()) {
            showMessage("Matrix sizes are not compatible for subtraction.");
            return;
        }

        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                resultMatrix[i][j] = sparseMatrix1[i][j] - sparseMatrix2[i][j];
            }
        }

        updateMatrixDisplay(resultTable, resultMatrix);
    }

    private void multiplyMatrices() {
        if (!checkMatrixMultiplication()) {
            showMessage("Matrix sizes are not compatible for multiplication.");
            return;
        }

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

        updateMatrixDisplay(resultTable, resultMatrix);
    }

    private void multiplySparseMatrices() {
        if (!checkMatrixMultiplication()) {
            showMessage("Matrix sizes are not compatible for multiplication.");
            return;
        }

        // Assuming sparse matrices are represented with sparseMatrix1 and sparseMatrix2

        // Create resultMatrix using a sparse matrix representation
        resultMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += sparseMatrix1[i][k] * sparseMatrix2[k][j];
                }
                if (sum != 0) {
                    resultMatrix[i][j] = sum; // Store non-zero values only
                }
            }
        }

        updateMatrixDisplay(resultTable, resultMatrix);
    }

    private void transposeSecondMatrix() {
        if (sparseMatrix2 == null) {
            showMessage("Second matrix is not generated yet.");
            return;
        }

        int[][] transposedMatrix = transpose(sparseMatrix2);

        // Update the table model to display transposed matrix 2 based on current view mode
        updateMatrixDisplay(matrixTable2, transposedMatrix);
    }

    private int[][] transpose(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] transposed = new int[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }

    private boolean checkMatrixSizes() {
        if (sparseMatrix1 == null || sparseMatrix2 == null) {
            return false;
        }

        return sparseMatrix1.length == matrixSize && sparseMatrix1[0].length == matrixSize &&
                sparseMatrix2.length == matrixSize && sparseMatrix2[0].length == matrixSize;
    }

    private boolean checkMatrixMultiplication() {
        if (sparseMatrix1 == null || sparseMatrix2 == null) {
            return false;
        }

        // Check if the number of columns in sparseMatrix1 equals the number of rows in sparseMatrix2
        return sparseMatrix1[0].length == sparseMatrix2.length;
    }

    private void updateMatrixDisplay(JTable table, int[][] matrix) {
        DefaultTableModel tableModel = new DefaultTableModel(matrixSize, matrixSize);
        if (isSparseView) {
            // Display sparse matrix
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    if (matrix[i][j] != 0) {
                        tableModel.setValueAt(matrix[i][j], i, j);
                    }
                }
            }
        } else {
            // Display dense matrix
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    tableModel.setValueAt(matrix[i][j], i, j);
                }
            }
        }
        table.setModel(tableModel);
    }

    private void toggleMatrixView() {
        isSparseView = !isSparseView;
        updateMatrixDisplay(matrixTable1, sparseMatrix1);
        updateMatrixDisplay(matrixTable2, sparseMatrix2);
        updateMatrixDisplay(resultTable, resultMatrix);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SparseMatrixGUI();
            }
        });
    }
}
