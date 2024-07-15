import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SparseMatrixGUI extends JFrame {

    private int matrixSize;
    private double sparsityPercentage; // New variable to hold sparsity percentage
    private int[][] sparseMatrix;

    private JTable matrixTable;
    private JButton generateButton;
    private JTextField sizeTextField;
    private JLabel sparsityLabel;
    private JTextField sparsityTextField;

    public SparseMatrixGUI() {
        setTitle("Sparse Matrix Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JLabel sizeLabel = new JLabel("Enter matrix size:");
        sizeTextField = new JTextField(5);
        generateButton = new JButton("Generate Matrix");

        // New components for sparsity percentage
        sparsityLabel = new JLabel("Enter sparsity percentage (0-1):");
        sparsityTextField = new JTextField(5);

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

        matrixTable = new JTable();
        matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(matrixTable);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private void generateMatrix() {
        try {
            matrixSize = Integer.parseInt(sizeTextField.getText());
            if (matrixSize <= 0) {
                JOptionPane.showMessageDialog(this, "Matrix size must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sparsityPercentage = Double.parseDouble(sparsityTextField.getText());
            if (sparsityPercentage < 0 || sparsityPercentage > 1) {
                JOptionPane.showMessageDialog(this, "Sparsity percentage must be between 0 and 1.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sparseMatrix = new int[matrixSize][matrixSize];

            Random random = new Random();

            // Generate sparse matrix
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    // Generate random number between 0 and 1
                    double randomValue = random.nextDouble();
                    if (randomValue > sparsityPercentage) {
                        // If randomValue is greater than sparsityPercentage, set to random number between 0 and 9
                        sparseMatrix[i][j] = random.nextInt(10);
                    } else {
                        // Otherwise, set to 0
                        sparseMatrix[i][j] = 0;
                    }
                }
            }

            // Update the table model to display the matrix
            DefaultTableModel tableModel = new DefaultTableModel(matrixSize, matrixSize);
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    tableModel.setValueAt(sparseMatrix[i][j], i, j);
                }
            }
            matrixTable.setModel(tableModel);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SparseMatrixGUI();
            }
        });
    }
}
