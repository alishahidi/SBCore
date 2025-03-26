package net.alishahidi.sbcore.core.generator;

public class CodeGenerationException extends Exception {
    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}