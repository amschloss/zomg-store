/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpstore;

/**
 *
 * @author Adam
 */
public class CartLine implements Cloneable {
    private static int availId = 0;
    
    private Product product;
    protected final int id;
    private int quantity;
    
    public Product getProduct() { return product; }
    
    private CartLine() { this(null); }
    
    public CartLine(Product p) {
        product = p;
        quantity = 1;
        id = availId++;
    }
    
    private CartLine(Product p, int q, int id) {
        product = p;
        quantity = q;
        this.id = id;
    }
    
    public int getQuantity() { return quantity; }
    
    public void setQuantity(int q) { quantity = q; }
    
    public double getExtPrice() { return product.getPrice() * quantity; }
    
    @Override
    public CartLine clone() {
        return new CartLine(product, quantity, id);
    }
}