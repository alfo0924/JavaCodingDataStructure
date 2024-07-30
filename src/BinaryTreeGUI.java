import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BinaryTreeGUI extends JFrame {
    private BinaryTree tree;
    private JPanel treePanel;
    private JTextField inputField;
    private JButton insertButton, deleteButton, generateButton;

    public BinaryTreeGUI() {
        tree = new BinaryTree();
        setLayout(new BorderLayout());

        // 初始化組件
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g, tree.root, getWidth() / 2, 30, getWidth() / 4);
            }
        };

        inputField = new JTextField(10);
        insertButton = new JButton("插入");
        deleteButton = new JButton("刪除");
        generateButton = new JButton("生成隨機樹");

        // 添加事件監聽器
        insertButton.addActionListener(e -> {
            int value = Integer.parseInt(inputField.getText());
            tree.insert(value);
            treePanel.repaint();
        });

        deleteButton.addActionListener(e -> {
            int value = Integer.parseInt(inputField.getText());
            tree.delete(value);
            treePanel.repaint();
        });

        generateButton.addActionListener(e -> {
            tree = generateRandomTree(10);
            treePanel.repaint();
        });

        // 佈局組件
        JPanel controlPanel = new JPanel();
        controlPanel.add(inputField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(generateButton);

        add(treePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void drawTree(Graphics g, TreeNode node, int x, int y, int xOffset) {
        if (node == null) return;

        g.drawOval(x - 15, y - 15, 30, 30);
        g.drawString(String.valueOf(node.val), x - 6, y + 4);

        if (node.left != null) {
            int childX = x - xOffset;
            int childY = y + 50;
            g.drawLine(x, y, childX, childY);
            drawTree(g, node.left, childX, childY, xOffset / 2);
        }

        if (node.right != null) {
            int childX = x + xOffset;
            int childY = y + 50;
            g.drawLine(x, y, childX, childY);
            drawTree(g, node.right, childX, childY, xOffset / 2);
        }
    }

    private BinaryTree generateRandomTree(int nodeCount) {
        BinaryTree tree = new BinaryTree();
        Random random = new Random();
        for (int i = 0; i < nodeCount; i++) {
            tree.insert(random.nextInt(100));
        }
        return tree;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinaryTreeGUI::new);
    }
}

class TreeNode {
    int val;
    TreeNode left, right;

    TreeNode(int val) {
        this.val = val;
    }
}

class BinaryTree {
    TreeNode root;

    void insert(int value) {
        root = insertRec(root, value);
    }

    TreeNode insertRec(TreeNode root, int value) {
        if (root == null) {
            root = new TreeNode(value);
            return root;
        }

        if (value < root.val)
            root.left = insertRec(root.left, value);
        else if (value > root.val)
            root.right = insertRec(root.right, value);

        return root;
    }

    void delete(int value) {
        root = deleteRec(root, value);
    }

    TreeNode deleteRec(TreeNode root, int value) {
        if (root == null) return null;

        if (value < root.val)
            root.left = deleteRec(root.left, value);
        else if (value > root.val)
            root.right = deleteRec(root.right, value);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            root.val = minValue(root.right);
            root.right = deleteRec(root.right, root.val);
        }

        return root;
    }

    int minValue(TreeNode root) {
        int minv = root.val;
        while (root.left != null) {
            minv = root.left.val;
            root = root.left;
        }
        return minv;
    }
}