import javax.swing.*;
import java.awt.*;

public class GCDLCMCalculator extends JFrame {
    private JTextField num1Field, num2Field;
    private JLabel gcdLabel, lcmLabel;

    public GCDLCMCalculator() {
        setTitle("GCD and LCM Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
        setSize(400, 200);
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

        panel.add(new JLabel("GCD:"));
        gcdLabel = new JLabel();
        panel.add(gcdLabel);

        panel.add(new JLabel("LCM:"));
        lcmLabel = new JLabel();
        panel.add(lcmLabel);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculateGCDAndLCM());
        panel.add(calculateButton);

        add(panel);
    }

    private int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    private void calculateGCDAndLCM() {
        try {
            int num1 = Integer.parseInt(num1Field.getText());
            int num2 = Integer.parseInt(num2Field.getText());

            int gcd = gcd(num1, num2);
            int lcm = lcm(num1, num2);

            gcdLabel.setText(String.valueOf(gcd));
            lcmLabel.setText(String.valueOf(lcm));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}