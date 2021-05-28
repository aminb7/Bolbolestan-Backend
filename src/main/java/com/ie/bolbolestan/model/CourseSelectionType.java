package com.ie.bolbolestan.model;

public enum CourseSelectionType {
    REGISTERED,
    WAITING_LIST;

    @Override
    public String toString() {
        if (this == REGISTERED) return "REGISTERED";
        else return "WAITING_LIST";
    }
}
