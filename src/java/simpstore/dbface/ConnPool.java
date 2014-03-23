/*
 * name: Adam Schloss
 * class: CMIS 440
 * date: 9 Oct 2012
 * 
 * Final Project
 * 
 * ConnPool singleton -- initializes and manages the connection pool
 * Based upon DBConnectionPool class from CMIS 440 Module 4, Section 3
 * 
 */
package simpstore.dbface;

import java.sql.*;
//import javax.naming.*;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;

public class ConnPool {

    private static ConnPool instance;
    private static OracleDataSource ds = null;
    private static final String DB_NAME = "jdbc/nova";

    protected ConnPool() throws SQLException { init(); }

    public static ConnPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnPool();
        }
        return instance;
    }

    private void init() throws SQLException {
        // JNDI lookup -- unfortunately broken
        /*try {
            Context initContext = new InitialContext();
            //Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) initContext.lookup(DB_NAME);
        } 
        catch (NamingException e) {
            System.err.println("Problem looking up " + DB_NAME + ": " + e.getMessage());
        }*/
        ds = new OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@nova.umuc.edu:1521:acad");
        ds.setUser("cm320a15");
        ds.setPassword("HoorayObfuscat10n");
    }

    public static Connection getConn() throws SQLException {
        instance = getInstance();
        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("Maximum number of connections in pool exceeded.");
        }
        return conn;
    }
}