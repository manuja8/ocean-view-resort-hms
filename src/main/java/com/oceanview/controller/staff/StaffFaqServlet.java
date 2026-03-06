package com.oceanview.controller.staff;

import com.oceanview.service.FaqService;
import com.oceanview.service.impl.FaqServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/faq")
public class StaffFaqServlet extends HttpServlet {

    private final FaqService faqService = new FaqServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("faqList", faqService.getActiveFaqs());
        req.getRequestDispatcher("/WEB-INF/views/staff/faq/list.jsp").forward(req, resp);
    }
}