import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class PetFeeder extends JFrame {
    private Queue<String> foodQueue; // 食物佇列
    private JTextArea displayArea; // 顯示區域
    private JTextField foodInput; // 食物輸入框

    public PetFeeder() {
        foodQueue = new LinkedList<>();

        // 設置 GUI
        setTitle("自動餵食器");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顯示區域
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // 輸入區域
        JPanel inputPanel = new JPanel();
        foodInput = new JTextField(15);
        JButton addButton = new JButton("添加食物");
        JButton feedButton = new JButton("餵食");

        inputPanel.add(foodInput);
        inputPanel.add(addButton);
        inputPanel.add(feedButton);
        add(inputPanel, BorderLayout.SOUTH);

        // 添加食物按鈕的事件處理
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String food = foodInput.getText().trim();
                if (!food.isEmpty()) {
                    foodQueue.offer(food); // 將食物添加到佇列
                    displayArea.append("添加食物: " + food + "\n");
                    foodInput.setText(""); // 清空輸入框
                }
            }
        });

        // 餵食按鈕的事件處理
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!foodQueue.isEmpty()) {
                    String food = foodQueue.poll(); // 從佇列中取出食物
                    displayArea.append("餵食: " + food + "\n");
                } else {
                    displayArea.append("沒有食物可餵食！\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetFeeder petFeeder = new PetFeeder();
            petFeeder.setVisible(true);
        });
    }
}