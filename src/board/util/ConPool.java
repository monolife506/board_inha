package board.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// JDBC Connection Pool
public class ConPool
{
    public static Connection getConnection() throws SQLException, NamingException {
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/boardDB");
        return ds.getConnection();
    }
}
