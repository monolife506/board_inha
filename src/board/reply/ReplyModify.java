package board.reply;


import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/modifyreply.do")
public class ReplyModify extends HttpServlet
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
        HttpSession session = req.getSession(true);

        int idx = Integer.parseInt(req.getParameter("idx"));
        int postIdx = Integer.parseInt(req.getParameter("postIdx"));
        String userId = (String) session.getAttribute("user.id");

        try {
            ReplyDB replyDB = new ReplyDB();
            Reply reply = replyDB.getReply(idx);
            if (reply.getAuthor().equals(userId)) {
                String content = req.getParameter("content");
                content = content.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
                content = Jsoup.clean(content, Whitelist.basic());

                reply.setContent(content);
                replyDB.modifyReply(idx, reply);
            }
            replyDB.close();
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/content?idx=" + postIdx);
    }
}
