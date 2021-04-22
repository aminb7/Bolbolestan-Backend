package com.IE_CA.CA5.model;

public enum CourseSelectionType {
    REGISTERED,
    WAITING_LIST;

    @Override
    public String toString() {
        return switch (this) {
            case REGISTERED -> "registered";
            case WAITING_LIST -> "waiting_list";
            default -> "";
        };
    }
}
