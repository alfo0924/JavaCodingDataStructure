import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GCDLCMCalculator());
        SwingUtilities.invokeLater(() -> new CommonFactorsCalculator());
        SwingUtilities.invokeLater(() -> new BMICalculator());
    }
}