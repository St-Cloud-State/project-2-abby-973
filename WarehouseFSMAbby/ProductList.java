import java.io.*;
import java.util.*;

public class ProductList implements Serializable{
    private static final long serialVersionUID = 1L;
    private LinkedList<Product> products;
    private static ProductList ProductList;

    public ProductList() {
        products = new LinkedList<>();
    }

    public LinkedList<Product> getList() {
        return products;
    }

    
    public static ProductList getInstance() {
        if (ProductList == null) {
            ProductList = new ProductList();
        }
        return ProductList;
        }

    public boolean addProduct(Product product) {
        return products.add(product);
    }

    public Iterator getProducts() {
        return products.iterator();
    }

    public Product find(int pID)
    {
        String findID = "P" + pID;
        for (int i=0; i < products.size(); i++)
        {
            if (products.get(i).getProductID().equals(findID))
            {
                return products.get(i);
            }
        }
        return null;
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
        output.writeObject(ProductList);
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (ProductList == null) {
            ProductList = (ProductList) input.readObject();
        } else {
            input.readObject();
        }
    }


    public void displayProducts() {
        System.out.println("The product List:");
        for (Product product : products) {
            System.out.println(product);
        }
    }
}