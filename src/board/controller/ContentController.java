package board.controller;

import board.like.Like;
import board.like.LikeDB;
import board.post.Post;
import board.post.PostDB;
import board.reply.Reply;
import board.reply.ReplyDB;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/content")
public class ContentController extends HttpServlet
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
        HttpSession session = req.getSession(true);

        int curPage; // 현재 댓글 페이지
        if (req.getParameter("curPage") == null) curPage = 1;
        else curPage = Integer.parseInt(req.getParameter("curPage"));
        int postIdx = Integer.parseInt(req.getParameter("idx"));

        String userId = (String) session.getAttribute("user.id");

        Cookie[] cookies = req.getCookies();
        Cookie viewCheck = null;

        try {
            PostDB postDB = new PostDB();
            ReplyDB replyDB = new ReplyDB();

            if (userId != null) {
                LikeDB likeDB = new LikeDB();
                if (!likeDB.checkLikeAvailable(userId, postIdx)) postDB.updateReadCnt(postIdx);

                Like likeStatus = likeDB.getLike(userId, postIdx);
                req.setAttribute("like", likeStatus);
                likeDB.close();
            } else {
                if (cookies != null && cookies.length > 0) {
                    for (Cookie c : cookies) {
                        if (c.getName().equals("view" + postIdx)) viewCheck = c;
                    }
                }

                if (session.getAttribute("view" + postIdx) == null && viewCheck == null) {
                    session.setAttribute("view" + postIdx, true);
                    Cookie newCookie = new Cookie("view" + postIdx, Integer.toString(postIdx));
                    resp.addCookie(newCookie);
                    postDB.updateReadCnt(postIdx);
                }
            }

            Post post = postDB.getPost(postIdx);
            int replyCnt = replyDB.getReplyCnt(postIdx); // 현재 글의 전체 댓글의 개수
            int replyCntStart = 1 + ((curPage - 1) / 10) * 10; // 페이징을 시작하는 페이지
            List<Reply> replyList = replyDB.getReplylist(postIdx, curPage - 1, 10); // 댓글 리스트

            req.setAttribute("post", post);
            req.setAttribute("curPage", curPage);
            req.setAttribute("replyCnt", replyCnt);
            req.setAttribute("replyCntStart", replyCntStart);
            req.setAttribute("replyList", replyList);

            replyDB.close();
            postDB.close();
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/content.jsp");
        rd.forward(req, resp);
    }
}
