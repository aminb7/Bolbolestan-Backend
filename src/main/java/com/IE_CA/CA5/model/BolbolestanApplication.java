package com.IE_CA.CA5.model;

import com.IE_CA.CA5.repository.BolbolestanRepository;
import com.IE_CA.CA5.repository.ConnectionPool;
import com.IE_CA.CA5.utilities.JsonParser;
import com.IE_CA.CA5.utilities.RawDataCollector;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BolbolestanApplication {
    private static BolbolestanApplication single_instance = null;
    private BolbolestanRepository repository = BolbolestanRepository.getInstance();

    private Map<String, Map<String, Course>> courses;
    private Map<String, Student> students;
    private String loggedInStudentId;
    private String searchFilter;
    private String typeSearchFilter;

    private BolbolestanApplication()
    {
        this.courses = new HashMap<>();
        this.students = new HashMap<>();
        this.loggedInStudentId = "";
        this.searchFilter = "";
        this.typeSearchFilter = "all";
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
            e.printStackTrace();
        }
        List<String> studentIds = new ArrayList<>();
        List.of(studentsList).forEach(student -> studentIds.add(student.getId()));

        try {
            Map<String, String> rawGrades = RawDataCollector.requestGrades(host, studentIds);
            for (Student student : studentsList) {
                student.setGradedCourses();

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
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from students where id = " + id);
            if (result.next())
                return true;
            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLoggedInStudentId() {
        return loggedInStudentId;
    }

    public void setLoggedInStudentId(String id) {
        this.loggedInStudentId = id;
    }

    public Student getLoggedInStudent() {
        Student student = null;
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from students where id = \"" + loggedInStudentId + "\"");
            if (result.next())
                student = new Student(result.getString("id"), result.getString("name"),
                        result.getString("secondName"), result.getString("birthDate"),
                        result.getString("field"), result.getString("faculty"),
                        result.getString("level"), result.getString("status"),
                        result.getString("img"));

            result.close();
            stmt.close();
            con.close();

            if (student != null) {
                student.setGradedCourses();
                student.setSelectedCourses();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public Course getCourse(String code, String classCode) {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet courseResult = stmt.executeQuery("select * from courses where code = " + code + " and classCode = " + classCode);

            if (courseResult.next()) {
                Statement stmt2 = con.createStatement();
                ResultSet prerequisitesResult = stmt2.executeQuery("select * from prerequisites where code = \"" + code + "\"");

                List<String> prerequisites = new ArrayList<String>();
                while (prerequisitesResult.next()) {
                    prerequisites.add(prerequisitesResult.getString("pcode"));
                }
                prerequisitesResult.close();
                stmt2.close();

                Statement stmt3 = con.createStatement();
                ResultSet classDaysResult = stmt3.executeQuery("select * from coursedays where code = \"" + code + "\"");
                List<String> days = new ArrayList<String>();
                while (classDaysResult.next()) {
                    days.add(classDaysResult.getString("day"));
                }
                classDaysResult.close();
                stmt3.close();

                ClassTime classTime = new ClassTime(days.toArray(new String[days.size()]), courseResult.getString("classStart") + "-" + courseResult.getString("classEnd"));
                ExamTime examTime = new ExamTime(LocalDateTime.parse(courseResult.getString("examStart")), LocalDateTime.parse(courseResult.getString("examEnd")));
                Course course = new Course(courseResult.getString("code"),
                        courseResult.getString("classCode"), courseResult.getString("name"),
                        courseResult.getInt("units"), courseResult.getString("type"),
                        courseResult.getString("instructor"), courseResult.getInt("capacity"),
                        prerequisites.toArray(new String[prerequisites.size()]), classTime, examTime);
                courseResult.close();
                stmt2.close();
                con.close();
                return course;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public void setTypeSearchFilter(String typeSearchFilter) {
        this.typeSearchFilter = typeSearchFilter;
    }

    public List<Course> getFilteredCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from courses where name like \"%" + searchFilter + "%\""
                    + " and (\"" + typeSearchFilter + "\" = \"all\" or \"" + typeSearchFilter + "\" = type)");

            while (result.next()) {
                courses.add(this.getCourse(result.getString("code"), result.getString("classCode")));
            }

            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
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
