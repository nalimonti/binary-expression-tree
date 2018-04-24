package com.alimonti;

import java.util.ArrayList;
import java.util.Stack;

public class ExpressionTree {
    private Node root;
    private String[] symbols;
    private ArrayList<String> preOrderSymbols;
    private ArrayList<String> postOrderSymbols;
    private Evaluator evaluator;

    public ExpressionTree(String[] symbols) {
        this.symbols = symbols;
        preOrderSymbols = new ArrayList<>();
        postOrderSymbols = new ArrayList<>();
        evaluator = new Evaluator();
    }

    protected void evaluateParsed() {
        root = addToLevelOrder(root, 0);
        preTraversal(root);
        postTraversal(root);
        printPre();
        printPost();
    }
    
    protected void parseRaw() {
        ArrayList<String> postfixSymbols = convertPostfix();
        root = buildFromPostOrder(postfixSymbols);
        printLevelOrder(root);
    }

    /* function to print level order traversal of tree*/
    void printLevelOrder(Node root)
    {
        int h = height(root);
        int i;
        for (i=1; i<=h; i++)
            printGivenLevel(root, i);
    }

    /* Compute the "height" of a tree -- the number of
    nodes along the longest path from the root node
    down to the farthest leaf node.*/
    int height(Node root)
    {
        if (root == null)
            return 0;
        else
        {
            /* compute  height of each subtree */
            int lheight = height(root.left);
            int rheight = height(root.right);

            /* use the larger one */
            if (lheight > rheight)
                return(lheight+1);
            else return(rheight+1);
        }
    }

    /* Print nodes at the given level */
    void printGivenLevel (Node root, int level)
    {
        if (root == null) {
            System.out.print("_ ");
            return;
        }
        if (level == 1)
            System.out.print(root.symbol + " ");
        else if (level > 1)
        {
            printGivenLevel(root.left, level-1);
            printGivenLevel(root.right, level-1);
        }
    }

    private ArrayList<String> convertPostfix() {
        ArrayList<String> postfixSymbols = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        
        for (String symbol : symbols) {
            if (!isOperator(symbol) && !isParen(symbol)) {
                postfixSymbols.add(symbol);
            }
            // If the scanned character is an '(', push it to the stack.
            else if (symbol.equals("("))
                stack.push(symbol);

                //  If the scanned character is an ')', pop and output from the stack
                // until an '(' is encountered.
            else if (symbol.equals(")"))
            {
                while (!stack.isEmpty() && !stack.peek().equals("("))
                    postfixSymbols.add(stack.pop());

                if (!stack.isEmpty() && !stack.peek().equals("("))
                    System.out.println("invalid expression");
                else
                    stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && precedence(symbol) <= precedence(stack.peek()))
                    postfixSymbols.add(stack.pop());
                stack.push(symbol);
            }
        }

        while (!stack.isEmpty())
            postfixSymbols.add(stack.pop());

        return postfixSymbols;
    }

    private Node buildFromPostOrder(ArrayList<String> symbols) {
        Stack<Node> stack = new Stack<>();

        for (String symbol : symbols) {
            if (!isOperator(symbol)) {
                Node node = new Node(symbol);
                stack.push(node);
            }
            else {
                Node node1 = stack.pop();
                Node node2 = stack.pop();
                Node newNode = new Node(symbol, node2, node1);
                stack.push(newNode);
            }
        }
        return stack.pop();
    }

    private Node addToLevelOrder(Node root, int i) {
        if (i < symbols.length) {
            Node temp = new Node(symbols[i]);
            root = temp;

            root.left = addToLevelOrder(root.left, (2 * i + 1));
            root.right = addToLevelOrder(root.right, (2 * i + 2));
        }
        return root;
    }

    private void preTraversal(Node node) {
        if (node == null) return;

        if (!node.symbol.equals("_")) {
            preOrderSymbols.add(node.symbol);
        }
        preTraversal(node.left);
        preTraversal(node.right);
    }

    private void postTraversal(Node node) {
        if (node == null) return;

        postTraversal(node.left);
        postTraversal(node.right);
        if (!node.symbol.equals("_")) {
            postOrderSymbols.add(node.symbol);
        }
    }

    private void printPre() {
        int value = evaluator.evaluatePre(preOrderSymbols);

        System.out.print("Pre-order sequence: ");
        for (String symbol : preOrderSymbols) {
            System.out.print(symbol + " ");
        }
        System.out.println();
        System.out.println("Result of pre-order evaluation: " + value);
    }

    private void printPost() {
        int value = evaluator.evaluatePost(postOrderSymbols);

        System.out.print("Post-order sequence: ");
        for (String symbol : postOrderSymbols) {
            System.out.print(symbol + " ");
        }
        System.out.println();
        System.out.println("Result of post-order evaluation: " + value);
    }

    private static boolean isOperator(String symbol) {
        return symbol.equals("+") || symbol.equals("-") || symbol.equals("*") || symbol.equals("/");
    }

    private static boolean isParen(String symbol) {
        return symbol.equals("(") || symbol.equals(")");
    }

    private static int precedence(String operator)
    {
        if (operator.equals("+") || operator.equals("-"))
            return 1;
        else if (operator.equals("*") || operator.equals("/"))
            return 2;
        else
            return -1;
    }

    private class Evaluator {

        private int evaluatePre(ArrayList<String> symbols) {
            Stack<Integer> values = new Stack<>();

            for (int i = symbols.size() - 1; i >= 0; i--) {
                String symbol = symbols.get(i);

                if (!isOperator(symbol)) {
                    values.push(Integer.parseInt(symbol));
                }
                else {
                    char opChar = symbol.charAt(0);
                    int val1 = values.pop();
                    int val2 = values.pop();

                    int result = applyOps(opChar, val2, val1);
                    values.push(result);
                }
            }

            return values.pop();
        }

        private int evaluatePost(ArrayList<String> symbols) {
            Stack<Integer> values = new Stack<>();

            for (int i = 0; i < symbols.size(); i++) {
                String symbol = symbols.get(i);

                if (!isOperator(symbol)) {
                    values.push(Integer.parseInt(symbol));
                }
                else {
                    char opChar = symbol.charAt(0);
                    int val1 = values.pop();
                    int val2 = values.pop();

                    int result = applyOps(opChar, val1, val2);
                    values.push(result);
                }
            }

            return values.pop();
        }

        private int applyOps(char op, int b, int a) {
            switch (op) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                default:
                    return a / b;
            }
        }
    }
}
