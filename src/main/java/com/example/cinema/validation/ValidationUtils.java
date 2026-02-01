package com.example.cinema.validation;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidName(String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.matches("[A-Za-z ]{2,50}");
    }

    public static boolean isValidUsername(String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.matches("[A-Za-z0-9_]{3,20}");
    }

    public static boolean isValidPassword(String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.length() >= 4;
    }

    public static Integer parsePositiveInt(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
