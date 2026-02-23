package com.oceanview.util;

import com.oceanview.config.DBConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            if(con != null){
                System.out.println("Success:DB Connected Successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
