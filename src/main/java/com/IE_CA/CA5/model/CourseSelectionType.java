package com.IE_CA.CA5.model;

public enum CourseSelectionType {
    REGISTERED,
    WAITING_LIST;

    @Override
    public String toString() {
        if (this == REGISTERED) return "registered";
        else return "waiting_list";
    }
}
