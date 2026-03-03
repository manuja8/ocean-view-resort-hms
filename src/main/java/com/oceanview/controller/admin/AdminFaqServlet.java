package com.oceanview.controller.admin;

import com.oceanview.dto.FaqDTO;
import com.oceanview.service.FaqService;
import com.oceanview.service.impl.FaqServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/faq")
public class AdminFaqServlet extends HttpServlet {

    private final FaqService faqService = new FaqServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        // flash messages (PRG)
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object success = session.getAttribute("flashSuccess");
            Object error = session.getAttribute("flashError");
            if (success != null) {
                req.setAttribute("success", success);
                session.removeAttribute("flashSuccess");
            }
            if (error != null) {
                req.setAttribute("error", error);
                session.removeAttribute("flashError");
            }
        }

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("mode", "create");
            req.setAttribute("faq", new FaqDTO());
            req.getRequestDispatcher("/WEB-INF/views/admin/faq/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            FaqDTO dto = faqService.getById(id);
            if (dto == null) {
                req.getSession().setAttribute("flashError", "FAQ not found.");
                resp.sendRedirect(req.getContextPath() + "/admin/faq");
                return;
            }
            req.setAttribute("mode", "edit");
            req.setAttribute("faq", dto);
            req.getRequestDispatcher("/WEB-INF/views/admin/faq/form.jsp").forward(req, resp);
            return;
        }

        // list
        req.setAttribute("faqList", faqService.getAll());
        req.getRequestDispatcher("/WEB-INF/views/admin/faq/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (mode == null) mode = "";

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            if ("delete".equalsIgnoreCase(mode)) {
                int id = Integer.parseInt(req.getParameter("id"));
                faqService.delete(id);
                req.getSession().setAttribute("flashSuccess", "FAQ deleted.");
                resp.sendRedirect(req.getContextPath() + "/admin/faq");
                return;
            }

            FaqDTO dto = new FaqDTO();
            dto.setQuestion(req.getParameter("question"));
            dto.setAnswer(req.getParameter("answer"));
            dto.setActive("1".equals(req.getParameter("isActive")) || "true".equalsIgnoreCase(req.getParameter("isActive")));

            if ("edit".equalsIgnoreCase(mode)) {
                dto.setFaqId(Integer.parseInt(req.getParameter("id")));
                faqService.update(dto, userId);
                req.getSession().setAttribute("flashSuccess", "FAQ updated.");
            } else {
                faqService.create(dto, userId);
                req.getSession().setAttribute("flashSuccess", "FAQ created.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/faq");

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("faq", dtoFromRequest(req));
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);
            req.getRequestDispatcher("/WEB-INF/views/admin/faq/form.jsp").forward(req, resp);
        }
    }

    private FaqDTO dtoFromRequest(HttpServletRequest req) {
        FaqDTO dto = new FaqDTO();
        dto.setQuestion(req.getParameter("question"));
        dto.setAnswer(req.getParameter("answer"));
        dto.setActive("1".equals(req.getParameter("isActive")) || "true".equalsIgnoreCase(req.getParameter("isActive")));
        try {
            dto.setFaqId(Integer.parseInt(req.getParameter("id")));
        } catch (Exception ignored) {
        }
        return dto;
    }
}