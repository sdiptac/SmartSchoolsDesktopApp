package database;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;

public class CSVHeartRate {
	final static String query = "select first_name,last_name,email,timeOfHR, dayOfHR, BPM from heartRate natural join user_device natural join user";
	static ResultSet resultset = null;
	static PreparedStatement statement = null;
	public static String write(String absolutePath){
		ArrayList<String[]> info = new ArrayList<String[]>();
		int count = 0;
		try{
			Connector.connect();
			statement = Connector.connection.prepareStatement(query);
			resultset = statement.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
        	do {
        		
        		String[] row = new String[6];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("timeOfHR");
        		row[4] = resultset.getString("dayOfHR");
        		row[5] = resultset.getString("BPM");
				
        		info.add(row);
        	} while (resultset.next());
        	resultset.close();
        	Connector.disconnect();
			try{
				if(!ExportToCSV.export(absolutePath, "HeartRateData", new String[]{"First_Name", "Last_Name", "Email", "Time_of_HR", "Day_of_HR", "BPM"}, info)){
					
					return "The number of columns provided does not match the number of columns in the data"; 
				}
			} catch(FileNotFoundException f){
				f.printStackTrace();
				return "File Creation Error";
			}
		return "Export Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
}
