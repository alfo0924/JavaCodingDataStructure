import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BinaryTreeGUI extends JFrame {
    private BinaryTree tree;
    private TreePanel treePanel;
    private JTextField inputField;
    private JButton insertButton, deleteButton, generateButton, clearButton;
    private JLabel statusLabel;

    public BinaryTreeGUI() {
        tree = new BinaryTree();
        setTitle("Binary Tree Visualizer");
        setLayout(new BorderLayout());

        // Initialize components
        treePanel = new TreePanel();
        inputField = new JTextField(10);
        insertButton = new JButton("插入");
        deleteButton = new JButton("刪除");
        generateButton = new JButton("生成隨機樹");
        clearButton = new JButton("清除樹");
        statusLabel = new JLabel("狀態: 準備就緒");

        // Add event listeners
        insertButton.addActionListener(e -> insertNode());
        deleteButton.addActionListener(e -> deleteNode());
        generateButton.addActionListener(e -> generateRandomTree());
        clearButton.addActionListener(e -> clearTree());

        // Layout components
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("輸入值:"));
        controlPanel.add(inputField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(generateButton);
        controlPanel.add(clearButton);

        add(treePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void insertNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            tree.insert(value);
            treePanel.repaint();
            statusLabel.setText("狀態: 插入節點 " + value);
        } catch (NumberFormatException ex) {
            statusLabel.setText("狀態: 請輸入有效的整數");
        }
        inputField.setText("");
    }

    private void deleteNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            if (tree.delete(value)) {
                treePanel.repaint();
                statusLabel.setText("狀態: 刪除節點 " + value);
            } else {
                statusLabel.setText("狀態: 節點 " + value + " 不存在");
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("狀態: 請輸入有效的整數");
        }
        inputField.setText("");
    }

    private void generateRandomTree() {
        tree = new BinaryTree();
        Random random = new Random();
        int nodeCount = random.nextInt(10) + 5; // Generate 5 to 14 nodes
        for (int i = 0; i < nodeCount; i++) {
            tree.insert(random.nextInt(100));
        }
        treePanel.repaint();
        statusLabel.setText("狀態: 生成隨機樹，節點數: " + nodeCount);
    }

    private void clearTree() {
        tree = new BinaryTree();
        treePanel.repaint();
        statusLabel.setText("狀態: 樹已清除");
    }

    class TreePanel extends JPanel {
        private final int NODE_DIAMETER = 30;
        private final int VERTICAL_GAP = 50;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.root != null) {
                drawTree(g, tree.root, getWidth() / 2, 30, getWidth() / 4);
            }
        }

        private void drawTree(Graphics g, TreeNode node, int x, int y, int xOffset) {
            if (node == null) return;

            g.setColor(Color.BLACK);
            g.drawOval(x - NODE_DIAMETER/2, y - NODE_DIAMETER/2, NODE_DIAMETER, NODE_DIAMETER);
            g.drawString(String.valueOf(node.val), x - 6, y + 4);

            if (node.left != null) {
                int childX = x - xOffset;
                int childY = y + VERTICAL_GAP;
                g.drawLine(x, y + NODE_DIAMETER/2, childX, childY - NODE_DIAMETER/2);
                drawTree(g, node.left, childX, childY, xOffset / 2);
            }

            if (node.right != null) {
                int childX = x + xOffset;
                int childY = y + VERTICAL_GAP;
                g.drawLine(x, y + NODE_DIAMETER/2, childX, childY - NODE_DIAMETER/2);
                drawTree(g, node.right, childX, childY, xOffset / 2);
            }
        }
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

    private TreeNode insertRec(TreeNode root, int value) {
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

    boolean delete(int value) {
        int initialSize = size(root);
        root = deleteRec(root, value);
        return size(root) < initialSize;
    }

    private TreeNode deleteRec(TreeNode root, int value) {
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

    private int minValue(TreeNode root) {
        int minv = root.val;
        while (root.left != null) {
            minv = root.left.val;
            root = root.left;
        }
        return minv;
    }

    private int size(TreeNode node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }
}