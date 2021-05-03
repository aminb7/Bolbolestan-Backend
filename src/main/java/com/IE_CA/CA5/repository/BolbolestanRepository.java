package com.IE_CA.CA5.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
//        PreparedStatement createTableStatement = con.prepareStatement(
//        );
//        createTableStatement.executeUpdate();
//        createTableStatement.close();
        con.close();
    }
}
