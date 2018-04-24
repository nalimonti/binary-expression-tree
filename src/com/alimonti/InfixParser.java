package com.alimonti;

import java.util.ArrayList;
import java.util.Stack;

public class InfixParser {
    Node root;

    public InfixParser(String expression) {
        String[] symbols = parseExpression(expression);
        ArrayList<String> postSymbols = convertPostfix(symbols);
        for (String str : postSymbols) {
            System.out.print(str + " ");
        }
        System.out.println();

        root = buildTree(postSymbols);
        System.out.println(root.getSymbol());
        System.out.println(root.getLeft().getSymbol());
        System.out.println(root.getRight().getSymbol());
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

    private Node buildTree(ArrayList<String> symbols) {
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

    private String[] parseExpression(String expression) {
        return expression.split(" ");
    }

    private boolean isOperator(String symbol) {
        return symbol.equals("+") || symbol.equals("-") || symbol.equals("*") || symbol.equals("/");
    }

    private boolean isParen(String symbol) {
        return symbol.equals("(") || symbol.equals(")");
    }

    private ArrayList<String> convertPostfix(String[] symbols) {
        ArrayList<String> postSymbols = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (int i = 0; i < symbols.length; ++i) {
            String symbol = symbols[i];

            if (!isOperator(symbol) && !isParen(symbol)) {
                postSymbols.add(symbol);
            }
            // If the scanned character is an '(', push it to the stack.
            else if (symbol.equals("("))
                stack.push(symbol);

                //  If the scanned character is an ')', pop and output from the stack
                // until an '(' is encountered.
            else if (symbol.equals(")"))
            {
                while (!stack.isEmpty() && !stack.peek().equals("("))
                    postSymbols.add(stack.pop());

                if (!stack.isEmpty() && !stack.peek().equals("("))
                    System.out.println("invalid expression");
//                    return "Invalid Expression"; // invalid expression
                else
                    stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Prec(symbol) <= Prec(stack.peek()))
                    postSymbols.add(stack.pop());
                stack.push(symbol);
            }
        }
        while (!stack.isEmpty())
            postSymbols.add(stack.pop());

        return postSymbols;
    }

    static int Prec(String str)
    {
        if (str.equals("+") || str.equals("-"))
            return 1;
        else if (str.equals("*") || str.equals("/"))
            return 2;
        else
            return -1;
    }
}
