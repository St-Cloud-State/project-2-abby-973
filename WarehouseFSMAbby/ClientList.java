import java.io.*;
import java.util.*;

/**
 * Singleton class to manage a list of clients.
 */
public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients = new LinkedList();
    private static ClientList clientsList;

    // Private constructor to prevent instantiation
    private ClientList() {
    }

    // Method to get the singleton instance of ClientsList
    public static ClientList getInstance() {
        if (clientsList == null) {
            clientsList = new ClientList();
        }
        return clientsList;
    }

    // Method to add a customer to the list
    public boolean addClient(Client customer) {
        return clients.add(customer);
    }

    // Method to get an iterable list of clients
    public Iterator getClients() {
        return clients.iterator();
    }
    public Client find(int cid)
    {
        String findId = "C" + cid;
        for (int i=0; i < clients.size(); i++)
        {
            if (clients.get(i).getClientID().equals(findId)) 
            {
                return clients.get(i);
            }
        }
        return null;
    }

    // Method to serialize the object
    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
        output.writeObject(clientsList);
    }

    // Method to deserialize the object
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (clientsList == null) {
            clientsList = (ClientList) input.readObject();
        } else {
            input.readObject();
        }
    }

    // Method to return a string representation of the clients list
    public String toString() {
        return clients.toString();
    }
    public void displayClients() {
        System.out.println("Client List:");
        for (Client client : clients) {
            System.out.println(client.toString());
        }
    }

    public List<Client> getList()
    {
        return clients;
    }

    public Iterator getClient() {
        return clients.iterator();
    }
}