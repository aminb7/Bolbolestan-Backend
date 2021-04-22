package com.IE_CA.CA5.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawDataCollector {

    final static String coursesPath = "/api/courses";
    final static String studentsPath = "/api/students";
    final static String gradesPath = "/api/grades";

    public static String requestCourses(String host) throws IOException, InterruptedException {
        return HttpGetter.Get(host + coursesPath);
    }

    public static String requestStudents(String host) throws IOException, InterruptedException {
        return HttpGetter.Get(host + studentsPath);
    }

    public static Map<String, String> requestGrades(String host, List<String> studentIdList)
            throws IOException, InterruptedException {
        Map<String, String> grades = new HashMap<>();

        for (String StudentId : studentIdList) {
            String currentStudentGrades = HttpGetter.Get(host + gradesPath + "/" + StudentId);
            grades.put(StudentId, currentStudentGrades);
        }

        return grades;
    }
}
