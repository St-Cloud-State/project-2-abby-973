import java.io.*;
import java.lang.*;

public class WaitListItem implements Serializable{

    private Client client;
    private int quantity;

    public WaitListItem(Client c, int q)
    {
        this.client = c;
        this.quantity = q;
    }
    public Client getClient() {
        return client;
    }
    public int getClientId() {
        return Integer.parseInt(client.getClientID());
    }
    public int getQuantity() {
        return quantity;
    }
    public String toString()
    {
        return "Client:"+client.getClientID()+" Quantity: " +quantity; 
    }
}