import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NumberMatrix extends JFrame {
    private int[][] matrix;
    private int size;
    private JTextField inputField;
    private JLabel resultLabel, sequentialTimeLabel, binaryTimeLabel, hashTimeLabel;
    private JButton randomButton, sequentialSearchButton, binarySearchButton, hashSearchButton;
    private JPanel mainPanel, matrixPanel, searchPanel, logPanel;
    private int[] sortedMatrix;
    private Map<Integer, int[]> matrixPositions;

    public NumberMatrix(int size) {
        this.size = size;
        matrix = generateMatrix(size);
        initGUI();
    }

    private int[][] generateMatrix(int size) {
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (int) (Math.random() * 100) + 1;
            }
        }

        return matrix;
    }

    private void initGUI() {
        setTitle("Number Matrix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        matrixPanel = new JPanel(new GridLayout(size, size, 10, 10));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JLabel label = new JLabel(String.valueOf(matrix[i][j]), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                matrixPanel.add(label);
            }
        }
        mainPanel.add(matrixPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(20, 20));

        searchPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        searchPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.CENTER);
        searchPanel.add(new JLabel("Enter a number:", SwingConstants.RIGHT));
        searchPanel.add(inputField);

        sequentialSearchButton = new JButton("Sequential Search");
        sequentialSearchButton.addActionListener(e -> sequentialSearch());
        searchPanel.add(sequentialSearchButton);
        sequentialTimeLabel = new JLabel("", SwingConstants.CENTER);
        searchPanel.add(sequentialTimeLabel);

        binarySearchButton = new JButton("Binary Search");
        binarySearchButton.addActionListener(e -> binarySearch());
        searchPanel.add(binarySearchButton);
        binaryTimeLabel = new JLabel("", SwingConstants.CENTER);
        searchPanel.add(binaryTimeLabel);

        hashSearchButton = new JButton("Hash Search");
        hashSearchButton.addActionListener(e -> hashSearch());
        searchPanel.add(hashSearchButton);
        hashTimeLabel = new JLabel("", SwingConstants.CENTER);
        searchPanel.add(hashTimeLabel);

        randomButton = new JButton("Randomize");
        randomButton.addActionListener(e -> randomMatrix());
        searchPanel.add(randomButton);
        searchPanel.add(new JLabel("", SwingConstants.CENTER));

        rightPanel.add(searchPanel, BorderLayout.NORTH);

        logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        resultLabel = new JLabel("", SwingConstants.CENTER);
        logPanel.add(resultLabel, BorderLayout.CENTER);
        rightPanel.add(logPanel, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel);
        setVisible(true);
    }

    private void randomMatrix() {
        matrix = generateMatrix(size);
        sortedMatrix = null;
        matrixPositions = null;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JLabel label = (JLabel) matrixPanel.getComponent(i * size + j);
                label.setText(String.valueOf(matrix[i][j]));
            }
        }
    }

    private void sequentialSearch() {
        int number;
        try {
            number = Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number.");
            sequentialTimeLabel.setText("");
            return;
        }

        long startTime = System.nanoTime();
        boolean found = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == number) {
                    resultLabel.setText("Number " + number + " found at (" + (i + 1) + ", " + (j + 1) + ").");
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        if (!found) {
            resultLabel.setText("Number " + number + " not found in the matrix.");
        }
        sequentialTimeLabel.setText("Sequential Search used " + duration + " nanoseconds.");
    }

    private void binarySearch() {
        int number;
        try {
            number = Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number.");
            binaryTimeLabel.setText("");
            return;
        }

        if (sortedMatrix == null) {
            sortedMatrix = flattenAndSortMatrix();
        }

        long startTime = System.nanoTime();
        int index = Arrays.binarySearch(sortedMatrix, number);
        if (index >= 0) {
            int row = index / size + 1;
            int col = index % size + 1;
            resultLabel.setText("Number " + number + " found at (" + row + ", " + col + ").");
        } else {
            resultLabel.setText("Number " + number + " not found in the matrix.");
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        binaryTimeLabel.setText("Binary Search used " + duration + " nanoseconds.");
    }

    private void hashSearch() {
        int number;
        try {
            number = Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number.");
            hashTimeLabel.setText("");
            return;
        }

        long startTime = System.nanoTime();
        if (matrixPositions == null) {
            matrixPositions = buildHashMap();
        }
        boolean found = matrixPositions.containsKey(number);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        if (found) {
            int[] position = matrixPositions.get(number);
            resultLabel.setText("Number " + number + " found at (" + position[0] + ", " + position[1] + ").");
        } else {
            resultLabel.setText("Number " + number + " not found in the matrix.");
        }
        hashTimeLabel.setText("Hash Search used " + duration + " nanoseconds.");
    }

    private int[] flattenAndSortMatrix() {
        int[] sorted = new int[size * size];
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sorted[index++] = matrix[i][j];
            }
        }
        Arrays.sort(sorted);
        return sorted;
    }

    private Map<Integer, int[]> buildHashMap() {
        Map<Integer, int[]> positions = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                positions.put(matrix[i][j], new int[]{i + 1, j + 1});
            }
        }
        return positions;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NumberMatrix(5));
    }
}