package board.member;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register.do")
public class UserRegister extends HttpServlet
{
    public enum RegisterError
    {ID_ERROR, EMAIL_ERROR}

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
        String id = req.getParameter("id");
        String email = req.getParameter("email");
        String pwd = req.getParameter("pwd");
        String target = "/register";

        if (id == null || email == null || pwd == null) {
            target = "/list";
        } else {
            MemberDB memberDB = new MemberDB();
            if (memberDB.checkId(id)) {
                httpSession.setAttribute("registerError", RegisterError.ID_ERROR);
            } else if (memberDB.checkEmail(email)) {
                httpSession.setAttribute("registerError", RegisterError.EMAIL_ERROR);
            } else {
                Member member = new Member();
                member.setId(id);
                member.setEmail(email);
                member.setPwd(pwd);
                memberDB.insertMember(member);

                httpSession.setAttribute("user.id", id);
                httpSession.setAttribute("registerError", null);

                target = "/list";
            }
            memberDB.close();
        }

        resp.sendRedirect(target);
    }
}
