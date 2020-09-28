package board.reply;

import board.util.ConPool;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplyDB
{
    private Connection con;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public ReplyDB() throws SQLException, NamingException { con = ConPool.getConnection(); }

    // 댓글 삽입
    public void insertReply(Reply reply) throws SQLException
    {
        String sql = "INSERT INTO reply(post_idx, author, content, updated_at) values(?, ?, ?, ?)";
        pstmt = con.prepareStatement(sql);

        pstmt.setInt(1, reply.getPost_idx());
        pstmt.setString(2, reply.getAuthor());
        pstmt.setString(3, reply.getContent());
        pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();
    }

    // 특정 댓글 표시
    public Reply getReply(int idx) throws SQLException
    {
        String sql = "SELECT * FROM reply WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);

        rs = pstmt.executeQuery();
        rs.next();

        Reply reply = new Reply();
        reply.setIdx(rs.getInt(1));
        reply.setPost_idx(rs.getInt(2));
        reply.setAuthor(rs.getString(3));
        reply.setContent(rs.getString(4));
        reply.setUpdated_at(rs.getTimestamp(5));
        
        if (rs.getBoolean(6)) return null;
        else return reply;
    }

    // 댓글 수정
    public void modifyReply(int idx, Reply reply) throws SQLException
    {
        String sql = "UPDATE reply SET content=?, updated_at=? WHERE idx=?";
        pstmt = con.prepareStatement(sql);

        pstmt.setString(1, reply.getContent());
        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        pstmt.setInt(3, idx);

        pstmt.executeUpdate();
    }

    // 댓글 삭제 (실제로 DB에서 지우지는 않음)
    public void deleteReply(int idx) throws SQLException
    {
        String sql = "UPDATE reply SET is_deleted=true WHERE idx=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idx);

        pstmt.executeUpdate();
    }

    // 댓글 목록 받기
    public List<Reply> getReplylist(int postIdx, int page, int pageSize) throws SQLException
    {
        List<Reply> replyList = new ArrayList<Reply>();

        String sql = "SELECT * FROM reply WHERE post_idx=? AND is_deleted=false ORDER BY updated_at DESC LIMIT ?, ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, postIdx);
        pstmt.setInt(2, page * pageSize);
        pstmt.setInt(3, pageSize);

        rs = pstmt.executeQuery();
        while (rs.next()) {
            Reply reply = new Reply();
            reply.setIdx(rs.getInt(1));
            reply.setPost_idx(rs.getInt(2));
            reply.setAuthor(rs.getString(3));
            reply.setContent(rs.getString(4));
            reply.setUpdated_at(rs.getTimestamp(5));

            replyList.add(reply);
        }

        return replyList;
    }

    // 댓글 개수 출력
    public int getReplyCnt(int postIdx) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM reply WHERE post_idx=? AND is_deleted=false";
        pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, postIdx);

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
