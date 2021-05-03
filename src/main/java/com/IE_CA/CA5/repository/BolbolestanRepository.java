package com.IE_CA.CA5.repository;

import java.sql.*;
import java.util.ArrayList;

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
    }
}
