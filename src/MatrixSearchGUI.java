import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MatrixSearchGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private int[][] matrix;
    private int size;
    private Random random = new Random();
    private JTextField sizeField;
    private JTextField searchField;
    private JTextArea outputArea;

    public MatrixSearchGUI() {
        setTitle("Matrix Search");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel sizeLabel = new JLabel("Matrix Size (N*N): ");
        sizeField = new JTextField(5);
        JButton generateButton = new JButton("Generate Matrix");
        topPanel.add(sizeLabel);
        topPanel.add(sizeField);
        topPanel.add(generateButton);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("Search Number: ");
        searchField = new JTextField(5);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(15, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton sequentialSearchButton = new JButton("Sequential Search");
        JButton binarySearchButton = new JButton("Binary Search");
        JButton hashSearchButton = new JButton("Hash Search");

        rightPanel.add(sequentialSearchButton);
        rightPanel.add(binarySearchButton);
        rightPanel.add(hashSearchButton);

        centerPanel.add(rightPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        add(panel);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    size = Integer.parseInt(sizeField.getText());
                    initializeMatrix(size);
                    printMatrix();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(searchField.getText());
                    if (matrix == null) {
                        outputArea.setText("Matrix not generated. Please generate the matrix first.\n");
                        return;
                    }

                    outputArea.append("Searching for number " + num + "...\n");

                    // Perform sequential search
                    long startTimeSeq = System.nanoTime();
                    boolean foundSeq = sequentialSearch(num);
                    long endTimeSeq = System.nanoTime();
                    long durationSeq = (endTimeSeq - startTimeSeq);

                    outputArea.append("Sequential Search:\n");
                    if (foundSeq) {
                        outputArea.append("Number found in matrix.\n");
                    } else {
                        outputArea.append("Number not found in matrix.\n");
                    }
                    outputArea.append("Time taken: " + durationSeq + " nanoseconds\n\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });

        sequentialSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(searchField.getText());
                    if (matrix == null) {
                        outputArea.setText("Matrix not generated. Please generate the matrix first.\n");
                        return;
                    }

                    outputArea.append("Performing Sequential Search for number " + num + "...\n");

                    // Perform sequential search
                    long startTimeSeq = System.nanoTime();
                    boolean foundSeq = sequentialSearch(num);
                    long endTimeSeq = System.nanoTime();
                    long durationSeq = (endTimeSeq - startTimeSeq);

                    outputArea.append("Sequential Search:\n");
                    if (foundSeq) {
                        outputArea.append("Number found in matrix.\n");
                    } else {
                        outputArea.append("Number not found in matrix.\n");
                    }
                    outputArea.append("Time taken: " + durationSeq + " nanoseconds\n\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });

        binarySearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(searchField.getText());
                    if (matrix == null) {
                        outputArea.setText("Matrix not generated. Please generate the matrix first.\n");
                        return;
                    }

                    outputArea.append("Performing Binary Search for number " + num + "...\n");

                    // Perform binary search (requires sorted data)
                    int[] sortedData = sortMatrix();
                    long startTimeBin = System.nanoTime();
                    boolean foundBin = binarySearch(sortedData, num);
                    long endTimeBin = System.nanoTime();
                    long durationBin = (endTimeBin - startTimeBin);

                    outputArea.append("Binary Search:\n");
                    if (foundBin) {
                        outputArea.append("Number found in matrix.\n");
                    } else {
                        outputArea.append("Number not found in matrix.\n");
                    }
                    outputArea.append("Time taken: " + durationBin + " nanoseconds\n\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });

        hashSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(searchField.getText());
                    if (matrix == null) {
                        outputArea.setText("Matrix not generated. Please generate the matrix first.\n");
                        return;
                    }

                    outputArea.append("Performing Hash Search for number " + num + "...\n");

                    // Perform hash search
                    long startTimeHash = System.nanoTime();
                    boolean foundHash = hashSearch(num);
                    long endTimeHash = System.nanoTime();
                    long durationHash = (endTimeHash - startTimeHash);

                    outputArea.append("Hash Search:\n");
                    if (foundHash) {
                        outputArea.append("Number found in matrix.\n");
                    } else {
                        outputArea.append("Number not found in matrix.\n");
                    }
                    outputArea.append("Time taken for search: " + durationHash + " nanoseconds\n\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });
    }

    private void initializeMatrix(int size) {
        matrix = new int[size][size];
        Set<Integer> usedNumbers = new HashSet<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int randomNumber;
                do {
                    randomNumber = random.nextInt(size * size) + 1; // generate 1 to size*size
                } while (usedNumbers.contains(randomNumber));
                matrix[i][j] = randomNumber;
                usedNumbers.add(randomNumber);
            }
        }
    }

    private void printMatrix() {
        outputArea.setText("Generated Matrix:\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                outputArea.append(matrix[i][j] + "\t");
            }
            outputArea.append("\n");
        }
        outputArea.append("\n");
    }

    private boolean sequentialSearch(int num) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] sortMatrix() {
        int[] sortedArray = new int[size * size];
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sortedArray[index++] = matrix[i][j];
            }
        }
        Arrays.sort(sortedArray);
        return sortedArray;
    }

    private boolean binarySearch(int[] sortedData, int num) {
        int low = 0;
        int high = sortedData.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (sortedData[mid] == num) {
                return true;
            } else if (sortedData[mid] < num) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    private boolean hashSearch(int num) {
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                hashSet.add(matrix[i][j]);
            }
        }
        long startTimeHash = System.nanoTime();
        boolean found = hashSet.contains(num);
        long endTimeHash = System.nanoTime();
        // Calculate only search time, excluding set-up time
        long durationHash = (endTimeHash - startTimeHash);

        return found;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MatrixSearchGUI().setVisible(true);
            }
        });
    }
}
