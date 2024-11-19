import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Invoice implements Serializable{
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private List<OrderItem> items = new LinkedList();
    private double totalPrice;

    public Invoice()
    {
        this.totalPrice = 0.00;
    }

    public boolean addItem(OrderItem item) {
        this.totalPrice += item.getPrice();
        return items.add(item);
    }

    public double getPrice()
    {
        return totalPrice;
    }
    public Iterator getOrderItems() {
        return items.iterator();
    }

    public String toString() {
        return items.toString() + " Total Price: " + df.format(this.getPrice());
    }
    public void display()
    {
        Iterator is = items.iterator();
        while (is.hasNext()) {
            System.out.println(is.next().toString());
        }
        System.out.println("Total Price: " + df.format(this.getPrice()));
    }
}
