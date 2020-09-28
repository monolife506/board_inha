package board.member;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/usermodify.do")
public class UserModify extends HttpServlet
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
        String userId = (String) httpSession.getAttribute("user.id");
        String email = req.getParameter("email");
        String pwd = req.getParameter("pwd");
        String target = "/usermodify";

        if (userId == null || email == null || pwd == null) {
            target = "/list";
        } else {
            MemberDB memberDB = new MemberDB();
            if (memberDB.checkEmail(email) && !Objects.equals(memberDB.getEmail(userId), email)) {
                httpSession.setAttribute("registerError", UserRegister.RegisterError.EMAIL_ERROR);
            } else {
                Member member = new Member();
                member.setId(userId);
                member.setEmail(email);
                member.setPwd(pwd);
                memberDB.updateMember(member);

                httpSession.setAttribute("user.id", userId);
                httpSession.setAttribute("registerError", null);

                target = "/list";
            }
            memberDB.close();
        }

        resp.sendRedirect(target);
    }
}
