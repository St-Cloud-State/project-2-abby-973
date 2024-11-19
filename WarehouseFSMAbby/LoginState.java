import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class LoginState extends WareState implements ActionListener{
  private static final int CUSTOMER_LOGIN = 0;
  private static final int SALESCLERK_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private WareContext context;
  private JFrame frame;
  private static LoginState instance;
  private AbstractButton clientButton, logoutButton, managerButton, salesclerkButton;
  //private ClerkButton clerkButton;
  private LoginState() {
      super();
      clientButton = new JButton("user");
      managerButton =  new JButton("clerk");
      logoutButton = new JButton("logout");
      clientButton.addActionListener(this);
      logoutButton.addActionListener(this);
      //clerkButton.addActionListener(this); */
 //     ((ClerkButton)clerkButton).setListener();
  }

  public static LoginState instance() {
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.clientButton)) 
       {//System.out.println("user \n"); 
         this.client();}
    else if (event.getSource().equals(this.logoutButton)) 
       (WareContext.instance()).changeState(3);
    else if (event.getSource().equals(this.managerButton)) 
       this.manager();
    else if (event.getSource().equals(this.salesclerkButton)) 
      this.salesclerk();
  } 

  public void clear() { //clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());   
  }  
  
  private void client(){
    int userId = getNumber("Input user password");
    if (Warehouse.instance().searchClient(userId) != null){
      (WareContext.instance()).setLogin(WareContext.IsClient);
      (WareContext.instance()).setUser(Integer.toString(userId));  
       //clear();
      (WareContext.instance()).changeState(0);
    }
    else 
      System.out.println("Invalid user id.");
  } 
  private void salesclerk(){
    (WareContext.instance()).setLogin(WareContext.IsClerk);
    (WareContext.instance()).changeState(1);
  } 
  private void manager(){
    (WareContext.instance()).setLogin(WareContext.IsManager);
    (WareContext.instance()).changeState(2);
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
  public void terminate()
  {
    (WareContext.instance()).changeState(3); // exit with a code 
  }
  public void run() {
    int choice = getNumber("Please enter state to login too: (0 for client, 1 for clerk, 2 for manager)");
    switch (choice) {
      case 0:
          client();
        break;
      case 1:
          salesclerk();
        break;
      case 2:
          manager();
        break;
      default:
        terminate();
        break;
    }
  }
}
