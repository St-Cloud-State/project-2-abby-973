import java.io.*;
import java.lang.*;

public class WishListItem implements Serializable{

    private Product product;
    private int Quantity;

    public WishListItem(Product p, int q)
    {
        this.product = p;
        this.Quantity = q;
    }
    //Get the product 
    public Product getProduct() {
        return product;
    }
    //Get the product ID
    public String getProductId() {
        return product.getProductID();
    }
    //Get the quantity 
    public int getQuantity() {
        return Quantity;
    }
    //Combine into one string 
    public String toString()
    {
        return "The product: " +product.getName()+ " Product quantity: " +Quantity; 
    }
}