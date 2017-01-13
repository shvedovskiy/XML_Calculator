package ru.shvedov.calculator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Calculating prefix expression.
 */
public class Solver {
    private String expression;

    /**
     * Initialization of expression.
     * @param expression
     *        String with one prefix expression.
     */
    public Solver(String expression) {
        this.expression = expression;
    }

    /**
     * Obtaining the result of prefix expression.
     * @return Result value.
     * @throws PrefixException In case of errors in expression logic.
     * @throws ArithmeticException While divide by zero.
     */
    public double solve() throws PrefixException, ArithmeticException {
        Deque<String> stack = new ArrayDeque<>();
        double leftOperand, rightOperand, result;
        int cnt = 0;
        String operator;

        String[] expression = this.expression.split(" ");
        for (String token : expression) {
            if (token.matches("[0-9]+")) {
                stack.push(token);
                cnt += 1;
                while (cnt == 2) {
                    rightOperand = Double.parseDouble(stack.peek());
                    stack.pop();
                    leftOperand = Double.parseDouble(stack.peek());
                    stack.pop();

                    if (stack.isEmpty()) {
                        throw new PrefixException("Too many operands.");
                    }

                    operator = stack.peek();
                    stack.pop();

                    switch (operator) {
                        case "DIV":
                            if (rightOperand == 0) {
                                throw new ArithmeticException("Divide by zero.");
                            }
                            result = leftOperand / rightOperand;
                            break;
                        case "MUL":
                            result = leftOperand * rightOperand;
                            break;
                        case "SUM":
                            result = leftOperand + rightOperand;
                            break;
                        case "SUB":
                            result = leftOperand - rightOperand;
                            break;
                        default:
                            throw new PrefixException("Illegal operator: " + operator);
                    }

                    if (stack.isEmpty() || (!stack.peek().substring(0, 1).matches("[0-9]"))) {
                        cnt = 1;
                    }
                    stack.push(result + "");
                }
            } else {
                cnt = 0;
                stack.push(token);
            }
        }

        if (stack.isEmpty()) {
            throw new PrefixException("Nothing entered in expression.");
        }
        result = Double.parseDouble(stack.peek());
        stack.pop();

        if (!stack.isEmpty()) {
            throw new PrefixException("Too many operations.");
        }
        return result;
    }
}
