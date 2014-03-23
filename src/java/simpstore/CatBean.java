/*
 * name: Adam Schloss
 * class: CMIS 440
 * date: 11 Oct 2012
 * 
 * Final Project
 * 
 * CatBean JSF managed bean
 * 
 */
package simpstore;

import simpstore.dbface.CategoryDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Adam
 */
@ManagedBean
@SessionScoped
public class CatBean {
    private String[] categories = null;
    private String status;
    
    /**
     * Creates a new instance of CatBean
     */
    public CatBean() {
        try {
            categories = CategoryDAO.readNames();
        }
        catch(Exception e) {
            status = e.getMessage();
        }
    }
    
    public String[] getCategories() { return categories; }
    
    public String getStatus() { return status; }
}
