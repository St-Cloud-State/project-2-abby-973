import java.util.*;
import java.text.*;
import java.io.*;
public class ManagerState extends WareState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WareContext context;
  private static ManagerState instance;
  private static final int EXIT = 0;
  private static final int ADD_PRODUCT = 1;
  private static final int DISPLAY_WAITLIST = 2;
  private static final int ADD_SHIPMENT = 3;
  private static final int BECOME_CLERK = 4;
  private static final int LOGOUT = 5;
  private static final int HELP = 6;
  private ManagerState() { 
      super();
      warehouse = Warehouse.instance();
      //context = WareContext.instance();
  }

  public static ManagerState instance() {
    if (instance == null) {
      instance = new ManagerState();
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
    System.out.println(ADD_PRODUCT + " to  add products");
    System.out.println(DISPLAY_WAITLIST + " to  show product waitlist");
    System.out.println(ADD_SHIPMENT + " to  add product shipment ");
    System.out.println(BECOME_CLERK + " to  become a salesclerk ");
    System.out.println(LOGOUT + " to  logout");
    System.out.println(HELP + " for help");
  }

  public void addProduct() {
    Product result;
    do {
      String name = getToken("Enter name");
      double price = Double.parseDouble(getToken("Enter price"));
      int quantity = Integer.parseInt(getToken("Enter quantity"));
      result = warehouse.addProduct(name, price, quantity);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Products could not be added");
      }
      if (!yesOrNo("Add more products?")) {
        break;
      }
    } while (true);
  }

  public void addStock() {
    int pid = Integer.parseInt(getToken("Enter product Id"));
    int amount = Integer.parseInt(getToken("Enter the amount of stock to add"));
    System.out.println("Stock Added:");
    System.out.println(warehouse.addStock(pid, amount).toString());
  }

  public void showWaitlist() {
    int pid = Integer.parseInt(getToken("Enter product Id"));
    Iterator w = warehouse.getWaitlist(pid); 
    while (w.hasNext()){
      WaitListItem wi = (WaitListItem)(w.next());
      System.out.println(wi.toString());
    }
    System.out.println("End of Waitlist");
  }
  public void becomeClerk()
  {
      (WareContext.instance()).changeState(1);
  }
  public void logout()
  {
    (WareContext.instance()).changeState(3);
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
        case ADD_PRODUCT:       addProduct();
                                break;
        case DISPLAY_WAITLIST:  showWaitlist();
                                break;
        case ADD_SHIPMENT:      addStock();
                                break;
        case BECOME_CLERK:      becomeClerk();
                                break;
        case LOGOUT:            logout();
                                break;
        case HELP:              help();
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
