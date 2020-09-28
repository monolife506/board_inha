package board.like;

import board.util.ConPool;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeDB
{
    private Connection con;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public LikeDB() throws SQLException, NamingException { con = ConPool.getConnection(); }

    public boolean checkLikeAvailable(String user, int postIdx) throws SQLException
    {
        String sql1 = "SELECT toggle FROM `like` WHERE user=? AND post_idx=?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setString(1, user);
        pstmt.setInt(2, postIdx);
        rs = pstmt.executeQuery();

        return rs.next();
    }

    public boolean checkLike(String user, int postIdx) throws SQLException
    {
        String sql1 = "SELECT toggle FROM `like` WHERE user=? AND post_idx=?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setString(1, user);
        pstmt.setInt(2, postIdx);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getBoolean(1);
        } else {
            String sql2 = "INSERT INTO `like`(user, post_idx, toggle) VALUES(?, ?, ?)";
            pstmt = con.prepareStatement(sql2);
            pstmt.setString(1, user);
            pstmt.setInt(2, postIdx);
            pstmt.setBoolean(3, false);
            pstmt.executeUpdate();

            return false;
        }
    }

    public Like getLike(String user, int postIdx) throws SQLException
    {
        boolean curValue = checkLike(user, postIdx);

        Like like = new Like();
        like.setUser(user);
        like.setPost_idx(postIdx);
        like.setToggle(curValue);
        return like;
    }

    public void toggleLike(String user, int postIdx) throws SQLException
    {
        String sql = "UPDATE `like` SET toggle=? WHERE user=? AND post_idx=?";
        boolean curValue = checkLike(user, postIdx);

        pstmt = con.prepareStatement(sql);
        pstmt.setBoolean(1, !curValue);
        pstmt.setString(2, user);
        pstmt.setInt(3, postIdx);
        pstmt.executeUpdate();
    }

    public void close() throws SQLException
    {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (con != null) con.close();
    }
}
