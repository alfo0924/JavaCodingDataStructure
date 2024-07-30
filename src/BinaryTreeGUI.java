import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class TreeNode {
    int data;
    TreeNode left, right;

    TreeNode(int item) {
        data = item;
        left = right = null;
    }
}

class BinaryTree {
    TreeNode root;

    // 插入節點
    void insert(int key) {
        root = insertRec(root, key);
    }

    TreeNode insertRec(TreeNode root, int key) {
        if (root == null) {
            return new TreeNode(key);
        }

        // 使用 Queue 實現從左到右填滿的插入順序
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode temp = queue.poll();

            if (temp.left == null) {
                temp.left = new TreeNode(key);
                return root;
            } else {
                queue.add(temp.left);
            }

            if (temp.right == null) {
                temp.right = new TreeNode(key);
                return root;
            } else {
                queue.add(temp.right);
            }
        }

        return root;
    }

    // 刪除節點
    TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return root;

        if (key < root.data) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.data) {
            root.right = deleteNode(root.right, key);
        } else {
            // 找到節點
            if (root.left == null && root.right == null) {
                // 如果是葉節點，直接刪除
                return null;
            } else {
                // 如果是中間節點，刪除整個子樹
                return null; // 刪除整個子樹，返回 null
            }
        }
        return root;
    }

    // 中序遍歷
    String inorder() {
        StringBuilder result = new StringBuilder();
        inorderRec(root, result);
        return result.toString();
    }

    void inorderRec(TreeNode node, StringBuilder result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.append(node.data).append(" ");
            inorderRec(node.right, result);
        }
    }

    // 前序遍歷
    String preorder() {
        StringBuilder result = new StringBuilder();
        preorderRec(root, result);
        return result.toString();
    }

    void preorderRec(TreeNode node, StringBuilder result) {
        if (node != null) {
            result.append(node.data).append(" ");
            preorderRec(node.left, result);
            preorderRec(node.right, result);
        }
    }

    // 後序遍歷
    String postorder() {
        StringBuilder result = new StringBuilder();
        postorderRec(root, result);
        return result.toString();
    }

    void postorderRec(TreeNode node, StringBuilder result) {
        if (node != null) {
            postorderRec(node.left, result);
            postorderRec(node.right, result);
            result.append(node.data).append(" ");
        }
    }

    // 清空樹
    void clear() {
        root = null;
    }

    // 繪製樹的圖形表示
    void draw(Graphics g, TreeNode node, int x, int y, int xOffset) {
        if (node == null) return;

        g.drawString(String.valueOf(node.data), x, y);
        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + 30);
            draw(g, node.left, x - xOffset, y + 30, xOffset / 2);
        }
        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + 30);
            draw(g, node.right, x + xOffset, y + 30, xOffset / 2);
        }
    }
}

public class BinaryTreeGUI extends JFrame {
    private BinaryTree tree;
    private JTextField inputField;
    private JTextField nField;
    private DrawPanel drawPanel;
    private JTextArea resultArea;
    private JCheckBox inorderCheckBox;
    private JCheckBox preorderCheckBox;
    private JCheckBox postorderCheckBox;
    private HashSet<Integer> generatedNumbers;

    public BinaryTreeGUI() {
        tree = new BinaryTree();
        generatedNumbers = new HashSet<>();
        setTitle("D1204433 林俊傑 二元樹 Binary Tree GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        inputField = new JTextField(10);
        nField = new JTextField(10);
        JButton insertButton = new JButton("置入 Insert");
        JButton deleteButton = new JButton("刪除 Delete");
        JButton randomButton = new JButton("隨機生成數字 Generate Random Numbers");
        JButton clearButton = new JButton("清除二元樹 Clear Tree");
        JButton traverseButton = new JButton("遍歷 Traverse");
        drawPanel = new DrawPanel();

        inorderCheckBox = new JCheckBox("中序 Inorder");
        preorderCheckBox = new JCheckBox("前序 Preorder");
        postorderCheckBox = new JCheckBox("後序 Postorder");

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(inputField.getText());
                    if (!generatedNumbers.contains(value)) {
                        tree.insert(value);
                        generatedNumbers.add(value);
                        drawPanel.repaint();
                    } else {
                        System.out.println("數字已存在，請輸入其他數字。");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    System.out.println("請輸入有效的整數");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(inputField.getText());
                    tree.root = tree.deleteNode(tree.root, value);
                    generatedNumbers.remove(value); // 刪除數字
                    drawPanel.repaint();
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    System.out.println("請輸入有效的整數");
                }
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int n = Integer.parseInt(nField.getText());
                    generateRandomNumbers(n);
                    drawPanel.repaint();
                    nField.setText("");
                } catch (NumberFormatException ex) {
                    System.out.println("請輸入有效的整數");
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.clear(); // 清空樹
                generatedNumbers.clear(); // 清空生成的數字
                drawPanel.repaint();
                resultArea.setText(""); // 清空結果區域
            }
        });

        traverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder result = new StringBuilder();
                if (inorderCheckBox.isSelected()) {
                    result.append("Inorder: ").append(tree.inorder()).append("\n");
                }
                if (preorderCheckBox.isSelected()) {
                    result.append("Preorder: ").append(tree.preorder()).append("\n");
                }
                if (postorderCheckBox.isSelected()) {
                    result.append("Postorder: ").append(tree.postorder()).append("\n");
                }
                resultArea.setText(result.toString());
            }
        });

        add(new JLabel("輸入數字:"));
        add(inputField);
        add(new JLabel("生成數量:"));
        add(nField);
        add(insertButton);
        add(deleteButton);
        add(randomButton);
        add(clearButton);
        add(inorderCheckBox);
        add(preorderCheckBox);
        add(postorderCheckBox);
        add(traverseButton);
        add(drawPanel);

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea)); // 使用 JScrollPane 以便顯示多行結果

        setVisible(true);
    }

    void generateRandomNumbers(int n) {
        Random rand = new Random();
        while (generatedNumbers.size() < n) {
            int num = rand.nextInt(100); // 隨機數字範圍 0-99
            if (generatedNumbers.add(num)) { // 確保不重複
                tree.insert(num);
            }
        }
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.root != null) {
                tree.draw(g, tree.root, getWidth() / 2, 30, 100);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600); // 設定繪圖面板的大小
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BinaryTreeGUI());
    }
}
