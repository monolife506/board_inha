package board.reply;

import board.post.PostDB;
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

@WebServlet("/inputreply.do")
public class ReplyInput extends HttpServlet
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

        int postIdx = Integer.parseInt(req.getParameter("idx"));
        String content = req.getParameter("content");
        String userID = (String) session.getAttribute("user.id");

        try {
            PostDB postDB = new PostDB();
            ReplyDB replyDB = new ReplyDB();
            Reply reply = new Reply();

            if (userID != null) {
                content = content.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
                content = Jsoup.clean(content, Whitelist.basic());

                reply.setPost_idx(postIdx);
                reply.setAuthor(userID);
                reply.setContent(content);

                postDB.updateReplyCnt(postIdx, true);
                replyDB.insertReply(reply);
            }

            replyDB.close();
            postDB.close();
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/content?idx=" + postIdx);
    }
}
