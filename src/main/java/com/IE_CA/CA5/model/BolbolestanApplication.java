package com.IE_CA.CA5.model;

import com.IE_CA.CA5.utilities.JsonParser;
import com.IE_CA.CA5.utilities.RawDataCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BolbolestanApplication {
    private static BolbolestanApplication single_instance = null;

    private Map<String, Map<String, Course>> courses;
    private Map<String, Student> students;
    private String loggedInStudentId;
    private String searchFilter;

    private BolbolestanApplication()
    {
        this.courses = new HashMap<>();
        this.students = new HashMap<>();
        this.loggedInStudentId = "";
        this.searchFilter = "";
        fillInformation();
    }

    public static BolbolestanApplication getInstance()
    {
        if (single_instance == null)
            single_instance = new BolbolestanApplication();

        return single_instance;
    }

    private void fillInformation() {
        String host = "http://138.197.181.131:5100";
        Course[] coursesList = null;
        try {
            coursesList = JsonParser.createObject(RawDataCollector.requestCourses(host), Course[].class);
        }
        catch (Exception e) {
        }

        List.of(coursesList).forEach(course -> {
            if (!courses.containsKey(course.getCode()))
                courses.put(course.getCode(), new HashMap<>());

            this.courses.get(course.getCode()).put(course.getClassCode(), course);
        });

        Student[] studentsList = null;
        try {
            studentsList = JsonParser.createObject(RawDataCollector.requestStudents(host), Student[].class);
        }
        catch (Exception e){
        }
        List<String> studentIds = new ArrayList<>();
        List.of(studentsList).forEach(student -> studentIds.add(student.getId()));

        try {
            Map<String, String> rawGrades = RawDataCollector.requestGrades(host, studentIds);
            for (Student student : studentsList) {
                student.setGradedCourses(JsonParser.createObject(rawGrades.get(student.getId()), GradedCourse[].class));

                for (Map.Entry<String, GradedCourse> entry : student.getGradedCourses().entrySet()) {
                    entry.getValue().setCourse(this.courses.get(entry.getKey()).entrySet().iterator().next().getValue());
                }
            }

            List.of(studentsList).forEach(student -> this.students.put(student.getId(), student));
        }
        catch (Exception e) {
        }
    }

    public boolean studentExists(String id) {
        return students.containsKey(id);
    }

    public String getLoggedInStudentId() {
        return loggedInStudentId;
    }

    public void setLoggedInStudentId(String id) {
        this.loggedInStudentId = id;
    }

    public Student getLoggedInStudent() {
        return students.get(loggedInStudentId);
    }

    public Map<String, Map<String, Course>> getCourses() {
        return courses;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public List<Course> getFilteredCourses() {
        List<Course> courses = new ArrayList<>();

        for (Map.Entry<String, Map<String, Course>> entry : this.courses.entrySet()) {
            for (Map.Entry<String, Course> course : entry.getValue().entrySet()) {
                if (course.getValue().getName().contains(searchFilter))
                    courses.add(course.getValue());
            }
        }

        return courses;
    }

    public void updateWaitingLists() {
        for (Map.Entry<String, Map<String, Course>> entry : this.courses.entrySet()) {
            for (Map.Entry<String, Course> course : entry.getValue().entrySet()) {
                course.getValue().updateWaitingList();
            }
        }
    }
}
