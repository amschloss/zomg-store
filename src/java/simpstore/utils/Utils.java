/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpstore.utils;

import java.sql.*;

public class Utils {

    public static void resClose(Connection c, Statement s, ResultSet r) throws Exception {
        try {
            if (r != null) {
                r.close();
            }
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            throw new Exception("Exception on close: " + e.getMessage());
        }
    }
    
}
