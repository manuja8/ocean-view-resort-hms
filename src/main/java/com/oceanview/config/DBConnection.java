package com.oceanview.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;   // Singleton instance
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/oceanview_hms";
    private static final String USER = "root";
    private static final String PASS = ""; // change if needed

    // private constructor (Singleton)
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("DB Connected Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get instance
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    // return connection
    public Connection getConnection() {
        return connection;
    }
}

