package database;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;

public class CSVSleep{
	final static String query = "select first_name, last_name, startTimeOfSleep, dayOfSleep, sleepDuration, inBedDuration, restlessCount, restlessDuration,sleepRecords from sleep natural join user_device natural join user";
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
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("startTimeOfSleep");
        		row[3] = resultset.getString("dayOfSleep");
        		row[4] = resultset.getString("sleepDuration");
        		row[5] = resultset.getString("inBedDuration");
        		row[6] = resultset.getString("restlessCount");
        		row[7] = resultset.getString("restlessDuration");
				row[8] = resultset.getString("sleepRecords");
				
				
				info.add(row);
				System.out.println("after");
        	} while (resultset.next());
        	resultset.close();
        	Connector.disconnect();
			try{
				if(!ExportToCSV.export(absolutePath, "SleepData", new String[]{"First_Name", "Last_Name", "Start_Time_Of_Sleep", "Day_of_Sleep", "Sleep_Duration","In_Bed_Duration","Restless_Count","Restless_Duration","Sleep_Records"}, info)){
					
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