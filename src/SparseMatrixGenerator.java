import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class SparseMatrixGenerator extends JFrame {

    private JPanel panel;
    private JTextField sizeField;
    private JTextField densityField;
    private JTextArea matrixArea1;
    private JTextArea matrixArea2;
    private JTextArea sparseMatrixArea;
    private JButton generateButton;
    private JButton generateSecondButton;
    private JButton convertButton;
    private JButton addSparseButton;
    private JButton transposeButton;
    private JButton multiplyButton;

    private ArrayList<ArrayList<Integer>> matrix1;
    private ArrayList<ArrayList<Integer>> matrix2;

    public SparseMatrixGenerator() {
        setTitle("Sparse Matrix Generator");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 5));

        JPanel inputPanel = new JPanel(new GridLayout(4, 1));

        JLabel sizeLabel = new JLabel("Matrix Size (n):");
        sizeField = new JTextField();
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);

        JLabel densityLabel = new JLabel("Density Percentage (0-1):");
        densityField = new JTextField();
        inputPanel.add(densityLabel);
        inputPanel.add(densityField);

        generateButton = new JButton("Generate Dense Matrix");
        inputPanel.add(generateButton);

        generateSecondButton = new JButton("Generate Second Matrix");
        inputPanel.add(generateSecondButton);

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

        JPanel sparsePanel = new JPanel(new GridLayout(1, 2));

        sparseMatrixArea = new JTextArea();
        sparseMatrixArea.setEditable(false);
        JScrollPane scrollPane3 = new JScrollPane(sparseMatrixArea);
        sparsePanel.add(scrollPane3);

        JPanel sparseButtonPanel = new JPanel(new GridLayout(4, 1));

        convertButton = new JButton("Convert to Sparse");
        sparseButtonPanel.add(convertButton);

        addSparseButton = new JButton("Add Sparse Matrices");
        sparseButtonPanel.add(addSparseButton);

        transposeButton = new JButton("Transpose Sparse Matrix");
        sparseButtonPanel.add(transposeButton);

        multiplyButton = new JButton("Multiply Sparse Matrices");
        sparseButtonPanel.add(multiplyButton);

        sparsePanel.add(sparseButtonPanel);

        panel.add(sparsePanel);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateDenseMatrix();
            }
        });

        generateSecondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSecondMatrix();
            }
        });

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertToSparseMatrix();
            }
        });

        addSparseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSparseMatrices();
            }
        });

        transposeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transposeSparseMatrix();
            }
        });

        multiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiplySparseMatrices();
            }
        });

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
                ArrayList<Integer> row = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (random.nextDouble() <= density) {
                        // Generate non-zero element
                        int value = random.nextInt(10) + 1; // Generate non-zero value (1-10 for example)
                        row.add(value);
                        matrixStr.append(value).append("\t");
                    } else {
                        // Generate zero element
                        row.add(0);
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
                ArrayList<Integer> row = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (random.nextDouble() <= density) {
                        // Generate non-zero element
                        int value = random.nextInt(10) + 1; // Generate non-zero value (1-10 for example)
                        row.add(value);
                        matrixStr.append(value).append("\t");
                    } else {
                        // Generate zero element
                        row.add(0);
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

    private void convertToSparseMatrix() {
        StringBuilder sparseMatrixStr = new StringBuilder();

        // Convert and display matrix 1 as sparse
        sparseMatrixStr.append("Sparse Matrix 1 Representation:\n");
        sparseMatrixStr.append(convertToSparse(matrix1));
        sparseMatrixStr.append("\n\n");

        // Convert and display matrix 2 as sparse
        sparseMatrixStr.append("Sparse Matrix 2 Representation:\n");
        sparseMatrixStr.append(convertToSparse(matrix2));

        sparseMatrixArea.setText(sparseMatrixStr.toString());
    }

    private String convertToSparse(ArrayList<ArrayList<Integer>> matrix) {
        StringBuilder sparseMatrixStr = new StringBuilder();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Integer> row = matrix.get(i);
            for (int j = 0; j < row.size(); j++) {
                int value = row.get(j);
                if (value != 0) {
                    sparseMatrixStr.append("(").append(i).append(",").append(j).append("): ").append(value).append("\n");
                }
            }
        }

        return sparseMatrixStr.toString();
    }

    private void addSparseMatrices() {
        // Assuming matrices are of the same size for simplicity
        if (matrix1.size() != matrix2.size() || matrix1.get(0).size() != matrix2.get(0).size()) {
            JOptionPane.showMessageDialog(this, "Matrices must be of the same size for addition.");
            return;
        }

        ArrayList<ArrayList<Integer>> resultMatrix = new ArrayList<>();
        int n = matrix1.size();

        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int sum = matrix1.get(i).get(j) + matrix2.get(i).get(j);
                row.add(sum);
            }
            resultMatrix.add(row);
        }

        sparseMatrixArea.setText("Result of Sparse Matrix Addition:\n\n" + convertToSparse(resultMatrix));
    }

    private void transposeSparseMatrix() {
        ArrayList<ArrayList<Integer>> transposedMatrix = new ArrayList<>();
        int n = matrix1.size();

        for (int j = 0; j < n; j++) {
            ArrayList<Integer> newRow = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                newRow.add(matrix1.get(i).get(j));
            }
            transposedMatrix.add(newRow);
        }

        sparseMatrixArea.setText("Transposed Sparse Matrix:\n\n" + convertToSparse(transposedMatrix));
    }

    private void multiplySparseMatrices() {
        // Assuming matrices are of the same size for simplicity
        if (matrix1.size() != matrix2.size() || matrix1.get(0).size() != matrix2.get(0).size()) {
            JOptionPane.showMessageDialog(this, "Matrices must be of the same size for multiplication.");
            return;
        }

        int n = matrix1.size();
        ArrayList<ArrayList<Integer>> resultMatrix = new ArrayList<>();

        // Initialize resultMatrix with zeros
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(0);
            }
            resultMatrix.add(row);
        }

        // Perform multiplication
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    int product = matrix1.get(i).get(k) * matrix2.get(k).get(j);
                    resultMatrix.get(i).set(j, resultMatrix.get(i).get(j) + product);
                }
            }
        }

        sparseMatrixArea.setText("Result of Sparse Matrix Multiplication:\n\n" + convertToSparse(resultMatrix));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SparseMatrixGenerator();
            }
        });
    }
}
