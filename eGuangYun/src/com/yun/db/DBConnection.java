package com.yun.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static void main(String[] args) {
        DBConnection connection = DBConnection.getInstance();
        connection.close();

    }

    private DBConnection() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM YUN");
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE YUN(");
            builder.append("INDEX INT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),");
            builder.append("NAME CHAR(1),");
            builder.append("TONE CHAR(1),");
            builder.append("VOLUME INT,");






        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if ("42X05".equals(e.getErrorCode())) {

            }
        }

        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    public void close() {
        try {
            conn.close();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection conn;
    private static DBConnection instance;
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DB_URL = "jdbc:derby:GuangYun;create=true";
}
