package board.member;

import board.util.ConPool;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// MemberDB: 사용자에 대한 DAO
public class MemberDB
{
    private Connection con;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public MemberDB() throws SQLException, NamingException { con = ConPool.getConnection(); }

    public void insertMember(Member user) throws SQLException
    {
        String hashedPwd = BCrypt.hashpw(user.getPwd(), BCrypt.gensalt());
        String sql = "INSERT INTO member(id, email, pwd) values(?, ?, ?)";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, hashedPwd);
        pstmt.executeUpdate();
    }

    public void updateMember(Member user) throws SQLException
    {
        String hashedPwd = BCrypt.hashpw(user.getPwd(), BCrypt.gensalt());
        String sql = "UPDATE member SET pwd=?, email=? WHERE id=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, hashedPwd);
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getId());
        pstmt.executeUpdate();
    }

    public void deleteMember(String id) throws SQLException
    {
        String sql = "DELETE FROM member WHERE id=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.executeUpdate();
    }

    // 중복된 Id 확인
    public boolean checkId(String id) throws SQLException
    {
        String sql = "SELECT * FROM member WHERE id=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        rs = pstmt.executeQuery();

        return rs.next();
    }

    // 중복된 이메일 존재 확인
    public boolean checkEmail(String email) throws SQLException
    {
        String sql = "SELECT * FROM member WHERE email=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        rs = pstmt.executeQuery();

        return rs.next();
    }

    public String getEmail(String id) throws SQLException
    {
        String sql = "SELECT email FROM member WHERE id=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        rs = pstmt.executeQuery();
        rs.next();

        return rs.getString(1);
    }

    // 비밀번호가 일치하는지 확인
    public boolean checkPwd(String id, String pwd) throws SQLException
    {
        String sql = "SELECT pwd FROM member WHERE id=?";

        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        rs = pstmt.executeQuery();
        rs.next();

        return BCrypt.checkpw(pwd, rs.getString("pwd"));
    }

    public void close() throws SQLException
    {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (con != null) con.close();
    }
}
