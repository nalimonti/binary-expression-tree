package com.alimonti;

public class Node {
    String symbol;
    Node left;
    Node right;

    public Node(String symbol) {
        this.symbol = symbol;
        this.left = this.right = null;
    }

    public Node(String symbol, Node left, Node right) {
        this.symbol = symbol;
        this.left = left;
        this.right = right;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }
}
