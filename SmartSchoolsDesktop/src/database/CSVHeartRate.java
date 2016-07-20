package database;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;

public class CSVHeartRate {
	public static String write(String absolutePath){
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement("select first_name,last_name,timeOfHR, dayOfHR, BPM from heartRate natural join user order by userid");
	
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[5];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("timeOfHR");
        		row[3] = resultset.getString("dayOfHR");
        		row[4] = resultset.getString("BPM");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export(absolutePath, "HeartRateData", new String[]{"First_Name", "Last_Name", "Time of HR", "Day of HR", "BPM"}, info)){
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
