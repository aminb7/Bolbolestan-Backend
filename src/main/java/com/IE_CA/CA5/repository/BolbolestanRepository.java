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
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format("CREATE TABLE IF NOT EXISTS %s(id CHAR(50),\nname CHAR(225),\nhabitat CHAR(225),\nPRIMARY KEY(id));", TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }
}
