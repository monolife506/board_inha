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
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/search")
public class SearchController extends HttpServlet
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

        int board; // 현재 게시판이 무엇인지 확인하는 변수
        if (req.getParameter("board") == null) board = 0;
        else board = Integer.parseInt(req.getParameter("board"));

        int curPage; // 현재 페이지
        if (req.getParameter("curPage") == null) curPage = 1;
        else curPage = Integer.parseInt(req.getParameter("curPage"));

        // 검색 타입 (1: 제목 + 내용, 2: 제목, 3: 내용, 4: 작성자)
        int searchType = Integer.parseInt(req.getParameter("searchType"));
        String searchedWordEncoded = req.getParameter("searchedWord");
        String searchedWord = URLDecoder.decode(searchedWordEncoded, StandardCharsets.UTF_8);

        try {
            PostDB postDB = new PostDB();

            List<Post> postList = postDB.getPostlistBySearch(board, curPage - 1, 12, searchedWord, searchType); // 게시판의 리스트
            int postCnt = postDB.getPostCntBySearch(board, searchedWord, searchType); // 현재 게시판의 전체 글의 개수
            int postCntStart = 1 + ((curPage - 1) / 10) * 10; // 페이징을 시작하는 페이지

            req.setAttribute("board", board);
            req.setAttribute("curPage", curPage);

            req.setAttribute("postCnt", postCnt);
            req.setAttribute("postCntStart", postCntStart);
            req.setAttribute("postList", postList);

            req.setAttribute("searchType", searchType);
            req.setAttribute("searchedWordEncoded", searchedWordEncoded);

            postDB.close();
        }
        catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/search.jsp");
        rd.forward(req, resp);
    }
}