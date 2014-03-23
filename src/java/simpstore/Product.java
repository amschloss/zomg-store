/*
 * name: Adam Schloss
 * class: CMIS 440
 * date: 11 Oct 2012
 * 
 * Final Project
 * 
 * Product object / JSF managed bean
 * Encapsulates the Product object and its attributes
 * Also provides functionality linking to the Product DAO
 * 
 */
package simpstore;

import simpstore.dbface.ProductDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="prodBean")
@RequestScoped
public class Product {
    // Object fields
    private int id;
    private String category;
    private String productName;
    private String description;
    private String imageURL;
    private double price;
    private int minAge;
    private int maxAge;
    private int invCount;
    
    // Managed bean fields
    private String status;
    public static final String AGE_ERROR = "Minimum age must be less than maximum age.";
    
    // Object getter/setters
    public int getID() { return id; }
    
    public String getCategory() { return category; }
    
    public String getProductName() { return productName; }
    
    public String getDescription() { return description; }
    
    public String getImageURL() { return imageURL; }
    
    public double getPrice() { return price; }
    
    public int getMinAge() { return minAge; }
    
    public int getMaxAge() { return maxAge; }
    
    public int getInvCount() { return invCount; }
    
    public void setID(int newID) { id = newID; }
    
    public void setCategory(String newCat) { category = newCat; }
    
    public void setProductName(String newName) { productName = newName; }
    
    public void setDescription(String newDesc) { description = newDesc; }
    
    public void setImageURL(String newURL) { imageURL = newURL; }
    
    public void setPrice(double newPrice) { price = newPrice; }
    
    public void setMinAge(int newAge) { 
        if(minAge > maxAge && maxAge != 0) {
            status = AGE_ERROR;
        }
        minAge = newAge;
    }
    
    public void setMaxAge(int newAge) { 
        if(minAge > maxAge && maxAge != 0) {
            status = AGE_ERROR;
        }
        maxAge = newAge; 
    }
    
    public void setInvCount(int newCt) { invCount = newCt; }
    
    // Managed bean methods
    public String getStatus() { return status; }
    
    // Constructor 
    public Product() { doClear(); }
    
    // Inserts a new product into the database
    public void doInsert() {
        try {
            ProductDAO.insert(this);
            status = "Product added to the database";
        }
        catch(Exception e) {
            status = e.getMessage();
        }
    }
    
    // Deletes the product
    public void doDelete() {
        try {
            ProductDAO.delete(id);
            doClear();
            status = "Product removed from the database";
        }
        catch(Exception e) {
            status = e.getMessage();
        }
    }
    
    // Updates the product
    public void doUpdate() {
        try {
            ProductDAO.update(this);
            status = "Product listing updated";
        }
        catch(Exception e) {
            status = e.getMessage();
        }
    }
    
    // Searches for a product by ID
    public void doSearch() {
        try {
            java.util.ArrayList<Product> l = ProductDAO.search("", 0, 0, id);
            if(l.size() > 1)
                status = "Your search has found more than one product. Please be more specific.";
            else if(l.size() < 1) {
                doClear();
                status = "Your search did not return any products. Please try again.";
            }
            else {
                Product p = l.get(0);
                id = p.id;
                category = p.category;
                productName = p.productName;
                description = p.description;
                imageURL = p.imageURL;
                price = p.price;
                minAge = p.minAge;
                maxAge = p.maxAge;
                invCount = p.invCount;
            }
        }
        catch (Exception e) {
            status = e.getMessage();
        }
    }
    
    // Clears the input fields
    public void doClear() {
        id = 0;
        category = "";
        productName = "";
        description = "";
        imageURL = "";
        price = 0;
        minAge = 0;
        maxAge = 0;
        invCount = 0;
        status = "";
    }
    
    // Logs out and begins a new session
    public String doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "Home";
    }
}
