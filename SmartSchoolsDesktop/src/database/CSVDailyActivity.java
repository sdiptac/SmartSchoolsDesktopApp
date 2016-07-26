package database;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;

public class CSVDailyActivity {
	final static String query = "select first_name,last_name,email, calories, stepCount, floors,restingHR,dayOfActivity from dailyActivity natural join user_device natural join user";
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
        		
        		String[] row = new String[8];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("calories");
        		row[4] = resultset.getString("stepCount");
        		row[5] = resultset.getString("floors");
        		row[6] = resultset.getString("restingHR");
        		row[7] = resultset.getString("dayOfActivity");
        		
				
        		info.add(row);
        	} while (resultset.next());
        	resultset.close();
        	Connector.disconnect();
			try{
				if(!ExportToCSV.export(absolutePath, "DailyActivityData", new String[]{"First_Name", "Last_Name", "Email", "Calories", "Step_Count", "Floors", "Resting_Heart_Rate", "Day_of_Activity"}, info)){
					
					return "The number of columns provided does not match the number of columns in the data"; 
				}
			} catch(FileNotFoundException f){
				return "File Creation Error";
			}
		return "Export Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
}