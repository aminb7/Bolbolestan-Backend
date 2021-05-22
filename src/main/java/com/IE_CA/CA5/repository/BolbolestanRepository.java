package com.IE_CA.CA5.repository;

import com.IE_CA.CA5.model.Course;
import com.IE_CA.CA5.model.GradedCourse;
import com.IE_CA.CA5.model.Student;
import com.IE_CA.CA5.utilities.JsonParser;
import com.IE_CA.CA5.utilities.RawDataCollector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "\ncapacity INTEGER,\nclassStart CHAR(100),\nclassEnd CHAR(100),\nexamStart CHAR(100),\nexamEnd CHAR(100)," +
                "\nPRIMARY KEY(code, classCode));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS CourseDays(code CHAR(100),\nclassCode CHAR(100),\nday CHAR(100),\nPRIMARY KEY(code, classCode, day));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Prerequisites(code CHAR(100),\nclassCode CHAR(100)," +
                "\npcode CHAR(100),\npclassCode CHAR(100),\nPRIMARY KEY(code, classCode, pcode, pclassCode)" +
                ",FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode)" +
                ",FOREIGN KEY (pcode, pclassCode) REFERENCES Courses(code, classCode));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Students(id CHAR(100),\nname CHAR(100),\nsecondName CHAR(100)," +
                "\nemail CHAR(100),\npassword CHAR(100),\nbirthDate CHAR(100),\nfield CHAR(100)," +
                "\nfaculty CHAR(100),\nlevel CHAR(100),\nstatus CHAR(100),\nimg CHAR(100),\nPRIMARY KEY(id));");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS SelectedCourses(id CHAR(100),\ncode CHAR(100),\nclassCode CHAR(100)," +
                "\ncourseState CHAR(100), \ncourseSelectionType CHAR(100), \nPRIMARY KEY(id, code, classCode)," +
                "FOREIGN KEY (id) REFERENCES Students(id)," +
                "FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode))");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS GradedCourses(id CHAR(100),\ncode CHAR(100),\nclassCode CHAR(100)," +
                "\ngrade FLOAT,\nterm INTEGER,\nPRIMARY KEY(id, code, classCode)," +
                "FOREIGN KEY (id) REFERENCES Students(id)," +
                "FOREIGN KEY (code, classCode) REFERENCES Courses(code, classCode))");
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
        fillTables();
    }

    private void fillTables() throws SQLException {
        fillCourses();
        fillStudentsAndGradedCourses();
    }

    private void fillCourses() throws SQLException {
        String host = "http://138.197.181.131:5200";
        Course[] coursesList = null;
        try {
            coursesList = JsonParser.createObject(RawDataCollector.requestCourses(host), Course[].class);
        }
        catch (Exception e) {
            System.out.println("catch1");
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Courses VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update code = code");
        PreparedStatement stmt2 = con.prepareStatement("INSERT INTO Prerequisites VALUES (?, ?, ?, ?) on duplicate key update code = code");
        PreparedStatement stmt3 = con.prepareStatement("INSERT INTO CourseDays VALUES (?, ?, ?) on duplicate key update code = code");
        List.of(coursesList).forEach(course -> {
            try {
                stmt1.setString(1, course.getCode());
                stmt1.setString(2, course.getClassCode());
                stmt1.setString(3, course.getName());
                stmt1.setInt(4, course.getUnits());
                stmt1.setString(5, course.getType());
                stmt1.setString(6, course.getInstructor());
                stmt1.setInt(7, course.getCapacity());
                stmt1.setString(8, course.getClassTime().getStart().toString());
                stmt1.setString(9, course.getClassTime().getEnd().toString());
                stmt1.setString(10, course.getExamTime().getStart().toString());
                stmt1.setString(11, course.getExamTime().getEnd().toString());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            List.of(course.getPrerequisites()).forEach(prerequisite -> {
                try {
                    stmt2.setString(1,course.getCode());
                    stmt2.setString(2,course.getClassCode());
                    stmt2.setString(3,prerequisite);
                    stmt2.setString(4,"1");
                    stmt2.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            List.of(course.getClassTime().getDays()).forEach(day -> {
                try {
                    stmt3.setString(1, course.getCode());
                    stmt3.setString(2, course.getClassCode());
                    stmt3.setString(3, day);
                    stmt3.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        int[] result1 = stmt1.executeBatch();
        stmt1.close();
        int[] result2 = stmt2.executeBatch();
        stmt2.close();
        int[] result3 = stmt3.executeBatch();
        stmt3.close();
        con.close();
    }

    private void fillStudentsAndGradedCourses() throws SQLException {
        String host = "http://138.197.181.131:5200";
        Student[] studentsList = null;
        try {
            studentsList = JsonParser.createObject(RawDataCollector.requestStudents(host), Student[].class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        List<String> studentIds = new ArrayList<>();

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Students VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update id = id;");
        List.of(studentsList).forEach(student -> {
            studentIds.add(student.getId());
            try {
                stmt1.setString(1, student.getId());
                stmt1.setString(2, student.getName());
                stmt1.setString(3, student.getSecondName());
                stmt1.setString(4, student.getEmail());
                stmt1.setString(5, hashPassword(student.getPassword()));
                stmt1.setString(6, student.getBirthDate());
                stmt1.setString(7, student.getField());
                stmt1.setString(8, student.getFaculty());
                stmt1.setString(9, student.getLevel());
                stmt1.setString(10, student.getStatus());
                stmt1.setString(11, student.getImg());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] result1 = stmt1.executeBatch();
        stmt1.close();

        PreparedStatement stmt2 = con.prepareStatement("INSERT INTO GradedCourses VALUES (?, ?, ?, ?, ?) on duplicate key update id = id");
        try {
            Map<String, String> rawGrades = RawDataCollector.requestGrades(host, studentIds);
            for (Student student : studentsList) {
                GradedCourse[] gradedCourses = JsonParser.createObject(rawGrades.get(student.getId()), GradedCourse[].class);
                for (GradedCourse gradedCourse : gradedCourses) {
                    stmt2.setString(1, student.getId());
                    stmt2.setString(2, gradedCourse.getCode());
                    stmt2.setString(3, "1");
                    stmt2.setFloat(4, gradedCourse.getGrade());
                    stmt2.setInt(5, gradedCourse.getTerm());
                    stmt2.addBatch();
                }
            }
        }
        catch (Exception e) {
        }
        int[] result2 = stmt2.executeBatch();
        stmt2.close();
        con.close();
    }

    public void addStudent(Student student) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO Students VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update id = id;");
        stmt.setString(1, student.getId());
        stmt.setString(2, student.getName());
        stmt.setString(3, student.getSecondName());
        stmt.setString(4, student.getEmail());
        stmt.setString(5, hashPassword(student.getPassword()));
        stmt.setString(6, student.getBirthDate());
        stmt.setString(7, student.getField());
        stmt.setString(8, student.getFaculty());
        stmt.setString(9, student.getLevel());
        stmt.setString(10, student.getStatus());
        stmt.setString(11, student.getImg());
        stmt.addBatch();
        int[] result2 = stmt.executeBatch();
        stmt.close();
        con.close();
    }

    public static String hashPassword(String inputPassword)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputPassword.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
