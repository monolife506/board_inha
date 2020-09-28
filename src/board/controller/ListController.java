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
import java.util.List;

@WebServlet("/list")
public class ListController extends HttpServlet
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
        HttpSession session = req.getSession();

        int board; // 현재 게시판이 무엇인지 확인하는 변수
        if (req.getParameter("board") == null) board = 0;
        else board = Integer.parseInt(req.getParameter("board"));

        int curPage; // 현재 페이지
        if (req.getParameter("curPage") == null) curPage = 1;
        else curPage = Integer.parseInt(req.getParameter("curPage"));

        boolean validLogin; // 올바른 로그인인지 확인
        if (session.getAttribute("validLogin") == null) validLogin = true;
        else validLogin = (Boolean) session.getAttribute("validLogin");

        try {
            PostDB postDB = new PostDB();
            int postCnt = postDB.getPostCnt(board); // 현재 게시판의 전체 글의 개수
            int postCntStart = 1 + ((curPage - 1) / 10) * 10; // 페이징을 시작하는 페이지
            List<Post> postList = postDB.getPostlist(board, curPage - 1, 12); // 게시판의 리스트

            req.setAttribute("board", board);
            req.setAttribute("curPage", curPage);
            req.setAttribute("validLogin", validLogin);

            req.setAttribute("postCnt", postCnt);
            req.setAttribute("postCntStart", postCntStart);
            req.setAttribute("postList", postList);

            postDB.close();
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/list.jsp");
        rd.forward(req, resp);
    }
}
