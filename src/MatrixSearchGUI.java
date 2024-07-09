import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;

public class MatrixSearchGUI extends JFrame {

    private JTextField sizeField;
    private JButton generateButton;
    private JTextArea resultArea;

    private int[][] matrix;
    private int size;

    public MatrixSearchGUI() {
        setTitle("Matrix Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel sizeLabel = new JLabel("Enter size (n):");
        sizeField = new JTextField(10);
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);

        generateButton = new JButton("Generate Matrix");
        inputPanel.add(generateButton);

        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
                try {
                    size = Integer.parseInt(sizeField.getText().trim());
                    if (size <= 0) {
                        resultArea.append("Please enter a positive integer.\n");
                        return;
                    }
                    generateMatrix();
                    performSearches();

                } catch (NumberFormatException ex) {
                    resultArea.append("Invalid input. Please enter a valid integer.\n");
                }
            }
        });
    }

    private void generateMatrix() {
        matrix = new int[size][size];
        HashSet<Integer> usedNumbers = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num;
                do {
                    num = random.nextInt(size * size) + 1; // Generate a unique number
                } while (usedNumbers.contains(num));
                usedNumbers.add(num);
                matrix[i][j] = num;
            }
        }

        // Display matrix in resultArea
        resultArea.append("Generated Matrix:\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resultArea.append(matrix[i][j] + "\t");
            }
            resultArea.append("\n");
        }
        resultArea.append("\n");
    }

    private void performSearches() {
        int elementToFind = matrix[size / 2][size / 2]; // Middle element of the matrix

        // Sequential Search
        long startTime = System.nanoTime();
        boolean foundSequential = sequentialSearch(elementToFind);
        long endTime = System.nanoTime();
        long sequentialTime = endTime - startTime;

        // Binary Search
        startTime = System.nanoTime();
        boolean foundBinary = binarySearch(elementToFind);
        endTime = System.nanoTime();
        long binaryTime = endTime - startTime;

        // Hash Search
        startTime = System.nanoTime();
        boolean foundHash = hashSearch(elementToFind);
        endTime = System.nanoTime();
        long hashTime = endTime - startTime;

        // Output results
        resultArea.append("Searching for element " + elementToFind + ":\n");
        resultArea.append("Sequential Search: " + (foundSequential ? "Found" : "Not Found") + ", Time taken (nanoseconds): " + sequentialTime + "\n");
        resultArea.append("Binary Search: " + (foundBinary ? "Found" : "Not Found") + ", Time taken (nanoseconds): " + binaryTime + "\n");
        resultArea.append("Hash Search: " + (foundHash ? "Found" : "Not Found") + ", Time taken (nanoseconds): " + hashTime + "\n");
    }

    private boolean sequentialSearch(int target) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean binarySearch(int target) {
        int low = 0;
        int high = size * size - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int midValue = matrix[mid / size][mid % size];
            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    private boolean hashSearch(int target) {
        HashSet<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                hashSet.add(matrix[i][j]);
            }
        }
        return hashSet.contains(target);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatrixSearchGUI().setVisible(true);
            }
        });
    }
}
