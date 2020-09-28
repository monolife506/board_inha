package board.member;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.do")
public class UserLogin extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try {
            process(req, resp);
        }
        catch (SQLException | NamingException | ServletException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try {
            process(req, resp);
        }
        catch (SQLException | NamingException | ServletException throwables) {
            throwables.printStackTrace();
        }
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NamingException, ServletException
    {
        req.setCharacterEncoding("utf-8");

        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");

        boolean validLogin = true;
        MemberDB memberDB = new MemberDB();
        HttpSession httpSession = req.getSession(true);

        if (httpSession.getAttribute("user.id") == null) {
            if (!memberDB.checkId(id)) {
                validLogin = false;
            } else if (!memberDB.checkPwd(id, pwd)) {
                validLogin = false;
            } else {
                httpSession.setAttribute("user.id", id);
            }
        }

        memberDB.close();
        httpSession.setAttribute("validLogin", validLogin);
        resp.sendRedirect("/list");
    }
}
