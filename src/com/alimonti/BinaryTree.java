package com.alimonti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

//https://www.geeksforgeeks.org/construct-complete-binary-tree-given-array/
public class BinaryTree {

    private Node root;
    private String[] symbols;
    private ArrayList<String> preSymbols;
    private ArrayList<String> postSymbols;
    private Evaluator eval;

    public BinaryTree() {
        preSymbols = new ArrayList<>();
        postSymbols = new ArrayList<>();
        eval = new Evaluator();
    }

    public static void main(String[] args) throws FileNotFoundException {
	    BinaryTree tree = new BinaryTree();
//	    tree.parseFile();
//	    tree.root = tree.addNode(tree.root, 0);
//        tree.preTraversal(tree.root, tree.preSymbols);
//        tree.postTraversal(tree.root, tree.postSymbols);
//
//        printPre(tree);
//        System.out.println();
//        printPost(tree);

//        String expression = tree.parse();
//        System.out.println(expression);
//        String postExpression = tree.convertToPostfix(expression);
//        System.out.println(postExpression);

        InfixParser parser = new InfixParser(tree.parse());
    }

//    private String convertToPostfix(String expression) {
//        String result = new String("");
//        Stack<Character> stack = new Stack<>();
//
//        for (int i = 0; i < expression.length(); ++i) {
//            char c = expression.charAt(i);
//            if (c == ' ') continue;
//
//            // If the scanned character is an operand, add it to output.
//            if (Character.isLetterOrDigit(c)) {
//                result += c;
//                while (Character.isLetterOrDigit(expression.charAt(i++))) {
//                    result += expression.charAt(i++);
//                }
//            }
//                // If the scanned character is an '(', push it to the stack.
//            else if (c == '(')
//                stack.push(c);
//
//                //  If the scanned character is an ')', pop and output from the stack
//                // until an '(' is encountered.
//            else if (c == ')')
//            {
//                while (!stack.isEmpty() && stack.peek() != '(')
//                    result += stack.pop();
//
//                if (!stack.isEmpty() && stack.peek() != '(')
//                    return "Invalid Expression"; // invalid expression
//                else
//                    stack.pop();
//            }
//            else // an operator is encountered
//            {
//                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek()))
//                    result += stack.pop();
//                stack.push(c);
//            }
//        }
//        while (!stack.isEmpty())
//            result += stack.pop();
//
//        return result;
//    }

    static int Prec(char ch)
    {
        switch (ch)
        {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;
        }
        return -1;
    }

    private String parse() throws FileNotFoundException {
        File file = new File("math.txt");
        Scanner sc = new Scanner(file);

        String expression = new String("");

        while (sc.hasNextLine()) {
            expression += sc.nextLine();
        }

        sc.close();
        return expression;
    }

    private static void printPre(BinaryTree tree) {
        int value = tree.eval.evaluatePre(tree.preSymbols);

        System.out.print("Pre-order sequence: ");
        for (String symbol : tree.preSymbols) {
            System.out.print(symbol + " ");
        }
        System.out.println();
        System.out.println("Result of pre-order evaluation: " + value);
    }

    private static void printPost(BinaryTree tree) {
        int value = tree.eval.evaluatePost(tree.postSymbols);

        System.out.print("Post-order sequence: ");
        for (String symbol : tree.postSymbols) {
            System.out.print(symbol + " ");
        }
        System.out.println();
        System.out.println("Result of post-order evaluation: " + value);
    }

    private void parseFile() throws FileNotFoundException {
        File file = new File("expression.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String expression = sc.nextLine();
            symbols = expression.split(" ");
        }

        sc.close();
    }

    private Node addNode(Node root, int i) {
        if (i < symbols.length) {
            Node temp = new Node(symbols[i]);
            root = temp;

            root.left = addNode(root.left, (2 * i + 1));
            root.right = addNode(root.right, (2 * i + 2));
        }
        return root;
    }

    private void preTraversal(Node node, ArrayList<String> symbols) {
        if (node == null) return;

        if (!node.symbol.equals("_")) {
            symbols.add(node.symbol);
        }
        preTraversal(node.left, symbols);
        preTraversal(node.right, symbols);
    }

    private void postTraversal(Node node, ArrayList<String> symbols) {
        if (node == null) return;

        postTraversal(node.left, symbols);
        postTraversal(node.right, symbols);
        if (!node.symbol.equals("_")) {
            symbols.add(node.symbol);
        }
    }

//    private class Node {
//        String symbol;
//        Node left;
//        Node right;
//
//        public Node(String symbol) {
//            this.symbol = symbol;
//            this.left = this.right = null;
//        }
//    }

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

        private boolean isOperator(String symbol) {
            return symbol.equals("+") || symbol.equals("-") || symbol.equals("*") || symbol.equals("/");
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
