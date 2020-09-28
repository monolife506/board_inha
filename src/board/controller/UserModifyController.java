package board.controller;

import board.member.MemberDB;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/usermodify")
public class UserModifyController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(true);
        String userId = (String) session.getAttribute("user.id");
        String userEmail = "";

        if (userId != null) {
            try {
                MemberDB memberDB = new MemberDB();
                userEmail = memberDB.getEmail(userId);
                memberDB.close();
            }
            catch (SQLException | NamingException throwables) {
                throwables.printStackTrace();
            }

            req.setAttribute("user.email", userEmail);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/usermodify.jsp");
            rd.forward(req, resp);
        } else {
            resp.sendRedirect("/list");
        }
    }
}
