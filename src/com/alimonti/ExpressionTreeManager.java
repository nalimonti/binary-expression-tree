package com.alimonti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExpressionTreeManager {

    public static void main(String[] args) throws FileNotFoundException {
        ExpressionTreeManager manager = new ExpressionTreeManager();
        String fileName = "math.txt";
        String[] symbols = manager.parseFile(fileName);
        ExpressionTree tree = new ExpressionTree(symbols);

        if (fileName.equals("expression.txt")) {
            tree.evaluateParsed();
        }
        else {
            tree.parseRaw();
        }
    }

    private String[] parseFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        String expression = new String("");

        while (sc.hasNextLine()) {
            expression = sc.nextLine();
        }

        sc.close();

        return expression.split(" ");
    }
}
