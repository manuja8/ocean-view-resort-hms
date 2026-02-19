package com.oceanview.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static int activeUsers = 0;

    public void sessionCreated(HttpSessionEvent se) {
        activeUsers++;
        System.out.println("Active Users: " + activeUsers);
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        activeUsers--;
        System.out.println("Active Users: " + activeUsers);
    }
}
