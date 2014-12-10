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
                                    //user_menu();
                                    //This is basically a pause
                                    pause = reader.next();
                                    resultSet.close();                   
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
                                    //admin_menu();
                                    //This is basically a pause
                                    pause = reader.next();
                                    resultSet.close();                   
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