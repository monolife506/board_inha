package board.post;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/like.do")
public class LikeDo extends HttpServlet
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
        boolean addUpvote = Boolean.parseBoolean(req.getParameter("add"));
        String userID = (String) session.getAttribute("user.id");

        if (userID != null) {
            try {
                PostDB postDB = new PostDB();
                LikeDB likeDB = new LikeDB();
                postDB.updateUpvoteCnt(postIdx, addUpvote);
                likeDB.toggleLike(userID, postIdx);

                postDB.close();
                likeDB.close();
            }
            catch (SQLException | NamingException e) {
                e.printStackTrace();
            }
        }

        resp.sendRedirect("/content?idx=" + postIdx);
    }
}
