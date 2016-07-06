package application;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;


public class RawData {

	static Scanner scanner = new Scanner(System.in);
	static Connection connection = null;
	static String query = null;
	static Statement statement = null;
	static PreparedStatement prepare = null;
	static ResultSet resultset = null;
	
	static final String url = "jdbc:mysql:lusmartschools.cymnuwsxway1.us-east-1.rds.amazonaws.com";
	static final String user = "admin";
	static String password = "smartschools";
	private static String[] email;
	private static String[] userID;
	private static String[] firstName;
	private static String[] lastName;
	private static String[] duration;
	private static String[] timeOfAccess;
	private static String[] room;
	private static String[] building;
	private static String[] org;
	
		
	public static boolean connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.	getConnection(url,user,password);
			return true;
		} catch(Exception e){
			return false;
		}
	}
		
	public static String[][] enteredName(String firstname, String lastname){
		firstname = firstname.replace("%", "");
		lastname = lastname.replace("_", "");
		firstname = firstname.toLowerCase();
		lastname = lastname.toLowerCase();
		String[][] info;
		userID = null;
		firstName = null;
		lastName = null;
		email = null;
		
		int i = 0;
		try{
			prepare = connection.prepareStatement("select userID, first_name, last_name, email from user where first_name like ? and last_name like ?");
			prepare.setString(1, "%" + firstname + "%");
			prepare.setString(2, "%" + lastname + "%");
			resultset = prepare.executeQuery();
            	if(!resultset.next())
            		System.out.println("No such user found");
            	else 
            		while(resultset.next()){
            			userID[i] = resultset.getString("userID");
            			firstName[i] = resultset.getString("first_name");
            			lastName[i] = resultset.getString("last_name");
            			email[i] = resultset.getString("email");
            			i++;
            		}
		}catch(Exception e){
            	System.out.println("Database Error");
		}
		int howMany = userID.length;
		info = new String[howMany][4];
		for(int row = 0; row < howMany; row++){
			info[row][0] = userID[row];
			info[row][1] = firstName[row];
			info[row][2] = lastName[row];
			info[row][3] = email[row];
		}
            
		return info;
            
	}
	

	public static void write(int userID){
		int i = 0;
		String[][] info = null;
		String[] columns = new String[7];
		duration = null;
		timeOfAccess = null;
		room = null;
		building = null;
		org = null;
		firstName = null;
		lastName = null;
		
		try{
			prepare = connection.prepareStatement("select duration,timeOfAccess,room,building,org,first_name,last_name from accessedAP natural join accesspoint natural join user when userID = ?");
			prepare.setInt(1, userID);
			resultset = prepare.executeQuery();
			
			
			if (!resultset.next()) System.out.println ("Empty result.");
            else {
            	do {
            		duration[i] = resultset.getString("duration");
      				timeOfAccess[i] = resultset.getString("timeOfAccess");
      				room[i] = resultset.getString("room");
      				building[i] = resultset.getString("building");
      				org[i] = resultset.getString("org");
      				firstName[i] = resultset.getString("first_name");
      				lastName[i] = resultset.getString("last_name");
      				i++;
            	} while (resultset.next());
            }
		
			
			int howMany = duration.length;
			info = new String[howMany][5];
			for(int row = 0; row < howMany; row++){
				info[row][0] = room[row];
				info[row][1] = building[row];
				info[row][2] = org[row];
				info[row][3] = duration[row];
				info[row][4] = timeOfAccess[row];
			}
			columns[0] = "Room";
			columns[1] = "Building";
			columns[2] = "Org";	 
			columns[3] = "Duration";
			columns[4] = "Time of Access";
			try{
				ExportToCSV.export(firstName[0] + "-" + lastName[0], columns, info);
			}catch(FileNotFoundException f){
				System.out.println("File Creation Error");
			}
			
		}catch(Exception e){
			System.out.println("Database Error");
		}
	}
}
