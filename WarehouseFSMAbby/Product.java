import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
public class Product implements Serializable{
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final String PRODUCT_STRING = "P";
    private String productID;
    private String name;
    private double salePrice;
    private int quantityInStock;
    private WaitList waitList;

    public Product(String name, double salePrice, int initialQuantity) {
        this.productID = PRODUCT_STRING + ProductIDServer.getInstance().getProductID();
        this.name = name;
        this.salePrice = salePrice;
        this.quantityInStock = initialQuantity;
        this.waitList = new WaitList(ProductIDServer.getInstance().getProductID());
    }

    public void updateStock(int quantity) {
        this.quantityInStock += quantity;
    }

    public void setStockAmount(int quantity)
    {
        this.quantityInStock = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public boolean addItemToWaitList(WaitListItem wi)
    {
        waitList.add(wi);
        return true;
    }

    public int fulfillWaitList(int q)
    {
        Iterator items = this.waitList.getItems();
        int remaining = q;
        while (items.hasNext()) 
        {
            WaitListItem wli = (WaitListItem)items.next();
            if(remaining > 0 && wli.getQuantity() < remaining)
            {
                OrderItem oi = new OrderItem(this, wli.getQuantity());
                Invoice in = new Invoice();
                in.addItem(oi);
                Client c = wli.getClient();
                c.setClientBalance((Double.parseDouble(df.format(c.getBalance() - in.getPrice()))));
                c.addInvoice(in);
                remaining = remaining - wli.getQuantity();
                waitList.remove(wli);
            }
        }
        return remaining;
    }

    public boolean addToWaitlist(WaitListItem waitListItem)
    {
        waitList.add(waitListItem);
        return true;
    }

    public String toString() {
        return "Product{" +
                "Product ID: '" + productID + '\'' +
                ", Name: '" + name + '\'' +
                ", Sale Price: $" + String.format("%.2f", salePrice) +
                ", Quantity in Stock: " + quantityInStock +
                '}';
    }
}