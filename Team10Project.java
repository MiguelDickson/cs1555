/*
 ----------------------------------------------------------------------------------------------------
--- Name: John Lee, Miguel Dickson (Group 10)
--- Pitt ID: JOL59, LMD90
---
----------------------------------------------------------------------------------------------------
  IMPORTANT (otherwise, your code may not compile)	
  Same as using sqlplus, you NEED TO SET oracle environment variables by 
  sourcing bash.env or tcsh.env
*/


import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
                    //needed by java for database connection and manipulation
                    
import java.util.Scanner;
import java.lang.*;
import java.lang.Integer;
import java.text.SimpleDateFormat;
                
public class Team10Project
{
  private Connection connection; //used to hold the jdbc connection to the DB
  private Statement statement; //used to create an instance of the connection
  private ResultSet resultSet; //used to hold the result of your query (if one
                               // exists)
  private ResultSet resultSet2;
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
           System.out.println("0: Back to User Menu");
           System.out.println("1: Print all mutual funds in alphabetical order");
           //System.out.println("2: ");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           pause = reader.nextLine();
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
                             System.out.println("-----------------------");
                             System.out.println("SYMBOL     NAME");
                             System.out.println("DESCRIPTION");
                             System.out.println("CATEGORY   DATE_CREATED");
                             System.out.println("-----------------------");
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
                             pause = reader.nextLine();
                             //resultSet.close();  
                             
                           }
                           else
                           {
                             System.out.println("For some reason there are no mutual funds in this database."); 
                             System.out.println("Type anything and hit Enter to continue!");
                             //This is basically a pause
                             quit = false;
                             pause = reader.nextLine();
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
  
void find_mutual_funds()
{
        String search1;
        String search2;
        String fundname;
        String funddescription;
        String pause;
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the first search term you'd like to find in a mutual fund description: [Type QUIT to quit]:");
        search1 = reader.nextLine();
        if (!(search1.equals("QUIT")))
        {
            System.out.println("Please enter the second search term you'd like to find in a mutual fund description: [Type QUIT to quit]:");
            search2 = reader.nextLine();
            if (!(search1.equals("QUIT")))
            {
                 try {
                    
                    statement = connection.createStatement(); //create an instance
                    query = "SELECT symbol, description FROM MUTUALFUND WHERE DESCRIPTION LIKE " + "'%" + search1 + "%'" + " OR DESCRIPTION LIKE " + "'%" + search2 + "%'"; 
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.isBeforeFirst())
                    {
                    System.out.println("No matching funds found!");
					//This is basically a pause
					pause = reader.nextLine();
                    }
                    else
                    {
                        System.out.println("Results:");
                        while (resultSet.next())
                        {
                        fundname = resultSet.getString(1);
                        funddescription = resultSet.getString(2);
                        System.out.println("Fund:");
                        System.out.println(fundname);
                        System.out.println("Description:");
                        System.out.println(funddescription);
                        }
                       resultSet.close();
                       pause = reader.nextLine();
                    }
            
                }
                catch(Exception Ex)
				{
					System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
					//This is basically a pause
					pause = reader.nextLine();
				}
        
            }
        }




}  
  
  
  
// Check if input is integer
public static boolean isInteger(String s) {
	try { 
		Integer.parseInt(s); 
	} catch(NumberFormatException e) { 
		return false; 
	}
	return true;
}

// Check if input is float
public static boolean isFloat(String s) {
	try { 
		Float.parseFloat(s); 
	} catch(NumberFormatException e) { 
		return false; 
	}
	return true;
}
  
