import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommonFactorsCalculator extends JFrame {
    private JTextField num1Field, num2Field;
    private JTextArea factorsTextArea;

    public CommonFactorsCalculator() {
        setTitle("Common Factors Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Number 1:"));
        num1Field = new JTextField();
        panel.add(num1Field);

        panel.add(new JLabel("Number 2:"));
        num2Field = new JTextField();
        panel.add(num2Field);

        panel.add(new JLabel("Common Factors:"));
        factorsTextArea = new JTextArea();
        factorsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(factorsTextArea);
        panel.add(scrollPane);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculateCommonFactors());
        panel.add(calculateButton);

        add(panel);
    }

    private List<Integer> findCommonFactors(int a, int b) {
        List<Integer> factors = new ArrayList<>();
        int maxFactor = Math.min(a, b);

        for (int i = 1; i <= maxFactor; i++) {
            if (a % i == 0 && b % i == 0) {
                factors.add(i);
            }
        }

        return factors;
    }

    private void calculateCommonFactors() {
        try {
            int num1 = Integer.parseInt(num1Field.getText());
            int num2 = Integer.parseInt(num2Field.getText());

            List<Integer> commonFactors = findCommonFactors(num1, num2);

            StringBuilder sb = new StringBuilder();
            for (int factor : commonFactors) {
                sb.append(factor).append(", ");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 2); // Remove the last comma and space
            }
            factorsTextArea.setText(sb.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}