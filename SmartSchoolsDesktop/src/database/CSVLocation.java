package database;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;


public class CSVLocation {

	public static String write(String absolutePath){
		System.out.println("in function");
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement("select duration,timeOfAccess,room,building,bssid,ssid,first_name,last_name, email from accessedAP natural join accesspoint natural join user order by userid");
	
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				System.out.println("no entries found csv location");
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("duration");
        		row[4] = resultset.getString("timeOfAccess");
        		row[5] = resultset.getString("room");
        		row[6] = resultset.getString("building");
        		row[7] = resultset.getString("bssid");
        		row[8] = resultset.getString("ssid");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export(absolutePath, "AccessPointData", new String[]{"First_Name", "Last_Name", "Email", "Duration", "Time of Access", "Room", "Building", "BSSID", "SSID"}, info)){
					System.out.println("export failed");
					return "The number of columns provided does not match the number of columns in the data"; 
				}
			}catch(FileNotFoundException f){
				System.out.println("export failed file");
				return "File Creation Error";
			}
			System.out.println("export suceeded");
		return "Export Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
}
