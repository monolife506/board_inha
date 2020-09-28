package board.member;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/userdelete.do")
public class UserDelete extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try {
            process(req, resp);
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try {
            process(req, resp);
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NamingException
    {
        req.setCharacterEncoding("utf-8");

        HttpSession httpSession = req.getSession(true);
        String userId = (String) httpSession.getAttribute("user.id");
        try {
            MemberDB memberDB = new MemberDB();
            memberDB.deleteMember(userId);
            httpSession.setAttribute("user.id", null);

            memberDB.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/list");
    }
}
