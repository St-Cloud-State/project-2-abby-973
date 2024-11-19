import java.util.*;
import java.text.*;
import java.io.*;
public class ClientState extends WareState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WareContext context;
  private static ClientState instance;
  private static final int EXIT = 0;
  private static final int SHOW_DETAILS = 1;
  private static final int LIST_PRODUCTS = 2;
  private static final int SHOW_TRANSACTINOS = 3;
  private static final int ADD_TO_WISHLIST = 4;
  private static final int SHOW_WISHLIST = 5;
  private static final int PLACE_ORDER = 6;
  private static final int LOGOUT = 7;
  private static final int HELP = 8;
  private ClientState() { 
      super();
      warehouse = Warehouse.instance();
      //context = WareContext.instance();
  }

  public static ClientState instance() {
    if (instance == null) {
      instance = new ClientState();
    }
    return instance;
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and "+HELP+" as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(SHOW_DETAILS + " to  show deatils");
    System.out.println(LIST_PRODUCTS + " to  all products");
    System.out.println(SHOW_TRANSACTINOS + " to  list Transactions ");
    System.out.println(ADD_TO_WISHLIST + " to  add item to wishlist");
    System.out.println(SHOW_WISHLIST + " to  show wishlist");
    System.out.println(PLACE_ORDER + " to  place order");
    System.out.println(LOGOUT + " to  logout");
    System.out.println(HELP + " for help");
  }

  public void printClient() {
    Client result;
    String memberID = WareContext.instance().getUser();
    result = warehouse.searchClient(memberID);
    if (result == null) {
      System.out.println("Invalid Member ID");
    } else {
      System.out.println(result.toString());
    }
  }

  public void listProducts() {
    Iterator result;
    result = warehouse.getProducts();
    if (result == null) {
      System.out.println("Invalid Member ID");
    } else {
      while (result.hasNext()){
        Product product = (Product)(result.next());
        System.out.println(product.toString());
      }
      System.out.println("\n  There are no more transactions \n" );
    }
  }

  public void listInvoices() {
    Iterator result;
    String memberID = WareContext.instance().getUser();
    result = warehouse.getInvoices(Integer.parseInt(memberID));
    if (result == null) {
      System.out.println("Invalid Member ID");
    } else {
      while (result.hasNext()){
        Invoice ii = (Invoice)(result.next());
        ii.display();
      }
      System.out.println("\n  There are no more transactions \n" );
    }
  }
 
  public void addToWishList()
  {
    String memberID = WareContext.instance().getUser();
    String prodId = getToken("Enter product id");
    String quantity = getToken("How many would you like?");
    WishListItem result = warehouse.addToWishlist(Integer.parseInt(memberID), Integer.parseInt(prodId), Integer.parseInt(quantity));
    System.out.println(result.toString());
  }
  public void showWishList() {
    Iterator result;
    String memberID = WareContext.instance().getUser();
    Iterator w = warehouse.getWishlist(Integer.parseInt(memberID)); 
    while (w.hasNext()){
      WishListItem wi = (WishListItem)(w.next());
      System.out.println(wi.toString());
    }
  }
  public void placeOrder() {
    Iterator result;
    int cid = Integer.parseInt(WareContext.instance().getUser());
    Iterator w = warehouse.getWishlist(cid);
    while (w.hasNext())
    {
      WishListItem wi = (WishListItem)(w.next());
      System.out.println(wi.toString());
      if(!yesOrNo("Add Item to Order as is?"))
      {
        int amount = Integer.parseInt(getToken("Please enter a new amount of " + wi.getProduct().getName()+" (Enter 0 to remove)"));
        warehouse.updateWishlistItem(cid, Integer.parseInt(wi.getProduct().getProductID()), amount);
      }
    }
    Invoice i = warehouse.createOrder(cid);
    System.out.println("Order Invoice:");
    i.display();
  }

  public void logout()
  {
    var state = WareContext.instance().getLogin();
    if(state == 0)
    {
      (WareContext.instance()).changeState(3);
    }
    else
    {
      (WareContext.instance()).changeState(1);
    }
  }

  public void terminate(int exitcode)
  {
    (WareContext.instance()).changeState(exitcode); // exit with a code 
  }
 

  public void process() {
    int command, exitcode = -1;
    help();
    boolean done = false;
    while (!done) {
      switch (getCommand()) {
        case SHOW_DETAILS:        printClient();
                                break;
        case LIST_PRODUCTS:         listProducts();
                                break;
        case SHOW_TRANSACTINOS:     listInvoices();
                                break;
        case ADD_TO_WISHLIST:       addToWishList();
                                break;
        case SHOW_WISHLIST:         showWishList();
                                break;
        case PLACE_ORDER:           placeOrder();
                                break;
        case LOGOUT:                logout();
                                break;
        case HELP:                  help();
                                break;
        case EXIT:              exitcode = 0;
                                done = true; break;
      }
    }
    terminate(exitcode);
  }
  public void run() {
    process();
  }
}
