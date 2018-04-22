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
	    tree.parseFile();
	    tree.root = tree.addNode(tree.root, 0);
        tree.preTraversal(tree.root, tree.preSymbols);
        tree.postTraversal(tree.root, tree.postSymbols);

        printPre(tree);
        System.out.println();
        printPost(tree);
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

    private class Node {
        String symbol;
        Node left;
        Node right;

        public Node(String symbol) {
            this.symbol = symbol;
            this.left = this.right = null;
        }
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
