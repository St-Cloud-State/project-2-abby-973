import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.LinkedList;
public class WaitList implements Serializable{
    private int productId;
    private List<WaitListItem> items  = new LinkedList<WaitListItem>();
    
    //constructor
    public WaitList(int pId)
    {
        this.productId = pId;
    }
    //get operations
    public int getProductId() {
        return productId;
    }
    public Iterator getItems() {
        return items.iterator();
    }
    public void setProductId(int pId) {
        this.productId = pId;
    }
    public boolean add(WaitListItem w)
    {
        items.add(w);
        return true;
    }

    public boolean remove(WaitListItem w)
    {
        items.remove(w);
        return true;
    }

    public WaitListItem find(int cid)
    {
        for (int i=0; i < items.size(); i++)
        {
            if (items.get(i).getClientId() == cid) 
            {
                return items.get(i);
            }
        }
        return null;
    }

    public boolean updateWaitListItem(WaitListItem newWI)
    {
        WaitListItem oldItem = find(newWI.getClientId());
        if(remove(oldItem) && add(newWI))
        {
            return true;
        }
        return false;
    }

    public String toString()
    {
        return items.toString();
    }
}
