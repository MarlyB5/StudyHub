package org.todo;

public enum TaskPriority {
    EXTRA_IMPORTANT,
    MODERATELY_IMPORTANT,
    NOT_IMPORTANT;

    public String displayName() {
        return switch (this) {
            case EXTRA_IMPORTANT -> "Extra Important";
            case MODERATELY_IMPORTANT -> "Moderately Important";
            case NOT_IMPORTANT -> "Not Important";
        };
    }

    @Override
    public String toString() {
        return displayName();
    }
}
