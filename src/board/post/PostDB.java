package board.post;

import board.util.ConPool;

import javax.naming.NamingException;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDB
{
    private Connection con;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public PostDB() throws SQLException, NamingException { con = ConPool.getConnection(); }

    // 게시글 삽입
    public void insertPost(Post post) throws SQLException
    {
        String sql = "INSERT INTO post(title, author, board, img, sub_img, content, updated_at) values(?, ?, ?, ?, ?, ?, ?)";
        pstmt = con.prepareStatement(sql);

        pstmt.setString(1, post.getTitle());
        pstmt.setString(2, post.getAuthor());
        pstmt.setInt(3, post.getBoard());
        pstmt.setString(4, post.getImg());
        pstmt.setString(5, post.getSub_img());
        pstmt.setString(6, post.getContent());
        pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();
    }

    // 게시글 수정
    public void modifyPost(int idx, Post post) throws SQLException
    {
        String sql = "UPDATE post SET title=?, board=?, img=?, sub_img=?, content=?, updated_at=? WHERE idx=?";
        pstmt = con.prepareStatement(sql);

        pstmt.setString(1, post.getTitle());
        pstmt.setInt(2, post.getBoard());
        pstmt.setString(3, post.getImg());
        pstmt.setString(4, post.getSub_img());
        pstmt.setString(5, post.getContent());
        pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        pstmt.setInt(7, idx);

        pstmt.executeUpdate();
    }

    // 게시글 삭제 (실제로 DB에서 지우지는 않음)
    public void deletePost(int idx) throws SQLException
    {
        String sql = "UPDATE post SET is_deleted=true WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);

        pstmt.executeUpdate();
    }

    // 사진 삭제
    public void deleteImg(int idx, String realFolder) throws SQLException
    {
        String sql = "SELECT img FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);
        rs = pstmt.executeQuery();
        rs.next();

        String oldImg = rs.getString(1);
        File file = new File(realFolder + "\\" + oldImg);
        file.delete();
    }

    // 보조 사진 삭제
    public void deleteSubImg(int idx, String realFolder) throws SQLException
    {
        String sql = "SELECT sub_img FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);
        rs = pstmt.executeQuery();
        rs.next();

        String oldImg = rs.getString(1);
        File file = new File(realFolder + "\\" + oldImg);
        file.delete();
    }

    // 특정 게시글 표시
    public Post getPost(int idx) throws SQLException
    {
        String sql = "SELECT * FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);

        rs = pstmt.executeQuery();
        rs.next();

        Post post = new Post();
        post.setIdx(rs.getInt(1));
        post.setTitle(rs.getString(2));
        post.setAuthor(rs.getString(3));
        post.setBoard(rs.getInt(4));
        post.setImg(rs.getString(5));
        post.setContent(rs.getString(6));
        post.setUpdated_at(rs.getTimestamp(7));

        post.setReply_cnt(rs.getInt(8));
        post.setUpvote_cnt(rs.getInt(9));
        post.setRead_cnt(rs.getInt(10));

        post.setSub_img(rs.getString(12));

        if (rs.getBoolean(11)) return null;
        else return post;
    }

    // 댓글 횟수 증가
    public void updateReplyCnt(int idx, boolean addReply) throws SQLException
    {
        String sql1 = "SELECT reply_cnt FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setInt(1, idx);
        rs = pstmt.executeQuery();
        rs.next();

        int cur_cnt = rs.getInt(1);
        String sql2 = "UPDATE post SET reply_cnt=? WHERE idx=?";
        pstmt = con.prepareStatement(sql2);
        pstmt.setInt(2, idx);

        if (addReply) pstmt.setInt(1, cur_cnt + 1);
        else pstmt.setInt(1, cur_cnt - 1);
        pstmt.executeUpdate();
    }

    // 글의 좋아요 횟수 증가 
    public void updateUpvoteCnt(int idx, boolean addUpvote) throws SQLException
    {
        String sql1 = "SELECT upvote_cnt FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setInt(1, idx);
        rs = pstmt.executeQuery();
        rs.next();

        int cur_cnt = rs.getInt(1);
        String sql2 = "UPDATE post SET upvote_cnt=? WHERE idx=?";
        pstmt = con.prepareStatement(sql2);
        pstmt.setInt(2, idx);

        if (addUpvote) pstmt.setInt(1, cur_cnt + 1);
        else pstmt.setInt(1, cur_cnt - 1);
        pstmt.executeUpdate();
    }

    // 글의 조회수 증가
    public void updateReadCnt(int idx) throws SQLException
    {
        String sql1 = "SELECT read_cnt FROM post WHERE idx=?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setInt(1, idx);
        rs = pstmt.executeQuery();
        rs.next();

        int cur_cnt = rs.getInt(1);
        String sql2 = "UPDATE post SET read_cnt=? WHERE idx=?";
        pstmt = con.prepareStatement(sql2);
        pstmt.setInt(1, cur_cnt + 1);
        pstmt.setInt(2, idx);
        pstmt.executeUpdate();
    }

    // 글의 목록 받기 
    public List<Post> getPostlist(int board, int page, int pageSize) throws SQLException
    {
        List<Post> postList = new ArrayList<Post>();

        if (board == 0) {
            String sql = "SELECT * FROM post WHERE is_deleted=false ORDER BY updated_at DESC LIMIT ?, ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, page * pageSize);
            pstmt.setInt(2, pageSize);
        } else {
            String sql = "SELECT * FROM post WHERE board=? AND is_deleted=false ORDER BY updated_at DESC LIMIT ?, ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board);
            pstmt.setInt(2, page * pageSize);
            pstmt.setInt(3, pageSize);
        }

        rs = pstmt.executeQuery();
        while (rs.next()) {
            Post post = new Post();
            post.setIdx(rs.getInt(1));
            post.setTitle(rs.getString(2));
            post.setAuthor(rs.getString(3));
            post.setBoard(rs.getInt(4));
            post.setImg(rs.getString(5));
            post.setContent(rs.getString(6));
            post.setUpdated_at(rs.getTimestamp(7));

            post.setReply_cnt(rs.getInt(8));
            post.setUpvote_cnt(rs.getInt(9));
            post.setRead_cnt(rs.getInt(10));

            postList.add(post);
        }

        return postList;
    }

    // 글의 개수 출력
    public int getPostCnt(int board) throws SQLException
    {
        if (board == 0) {
            String sql = "SELECT COUNT(*) FROM post WHERE is_deleted=false";
            pstmt = con.prepareStatement(sql);
        } else {
            String sql = "SELECT COUNT(*) FROM post WHERE board=? AND is_deleted=false";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board);
        }

        rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    // 글의 목록 받기 (검색)
    public List<Post> getPostlistBySearch(int board, int page, int pageSize, String searchStr, int searchType) throws SQLException
    {
        List<Post> postList = new ArrayList<Post>();

        String sql = "";
        if (board == 0) {
            switch (searchType) {
                case 1:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND (CONCAT(title, content)) LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 2:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND title LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 3:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND content LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 4:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND author LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + searchStr + "%");
            pstmt.setInt(2, page * pageSize);
            pstmt.setInt(3, pageSize);
        } else {
            switch (searchType) {
                case 1:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND board=? AND (CONCAT(title, content))LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 2:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND board=? AND title LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 3:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND board=? AND content LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
                case 4:
                    sql = "SELECT * FROM post WHERE is_deleted=false AND board=? AND author LIKE ? ORDER BY updated_at DESC LIMIT ?, ?";
                    break;
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board);
            pstmt.setString(2, "%" + searchStr + "%");
            pstmt.setInt(3, page * pageSize);
            pstmt.setInt(4, pageSize);
        }

        rs = pstmt.executeQuery();
        while (rs.next()) {
            Post post = new Post();
            post.setIdx(rs.getInt(1));
            post.setTitle(rs.getString(2));
            post.setAuthor(rs.getString(3));
            post.setBoard(rs.getInt(4));
            post.setImg(rs.getString(5));
            post.setContent(rs.getString(6));
            post.setUpdated_at(rs.getTimestamp(7));

            post.setReply_cnt(rs.getInt(8));
            post.setUpvote_cnt(rs.getInt(9));
            post.setRead_cnt(rs.getInt(10));

            postList.add(post);
        }

        return postList;
    }

    // 글의 개수 출력 (제목 + 내용 검색)
    public int getPostCntBySearch(int board, String searchStr, int searchType) throws SQLException
    {
        String sql = "";
        if (board == 0) {
            switch (searchType) {
                case 1:
                    sql = "SELECT COUNT(*) FROM post WHERE is_deleted=false AND title||content LIKE ?";
                    break;
                case 2:
                    sql = "SELECT COUNT(*) FROM post WHERE is_deleted=false AND title LIKE ?";
                    break;
                case 3:
                    sql = "SELECT COUNT(*) FROM post WHERE is_deleted=false AND content LIKE ?";
                    break;
                case 4:
                    sql = "SELECT COUNT(*) FROM post WHERE is_deleted=false AND author LIKE ?";
                    break;
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + searchStr + "%");
        } else {
            switch (searchType) {
                case 1:
                    sql = "SELECT COUNT(*) FROM post WHERE board=? AND is_deleted=false AND title||content LIKE ?";
                    break;
                case 2:
                    sql = "SELECT COUNT(*) FROM post WHERE board=? AND is_deleted=false AND title LIKE ?";
                    break;
                case 3:
                    sql = "SELECT COUNT(*) FROM post WHERE board=? AND is_deleted=false AND content LIKE ?";
                    break;
                case 4:
                    sql = "SELECT COUNT(*) FROM post WHERE board=? AND is_deleted=false AND author LIKE ?";
                    break;
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board);
            pstmt.setString(2, "%" + searchStr + "%");
        }

        rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public void close() throws SQLException
    {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (con != null) con.close();
    }
}
