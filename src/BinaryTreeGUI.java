import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            root = new TreeNode(key);
            return root;
        }

        if (key < root.data) {
            root.left = insertRec(root.left, key);
        } else if (key > root.data) {
            root.right = insertRec(root.right, key);
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
            // 節點只有一個子節點或沒有子節點
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            // 節點有兩個子節點，找到右子樹的最小值
            root.data = minValue(root.right);
            root.right = deleteNode(root.right, root.data);
        }
        return root;
    }

    int minValue(TreeNode root) {
        int minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
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
    private DrawPanel drawPanel;

    public BinaryTreeGUI() {
        tree = new BinaryTree();
        setTitle("Binary Tree GUI");
        setSize(800, 600); // 增加視窗大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        inputField = new JTextField(10);
        JButton insertButton = new JButton("Insert");
        JButton deleteButton = new JButton("Delete");
        JButton randomButton = new JButton("Generate Random Tree");
        JButton clearButton = new JButton("Clear Tree");
        drawPanel = new DrawPanel();

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(inputField.getText());
                    tree.insert(value);
                    drawPanel.repaint();
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "請輸入有效的整數");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(inputField.getText());
                    tree.root = tree.deleteNode(tree.root, value);
                    drawPanel.repaint();
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "請輸入有效的整數");
                }
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                int n = 10; // 生成 10 個隨機數字
                for (int i = 0; i < n; i++) {
                    tree.insert(rand.nextInt(100)); // 隨機數字範圍 0-99
                }
                drawPanel.repaint();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.clear(); // 清空樹
                drawPanel.repaint();
            }
        });

        add(new JLabel("輸入數字:"));
        add(inputField);
        add(insertButton);
        add(deleteButton);
        add(randomButton);
        add(clearButton);
        add(drawPanel);

        setVisible(true);
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