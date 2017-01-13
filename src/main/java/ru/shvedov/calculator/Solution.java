package ru.shvedov.calculator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class Solution {
    private static String getClassLocation() {
        URL location = Solution.class.getProtectionDomain().getCodeSource().getLocation();
        String classLocation = location.getFile();
        try {
            classLocation = URLDecoder.decode(classLocation.substring(0, classLocation.length() - 22).replace('/', File.separatorChar),
                    Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        return classLocation;
    }

    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar \"XML_Calculator-1.0.jar\" <input>.xml");
            System.exit(-1);
        }

        String classLocation = getClassLocation();
        Parser parser = new Parser(classLocation, args[0]);
        System.out.println("Reading from input...");
        List<String> expressions = parser.parse();

        Solver solver;
        List<Double> results = new LinkedList<>();

        System.out.println("Expressions solving...");
        for (String expression : expressions) {
            solver = new Solver(expression);
            try {
                results.add(solver.solve());
            } catch (PrefixException | ArithmeticException e) {
                System.err.println("Problems while solving!");
                e.getMessage();
                System.exit(-1);
            }
        }

        System.out.println("Writing to output...");
        parser.write(results, classLocation);
    }
}
