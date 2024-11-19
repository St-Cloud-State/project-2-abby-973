import java.io.*;
//Inports the linked list java class
import java.util.*;
import java.text.DecimalFormat;

public class Client implements Serializable{
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_STRING = "C";
    // Declare all parameters of Client
    private String FirstName;
    private String LastName;
    private String Email;
    private String ClientID;
    private String Address;
    private String PhoneNumber;
    private double Balance;
    private WishList WishList;
    private List<Invoice> invoices = new LinkedList();
    //Constructor
    public Client(String FirstName, String LastName, String Email, String Address, String PhoneNumber) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.ClientID = CLIENT_STRING + (ClientIDServer.instance().getId());
        this.Address = Address;
        this.PhoneNumber = PhoneNumber;
        this.Balance = 0.00;
        this.WishList = new WishList(ClientID);
    }

    // Get functions for the parameters
    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public double getBalance()
    {
        return Balance;
    }

    public void setClientBalance(double amount)
    {
        this.Balance = amount;
    }

    public void addInvoice(Invoice I)
    {
        invoices.add(I);
    }

    public WishList getWishList() {
        return WishList;
    }
    
    public Iterator getInvoices()
    {
        return invoices.iterator();
    }

    public Invoice processOrder()
    {
        Invoice invoice = new Invoice();
        Product p;
        WishList userWishList = getWishList();
        List<WishListItem> wishlistItems = userWishList.getRealItems();
        int refSize = wishlistItems.size();
        for(int i = 0; i < refSize; i++)
        {
            OrderItem newOI;
            WishListItem w = wishlistItems.get(i);
            p = w.getProduct();
            if(p.getQuantityInStock() > w.getQuantity())
            {
                int remaining = p.getQuantityInStock() - w.getQuantity();
                newOI = new OrderItem(w.getProduct(), w.getQuantity());
                p.setStockAmount(remaining);
            }
            else
            {
                int overflow = w.getQuantity() - p.getQuantityInStock();
                WaitListItem wli = new WaitListItem(this, overflow);
                p.addToWaitlist(wli);
                newOI = new OrderItem(w.getProduct(), p.getQuantityInStock());
                p.setStockAmount(0);
            }
            invoice.addItem(newOI);
        }
        setClientBalance(Double.parseDouble(df.format(Balance - invoice.getPrice())));
        addInvoice(invoice);
        userWishList.clear();
        return invoice;
    }

    // Combine all parameters into one string
    @Override
    public String toString() {
        return "Client{" +
                "First Name:'" + FirstName + '\'' +
                ", Last Name:'" + LastName + '\'' +
                ", Email:'" + Email + '\'' +
                ", Client ID:'" + ClientID + '\'' +
                ", Address:'" + Address + '\'' +
                ", Phone Number:'" + PhoneNumber + '\'' +
                ", Balance:'"+Balance+ '\''+
                '}';

 }
}
 
 