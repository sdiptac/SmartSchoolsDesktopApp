package application;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;


public class CSVLocation {
	
	public static ArrayList<String[]> findAllUsers(){
		return findUsers("", "");
	}
	
	public static ArrayList<String[]> findUsers(String firstname, String lastname){
		final String query = "select userID, first_name, last_name, email from user where first_name like ? and last_name like ?";

		firstname = firstname.toLowerCase();
		lastname = lastname.toLowerCase();
		ArrayList<String[]> info = new ArrayList<String[]>();
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(query);
			prepare.setString(1, "%" + firstname + "%");
			prepare.setString(2, "%" + lastname + "%");
			ResultSet resultset = prepare.executeQuery();
            	if(!resultset.next()){
            		System.out.println("No such user found");
            	}else {
            		while(resultset.next()){
            			String[] row = new String[4];
            			row[0] = resultset.getString("userID");
            			row[1] = resultset.getString("first_name");
            			row[2] = resultset.getString("last_name");
            			row[3] = resultset.getString("email");
            			info.add(row);
            		}
            	}
            	Connector.disconnect();
		}catch(Exception e){
            	System.out.println("Database Error");
            	return info;
		}
		return info;
	}

	public static String write(){
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement("select duration,timeOfAccess,room,building,bssid,ssid,org,first_name,last_name from accessedAP natural join accesspoint natural join user order by userid");
	
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("duration");
        		row[3] = resultset.getString("timeOfAccess");
        		row[4] = resultset.getString("room");
        		row[5] = resultset.getString("building");
        		row[6] = resultset.getString("org");
        		row[7] = resultset.getString("bssid");
        		row[8] = resultset.getString("ssid");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export("AccessPointData", new String[]{"First_Name", "Last_Name", "Duration", "Time of Access", "Room", "Building", "BSSID", "SSID", "Org"}, info)){
					return "The number of columns provided does not match the number of columns in the data"; 
				}
			}catch(FileNotFoundException f){
				return "File Creation Error";
			}
		return "Export Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
}
