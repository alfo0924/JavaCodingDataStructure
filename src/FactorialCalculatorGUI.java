import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class FactorialCalculatorGUI extends JFrame {

    private JLabel inputLabel;
    private JTextField inputField;
    private JButton calculateButton;
    private JButton clearButton;
    private JTextArea resultArea;

    public FactorialCalculatorGUI() {
        setTitle("Factorial Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        inputLabel = new JLabel("Enter N:");
        inputField = new JTextField(10);
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear");

        topPanel.add(inputLabel);
        topPanel.add(inputField);
        topPanel.add(calculateButton);
        topPanel.add(clearButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFactorials();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
            }
        });
    }

    private void calculateFactorials() {
        resultArea.append("Calculating factorials...\n");

        String inputStr = inputField.getText().trim();
        if (!inputStr.matches("\\d+")) {
            resultArea.append("Invalid input. Please enter a non-negative integer.\n");
            return;
        }

        int n = Integer.parseInt(inputStr);
        if (n < 0) {
            resultArea.append("Invalid input. Please enter a non-negative integer.\n");
            return;
        }

        BigInteger recursiveResult;
        BigInteger iterativeResult;

        long startTime = System.nanoTime();
        try {
            recursiveResult = factorialRecursive(BigInteger.valueOf(n));
            long recursiveTime = System.nanoTime() - startTime;
            resultArea.append("Recursive factorial (" + n + "): " + recursiveResult.toString() + "\n");
            resultArea.append("Time taken (recursive): " + recursiveTime + " nanoseconds\n");
        } catch (StackOverflowError soe) {
            resultArea.append("Recursive factorial calculation overflowed for " + n + ".\n");
        }

        startTime = System.nanoTime();
        iterativeResult = factorialIterative(n);
        long iterativeTime = System.nanoTime() - startTime;
        resultArea.append("Iterative factorial (" + n + "): " + iterativeResult.toString() + "\n");
        resultArea.append("Time taken (iterative): " + iterativeTime + " nanoseconds\n");

        resultArea.append("Calculation completed.\n");
    }

    private BigInteger factorialRecursive(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        } else {
            return n.multiply(factorialRecursive(n.subtract(BigInteger.ONE)));
        }
    }

    private BigInteger factorialIterative(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FactorialCalculatorGUI();
            }
        });
    }
}
