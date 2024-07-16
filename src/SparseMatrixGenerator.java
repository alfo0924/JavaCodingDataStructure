import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

// Define SparseMatrixEntry class
class SparseMatrixEntry {
    private int row;
    private int column;
    private int value;

    public SparseMatrixEntry(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getValue() {
        return value;
    }
}

public class SparseMatrixGenerator extends JFrame {

    private JPanel panel;
    private JTextField sizeField;
    private JTextField densityField;
    private JTextArea matrixArea1;
    private JTextArea matrixArea2;
    private JTextArea resultArea1;
    private JTextArea resultArea2;
    private JTextArea resultArea3;
    private JTextArea resultArea4;
    private JLabel messageLabel;
    private JLabel timeLabel; // Label to display execution time
    private JButton hideZerosButton;
    private JButton traditionalMultiplyButton;
    private JButton sparseMultiplyButton;
    private JButton transposeOriginalButton;
    private JButton convertButton; // Button for converting to sparse
    private JButton addSparseButton; // Button for adding sparse matrices
    private JButton subtractSparseButton; // Button for subtracting sparse matrices
    private ArrayList<ArrayList<SparseMatrixEntry>> matrix1; // Use SparseMatrixEntry for matrix representation
    private ArrayList<ArrayList<SparseMatrixEntry>> matrix2; // Use SparseMatrixEntry for matrix representation
    private boolean zerosHidden = false;

