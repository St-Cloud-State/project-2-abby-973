import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private ClientList clientList;
    private ProductList productList;
    private static Warehouse warehouse;

    private Warehouse(){
        clientList = ClientList.getInstance();
        productList = ProductList.getInstance();
    }

public static Warehouse instance() {
    if (warehouse == null) {
      ClientIDServer.instance(); // instantiate all singletons
      ProductIDServer.getInstance();
      return (warehouse = new Warehouse());
    } else {
      return warehouse;
    }
  }
  public Client addClient(String FirstName, String LastName, String Email, String Address, String PhoneNumber) {
    Client client = new Client(FirstName, LastName, Email, Address, PhoneNumber);
    if (clientList.addClient(client)) {
      return (client);
    }
    return null;
  }
  public Product addProduct(String name, double salePrice, int initialQuantity) {
    Product product = new Product(name, salePrice, initialQuantity);
    if (productList.addProduct(product)) {
      return (product);
    }
    return null;
  }

  public Iterator getClients() {
      return clientList.getClient();
  }

  public Iterator getProducts() {
      return productList.getProducts();
  }

  public WishListItem addToWishlist(int cID, int pID, int initialQuantity)
  {
    WishList userWishlist = clientList.find(cID).getWishList();
    WishListItem item = new WishListItem(productList.find(pID), initialQuantity); 
    userWishlist.add(item);
    return item;
  }
  public Iterator getWishlist(int cID)
  {
    WishList userWishlist = clientList.find(cID).getWishList();
    return userWishlist.getItems();
  }
  public Iterator getInvoices(int cid)
  {
    return clientList.find(cid).getInvoices();
  }
  public Iterator getWaitlist(int pid)
  {
    WaitList productWaitlist = productList.find(pid).getWaitList();
    return productWaitlist.getItems();
  }

  public Product addStock(int pId, int quantity)
  {
    Product p = productList.find(pId);
    int remaining = p.fulfillWaitList(quantity);
    if(remaining > 0)
    {
      p.setStockAmount((p.getQuantityInStock() + remaining));
    }
    return p;
  }
  public Client addPayment(int cId, double a)
  {
    Client c = clientList.find(cId);
    c.setClientBalance(c.getBalance() + a);
    return c;
  }
  public void updateWishlistItem(int cid, int pid, int quantity)
  {
    WishList userWishlist = clientList.find(cid).getWishList();
    if(quantity > 0)
    {
      WishListItem wi = new WishListItem(productList.find(pid), quantity);
      if(!userWishlist.updateWishlistItem(wi))
      {
        System.out.println("Item was unable to be updated.");
      }
    }
    else
    {
      userWishlist.remove(userWishlist.find(pid));
    }
  }

  public Invoice createOrder(int cid)
  {
    return clientList.find(cid).processOrder();
  }
  public Client searchClient(String cId) {
    return clientList.find(Integer.parseInt(cId));
  }
  public Client searchClient(int cId) {
    return clientList.find(cId);
  }
  public static Warehouse retrieve() {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
      ClientIDServer.retrieve(input);
      return warehouse;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return null;
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
      return null;
    }
  }
  public static  boolean save() {
    try {
      FileOutputStream file = new FileOutputStream("WarehouseData");
      ObjectOutputStream output = new ObjectOutputStream(file);
      output.writeObject(warehouse);
      output.writeObject(ClientIDServer.instance());
      output.writeObject(ProductIDServer.getInstance());
      return true;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(warehouse);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      input.defaultReadObject();
      if (warehouse == null) {
        warehouse = (Warehouse) input.readObject();
      } else {
        input.readObject();
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  public String toString() {
    return clientList + "\n" + productList;
  } 
}

