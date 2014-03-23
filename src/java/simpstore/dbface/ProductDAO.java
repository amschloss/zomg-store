/*
 * name: Adam Schloss
 * class: CMIS 440
 * date: 9 Oct 2012
 * 
 * Final Project
 * 
 * ProductDAO database interface class
 * Based upon DAO classes from CMIS 440 Module 4, Section 3
 * 
 */
package simpstore.dbface;

import simpstore.Product;
import simpstore.utils.Utils;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    private static final String INSERT_STMT = "INSERT INTO Product "
            + "(productID,category,productName,description,imageURL,price,minAge,maxAge,inventory)"
            + " VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPD_STMT = "UPDATE Product SET productName = ?,"
            + " description = ?, imageURL = ?, price = ?, minAge = ?, maxAge = ?,"
            + " inventory = ? WHERE productID = ?";
    private static final String SRCH_GEN = "SELECT * FROM Product WHERE category = ? AND minAge >= ? AND maxAge <= ?";
    private static final String SRCH_ID = "SELECT * FROM Product WHERE productID = ?";

    // Generate a Product ID using the sequence in the database
    // Thanks, Professor Teodosescu
    private static int nextID(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ProdSeq.nextval FROM DUAL");
        int newID = 0;
        if (rs.next()) {
            newID = rs.getInt(1);
        } else {
            throw new RuntimeException("Unable to generate primary key value for new Product.");
        }
        return newID;
    }

    // Insert a new Product into the database
    public static void insert(Product prod) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnPool.getConn();
            stmt = conn.prepareStatement(INSERT_STMT);
            int id = nextID(conn);
            stmt.setInt(1, id);
            stmt.setString(2, prod.getCategory());
            stmt.setString(3, prod.getProductName());
            stmt.setString(4, prod.getDescription());
            stmt.setString(5, prod.getImageURL());
            stmt.setDouble(6, prod.getPrice());
            stmt.setInt(7, prod.getMinAge());
            stmt.setInt(8, prod.getMaxAge());
            stmt.setInt(9, prod.getInvCount());
            stmt.executeUpdate();
            prod.setID(id);
        } // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on product insert: " + se.getMessage());
        } catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        } finally {
            Utils.resClose(conn, stmt, null);
        }
    }

    // Update a Product that already exists in the database
    public static void update(Product prod) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnPool.getConn();
            stmt = conn.prepareStatement(UPD_STMT);
            stmt.setInt(8, prod.getID());
            stmt.setString(1, prod.getProductName());
            stmt.setString(2, prod.getDescription());
            stmt.setString(3, prod.getImageURL());
            stmt.setDouble(4, prod.getPrice());
            stmt.setInt(5, prod.getMinAge());
            stmt.setInt(6, prod.getMaxAge());
            stmt.setInt(7, prod.getInvCount());
            stmt.executeUpdate();
        } // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on product update: " + se.getMessage());
        } catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        } finally {
            Utils.resClose(conn, stmt, null);
        }
    }

    // Perform a batch update of inventory levels
    // Parameters prodID and inventory must have the same length
    // prodID: a list of the product IDs to be updated
    // inventory: the inventory levels for the respective products
    public static void batchUpdate(int[] prodID, int[] inventory) throws Exception {
        // this should never get thrown but is here just in case
        if (prodID.length != inventory.length) {
            throw new Exception("List of product IDs must be as long as list of new inventory counts.");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Establish DB connection
            conn = ConnPool.getConn();
            stmt = conn.prepareStatement("UPDATE Product SET inventory = ? WHERE productID = ?");
            conn.setAutoCommit(false);

            for (int i = 0; i < prodID.length; i++) {
                stmt.setInt(1, inventory[i]);
                stmt.setInt(2, prodID[i]);
                stmt.addBatch();
            }

            int[] counts = stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        } // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on batch update: " + se.getMessage());
        } catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        } finally {
            Utils.resClose(conn, stmt, null);
        }
    }

    // Delete a Product as specified by prodID
    public static void delete(int prodID) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnPool.getConn();
            stmt = conn.prepareStatement("DELETE FROM Product WHERE productID = ?");
            stmt.setInt(1, prodID);
            stmt.executeUpdate();
        } // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on product delete: " + se.getMessage());
        } catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        } finally {
            Utils.resClose(conn, stmt, null);
        }
    }

    // Search the Product table
    // Searches by category, minimum and maximum recommended age, or by product ID
    // A nonzero ID will override the presence of anything else
    public static ArrayList<Product> search(String cat, int min, int max, int id) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<Product> result = new ArrayList<Product>();
        ResultSet rs = null;
        try {
            conn = ConnPool.getConn();
            if (max <= 0) {
                max = Integer.MAX_VALUE;
            }
            if (id > 0) {
                stmt = conn.prepareStatement(SRCH_ID);
                stmt.setInt(1, id);
            } else {
                stmt = conn.prepareStatement(SRCH_GEN);
                stmt.setString(1, cat);
                stmt.setInt(2, min);
                stmt.setInt(3, max);
            }
            rs = stmt.executeQuery();
            Product p;
            while (rs.next()) {
                p = new Product();
                p.setID(rs.getInt("productID"));
                p.setProductName(rs.getString("productName"));
                p.setCategory(rs.getString("category"));
                p.setDescription(rs.getString("description"));
                p.setImageURL(rs.getString("imageURL"));
                p.setPrice(rs.getDouble("price"));
                p.setMinAge(rs.getInt("minAge"));
                p.setMaxAge(rs.getInt("maxAge"));
                p.setInvCount(rs.getInt("inventory"));
                result.add(p);
            }
        } // Handle exceptions
        catch (SQLException se) {
            throw new Exception(
                    "Database error on product search: " + se.getMessage());
        } catch (Exception e) {
            throw new Exception("Other exception: " + e.getMessage());
        } finally {
            Utils.resClose(conn, stmt, rs);
            return result;
        }
    }
}