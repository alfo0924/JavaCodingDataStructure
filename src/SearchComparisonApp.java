import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class SearchComparisonApp extends JFrame {
    private JPanel mainPanel;
    private JPanel matrixPanel;
    private JPanel searchPanel;
    private JPanel resultsPanel;

    private JTextArea matrixTextArea;
    private JTextField matrixSizeField;
    private JButton generateMatrixButton;

    private JTextField searchNumberField;
    private JButton searchButton;
    private JButton clearButton;

    private JTextArea resultsTextArea;

    private int matrixSize = 10;
    private int[][] matrix;
    private int searchNumber;

    // Data structures for search
    private List<Integer> sequentialData; // for sequential search
    private List<Integer> sortedData;     // for binary search
    private HashSet<Integer> hashSetData; // for hash search

    public SearchComparisonApp() {
        setTitle("Search Algorithm Comparison");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        // Matrix panel
        matrixPanel = new JPanel();
        matrixPanel.setBorder(BorderFactory.createTitledBorder("Matrix"));
        matrixTextArea = new JTextArea(10, 30);
        matrixTextArea.setEditable(false);
        JScrollPane matrixScrollPane = new JScrollPane(matrixTextArea);
        matrixPanel.add(matrixScrollPane);
        mainPanel.add(matrixPanel, BorderLayout.CENTER);

        // Search panel
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel matrixSizeLabel = new JLabel("Matrix Size:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        searchPanel.add(matrixSizeLabel, constraints);

        matrixSizeField = new JTextField("10", 10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(matrixSizeField, constraints);

        generateMatrixButton = new JButton("Generate Matrix");
        constraints.gridx = 2;
        constraints.gridy = 0;
        searchPanel.add(generateMatrixButton, constraints);

        JLabel searchNumberLabel = new JLabel("Search Number:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        searchPanel.add(searchNumberLabel, constraints);

        searchNumberField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 1;
        searchPanel.add(searchNumberField, constraints);

        searchButton = new JButton("Search");
        constraints.gridx = 2;
        constraints.gridy = 1;
        searchPanel.add(searchButton, constraints);

        clearButton = new JButton("Clear Results");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        searchPanel.add(clearButton, constraints);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsTextArea = new JTextArea(10, 30);
        resultsTextArea.setEditable(false);
        JScrollPane resultsScrollPane = new JScrollPane(resultsTextArea);
        resultsPanel.add(resultsScrollPane);
        mainPanel.add(resultsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        generateMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMatrix();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearResults();
            }
        });
    }

    private void generateMatrix() {
        try {
            matrixSize = Integer.parseInt(matrixSizeField.getText());
        } catch (NumberFormatException ex) {
            matrixSize = 10; // default to 10 if invalid input
        }

        matrix = new int[matrixSize][matrixSize];
        Random random = new Random();

        HashSet<Integer> generatedNumbers = new HashSet<>();
        int minRange = 1;
        int maxRange = matrixSize * matrixSize; // range from 1 to matrixSize^2

        // Generate random unique numbers for the matrix
        while (generatedNumbers.size() < matrixSize * matrixSize) {
            int num = random.nextInt(maxRange - minRange + 1) + minRange;
            generatedNumbers.add(num);
        }

        // Fill the matrix
        Iterator<Integer> iterator = generatedNumbers.iterator();
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrix[i][j] = iterator.next();
            }
        }

        displayMatrix();
    }

    private void displayMatrix() {
        matrixTextArea.setText("");
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixTextArea.append(matrix[i][j] + " ");
            }
            matrixTextArea.append("\n");
        }
    }

    private void performSearch() {
        try {
            searchNumber = Integer.parseInt(searchNumberField.getText());
        } catch (NumberFormatException ex) {
            resultsTextArea.append("Invalid search number!\n");
            return;
        }

        // Reset data structures for each search
        sequentialData = new ArrayList<>();
        sortedData = new ArrayList<>();
        hashSetData = new HashSet<>();

        // Fill data structures for each search
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int num = matrix[i][j];
                sequentialData.add(num);
                sortedData.add(num);
                hashSetData.add(num);
            }
        }

        StringBuilder results = new StringBuilder();

        // Find GCD of matrix size and search number
        int gcd = findGCD(matrixSize, searchNumber);
        results.append(String.format("GCD of Matrix Size and Search Number: %d\n", gcd));

        // Sequential search
        double sequentialTime = sequentialSearch();
        if (sequentialTime != -1) {
            results.append(String.format("Sequential Search Time: %.6f nanoseconds\n", sequentialTime));
        } else {
            results.append("Sequential Search: Number not found\n");
        }

        // Binary search
        double binaryTime = binarySearch();
        if (binaryTime != -1) {
            results.append(String.format("Binary Search Time: %.6f nanoseconds\n", binaryTime));
        } else {
            results.append("Binary Search: Number not found\n");
        }

        // Hash search
        double hashTime = hashSearch();
        if (hashTime != -1) {
            results.append(String.format("Hash Search Time: %.6f nanoseconds\n", hashTime));
        } else {
            results.append("Hash Search: Number not found\n");
        }

        resultsTextArea.setText(results.toString());
    }

    private double sequentialSearch() {
        long startTime = System.nanoTime();
        for (int num : sequentialData) {
            if (num == searchNumber) {
                long endTime = System.nanoTime();
                return (endTime - startTime); // time in nanoseconds
            }
        }
        return -1; // not found
    }

    private double binarySearch() {
        Collections.sort(sortedData); // Sort data for binary search
        long startTime = System.nanoTime();
        int index = Collections.binarySearch(sortedData, searchNumber);
        long endTime = System.nanoTime();

        if (index >= 0) {
            return (endTime - startTime); // time in nanoseconds
        }
        return -1; // not found
    }

    private double hashSearch() {
        long startTime = System.nanoTime();
        boolean found = hashSetData.contains(searchNumber);
        long endTime = System.nanoTime();

        if (found) {
            return (endTime - startTime); // time in nanoseconds
        }
        return -1; // not found
    }

    private int findGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    private void clearResults() {
        resultsTextArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SearchComparisonApp app = new SearchComparisonApp();
                app.setVisible(true);
            }
        });
    }
}
