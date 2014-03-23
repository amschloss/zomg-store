/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpstore;

import simpstore.dbface.ProductDAO;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Adam
 */
@ManagedBean(name = "cart")
@SessionScoped
public class ShopCart {

    private ArrayList<CartLine> cart;
    private String status;

    public ShopCart() {
        cart = new ArrayList<CartLine>();
    }

    public ArrayList<CartLine> getCart() {
        return cart;
    }

    public String getStatus() {
        return status;
    }

    // Searches for a product within the cart by ID
    // Returns the location within the backing ArrayList if found
    // Returns -1 if not found
    private int contains(int id) {
        int index = -1;
        for (int i = 0; i < cart.size(); i++) {
            if (id == cart.get(i).getProduct().getID()) {
                index = i;
                break;
            }
        }
        return index;
    }

    // Adds a product to the cart
    // Uses the standard constructor; default quantity is 1
    public String addToCart(Product p) {
        int index = contains(p.getID());
        CartLine cl;
        if (index >= 0) {
            cl = cart.remove(index);
            cl.setQuantity(cl.getQuantity() + 1);
        } else {
            cl = new CartLine(p);
        }
        cart.add(cl);
        return "ViewCart";
    }

    // Updates a cart line
    // 
    public void updateLine(CartLine cl) {
        for (CartLine tmp : cart) {
            if (cl.id == tmp.id) {
                cart.remove(tmp);
                tmp = cl.clone();
                if (tmp.getQuantity() > 0) {
                    cart.add(tmp);
                }
                break;
            }
        }
    }

    public String submitCart() {
        int len = cart.size();
        int[] ids = new int[len];
        int[] quants = new int[len];
        for (int i = 0; i < len; i++) {
            ids[i] = cart.get(i).getProduct().getID();
            quants[i] = cart.get(i).getQuantity();
        }
        try {
            ProductDAO.batchUpdate(ids, quants);
            return "ThankYou";
        } catch (Exception e) {
            status = e.getMessage();
            return "";
        }
    }
    
    // Clears the shopping cart
    public String clear() {
        // Invalidate the session -- excessive? maybe, but gets the job done
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "Home";
    }
}
