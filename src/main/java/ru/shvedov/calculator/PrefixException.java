package ru.shvedov.calculator;

@SuppressWarnings("serial")
public class PrefixException extends RuntimeException {
    public PrefixException() {
        super();
    }
    public PrefixException(String message) {
        super(message);
    }
}