    public SparseMatrixGenerator() {
        setTitle("Sparse Matrix Generator");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1)); // Increased to accommodate timeLabel

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        JLabel sizeLabel = new JLabel("Matrix Size (n):");
        sizeField = new JTextField();
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);

        JLabel densityLabel = new JLabel("Density Percentage (0-1):");
        densityField = new JTextField();
        inputPanel.add(densityLabel);
        inputPanel.add(densityField);

        JButton generateButton = new JButton("Generate Dense Matrix");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateDenseMatrix();
            }
        });
        inputPanel.add(generateButton);

        JButton generateSecondMatrixButton = new JButton("Generate Second Matrix");
        generateSecondMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSecondMatrix();
            }
        });
        inputPanel.add(generateSecondMatrixButton);

        panel.add(inputPanel);

        JPanel matrixPanel = new JPanel(new GridLayout(1, 2));

        matrixArea1 = new JTextArea();
        matrixArea1.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(matrixArea1);
        matrixPanel.add(scrollPane1);

        matrixArea2 = new JTextArea();
        matrixArea2.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(matrixArea2);
        matrixPanel.add(scrollPane2);

        panel.add(matrixPanel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));

        convertButton = new JButton("Convert to Sparse");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea1.setText(convertToSparse(matrix1, "Sparse Matrix 1"));
            }
        });
        buttonPanel.add(convertButton);

        addSparseButton = new JButton("Add Sparse Matrices");
        addSparseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkMatrixSizes()) {
                    long startTime = System.nanoTime();
                    resultArea2.setText(addSparseMatrices());
                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    updateExecutionTime(executionTime);
                    messageLabel.setText("");
                } else {
                    messageLabel.setText("Sizes do not match, cannot add or subtract!");
                }
            }
        });
        buttonPanel.add(addSparseButton);

        subtractSparseButton = new JButton("Subtract Sparse Matrices");
        subtractSparseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkMatrixSizes()) {
                    long startTime = System.nanoTime();
                    resultArea3.setText(subtractSparseMatrices());
                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    updateExecutionTime(executionTime);
                    messageLabel.setText("");
                } else {
                    messageLabel.setText("Sizes do not match, cannot add or subtract!");
                }
            }
        });
        buttonPanel.add(subtractSparseButton);

        transposeOriginalButton = new JButton("Transpose Original Matrix");
        transposeOriginalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long startTime = System.nanoTime();
                transposeOriginalMatrix(); // Invoke method to transpose original matrix
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                updateExecutionTime(executionTime);
            }
        });
        buttonPanel.add(transposeOriginalButton);

        traditionalMultiplyButton = new JButton("Traditional Multiply");
        traditionalMultiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkMatrixSizes()) {
                    long startTime = System.nanoTime();
                    resultArea4.setText(traditionalMultiply());
                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    updateExecutionTime(executionTime);
                    messageLabel.setText("");
                } else {
                    messageLabel.setText("Sizes do not match, cannot multiply!");
                }
            }
        });
        buttonPanel.add(traditionalMultiplyButton);

        sparseMultiplyButton = new JButton("Sparse Multiply");
        sparseMultiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkMatrixSizes()) {
                    long startTime = System.nanoTime();
                    resultArea4.setText(sparseMultiply());
                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    updateExecutionTime(executionTime);
                    messageLabel.setText("");
                } else {
                    messageLabel.setText("Sizes do not match, cannot multiply!");
                }
            }
        });
        buttonPanel.add(sparseMultiplyButton);

        hideZerosButton = new JButton("Hide Zeros");
        hideZerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zerosHidden) {
                    showZeros();
                } else {
                    hideZeros();
                }
            }
        });
        buttonPanel.add(hideZerosButton);

        panel.add(buttonPanel);

        JPanel resultPanel = new JPanel(new GridLayout(1, 4));

        resultArea1 = new JTextArea();
        resultArea1.setEditable(false);
        JScrollPane scrollPaneResult1 = new JScrollPane(resultArea1);
        resultPanel.add(scrollPaneResult1);

        resultArea2 = new JTextArea();
        resultArea2.setEditable(false);
        JScrollPane scrollPaneResult2 = new JScrollPane(resultArea2);
        resultPanel.add(scrollPaneResult2);

        resultArea3 = new JTextArea();
        resultArea3.setEditable(false);
        JScrollPane scrollPaneResult3 = new JScrollPane(resultArea3);
        resultPanel.add(scrollPaneResult3);

        resultArea4 = new JTextArea();
        resultArea4.setEditable(false);
        JScrollPane scrollPaneResult4 = new JScrollPane(resultArea4);
        resultPanel.add(scrollPaneResult4);

        panel.add(resultPanel);

        messageLabel = new JLabel();
        panel.add(messageLabel);

        timeLabel = new JLabel("Execution Time: ");
        panel.add(timeLabel);

        add(panel);
        setVisible(true);

        matrix1 = new ArrayList<>();
        matrix2 = new ArrayList<>();
    }

    private void generateDenseMatrix() {
        try {
            int n = Integer.parseInt(sizeField.getText());
            double density = Double.parseDouble(densityField.getText());

            if (n <= 0 || density < 0 || density > 1) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid values.");
                return;
            }

            Random random = new Random();
            StringBuilder matrixStr = new StringBuilder();

            matrix1.clear();
            for (int i = 0; i < n; i++) {
                ArrayList<SparseMatrixEntry> row = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (random.nextDouble() <= density) {
                        int value = random.nextInt(10) + 1;
                        row.add(new SparseMatrixEntry(i, j, value));
                        matrixStr.append(value).append("\t");
                    } else {
                        row.add(new SparseMatrixEntry(i, j, 0));
                        matrixStr.append("0\t");
                    }
                }
                matrix1.add(row);
                matrixStr.append("\n");
            }

            matrixArea1.setText(matrixStr.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please enter valid numeric values.");
        }
    }

    private void generateSecondMatrix() {
        try {
            int n = Integer.parseInt(sizeField.getText());
            double density = Double.parseDouble(densityField.getText());

            if (n <= 0 || density < 0 || density > 1) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid values.");
                return;
            }

            Random random = new Random();
            StringBuilder matrixStr = new StringBuilder();

            matrix2.clear();
            for (int i = 0; i < n; i++) {
                ArrayList<SparseMatrixEntry> row = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (random.nextDouble() <= density) {
                        int value = random.nextInt(10) + 1;
                        row.add(new SparseMatrixEntry(i, j, value));
                        matrixStr.append(value).append("\t");
                    } else {
                        row.add(new SparseMatrixEntry(i, j, 0));
                        matrixStr.append("0\t");
                    }
                }
                matrix2.add(row);
                matrixStr.append("\n");
            }

            matrixArea2.setText(matrixStr.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please enter valid numeric values.");
        }
    }

    private boolean checkMatrixSizes() {
        return matrix1.size() == matrix2.size();
    }

    private String convertToSparse(ArrayList<ArrayList<SparseMatrixEntry>> matrix, String matrixName) {
        StringBuilder sparseMatrixStr = new StringBuilder(matrixName + ":\n");

        for (ArrayList<SparseMatrixEntry> row : matrix) {
            for (SparseMatrixEntry entry : row) {
                sparseMatrixStr.append("(").append(entry.getRow()).append(",").append(entry.getColumn())
                        .append(",").append(entry.getValue()).append(") ");
            }
            sparseMatrixStr.append("\n");
        }

        return sparseMatrixStr.toString();
    }

    private String addSparseMatrices() {
        int n = matrix1.size();
        ArrayList<ArrayList<SparseMatrixEntry>> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            ArrayList<SparseMatrixEntry> resultRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int value = matrix1.get(i).get(j).getValue() + matrix2.get(i).get(j).getValue();
                resultRow.add(new SparseMatrixEntry(i, j, value));
            }
            result.add(resultRow);
        }

        return convertToSparse(result, "Result");
    }

    private String subtractSparseMatrices() {
        int n = matrix1.size();
        ArrayList<ArrayList<SparseMatrixEntry>> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            ArrayList<SparseMatrixEntry> resultRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int value = matrix1.get(i).get(j).getValue() - matrix2.get(i).get(j).getValue();
                resultRow.add(new SparseMatrixEntry(i, j, value));
            }
            result.add(resultRow);
        }

        return convertToSparse(result, "Result");
    }

    private void transposeOriginalMatrix() {
        int n = matrix1.size();
        ArrayList<ArrayList<SparseMatrixEntry>> transposedMatrix = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) {
                    transposedMatrix.add(new ArrayList<>());
                }
                transposedMatrix.get(j).add(matrix1.get(i).get(j));
            }
        }

        matrix1 = transposedMatrix;
        matrixArea1.setText(convertToSparse(matrix1, "Transposed Matrix"));
    }

    private String traditionalMultiply() {
        int n = matrix1.size();
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += matrix1.get(i).get(k).getValue() * matrix2.get(k).get(j).getValue();
                }
            }
        }

        return convertToStringMatrix(result);
    }

    private String sparseMultiply() {
        int n = matrix1.size();
        ArrayList<ArrayList<SparseMatrixEntry>> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            ArrayList<SparseMatrixEntry> resultRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int value = 0;
                for (int k = 0; k < n; k++) {
                    value += matrix1.get(i).get(k).getValue() * matrix2.get(k).get(j).getValue();
                }
                if (value != 0) {
                    resultRow.add(new SparseMatrixEntry(i, j, value));
                }
            }
            result.add(resultRow);
        }

        return convertToSparse(result, "Result");
    }

    // 將以下的相關部分做修改
    private void updateExecutionTime(long executionTime) {
        double seconds = (double) executionTime / 1_000_000_000.0;
        timeLabel.setText(String.format("Execution Time: %.15f seconds", seconds));
    }


    private void hideZeros() {
        zerosHidden = true;
        matrixArea1.setText(hideZerosInMatrix(matrix1));
        matrixArea2.setText(hideZerosInMatrix(matrix2));
    }

    private void showZeros() {
        zerosHidden = false;
        matrixArea1.setText(convertToSparse(matrix1, "Matrix 1"));
        matrixArea2.setText(convertToSparse(matrix2, "Matrix 2"));
    }

    private String hideZerosInMatrix(ArrayList<ArrayList<SparseMatrixEntry>> matrix) {
        StringBuilder matrixStr = new StringBuilder();

        for (ArrayList<SparseMatrixEntry> row : matrix) {
            for (SparseMatrixEntry entry : row) {
                if (entry.getValue() != 0) {
                    matrixStr.append(entry.getValue()).append("\t");
                } else {
                    matrixStr.append("0\t");
                }
            }
            matrixStr.append("\n");
        }

        return matrixStr.toString();
    }

    private String convertToStringMatrix(int[][] matrix) {
        StringBuilder matrixStr = new StringBuilder();

        for (int[] row : matrix) {
            for (int value : row) {
                matrixStr.append(value).append("\t");
            }
            matrixStr.append("\n");
        }

        return matrixStr.toString();
    }

    public static void main(String[] args) {
        new SparseMatrixGenerator();
    }
}
