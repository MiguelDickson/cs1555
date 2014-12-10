/*
 
  IMPORTANT (otherwise, your code may not compile)	
  Same as using sqlplus, you NEED TO SET oracle environment variables by 
  sourcing bash.env or tcsh.env
*/


import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
                    //needed by java for database connection and manipulation
                    
import java.util.Scanner;

                
public class Team10Project
{
  private Connection connection; //used to hold the jdbc connection to the DB
  private Statement statement; //used to create an instance of the connection
  private ResultSet resultSet; //used to hold the result of your query (if one
                               // exists)
  private String query;  //this will hold the query we are using
  private String username, password;

  public Team10Project()
  {
    int menu_option = -1;
    /*Making a connection to a DB causes certian exceptions.  In order to handle
    these, you either put the DB stuff in a try block or have your function
    throw the Execptions and handle them later.  For this demo I will use the
    try blocks*/
    username = "lmd90"; //This is your username in oracle
    password = "3964195"; //This is your password in oracle
    try{
      //Register the oracle driver.  This needs the oracle files provided
      //in the oracle.zip file, unzipped into the local directory and 
      //the class path set to include the local directory
      DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
      //This is the location of the database.  This is the database in oracle
      //provided to the class
      String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
      
      connection = DriverManager.getConnection(url, username, password); 
      //create a connection to DB on class3.cs.pitt.edu
   
    
    display_main_menu(); 
   
    connection.close();
    }
    catch(Exception Ex)  //What to do with any exceptions
    {
      System.out.println("Database error:  Machine Error: " +
            Ex.toString());
	Ex.printStackTrace();
    }
    
    System.out.println("Thanks for using BetterFutures!");
  }
  
  
  void browse_mutual_funds()
  {
       String pause;
       String line ="";
       String date ="";
       boolean quit = false;
       int option=-1;
       while (option!=0)
       {
           final String ANSI_CLS = "\u001b[2J";
           final String ANSI_HOME = "\u001b[H";
           System.out.print(ANSI_CLS + ANSI_HOME);
           System.out.flush();
           System.out.println("Browsing mutual funds!");
           System.out.println("Your options:");
           System.out.println("0: Logout");
           System.out.println("1: Print all mutual funds in alphabetical order:");
           //System.out.println("2: ");
           //System.out.println("3: ");
           //System.out.println("4: ");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           switch (option)
           { 
                case 1:
                      try{
                          PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MUTUALFUND ORDER BY NAME ASC"); 
                          resultSet = stmt.executeQuery();
                          if(resultSet.isBeforeFirst()) //Check whether that admin exists already
                          {
                             //resultSet.close();
                             System.out.print(ANSI_CLS + ANSI_HOME);
                             System.out.flush();
                             System.out.println("SYMBOL               NAME");
                             System.out.println("-------------------- ------------------------------");
                             System.out.println("DESCRIPTION");
                             System.out.println("-------------");
                             System.out.println("CATEGORY   DATE_CREATED");
                             System.out.println("---------- ---------------");
                             System.out.println("");
                             while(resultSet.next())
                             {
                                 line = "";
                                 date = "";
                                 line = resultSet.getString(1) + " " + resultSet.getString(2);
                                 System.out.println(line);
                                 line = "";
                                 line = resultSet.getString(3);
                                 System.out.println(line);
                                 line = "";
                                 line = resultSet.getString(4);
                                 date = (resultSet.getDate(5)).toString();
                                 line = line + " " + date;
                                 System.out.println(line);
                                 System.out.println("");
                             }
                             resultSet.close();
                             System.out.println("\n Results above. Type anything and hit Enter to continue!");
                             //This is basically a pause
                             quit = false;
                             pause = reader.next();
                             //resultSet.close();  
                             
                           }
                           else
                           {
                             System.out.println("For some reason there are no mutual funds in this database."); 
                             System.out.println("Type anything and hit Enter to continue!");
                             //This is basically a pause
                             quit = false;
                             pause = reader.next();
                             //resultSet.close();                               
                           }
                         }
                         catch(Exception Ex)
                         {
                          System.out.println("Error with the mutualfund table.  Machine Error: " + Ex.toString());
                         }
                
                case 0:
                default:
                break;
           }
       }
  
  
  
  }
  
  
  
  
  
