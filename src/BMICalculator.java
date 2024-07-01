import javax.swing.*;
import java.awt.*;

public class BMICalculator extends JFrame {
    private JTextField heightField, weightField;
    private JLabel bmiLabel, statusLabel;

    public BMICalculator() {
        setTitle("台灣人BMI計算器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("身高 (公分):"));
        heightField = new JTextField();
        panel.add(heightField);

        panel.add(new JLabel("體重 (公斤):"));
        weightField = new JTextField();
        panel.add(weightField);

        panel.add(new JLabel("BMI 值:"));
        bmiLabel = new JLabel();
        panel.add(bmiLabel);

        panel.add(new JLabel("健康狀態:"));
        statusLabel = new JLabel();
        panel.add(statusLabel);

        JButton calculateButton = new JButton("計算BMI");
        calculateButton.addActionListener(e -> calculateBMI());
        panel.add(calculateButton);

        add(panel);
    }

    private void calculateBMI() {
        try {
            double height = Double.parseDouble(heightField.getText()) / 100.0; // 將公分轉換為公尺
            double weight = Double.parseDouble(weightField.getText());

            double bmi = weight / (height * height);
            bmiLabel.setText(String.format("%.1f", bmi));

            String status;
            if (bmi < 18.5) {
                status = "體重過輕";
            } else if (bmi >= 18.5 && bmi < 24) {
                status = "正常範圍";
            } else if (bmi >= 24 && bmi < 27) {
                status = "過重";
            } else if (bmi >= 27 && bmi < 30) {
                status = "輕度肥胖";
            } else if (bmi >= 30 && bmi < 35) {
                status = "中度肥胖";
            } else {
                status = "重度肥胖";
            }
            statusLabel.setText(status);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "請輸入有效的數字。", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BMICalculator());
    }
}