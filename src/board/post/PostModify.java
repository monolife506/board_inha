package board.post;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

@WebServlet("/modify.do")
public class PostModify extends HttpServlet
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
        ServletContext context = getServletContext();

        int maxSize = 5 * 1024 * 1024;
        String realFolder = context.getRealPath("/static/img");
        String userId = (String) session.getAttribute("user.id");

        MultipartRequest multi = new MultipartRequest(req, realFolder, maxSize, "utf-8", new DefaultFileRenamePolicy());
        int postIdx = Integer.parseInt(multi.getParameter("idx"));

        try {
            PostDB postDB = new PostDB();
            Post post = postDB.getPost(postIdx);
            Enumeration files = multi.getFileNames();

            if (post.getAuthor().equals(userId)) {
                String title = multi.getParameter("title");
                int board = Integer.parseInt(multi.getParameter("board"));

                String newImgName = multi.getFilesystemName("img");
                String subImgName = multi.getFilesystemName("sub-img");
                String content = multi.getParameter("content");

                content = content.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
                content = Jsoup.clean(content, Whitelist.basic());

                if (newImgName != null) {
                    post.setImg(newImgName);
                    postDB.deleteImg(postIdx, realFolder);
                }

                if (subImgName != null) {
                    post.setSub_img(subImgName);
                    postDB.deleteSubImg(postIdx, realFolder);
                }

                post.setTitle(title);
                post.setBoard(board);
                post.setContent(content);

                postDB.modifyPost(postIdx, post);
            }
            postDB.close();
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/");
    }
}
