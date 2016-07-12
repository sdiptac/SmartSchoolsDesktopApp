package database;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;


public class CSVLocation {

	public static String write(String absolutePath){
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement("select duration,timeOfAccess,room,building,bssid,ssid,first_name,last_name from accessedAP natural join accesspoint natural join user order by userid");
	
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[8];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("duration");
        		row[3] = resultset.getString("timeOfAccess");
        		row[4] = resultset.getString("room");
        		row[5] = resultset.getString("building");
        		row[6] = resultset.getString("bssid");
        		row[7] = resultset.getString("ssid");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export(absolutePath, "AccessPointData", new String[]{"First_Name", "Last_Name", "Duration", "Time of Access", "Room", "Building", "BSSID", "SSID"}, info)){
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
