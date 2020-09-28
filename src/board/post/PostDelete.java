package board.post;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete.do")
public class PostDelete extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        process(req, resp);
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession(false);

        int postIdx = Integer.parseInt(req.getParameter("idx"));
        String userId = (String) session.getAttribute("user.id");
        String author = req.getParameter("author");

        try {
            PostDB postDB = new PostDB();

            if (userId.equals(author)) postDB.deletePost(postIdx);
            postDB.close();
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/");
    }
}
