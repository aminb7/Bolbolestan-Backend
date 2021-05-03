package com.IE_CA.CA5.repository;

import com.IE_CA.CA5.model.Course;
import com.IE_CA.CA5.model.Student;
import com.IE_CA.CA5.utilities.JsonParser;
import com.IE_CA.CA5.utilities.RawDataCollector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BolbolestanRepository {
    private static BolbolestanRepository instance;

    public static BolbolestanRepository getInstance() {
        if (instance == null) {
            try {
                instance = new BolbolestanRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in BolbolestanRepository.create query.");
            }
        }
        return instance;
    }

    private BolbolestanRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Courses(code CHAR(100),\nclassCode CHAR(100),\nname CHAR(100)," +
                "\nunits INTEGER,\ntype CHAR(100),\ninstructor CHAR(100)," +
                "\ncapacity INTEGER,\nPRIMARY KEY(code, classCode));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Prerequisites(code CHAR(100),\nclassCode CHAR(100)," +
                "\npcode CHAR(100),\npclassCode CHAR(100),\nPRIMARY KEY(code, classCode, pcode, pclassCode)" +
                ",FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode)" +
                ",FOREIGN KEY (pcode, pclassCode) REFERENCES Courses(code, classCode));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Students(id CHAR(100),\nname CHAR(100),\nsecondName CHAR(100)," +
                "\nemail CHAR(100),\npassword CHAR(100),\nbirthDate CHAR(100),\nfield CHAR(100)," +
                "\nfaculty CHAR(100),\nlevel CHAR(100),\nstatus CHAR(100),\nimg CHAR(100),\nPRIMARY KEY(id));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS SelectedCourses(id CHAR(100),\ncode CHAR(100),\nclassCode CHAR(100)," +
                "\nPRIMARY KEY(id, code, classCode)," +
                "FOREIGN KEY (id) REFERENCES Students(id)," +
                "FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode))");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS GradedCourses(id CHAR(100),\ncode CHAR(100),\nclassCode CHAR(100)," +
                "\nPRIMARY KEY(id, code, classCode)," +
                "FOREIGN KEY (id) REFERENCES Students(id)," +
                "FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode))");
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
        fillTables();
    }

    private void fillTables() throws SQLException {
        fillCourses();
        fillStudents();
        fillGradedCourses();
    }

    private void fillCourses() throws SQLException {
        String host = "http://138.197.181.131:5100";
        Course[] coursesList = null;
        try {
            coursesList = JsonParser.createObject(RawDataCollector.requestCourses(host), Course[].class);
        }
        catch (Exception e) {
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO Courses VALUES (?, ?, ?, ?, ?, ?, ?) on duplicate key update code = code");
        List.of(coursesList).forEach(course -> {
            try {
                stmt.setString(1, course.getCode());
                stmt.setString(2, course.getClassCode());
                stmt.setString(3, course.getName());
                stmt.setInt(4, course.getUnits());
                stmt.setString(5, course.getType());
                stmt.setString(6, course.getInstructor());
                stmt.setInt(7, course.getCapacity());
                stmt.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
    }

    private void fillStudents() throws SQLException {
        String host = "http://138.197.181.131:5100";
        Student[] studentsList = null;
        try {
            studentsList = JsonParser.createObject(RawDataCollector.requestStudents(host), Student[].class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO Students VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update id = id");
        List.of(studentsList).forEach(student -> {
            try {
                stmt.setString(1, student.getId());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getSecondName());
                stmt.setString(4, student.getEmail());
                stmt.setString(5, student.getPassword());
                stmt.setString(6, student.getBirthDate());
                stmt.setString(7, student.getField());
                stmt.setString(8, student.getFaculty());
                stmt.setString(9, student.getLevel());
                stmt.setString(10, student.getStatus());
                stmt.setString(11, student.getImg());
                stmt.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
    }

    private void fillGradedCourses() {

    }
}
