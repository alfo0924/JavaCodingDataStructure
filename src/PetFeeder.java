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
    private JButton feedButton; // 餵食按鈕
    private boolean feeding; // 餵食狀態

    public PetFeeder() {
        foodQueue = new LinkedList<>();
        feeding = false; // 初始為不餵食狀態

        // 設置 GUI
        setTitle("D1204433 林俊傑 自動餵食器");
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
        feedButton = new JButton("開始餵食");

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

        // 開始餵食按鈕的事件處理
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!feeding) {
                    feeding = true; // 設置為餵食狀態
                    feedButton.setEnabled(false); // 禁用餵食按鈕
                    new Thread(new FeederTask()).start(); // 啟動餵食執行緒
                }
            }
        });
    }

    private class FeederTask implements Runnable {
        @Override
        public void run() {
            while (!foodQueue.isEmpty()) {
                String food = foodQueue.poll(); // 從佇列中取出食物
                displayArea.append("餵食: " + food + "\n");
                try {
                    Thread.sleep(0500); // 模擬餵食的延遲，2秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            displayArea.append("所有食物已餵食完畢！\n");
            feeding = false; // 餵食結束
            feedButton.setEnabled(true); // 重新啟用餵食按鈕
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetFeeder petFeeder = new PetFeeder();
            petFeeder.setVisible(true);
        });
    }
}