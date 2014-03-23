/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpstore;

import simpstore.dbface.ProductDAO;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Adam
 */
@ManagedBean
@SessionScoped
public class SrchBean {
    private String category;
    private int min;
    private int max;
    private static final String FOUND_MSG = "We found the following products matching your search:";
    
    private ArrayList<Product> prods;
    private String status;
    private Product p;
    
    public SrchBean() {
        min = 0;
        max = 0;
    }
    
    public String getCategory() { return category; }

    public int getMin() { return min; }
    
    public int getMax() { return max; }
    
    public String getStatus() { return status; }
    
    public ArrayList<Product> getProducts() { return prods; }
    
    public Product getSelectedProduct() { return p; }
    
    public void setCategory(String newCat) { category = newCat; }
    
    public void setMin(int newAge) { 
        if(min > max && max != 0) {
            status = Product.AGE_ERROR;
        }
        min = newAge;
    }
    
    public void setMax(int newAge) { 
        if(min > max && max != 0) {
            status = Product.AGE_ERROR;
        }
        max = newAge; 
    }

    public String doSimpleSearch() {
        try {
            prods = ProductDAO.search(category, 0, 0, 0);
            status = FOUND_MSG;
        }
        catch(Exception e) {
            status = e.getMessage();
        }
        return "SearchResult";
    }
    
    public String doAdvSearch() {
        try {
            prods = ProductDAO.search(category, min, max, 0);
            status = FOUND_MSG;
        }
        catch (Exception e) {
            status = e.getMessage();
        }
        return "SearchResult";
    }
    
    public void doClear() {
        min = 0;
        max = 0;
    }
    
    public String moreInfo(Product p) {
        this.p = p;
        return "Product";
    }
}
