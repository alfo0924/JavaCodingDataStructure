import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ExpressionConverter extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;

    public ExpressionConverter() {
        setTitle("中序式轉換器");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        JButton convertButton = new JButton("轉換");
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String infix = inputField.getText();
                String postfix = infixToPostfix(infix);
                String prefix = infixToPrefix(infix);
                outputArea.setText("後序式: " + postfix + "\n前序式: " + prefix);
            }
        });

        add(inputField, BorderLayout.NORTH);
        add(convertButton, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    private String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        for (char ch : infix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                postfix.append(ch);
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop(); // pop '('
            } else { // operator
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch)) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private String infixToPrefix(String infix) {
        StringBuilder reversedInfix = new StringBuilder(infix).reverse();
        for (int i = 0; i < reversedInfix.length(); i++) {
            char ch = reversedInfix.charAt(i);
            if (ch == '(') {
                reversedInfix.setCharAt(i, ')');
            } else if (ch == ')') {
                reversedInfix.setCharAt(i, '(');
            }
        }
        String postfix = infixToPostfix(reversedInfix.toString());
        return new StringBuilder(postfix).reverse().toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpressionConverter converter = new ExpressionConverter();
            converter.setVisible(true);
        });
    }
}