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
import java.util.Objects;

@WebServlet("/input.do")
public class PostInput extends HttpServlet
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
        String userID = (String) session.getAttribute("user.id");

        try {
            PostDB postDB = new PostDB();
            Post post = new Post();
            MultipartRequest multi = new MultipartRequest(req, realFolder, maxSize, "utf-8", new DefaultFileRenamePolicy());
            Enumeration files = multi.getFileNames();

            if (userID != null) {
                String title = multi.getParameter("title");
                int board = Integer.parseInt(multi.getParameter("board"));

                String img = (String) files.nextElement();
                String imgName = multi.getFilesystemName(img);
                String subImg = (String) files.nextElement();
                String subImgName = multi.getFilesystemName(subImg);

                String content = multi.getParameter("content");
                content = content.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
                content = Jsoup.clean(content, Whitelist.basic());

                post.setTitle(title);
                post.setAuthor(userID);
                post.setBoard(board);
                post.setImg(imgName);
                post.setSub_img(subImgName);
                post.setContent(content);

                Objects.requireNonNull(postDB).insertPost(post);
            }

            postDB.close();
        }
        catch (IOException | SQLException | NamingException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/");
    }
}

