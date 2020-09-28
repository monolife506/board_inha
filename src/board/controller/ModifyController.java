package board.controller;

import board.post.Post;
import board.post.PostDB;

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

@WebServlet("/modify")
public class ModifyController extends HttpServlet
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
        int postIdx = Integer.parseInt(req.getParameter("idx"));
        String userID = (String) session.getAttribute("user.id");

        try {
            PostDB postDB = new PostDB();
            Post post = postDB.getPost(postIdx);
            post.setContent(post.getContent().replaceAll("<br>", ""));

            req.setAttribute("post", post);
            postDB.close();

            if (userID.equals(post.getAuthor())) {
                RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/modify.jsp");
                rd.forward(req, resp);
            } else {
                resp.sendRedirect("/list");
            }
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }
}
