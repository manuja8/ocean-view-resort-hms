package com.oceanview.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/oceanview_hms";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    private DBConnection() {
        try {
            String url = System.getProperty("db.url", DEFAULT_URL);

           
            if (url.startsWith("jdbc:h2")) {
                Class.forName(System.getProperty("db.driver", "org.h2.Driver"));
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) instance = new DBConnection();
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", DEFAULT_URL);
        String user = System.getProperty("db.user", DEFAULT_USER);
        String pass = System.getProperty("db.pass", DEFAULT_PASS);
        return DriverManager.getConnection(url, user, pass);
    }
}
