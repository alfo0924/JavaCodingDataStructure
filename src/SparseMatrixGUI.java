import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SparseMatrixGUI extends JFrame {

    private JTextField densityField;
    private JTextArea matrixArea, sparseMatrixArea, messageArea, timeArea;
    private JButton generateButton, compareTimeButton;
    private int[][] matrix;
    private ArrayList<int[]> sparseMatrix;

    public SparseMatrixGUI() {
        setTitle("D1204433 林俊傑 Sparse Matrix Operations");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        densityField = new JTextField(5);
        generateButton = new JButton("Generate and Compare");
        compareTimeButton = new JButton("Compare Times");

        inputPanel.add(new JLabel("Density:"));
        inputPanel.add(densityField);
        inputPanel.add(generateButton);
        inputPanel.add(compareTimeButton);

        matrixArea = new JTextArea(15, 30);
        sparseMatrixArea = new JTextArea(15, 30);
        messageArea = new JTextArea(3, 50);
        messageArea.setEditable(false);
        timeArea = new JTextArea(2, 50);
        timeArea.setEditable(false);

        JPanel matrixPanel = new JPanel(new GridLayout(1, 2));
        matrixPanel.add(new JScrollPane(matrixArea));
        matrixPanel.add(new JScrollPane(sparseMatrixArea));

        add(inputPanel, BorderLayout.NORTH);
        add(matrixPanel, BorderLayout.CENTER);
        add(new JScrollPane(messageArea), BorderLayout.SOUTH);
        add(new JScrollPane(timeArea), BorderLayout.EAST);

        setupListeners();
    }

    private void setupListeners() {
        generateButton.addActionListener(e -> generateAndDisplayMatrix());
        compareTimeButton.addActionListener(e -> compareTimes());
    }

    private void generateAndDisplayMatrix() {
        try {
            double density = Double.parseDouble(densityField.getText());
            int size = 10; // Fixed size for demonstration
            matrix = new int[size][size];
            Random rand = new Random();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rand.nextDouble() < density) {
                        matrix[i][j] = rand.nextInt(10) + 1; // Non-zero values between 1 and 10
                    }
                }
            }

            sparseMatrix = convertToSparseFormat(matrix);
            displayMatrix(matrixArea, matrix);
            displaySparseMatrix(sparseMatrixArea, sparseMatrix);
            messageArea.setText("Matrix generated with density: " + density);
        } catch (NumberFormatException ex) {
            messageArea.setText("Invalid input for density");
        }
    }

    private ArrayList<int[]> convertToSparseFormat(int[][] matrix) {
        ArrayList<int[]> sparseMatrix = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    sparseMatrix.add(new int[]{i, j, matrix[i][j]});
                }
            }
        }
        return sparseMatrix;
    }

    private void displayMatrix(JTextArea area, int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int val : row) {
                sb.append(String.format("%4d", val));
            }
            sb.append("\n");
        }
        area.setText(sb.toString());
    }

    private void displaySparseMatrix(JTextArea area, ArrayList<int[]> sparseMatrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] entry : sparseMatrix) {
            sb.append(String.format("(%d, %d, %d)\n", entry[0], entry[1], entry[2]));
        }
        area.setText(sb.toString());
    }

    private void compareTimes() {
        int[] sizes = {10, 100, 1000, 3000};
        double density = 0.01;

        StringBuilder results = new StringBuilder();
        for (int size : sizes) {
            long denseStartTime = System.nanoTime();
            int[][] denseMatrix = new int[size][size];
            Random rand = new Random();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rand.nextDouble() < density) {
                        denseMatrix[i][j] = rand.nextInt(10) + 1; // Non-zero values between 1 and 10
                    }
                }
            }
            long denseEndTime = System.nanoTime();
            double denseDuration = (denseEndTime - denseStartTime) / 1000000.0;

            long sparseStartTime = System.nanoTime();
            ArrayList<int[]> sparseMatrix = convertToSparseFormat(denseMatrix);
            long sparseEndTime = System.nanoTime();
            double sparseDuration = (sparseEndTime - sparseStartTime) / 1000000.0;

            results.append(String.format("Size: %d, Dense Matrix Time: %.2f ms, Sparse Matrix Time: %.2f ms%n", size, denseDuration, sparseDuration));
        }
        timeArea.setText(results.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SparseMatrixGUI().setVisible(true));
    }
}