//Takes userlogin for various queries
void user_menu(String userlogin)
{
	String pause;
	boolean quit = false;
	int option=-1;
	
	// For transaction values.
	String amount;
	float amt;
	String symbol;
	String numShares;
	int shr;
	int success;	// Return value from transaction stored procedures
	String choice;	// For purchase type
	int chc;
	String latestdate; // For allocation preferences
	String latestallocdate; // For allocation preferences
	
	while (option!=0)
	{
		final String ANSI_CLS = "\u001b[2J";
		final String ANSI_HOME = "\u001b[H";
		System.out.print(ANSI_CLS + ANSI_HOME);
		System.out.flush();
		System.out.println("Welcome to the user interface!");
		System.out.println("Your options:");
		System.out.println("0: Logout");
		System.out.println("1: Browse Mutual Funds");
		System.out.println("2: Find Mutual Funds");
		System.out.println("3: Deposit into account (auto-buy)");
		System.out.println("4: Sell shares");
		System.out.println("5: Purchase shares");
		// System.out.println("6: Change allocation preferences");
		Scanner reader = new Scanner(System.in);
		//reader.nextLine();
		option = reader.nextInt();
		pause = reader.nextLine();
		switch(option)
		{
			case 1:
				browse_mutual_funds();
			break;

            case 2:
                find_mutual_funds();
            break;
            
			case 3:
				try
				{	
					System.out.println("Please enter the amount you would like to deposit [Type QUIT to quit]:");
					System.out.print("$");
					amount = reader.nextLine();
					if (!amount.equals("QUIT") && isFloat(amount))
					{
						// System.out.println("TESTING: Inside the if block.");
						// pause = reader.nextLine();
						amt = Float.parseFloat(amount);
						success = -1;
						
						// Get ready to execute this stored procedure...
						CallableStatement stmt = connection.prepareCall("CALL deposit(?,?,?)");
						stmt.setString(1, userlogin);
						stmt.setFloat(2, amt);
						stmt.registerOutParameter(3, java.sql.Types.INTEGER);

						connection.setAutoCommit(false);
						connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
						resultSet = stmt.executeQuery();
						connection.commit();
						connection.setAutoCommit(true);
						connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
						
						// Retrieve success value.
						success = stmt.getInt(3);
						// System.out.println("TESTING: " + success);
						if(success == 2)
							System.out.println("Success! $" + amt + " deposited into account, and buying preferences automated accordingly.");
						else if(success == 1)
							System.out.println("$" + amt + " deposited into account. Not enough to automate buying preferences.");
						else if(success == 0)
							System.out.println("Error: unsuccessful deposit.");
						else
							System.out.println("Error: no value returned");
						pause = reader.nextLine();
					}
					else
					{
						System.out.println("Sorry, invalid amount!");
						//This is basically a pause
						pause = reader.nextLine();
					}
				}
				catch(Exception Ex)
				{
					System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
					//This is basically a pause
					pause = reader.nextLine();
				}
			break;
			
			case 4:
				try
				{	
					System.out.println("Please indicate the symbol of the fund you would like to sell shares of (No more than 20 characters!) [Type QUIT to quit]:");
					symbol = reader.nextLine();
					if (!symbol.equals("QUIT") && symbol.length() <= 20)
					{
						System.out.println("Please indicate how many shares of " + symbol + " you would like to sell [Type QUIT to quit]:");
						numShares = reader.nextLine();
						if (!numShares.equals("QUIT") && isInteger(numShares))
						{
							shr = Integer.parseInt(numShares);
							success = -1;
							
							// Get ready to execute this stored procedure...
							CallableStatement stmt = connection.prepareCall("CALL sale(?,?,?,?,?)");
							stmt.setString(1, userlogin);
							stmt.setString(2, symbol);
							stmt.setInt(3, shr);
							stmt.registerOutParameter(4, java.sql.Types.FLOAT);
							stmt.registerOutParameter(5, java.sql.Types.INTEGER);

							connection.setAutoCommit(false);
							connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							resultSet = stmt.executeQuery();
							connection.commit();
							connection.setAutoCommit(true);
							connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

							
							// Retrieve success value.
							amt = stmt.getFloat(4);
							success = stmt.getInt(5);
							// System.out.println("TESTING: " + success);
							if(success == 1)
								System.out.println("Success! " + shr + " shares sold, and $" + amt + " earned.");
							else if(success == 0)
								System.out.println("Error: unsuccessful sale.");
							else
								System.out.println("Error: no value returned");
							pause = reader.nextLine();
						}
						else
						{
							System.out.println("Sorry, invalid number of shares!");
							//This is basically a pause
							pause = reader.nextLine();
						}
					}
					else
					{
						System.out.println("Sorry, invalid fund symbol!");
						//This is basically a pause
						pause = reader.nextLine();
					}
				}
				catch(Exception Ex)
				{
					System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
					//This is basically a pause
					pause = reader.nextLine();
				}
			break;
			
			case 5:
				try
				{	
					System.out.println("Please indicate the symbol of the fund you would like to purchase shares of (No more than 20 characters!) [Type QUIT to quit]:");
					symbol = reader.nextLine();
					if (!symbol.equals("QUIT") && symbol.length() <= 20)
					{
						System.out.println("Press 1 if you'd like to input a certain number of shares to purchase.");
						System.out.println("Press 2 if you'd like to input a certain amount of money to expend on shares. [Type QUIT to quit]:");
						choice = reader.nextLine();
						if (!choice.equals("QUIT") && isInteger(choice) && (Integer.parseInt(choice) == 1 || Integer.parseInt(choice) == 2)) // please save us short-circuit
						{
							chc = Integer.parseInt(choice);
							if(chc == 1)
							{
								System.out.println("Please indicate the number of shares of " + symbol + " you'd like to purchase [Type QUIT to quit]:");
								numShares = reader.nextLine();
								if (!numShares.equals("QUIT") && isInteger(numShares))
								{
									shr = Integer.parseInt(numShares);
									success = -1;
									
									// Get ready to execute this stored procedure...
									CallableStatement stmt = connection.prepareCall("CALL purchase1(?,?,?,?,?)");
									stmt.setString(1, userlogin);
									stmt.setString(2, symbol);
									stmt.setInt(3, shr);
									stmt.registerOutParameter(4, java.sql.Types.FLOAT);
									stmt.registerOutParameter(5, java.sql.Types.INTEGER);

									connection.setAutoCommit(false);
									connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
									resultSet = stmt.executeQuery();
									connection.commit();
									connection.setAutoCommit(true);
									connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

									
									// Retrieve success value.
									amt = stmt.getFloat(4);
									success = stmt.getInt(5);
									// System.out.println("TESTING: " + success);
									if(success == 1)
										System.out.println("Success! " + shr + " shares of " + symbol + " bought, and $" + amt + " spent.");
									else if(success == 0)
										System.out.println("Error: unsuccessful purchase.");
									else
										System.out.println("Error: no value returned");
									pause = reader.nextLine();
								}
								else
								{
									System.out.println("Sorry, invalid number of shares!");
									// This is basically a pause
									pause = reader.nextLine();
								}
							}
							else
							{
								System.out.println("Please indicate the amount of money you'd like to expend on shares of " + symbol + " [Type QUIT to quit]:");
								amount = reader.nextLine();
								if (!amount.equals("QUIT") && isFloat(amount))
								{
									amt = Float.parseFloat(amount);
									success = -1;
									
									// Get ready to execute this stored procedure...
									CallableStatement stmt = connection.prepareCall("CALL purchase2(?,?,?,?,?,?)");
									stmt.setString(1, userlogin);
									stmt.setString(2, symbol);
									stmt.setFloat(3, amt);
									stmt.registerOutParameter(4, java.sql.Types.INTEGER);
									stmt.registerOutParameter(5, java.sql.Types.FLOAT);
									stmt.registerOutParameter(6, java.sql.Types.INTEGER);
									
									connection.setAutoCommit(false);
									connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
									resultSet = stmt.executeQuery();
									connection.commit();
									connection.setAutoCommit(true);
									connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
									
									// Retrieve success value.
									shr = stmt.getInt(4);
									amt = stmt.getFloat(5);
									success = stmt.getInt(6);
									// System.out.println("TESTING: " + success);
									if(success == 2)
										System.out.println("Success! " + shr + " shares of " + symbol + " bought with $" + amt + ".");
									else if(success == 1)
										System.out.println("Not enough to purchase any shares of " + symbol + ".");
									else if(success == 0)
										System.out.println("Error: unsuccessful purchase.");
									else
										System.out.println("Error: no value returned");
									pause = reader.nextLine();
								}
								else
								{
									System.out.println("Sorry, invalid amount!");
									// This is basically a pause
									pause = reader.nextLine();
								}
							}
						}
						else
						{
							System.out.println("Sorry, invalid choice!");
							// This is basically a pause
							pause = reader.nextLine();
						}
					}
					else
					{
						System.out.println("Sorry, invalid fund symbol!");
						// This is basically a pause
						pause = reader.nextLine();
					}
				}
				catch(Exception Ex)
				{
					System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
					// This is basically a pause
					pause = reader.nextLine();
				}
			break;
			/*
			case 6:
				try
				{	
					// First, check the date.
					PreparedStatement pulldate = connection.prepareStatement("SELECT EXTRACT(MONTH FROM C_DATE), EXTRACT(YEAR FROM C_DATE) FROM MUTUALDATE ORDER BY C_DATE DESC");
					resultSet2 = pulldate.executeQuery();
					if (resultSet2.isBeforeFirst()) //There is; keep going
					{
						resultSet2.next();
						latestdate = resultSet2.getString(1);
						// Get user's last allocation date.
						pulldate = connection.prepareStatement("SELECT EXTRACT(MONTH FROM P_DATE), EXTRACT(YEAR FROM P_DATE) FROM ALLOCATION WHERE login = ? ORDER BY P_DATE DESC");
						pulldate.setString(1, userlogin);
						resultSet2 = pulldate.executeQuery();
						latestallocdate = resultSet2.getString(1);
						if(!latestdate.equals(latestallocdate))
							System.out.println("You may reassign your allocation.");
						else
							System.out.println("You have already assigned your allocation this month.");
					}
					
				}
				catch(Exception Ex)
				{
					System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
					//This is basically a pause
					pause = reader.nextLine();
				}
			break;
			*/
			case 0:
			default:
			break;
		}
	}
}

    void stats_menu()
  {
       String pause;
       String line ="";
       String date ="";
       boolean quit = false;
       int option=-1;
       int num_months = 0;
       while (option!=0)
       {
           final String ANSI_CLS = "\u001b[2J";
           final String ANSI_HOME = "\u001b[H";
           System.out.print(ANSI_CLS + ANSI_HOME);
           System.out.flush();
           System.out.println("Usage Statistics menu:");
           System.out.println("Your options:");
           System.out.println("0: Back to Admin Menu");
           System.out.println("1: Display the top X categories in the past months.");
           System.out.println("2: Display the top X investors with the most money in shares in the past months.");
           System.out.println("3: Print all mutual funds in alphabetical order");
           //System.out.println("4: ");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           pause = reader.nextLine();
           try
           {
               switch (option)
               { 
                    case 1:
                    quit = false;
                    while (quit == false)
                    { System.out.println("Please enter how many months back from the current system date you wish to search: [Type 0 to quit]:");
                     num_months = reader.nextInt();
                     pause = reader.nextLine();
                     if (num_months > 0)
                     {
                      int num_investors = 0;
                      System.out.println("Please enter how many of the top categories you wish to find (from the top category down): [Type 0 to quit]:");
                      num_investors = reader.nextInt();
                      pause = reader.nextLine();
                        if (num_investors > 0)
                        {
                        
                        //The innermost query selects for the most recent date. X months is subtracted from that to form the time period to search through the trxlog
                        //For buy actions, whose num_shares are then summed; it's ordered by total_shares, then finally only the top k rownums are picked
                        String firstpart = "WITH ";
                        String secondpart = "current_date AS ";
                        String thirdpart = "(select * from (select * from mutualdate order by c_date desc) where rownum<=1 order by rownum) ";
                        String fourthpart = "SELECT login, total_shares ";
                        String fifthpart = "FROM ";
                        String sixthpart = "(select login, sum(num_shares) as total_shares from trxlog where 'action' = 'buy' and t_date < current_date - INTERVAL";
                        String seventhpart = " '" + Integer.toString(num_months) + "' MONTH group by login order by total_shares desc) ";
                        String eighthpart = "where rownum <=" + Integer.toString(num_investors) +" order by rownum";
                        String query = firstpart+secondpart+thirdpart+fourthpart+fifthpart+sixthpart+seventhpart+eighthpart;
                        Statement stmt = connection.createStatement();
                        //query); 
                        //stmt.setInt(1, num_months);
                        //stmt.setInt(2, num_investors);                   
                        resultSet = stmt.executeQuery(query);                  
                           if (!resultSet.isBeforeFirst())
                           {
                           System.out.println("There were no purchases by investors in this time frame.");
                           quit = true;
                           //This is basically a pause
                           pause = reader.nextLine();                       
                           }
                           else
                           {
                            int counter = 1;
                            while (resultSet.next())
                            {
                              String investor_name = resultSet.getString(1);
                              int num_shares = resultSet.getInt(2);
                              System.out.println("#"+counter+ " investor, " + investor_name + " bought " +num_shares + "shares");                       
                         
                            }
                             System.out.println("Press any key and hit enter to return to the usage statistics menu.");
                             quit = true;
                             //This is basically a pause
                             pause = reader.nextLine();  
                           }
                           
                           
                        
                        }
                        else
                        {
                          if (num_investors ==0)
                          {
                          quit = true;
                          }
                        
                        }
                     }
                     else
                     {
                       if (num_months == 0)
                       {
                       quit = true;
                       }
                     }
 
                    }
                    
                    option = 0;
                    break;
                    
                    case 2:
                    quit = false;
                    while (quit == false)
                    {
                     System.out.println("Please enter how many months back from the current system date you wish to search: [Type 0 to quit]:");
                     num_months = reader.nextInt();
                     pause = reader.nextLine();
                     if (num_months > 0)
                     {
                      int num_investors = 0;
                      System.out.println("Please enter how many of the top investors you wish to find (from the top investor down): [Type 0 to quit]:");
                      num_investors = reader.nextInt();
                      pause = reader.nextLine();
                        if (num_investors > 0)
                        {
                        
                        //The innermost query selects for the most recent date. X months is subtracted from that to form the time period to search through the trxlog
                        //For buy actions, whose num_shares are then summed; it's ordered by total_shares, then finally only the top k rownums are picked
                        String firstpart = "WITH ";
                        String secondpart = "current_date AS ";
                        String thirdpart = "(select * from (select * from mutualdate order by c_date desc) where rownum<=1 order by rownum) ";
                        String fourthpart = "SELECT login, total_shares ";
                        String fifthpart = "FROM ";
                        String sixthpart = "(select login, sum(num_shares) as total_shares from trxlog where 'action' = 'buy' and t_date < current_date - INTERVAL";
                        String seventhpart = " '" + Integer.toString(num_months) + "' MONTH group by login order by total_shares desc) ";
                        String eighthpart = "where rownum <=" + Integer.toString(num_investors) +" order by rownum";
                        String query = firstpart+secondpart+thirdpart+fourthpart+fifthpart+sixthpart+seventhpart+eighthpart;
                        Statement stmt = connection.createStatement();
                        //query); 
                        //stmt.setInt(1, num_months);
                        //stmt.setInt(2, num_investors);                   
                        resultSet = stmt.executeQuery(query);                  
                           if (!resultSet.isBeforeFirst())
                           {
                           System.out.println("There were no purchases by investors in this time frame.");
                           quit = true;
                           //This is basically a pause
                           pause = reader.nextLine();                       
                           }
                           else
                           {
                            int counter = 1;
                            while (resultSet.next())
                            {
                              String investor_name = resultSet.getString(1);
                              int num_shares = resultSet.getInt(2);
                              System.out.println("#"+counter+ " investor, " + investor_name + " bought " +num_shares + "shares");                       
                         
                            }
                             System.out.println("Press any key and hit enter to return to the usage statistics menu.");
                             quit = true;
                             //This is basically a pause
                             pause = reader.nextLine();  
                           }
                           
                           
                        
                        }
                        else
                        {
                          if (num_investors ==0)
                          {
                          quit = true;
                          }
                        
                        }
                     }
                     else
                     {
                       if (num_months == 0)
                       {
                       quit = true;
                       }
                     }
                    
                    }
                        
                    
                    option=0;
                    break;
                 
               
                    case 0:
                    default:
                    break;
               }
           }
           catch(Exception Ex)
           {
           System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
           //This is basically a pause
           pause = reader.nextLine();
           }
           
           
      }
   }














  void admin_menu()
  {
       String pause;
       boolean quit = false;
       boolean admin = false;
       //For new users
       String make_admin;
       String username;
       String password;
       String email;
       String address;
       String name;
       
            
       //For new pricequote & mutualfund
       String mfsymbol;
       String mfname;
       String mfdescription;
       String mfcategory; 
       Date latestdate;
       Date latestquotedate;
       float newprice;
       
       //For new sysdate
       String currentdate;
       String newdatestr;
       String newmonthstr;
       String curryearstr;
       String currmonthstr;
       String currdaystr;      
       
       int curryear;
       int currmonth;
       int currday;
       Date newdate;
 
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
           System.out.println("2: Update share quote");
           System.out.println("3: Add a new fund");
           System.out.println("4: Update the system date");
           System.out.println("5: View usage statistics");
           Scanner reader = new Scanner(System.in);
          // reader.nextLine();
           option = reader.nextInt();
           pause = reader.nextLine();
           switch (option)
           { 
           
              case 1:
                  quit = false;
                  try{
                       while (quit == false)
                        {                   
                          System.out.println("Please enter the new user's login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.nextLine();
                          if (!username.equals("QUIT") && username.length() < 10)
                          {
                             System.out.println("Please enter the new user's password (No more than 10 characters!) [Type QUIT to quit]:");
                             password = reader.nextLine();
                             if (!password.equals("QUIT") && password.length() < 10)
                             {
                                System.out.println("Please enter the new user's email (No more than 40 characters!) [Type QUIT to quit]:");
                                email = reader.nextLine();
                                if (!email.equals("QUIT") && email.length() < 40)
                                {
                                    System.out.println("Please enter the new user's address (No more than 30 characters!) [Type QUIT to quit]:");
                                    address = reader.nextLine();
                                        if (!address.equals("QUIT") && address.length() < 30)
                                        {
                                            System.out.println("Please enter the new user's name (No more than 20 characters!) [Type QUIT to quit]:");
                                            name = reader.nextLine();
                                            if (!name.equals("QUIT") && name.length() < 20)
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
                                                         connection.setAutoCommit(false);
														 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
														 stmt.executeUpdate();
													 	 connection.commit();
														 connection.setAutoCommit(true);
														 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
														 
                                                         
                                                         stmt = connection.prepareStatement("INSERT INTO CUSTOMER VALUES(?,?,?,?,?)");
                                                         stmt.setString(1, username);
                                                         stmt.setString(2, name);
                                                         stmt.setString(3, email);
                                                         stmt.setString(4, address);
                                                         stmt.setString(5, password);
                                                         stmt.setInt(6, 0);connection.setAutoCommit(false);
														 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
														 stmt.executeUpdate();
														 connection.commit();
														 connection.setAutoCommit(true);
														 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                         
                                                         System.out.println("User succesfully added as administrator and customer!");
                                                         //This is basically a pause
                                                         quit = false;
                                                         pause = reader.nextLine();
                                                         //resultSet.close();  
                                                      }
                                                      else
                                                      {
                                                        System.out.println("Sorry, an administrator with this login already exists!");
                                                        resultSet.close(); 
                                                        //user_menu();
                                                        //This is basically a pause
                                                        pause = reader.nextLine();
                                                                         
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
                                                         connection.setAutoCommit(false);
														 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
														 stmt.executeUpdate();
														 connection.commit();
														 connection.setAutoCommit(true);
														 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                         
                                                         System.out.println("User succesfully added as customer!");
                                                         //This is basically a pause
                                                         quit = false;
                                                         pause = reader.nextLine();
                                                         //resultSet.close();  
                                                      }
                                                      else
                                                      {
                                                        System.out.println("Sorry, a user with this login already exists!");
                                                        resultSet.close(); 
                                                        //user_menu();
                                                        //This is basically a pause
                                                        pause = reader.nextLine();
                                                                         
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
                                            pause = reader.nextLine();          
                                            
                                            }
                                        }
                                        else
                                        {
                                        System.out.println("Sorry, invalid address!");
                                        //This is basically a pause
                                        pause = reader.nextLine();  
                                        }
                                }
                                else
                                {
                                   System.out.println("Sorry, invalid email!");
                                   //This is basically a pause
                                   pause = reader.nextLine();                                         
                                }
                             }
                             else
                             {
                                System.out.println("Sorry, invalid password!");
                                //This is basically a pause
                                pause = reader.nextLine();                                         
                             }
                             
                          }
                          else
                          {
                                System.out.println("Sorry, invalid username!");
                                //This is basically a pause
                                pause = reader.nextLine();                                         
                          }
                      }
                   }
                   catch(Exception Ex)
                   {
                    System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
                   }
                   break;   
                     
             case 2:
                 quit = false;
                  try{
                    while (quit == false)
                        { 
                            //Check whether there's a valid date in the DB
                            PreparedStatement pulldate = connection.prepareStatement("SELECT * FROM MUTUALDATE ORDER BY C_DATE DESC");
                            resultSet2 = pulldate.executeQuery();
                            if (resultSet2.isBeforeFirst()) //There is; keep going
                            {
                              resultSet2.next();
                              latestdate = resultSet2.getDate(1);
                              resultSet2.close();
                              System.out.println("Please enter the mutual fund symbol you're entering a new price quote for (No more than 20 characters!) [Type QUIT to quit]:");
                              mfsymbol = "";
                              mfsymbol = reader.nextLine();                            
                              if (!mfsymbol.equals("QUIT") && mfsymbol.length() < 20)
                              {
                                    PreparedStatement stmt = connection.prepareStatement("SELECT symbol FROM MUTUALFUND WHERE SYMBOL = ?"); 
                                    //System.out.println(stmt.toString());
                                   // System.out.println(mfsymbol);
                                    
                                    stmt.setString(1, mfsymbol);
                                    resultSet = stmt.executeQuery();
                                    if(!resultSet.isBeforeFirst()) //Symbol doesn't exist
                                    {
                                        System.out.println("Sorry, that mutual fund symbol is not in the DB. Please try again! Type anything and hit enter to continue.");
                                        resultSet.close();
                                        pause = reader.nextLine();  
                                    }
                                    else //Check last closing price date. See if it's equal to now (latest date)
                                    { 
                                        resultSet.close();
                                        stmt = connection.prepareStatement("SELECT p_date FROM CLOSINGPRICE WHERE SYMBOL = ? ORDER BY p_date DESC"); 
                                        stmt.setString(1, mfsymbol);
                                        resultSet = stmt.executeQuery();
                                        if (!resultSet.isBeforeFirst()) //No earlier price, can definitely insert IF there's a valid date, check that next
                                        {
                                            resultSet.close();
                                            System.out.println("Please enter the price for the new quote: (XX.YY format please)");
                                            newprice = reader.nextFloat();
                                            stmt = connection.prepareStatement("INSERT INTO CLOSINGPRICE VALUES(?,?,?)");
                                            stmt.setString(1, mfsymbol);
                                            stmt.setFloat(2, newprice);
                                            stmt.setDate(3, latestdate);
                                            connection.setAutoCommit(false);
                                            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                                            stmt.executeUpdate();
                                            connection.commit();
                                            connection.setAutoCommit(true);
                                            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                            
                                            System.out.println("Price updated! Please type anything and hit enter to continue!");
                                            quit = true;
                                             //This is basically a pause
                                            pause = reader.nextLine(); 
                                                                              
                                        }
                                        else //Check whether these dates are the same
                                        {
                                            resultSet.next();
                                            latestquotedate = resultSet.getDate(1);
                                            resultSet.close();
                                            System.out.println("This price quote was last updated on: " + latestquotedate);
                                            System.out.println("The current system date is: " + latestdate);
                                          
                                            
                                            if (!latestquotedate.equals(latestdate))
                                            {
                                                System.out.println("Please enter the price for the new quote: (XX.YY format please)");
                                                newprice = reader.nextFloat();
                                                pause = reader.nextLine(); 
                                                stmt = connection.prepareStatement("INSERT INTO CLOSINGPRICE VALUES(?,?,?)");
                                                stmt.setString(1, mfsymbol);
                                                stmt.setFloat(2, newprice);
                                                stmt.setDate(3, latestdate);
                                                connection.setAutoCommit(false);
                                                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                                                stmt.executeUpdate();
                                                connection.commit();
                                                connection.setAutoCommit(true);
                                                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                
                                                System.out.println("Price updated! Please type anything and hit enter to continue!");
                                                quit = true;
                                                 //This is basically a pause
                                                pause = reader.nextLine(); 
                                                                                  
                                            }
                                            else
                                            {
                                             
                                             System.out.println("This share has already been updated for the current date.");
                                             System.out.println("Update the date if you wish to enter a new price.");
                                             System.out.println("Type anything and hit enter to continue!");
                                             quit = true;
                                              //This is basically a pause
                                             pause = reader.nextLine(); 
                                                                                      
                                            }
                                              
                                        }
                                    }
           
                              }
                              else
                              {    if (mfsymbol.equals("QUIT"))
                                            quit = true;
                                   else
                                   {
                                    System.out.println("Sorry, invalid mutual fund symbol!");
                                    //This is basically a pause
                                    pause = reader.nextLine();     
                                   }
                              }
                          }
                          else
                          {
                            System.out.println("Sorry, there is no valid date to apply this price to. Please update with a valid date before repeating!");
                            quit = true;
                             //This is basically a pause
                            pause = reader.nextLine(); 
                                                        
                          }
                     }
                    }
                    catch(Exception Ex)
                    {
                       System.out.println("Error with mutualfund or closingprice or mutual date tables.  Machine Error: " + Ex.toString());
                    }
                 break;
             case 3:
                 quit = false;
                 try{
                    while (quit == false)
                        {                   
                          System.out.println("Please enter the new mutual fund's symbol (No more than 20 characters!) [Type QUIT to quit]:");
                          mfsymbol = reader.nextLine();
                          if (!mfsymbol.equals("QUIT") && mfsymbol.length() < 20)
                          {
                             System.out.println("Please enter the new mutual fund's name (No more than 30 characters!) [Type QUIT to quit]:");
                             mfname = reader.nextLine();
                             if (!mfname.equals("QUIT") && mfname.length() < 30)
                             {
                                System.out.println("Please enter the new mutual fund's description (No more than 100 characters!) [Type QUIT to quit]:");
                                mfdescription = reader.nextLine();
                                if (!mfdescription.equals("QUIT") && mfdescription.length() < 100)
                                {
                                    System.out.println("Please enter the new mutual fund's category (No more than 10 characters!) [Type QUIT to quit]:");
                                    mfcategory = reader.nextLine();
                                        if (!mfcategory.equals("QUIT") && mfcategory.length() < 30)
                                        {
                                          PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MUTUALFUND WHERE SYMBOL = ?"); 
                                          stmt.setString(1, mfsymbol);
                                          resultSet = stmt.executeQuery();
                                           if(!resultSet.isBeforeFirst()) //Check whether that admin exists already
                                           {
                                             resultSet.close();
                                             
                                             PreparedStatement pulldate = connection.prepareStatement("SELECT * FROM MUTUALDATE ORDER BY C_DATE DESC");
                                             resultSet2 = pulldate.executeQuery();
                                               if (resultSet2.isBeforeFirst()) 
                                                {
                                                 resultSet2.next();
                                                 latestdate = resultSet2.getDate(1);
                                                 connection.setAutoCommit(false);
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                                                 stmt = connection.prepareStatement("INSERT INTO MUTUALFUND VALUES(?,?,?,?,?)");
                                                 stmt.setString(1, mfsymbol);
                                                 stmt.setString(2, mfname);
                                                 stmt.setString(3, mfdescription);
                                                 stmt.setString(4, mfcategory);
                                                 stmt.setDate(5, latestdate);
                                                 stmt.executeUpdate();
                                                 connection.commit();
                                                 connection.setAutoCommit(true);
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                 resultSet.close();
                                                 resultSet2.close();
                                                                                                                                                 
                                                 System.out.println("Mutual fund succesfully created! Type and enter anything to continue!");
                                                 //This is basically a pause
                                                 quit = true;
                                                 pause = reader.nextLine();
                                                 //resultSet.close();  
                                                }
                                                else
                                                {
                                                resultSet2.close();
                                                resultSet.close();
                                                System.out.println("No valid date entered in the system to create this mutual fund!");
                                                quit = true;
                                                pause = reader.nextLine();  
                                                }
                                            
                                           }
                                           else
                                           {
                                            System.out.println("Sorry, a mutual fund with this symbol already exists!");
                                            resultSet.close(); 
                                            //user_menu();
                                            //This is basically a pause
                                            pause = reader.nextLine();                                                         
                                           }
                                       }
                                    else
                                    {
                                    if (mfcategory.equals("QUIT"))
                                        quit = true;
                                    else
                                    {
                                        System.out.println("Sorry, invalid mutual fund category!");
                                        //This is basically a pause
                                        pause = reader.nextLine();   
                                    }
                                    }                                                                                       
                                }
                                else
                                {  if (mfdescription.equals("QUIT"))
                                        quit = true;
                                    else{
                                   System.out.println("Sorry, invalid mutual fund description!");
                                   //This is basically a pause
                                   pause = reader.nextLine();   
                                   }
                                }
                             }
                             else
                             {  if (mfname.equals("QUIT"))
                                        quit = true;
                                else{
                                System.out.println("Sorry, invalid mutual fund name!");
                                //This is basically a pause
                                pause = reader.nextLine();    
                                }
                             }
                             
                          }
                          else
                          {    if (mfsymbol.equals("QUIT"))
                                        quit = true;
                               else
                               {
                                System.out.println("Sorry, invalid mutual fund symbol!");
                                //This is basically a pause
                                pause = reader.nextLine();     
                               }
                          }
                      }
               
                }
                catch(Exception Ex)
                {
                 System.out.println("Error with inserting on customer or admin table.  Machine Error: " + Ex.toString());
                }
                break;    
         
             case 4:
                quit =false;
                try{
                   while (quit==false)
                   {
                 //Check whether there's a valid date already in the DB
                            PreparedStatement pulldate = connection.prepareStatement("SELECT * FROM MUTUALDATE ORDER BY C_DATE DESC");
                            resultSet2 = pulldate.executeQuery();
                            if (resultSet2.isBeforeFirst()) //There is; keep going - and check that the date entered by the user is legitimate
                            {
                              resultSet2.next();
                              latestdate = resultSet2.getDate(1);
                              resultSet2.close();
                              currentdate = latestdate.toString();
                              curryearstr = currentdate.substring(0,4);
                              currmonthstr = currentdate.substring(5,7);
                              currdaystr = currentdate.substring(8,10);
                              curryear = Integer.parseInt(curryearstr);
                              currmonth = Integer.parseInt(currmonthstr);
                              currday = Integer.parseInt(currdaystr);
                              System.out.println("The current system date is: " + currentdate);
                              System.out.println("Please enter the new system year: [Type 0 to quit.]");
                              int nextyear;
                              int nextmonth;
                              int nextday;
                              nextyear = reader.nextInt();
                              pause = reader.nextLine();
                              
                                if ((!(nextyear ==0))  && (nextyear >= curryear))
                                {
                                  System.out.println("Please enter the new system month (where 1 is January, 2 is February, etc.): [Type 0 to quit]:");
                                  nextmonth = reader.nextInt();
                                  pause = reader.nextLine();
                                 
                                    if ((!(nextmonth ==0)) && (!(nextmonth > 12)) && (!((nextmonth < currmonth) && (curryear == nextyear))))
                                    {
                                        System.out.println("Please enter the new system day: [Type 0 to quit]:");
                                        nextday = reader.nextInt();
                                        pause = reader.nextLine();
                                          
                                          if ((!(nextday ==0)) && (!(nextday > 31)) && (! ((nextday < currday) && (curryear == nextyear) && (currmonth == nextmonth))))
                                          {
                                                 newdatestr = Integer.toString(nextday) + "-" + Integer.toString(nextmonth) + "-" + Integer.toString(nextyear); 
                                                 //Nextmonth-1 because in this (deprecated) constructor, month was 0-indexed.
                                                 newdate = new Date(nextyear, (nextmonth-1), nextday);
                                                 //System.out.println("The new date string is: |" + newdatestr);
                                                 newdatestr = "";
                                                 newmonthstr = new SimpleDateFormat("MMM").format(newdate);
                                                 System.out.println(nextmonth);
                                                 
                                                 newdatestr = Integer.toString(nextday) + "-" + new SimpleDateFormat("MMM").format(newdate) + "-" + Integer.toString(nextyear);
                                                 //System.out.println("The new date string is: |" + newdatestr);
                                                 //System.out.println(newdate);
                                                 connection.setAutoCommit(false);
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                                                 PreparedStatement stmt = connection.prepareStatement("INSERT INTO MUTUALDATE VALUES(?)");
                                                 stmt.setString(1, newdatestr);
                                                 stmt.executeUpdate();
                                                 connection.commit();
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                 connection.setAutoCommit(true);
                                                 quit = true;
                                                 
                                                 System.out.println("New system date succesfully added!");
                                                  //This is basically a pause
                                                 pause = reader.nextLine();
                                          }
                                          else
                                          {
                                                if (nextday ==0)
                                                {
                                                    quit = true;
                                                }
                                                else
                                                {
                                                System.out.println("The new system date must be *after* the current system date and the new system day between 1-31.");  
                                                 //This is basically a pause
                                                 pause = reader.nextLine();                                                
                                                }   
                                          }
                                          
                                    }
                                   
                                    else
                                    {
                                        if (nextmonth ==0)
                                        {
                                            quit = true;
                                        }
                                        else
                                        {
                                            System.out.println("The new system month must be *after* the current system date and/or between 1-12.");      
                                            //This is basically a pause
                                             pause = reader.nextLine();                                         
                                        }                                    
                                    }


                                }
                                else
                                {
                                    if (nextyear ==0)
                                    {
                                        quit = true;
                                    }
                                    else
                                    {
                                    System.out.println("The new system date must be *after* the current system date.");   
                                    //This is basically a pause
                                    pause = reader.nextLine();                                      
                                    }
                                }
                              
                            }
                            else //Just add the new date if it's legit
                            {
                              int nextyear;
                              int nextmonth;
                              int nextday;
                              System.out.println("Please enter the new system year: [Type 0 to quit.]");
                              nextyear = reader.nextInt();
                              pause = reader.nextLine();
                              
                                if ((!(nextyear ==0)))
                                {
                                  System.out.println("Please enter the new system month (where 1 is January, 2 is February, etc.): [Type 0 to quit]:");
                                  nextmonth = reader.nextInt();
                                  pause = reader.nextLine();
                                 
                                    if ((!(nextmonth ==0)) && (!(nextmonth > 12)))
                                    {
                                        System.out.println("Please enter the new system day: [Type 0 to quit]:");
                                        nextday = reader.nextInt();
                                        pause = reader.nextLine();
                                          
                                          if ((!(nextday ==0)) && (!(nextday > 31)))
                                          {
                                                 newdatestr = Integer.toString(nextday) + "-" + Integer.toString(nextmonth) + "-" + Integer.toString(nextyear); 
                                                 //Nextmonth-1 because in this (deprecated) constructor, month was 0-indexed.
                                                 newdate = new Date(nextyear, (nextmonth-1), nextday);
                                                // System.out.println("The new date string is: |" + newdatestr);
                                                 newdatestr = "";
                                                 newmonthstr = new SimpleDateFormat("MMM").format(newdate);
                                                // System.out.println(nextmonth);
                                                 
                                                 newdatestr = Integer.toString(nextday) + "-" + new SimpleDateFormat("MMM").format(newdate) + "-" + Integer.toString(nextyear);
                                               //  System.out.println("The new date string is: |" + newdatestr);
                                                 //System.out.println(newdate);
                                                 connection.setAutoCommit(false);
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                                                 PreparedStatement stmt = connection.prepareStatement("INSERT INTO MUTUALDATE VALUES(?)");
                                                 stmt.setString(1, newdatestr);
                                                 stmt.executeUpdate();
                                                 connection.commit();
                                                 connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                                                 connection.setAutoCommit(true);
                                                 quit = true;
                                                 
                                                 System.out.println("New system date succesfully added!");
                                                  //This is basically a pause
                                                 pause = reader.nextLine();
                                          }
                                          else
                                          {
                                                if (nextday ==0)
                                                {
                                                    quit = true;
                                                }
                                                else
                                                {
                                                System.out.println("The new system day must be between 1-31.");  
                                                 //This is basically a pause
                                                 pause = reader.nextLine();                                                
                                                }   
                                          }
                                          
                                    }
                                   
                                    else
                                    {
                                        if (nextmonth ==0)
                                        {
                                            quit = true;
                                        }
                                        else
                                        {
                                            System.out.println("The new system month must be between 1-12.");      
                                            //This is basically a pause
                                             pause = reader.nextLine();                                         
                                        }                                    
                                    }


                                }
                                else
                                {
                                    if (nextyear ==0)
                                    {
                                        quit = true;
                                    }
                                  
                                }   
                
                            }
                   }
                }
                catch(Exception Ex)
                {
                 System.out.println("Error with inserting on mutualdate table.  Machine Error: " + Ex.toString());
                }
             break;  
             
             case 5:
                stats_menu();
                break;
             
             
             
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
        pause = reader.nextLine();
        switch (option)
        { case 1:
               quit = false;
               try{
                    statement = connection.createStatement(); //create an instance
                    query = "SELECT * FROM CUSTOMER"; 
                    resultSet = statement.executeQuery(query);
                    if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                    {
                        System.out.println("No customers registered in BetterFutures!");
                        //This is basically a pause
                        pause = reader.nextLine();
                        resultSet.close();  
                    }
                    else
                    {
                        resultSet.close();  
                        while (quit == false)
                        {                   
                          System.out.println("Please enter your login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.nextLine();
                          if (!username.equals("QUIT"))
                          {
                          System.out.println("Please enter your password (No more than 20 characters!) [Type QUIT to quit]:");
                          password = reader.nextLine();
                          }
                          if (username.length() > 10 || password.length() > 20 || username.equals("QUIT") || password.equals("QUIT") )
                          {
                              if (username.equals("QUIT") || password.equals("QUIT"))
                                 quit = true;
                              else
                              {
                              System.out.println("Incorrect username/password!!");
                              //This is basically a pause
                              pause = reader.nextLine();
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
                                     pause = reader.nextLine();
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
                                    //pause = reader.nextLine();
                                                      
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
                quit = false;
                try{
                    statement = connection.createStatement(); //create an instance
                    query = "SELECT * FROM ADMINISTRATOR"; 
                    resultSet = statement.executeQuery(query);
                    if(!resultSet.isBeforeFirst()) //Check whether there are any customers in DB
                    {
                        System.out.println("No administrator registered in BetterFutures!");
                        //This is basically a pause
                        pause = reader.nextLine();
                        resultSet.close();  
                    }
                    else
                    {
                        resultSet.close();  
                        while (quit == false)
                        {                   
                          System.out.println("Please enter your login name (No more than 10 characters!) [Type QUIT to quit]:");
                          username = reader.nextLine();
                          if (!username.equals("QUIT"))
                          {
                          System.out.println("Please enter your password (No more than 20 characters!) [Type QUIT to quit]:");
                          password = reader.nextLine();
                          }
                          if (username.length() > 10 || password.length() > 20 || username.equals("QUIT") || password.equals("QUIT") )
                          {
                              if (username.equals("QUIT") || password.equals("QUIT"))
                                 quit = true;
                              else
                              {
                              System.out.println("Incorrect username/password!!");
                              //This is basically a pause
                              pause = reader.nextLine();
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
                                     pause = reader.nextLine();
                                     resultSet.close();  
                                    }
                                 else
                                    {
                                    System.out.println("Go to administrator menu! Should be successful login!");
                                    resultSet.close(); 
                                    admin_menu();
                                    option = -1;
                                    //This is basically a pause
                                    //pause = reader.nextLine();
     
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