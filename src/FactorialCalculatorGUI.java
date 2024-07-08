import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class FactorialCalculatorGUI extends JFrame {

    private JTextField inputField;
    private JButton calculateButton;
    private JButton clearButton;
    private JTextArea resultArea;

    public FactorialCalculatorGUI() {
        setTitle("Factorial Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel inputLabel = new JLabel("Enter N:");
        inputField = new JTextField(10);
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);

        JPanel buttonPanel = new JPanel();
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear");
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false); // Make it read-only
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputStr = inputField.getText().trim();
                try {
                    int n = Integer.parseInt(inputStr);
                    if (n < 0) {
                        resultArea.append("Please enter a non-negative integer.\n");
                        return;
                    }
                    long startTime = System.nanoTime();
                    BigInteger recursiveResult = factorialRecursive(BigInteger.valueOf(n));
                    long endTime = System.nanoTime();
                    long recursiveTime = endTime - startTime;

                    startTime = System.nanoTime();
                    BigInteger iterativeResult = factorialIterative(n);
                    endTime = System.nanoTime();
                    long iterativeTime = endTime - startTime;

                    resultArea.append("Factorial using recursion:\n" + recursiveResult.toString() + "\n");
                    resultArea.append("Time taken (nanoseconds): " + recursiveTime + "\n");
                    resultArea.append("Factorial using iteration:\n" + iterativeResult.toString() + "\n");
                    resultArea.append("Time taken (nanoseconds): " + iterativeTime + "\n");

                } catch (NumberFormatException ex) {
                    resultArea.append("Invalid input. Please enter a valid integer.\n");
                } catch (ArithmeticException ex) {
                    resultArea.append("Error: Number is too large to compute factorial.\n");
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
                inputField.setText("");
            }
        });
    }

    private BigInteger factorialRecursive(BigInteger n) {
        if (n.equals(BigInteger.ZERO))
            return BigInteger.ONE;
        else
            return n.multiply(factorialRecursive(n.subtract(BigInteger.ONE)));
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
                new FactorialCalculatorGUI().setVisible(true);
            }
        });
    }
}
