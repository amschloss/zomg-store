/*
 * name: Adam Schloss
 * class: CMIS 440
 * date: 9 Oct 2012
 * 
 * Final Project
 * 
 * CategoryDAO database interface class
 * 
 */
package simpstore.dbface;

import java.sql.*;
import javax.sql.rowset.CachedRowSet;

public class CategoryDAO {
    public static String[] readNames() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        String[] names = null;
        CachedRowSet rs = new com.sun.rowset.CachedRowSetImpl();
        int i = 0;
        
        try {
            conn = ConnPool.getConn();
            stmt = conn.createStatement();
            rs.populate(stmt.executeQuery("SELECT * FROM Category"));
            names = new String[rs.size()];
            while(rs.next()) {
                names[i++] = rs.getString(1);
            }
        }
        // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on category read: " + se.getMessage());
        }
        catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        }
        finally {
            simpstore.utils.Utils.resClose(conn, stmt, rs);
            return names;
        }
    }
}