  //Takes userlogin for various queries
  void user_menu(String userlogin)
  {
       String pause;
       boolean quit = false;
       int option=-1;
       while (option!=0)
       {
           final String ANSI_CLS = "\u001b[2J";
           final String ANSI_HOME = "\u001b[H";
           System.out.print(ANSI_CLS + ANSI_HOME);
           System.out.flush();
           System.out.println("Welcome to the user interface!");
           System.out.println("Your options:");
           System.out.println("0: Logout");
           System.out.println("1: Browse Mutual Funds:");
           //System.out.println("2: ");
           //System.out.println("3: ");
           //System.out.println("4: ");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           switch (option)
           { 
                case 1:
                        browse_mutual_funds();
                default:
                break;
           }
       }
  }
    
  void admin_menu()
  {
       String pause;
       boolean quit = false;
       boolean admin = false;
       String make_admin;
       String username;
       String password;
       String email;
       String address;
       String name;
       int option=-1;
       final String ANSI_CLS = "\u001b[2J";
       final String ANSI_HOME = "\u001b[H";
       
       while (option!=0)
       {
           System.out.print(ANSI_CLS + ANSI_HOME);
           System.out.flush();
           System.out.println("Welcome to the administrator interface!");
           System.out.println("Your options:");
           System.out.println("0: Logout");
           System.out.println("1: Add new user");
           //System.out.println("2: Update share quote");
           //System.out.println("3: Add a new fund");
           //System.out.println("4: Update date and time");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           switch (option)
           { 
           
              case 1:
                  try{
                       while (quit == false)
                        {                   
                          System.out.println("Please enter the new user's login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.nextLine();
                          if (!username.equals("QUIT") || username.length() < 10)
                          {
                             System.out.println("Please enter the new user's password (No more than 10 characters!) [Type QUIT to quit]:");
                             password = reader.nextLine();
                             if (!password.equals("QUIT") || password.length() < 10)
                             {
                                System.out.println("Please enter the new user's email (No more than 40 characters!) [Type QUIT to quit]:");
                                email = reader.nextLine();
                                if (!email.equals("QUIT") || email.length() < 40)
                                {
                                    System.out.println("Please enter the new user's address (No more than 30 characters!) [Type QUIT to quit]:");
                                    address = reader.nextLine();
                                        if (!address.equals("QUIT") || address.length() < 30)
                                        {
                                            System.out.println("Please enter the new user's name (No more than 20 characters!) [Type QUIT to quit]:");
                                            name = reader.nextLine();
                                            if (!name.equals("QUIT") || name.length() < 20)
                                            {
                                                System.out.println("Make this user an admin? Type 'YES' if so (all other answers are no) [Type QUIT to quit]:");
                                                make_admin = reader.nextLine();
                                                if (!make_admin.equals("QUIT"))
                                                {
                                                    if (make_admin.equals("YES"))
                                                    {
                                                      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ADMINISTRATOR WHERE LOGIN = ?"); 
                                                      stmt.setString(1, username);
                                                      resultSet = stmt.executeQuery();
                                                      if(!resultSet.isBeforeFirst()) //Check whether that admin exists already
                                                      {
                                                         resultSet.close();
                                                         stmt = connection.prepareStatement("INSERT INTO ADMINISTRATOR VALUES(?,?,?,?,?)");
                                                         stmt.setString(1, username);
                                                         stmt.setString(2, name);
                                                         stmt.setString(3, email);
                                                         stmt.setString(4, address);
                                                         stmt.setString(5, password);
                                                         stmt.executeUpdate();
                                                         
                                                         stmt = connection.prepareStatement("INSERT INTO CUSTOMER VALUES(?,?,?,?,?)");
                                                         stmt.setString(1, username);
                                                         stmt.setString(2, name);
                                                         stmt.setString(3, email);
                                                         stmt.setString(4, address);
                                                         stmt.setString(5, password);
                                                         stmt.setInt(6, 0);
                                                         stmt.executeUpdate();
                                                         
                                                         System.out.println("User succesfully added as administrator and customer!");
                                                         //This is basically a pause
                                                         quit = false;
                                                         pause = reader.next();
                                                         //resultSet.close();  
                                                      }
                                                      else
                                                      {
                                                        System.out.println("Sorry, an administrator with this login already exists!");
                                                        resultSet.close(); 
                                                        //user_menu();
                                                        //This is basically a pause
                                                        pause = reader.next();
                                                                         
                                                      }
                                                    }
                                                    else
                                                    {
                                                      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE LOGIN = ?"); 
                                                      stmt.setString(1, username);
                                                      resultSet = stmt.executeQuery();
                                                      if(!resultSet.isBeforeFirst()) //Check whether that admin exists already
                                                      {
                                                         stmt = connection.prepareStatement("INSERT INTO CUSTOMER VALUES(?,?,?,?,?)");
                                                         stmt.setString(1, username);
                                                         stmt.setString(2, name);
                                                         stmt.setString(3, email);
                                                         stmt.setString(4, address);
                                                         stmt.setString(5, password);
                                                         stmt.setInt(6, 0);
                                                         stmt.executeUpdate();
                                                         
                                                         System.out.println("User succesfully added as customer!");
                                                         //This is basically a pause
                                                         quit = false;
                                                         pause = reader.next();
                                                         //resultSet.close();  
                                                      }
                                                      else
                                                      {
                                                        System.out.println("Sorry, a user with this login already exists!");
                                                        resultSet.close(); 
                                                        //user_menu();
                                                        //This is basically a pause
                                                        pause = reader.next();
                                                                         
                                                      }
                                                    }
                                                }
                                                else
                                                {
                                                    break;
                                                
                                                }
                                            }
                                            else
                                            {
                                            System.out.println("Sorry, invalid name!");
                                            //This is basically a pause
                                            pause = reader.next();          
                                            
                                            }
                                        }
                                        else
                                        {
                                        System.out.println("Sorry, invalid address!");
                                        //This is basically a pause
                                        pause = reader.next();  
                                        }
                                }
                                else
                                {
                                   System.out.println("Sorry, invalid email!");
                                   //This is basically a pause
                                   pause = reader.next();                                         
                                }
                             }
                             else
                             {
                                System.out.println("Sorry, invalid password!");
                                //This is basically a pause
                                pause = reader.next();                                         
                             }
                             
                          }
                          else
                          {
                                System.out.println("Sorry, invalid username!");
                                //This is basically a pause
                                pause = reader.next();                                         
                          }
                      }
                   }
                   catch(Exception Ex)
                   {
                    System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
                   }
                   break;   
                     
             case 2:
             
             case 3:
             
             
             
             
             
             
             
             
             case 0:
             default:
             break;
             
           }                     
      } 
       
   }    
       
  
  void display_main_menu()
  {
    String pause;
    String username ="";
    String password ="";
    boolean quit = false;
    int option=-1;
    int user_counter = 0;
    final String ANSI_CLS = "\u001b[2J";
    final String ANSI_HOME = "\u001b[H";
    
    while (option!=0)
    {    
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
        System.out.println("Welcome to the Team10 BetterFutures database!");
        System.out.println("Your options:");
        System.out.println("0: Logout");
        System.out.println("1: Login as user");
        System.out.println("2: Login as administrator");
        Scanner reader = new Scanner(System.in);
        //reader.nextLine();
        option = reader.nextInt();
        switch (option)
        { case 1:
               try{
                    statement = connection.createStatement(); //create an instance
                    query = "SELECT * FROM CUSTOMER"; 
                    resultSet = statement.executeQuery(query);
                    if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                    {
                        System.out.println("No customers registered in BetterFutures!");
                        //This is basically a pause
                        pause = reader.next();
                        resultSet.close();  
                    }
                    else
                    {
                        resultSet.close();  
                        while (quit == false)
                        {                   
                          System.out.println("Please enter your login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.next();
                          if (!username.equals("QUIT"))
                          {
                          System.out.println("Please enter your password (No more than 20 characters!) [Type QUIT to quit]:");
                          password = reader.next();
                          }
                          if (username.length() > 10 || password.length() > 20 || username.equals("QUIT") || password.equals("QUIT") )
                          {
                              if (username.equals("QUIT") || password.equals("QUIT"))
                                 quit = true;
                              else
                              {
                              System.out.println("Incorrect username/password!!");
                              //This is basically a pause
                              pause = reader.next();
                              resultSet.close();  
                              quit = false;
                              }
                          }
                          else
                          {
                                 try{
                                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE LOGIN = ? AND PASSWORD =?"); 
                                 stmt.setString(1, username);
                                 stmt.setString(2, password);
                                 resultSet = stmt.executeQuery(); //Query about the customer
                                 if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                                    {
                                     System.out.println("Incorrect username/password!!");
                                     //This is basically a pause
                                     quit = false;
                                     pause = reader.next();
                                     resultSet.close();  
                                    }
                                 else
                                    {
                                    System.out.println("Go to user menu! Should be successful login!");
                                    resultSet.close();  
                                    option = -1;
                                    user_menu(username);
                                    quit = true;
                                    //This is basically a pause
                                    //pause = reader.next();
                                                      
                                    }
                                 }
                                 catch(Exception Ex)  //What to do with any exceptions
                                 {
                                     System.out.println("Database error:  Machine Error: " +
                                     Ex.toString());
                                     Ex.printStackTrace();
                                 }
                                 
                          }
                        }    
                    }
               
                }
                
                catch(Exception Ex)
                {
                 System.out.println("Error with query checking customer table.  Machine Error: " + Ex.toString());
                }
                         
                break;
                
         case 2: 
                try{
                    statement = connection.createStatement(); //create an instance
                    query = "SELECT * FROM ADMINISTRATOR"; 
                    resultSet = statement.executeQuery(query);
                    if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                    {
                        System.out.println("No administrator registered in BetterFutures!");
                        //This is basically a pause
                        pause = reader.next();
                        resultSet.close();  
                    }
                    else
                    {
                        resultSet.close();  
                        while (quit == false)
                        {                   
                          System.out.println("Please enter your login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.next();
                          if (!username.equals("QUIT"))
                          {
                          System.out.println("Please enter your password (No more than 20 characters!) [Type QUIT to quit]:");
                          password = reader.next();
                          }
                          if (username.length() > 10 || password.length() > 20 || username.equals("QUIT") || password.equals("QUIT") )
                          {
                              if (username.equals("QUIT") || password.equals("QUIT"))
                                 quit = true;
                              else
                              {
                              System.out.println("Incorrect username/password!!");
                              //This is basically a pause
                              pause = reader.next();
                              resultSet.close();  
                              quit = false;
                              }
                          }
                          else
                          {
                                 try{
                                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ADMINISTRATOR WHERE LOGIN = ? AND PASSWORD =?"); 
                                 stmt.setString(1, username);
                                 stmt.setString(2, password);
                                 resultSet = stmt.executeQuery(); //Query about the customer
                                 if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                                    {
                                     System.out.println("Incorrect username/password!!");
                                     //This is basically a pause
                                     quit = false;
                                     pause = reader.next();
                                     resultSet.close();  
                                    }
                                 else
                                    {
                                    System.out.println("Go to administrator menu! Should be successful login!");
                                    resultSet.close(); 
                                    admin_menu();
                                    option = -1;
                                    //This is basically a pause
                                    //pause = reader.next();
     
                                    break;
                                    }
                                 }
                                 catch(Exception Ex)  //What to do with any exceptions
                                 {
                                     System.out.println("Database error:  Machine Error: " +
                                     Ex.toString());
                                     Ex.printStackTrace();
                                 }
                                 
                          }
                        }    
                    }
               
                }
                
                catch(Exception Ex)
                {
                 System.out.println("Error with query checking customer table.  Machine Error: " + Ex.toString());
                }
                         
                break;
         case 0:
         default:
         break;
     }         
                
                
      
    }
  }
  
  public static void main(String args[])
  {
    Team10Project demo = new Team10Project();
  }
}