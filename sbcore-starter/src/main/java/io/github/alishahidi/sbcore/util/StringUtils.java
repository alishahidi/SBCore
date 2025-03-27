package io.github.alishahidi.sbcore.util;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;

@UtilityClass
public class StringUtils {
    public String normalize(String inputName) {
        // Convert to lowercase for consistency
        String str = inputName.toLowerCase();

        // Remove accents and diacritics (e.g., "é" -> "e")
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Replace spaces with hyphens (or underscores if preferred)
        str = str.replaceAll("\\s+", "-");

        // Remove all non-alphanumeric characters except hyphens and underscores
        str = str.replaceAll("[^a-z0-9-_]", "");

        // Limit length if necessary (optional)
        int maxLength = 255;  // Adjust this limit based on your needs
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength);
        }

        return str;
    }

    public String toUpperCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    public String toLowerCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Normalize different separators to underscores
        String normalized = input.replaceAll("[-\\s]", "_");

        // Split by underscore or capital letters
        String[] parts = normalized.split("(?=[A-Z])|_");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }

            if (i == 0) {
                // First part should be lowercase
                result.append(part.toLowerCase());
            } else {
                // Subsequent parts should be capitalized
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    public String toSnakeCase(String input) {
        String snakeCase = input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();

        if (snakeCase.endsWith("y") && !snakeCase.endsWith("ay")) {
            snakeCase = snakeCase.substring(0, snakeCase.length() - 1) + "ies";
        } else if (!snakeCase.endsWith("s")) {
            snakeCase += "s";
        }

        return snakeCase;
    }
}
