package database;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

import application.Connector;
import application.ExportToCSV;


public class CSVQuestion {
	
	public static String write(String absolutePath){
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement("select first_name, last_name, email, typeOfEvent, typeOfQuestion, question, response, timeOfEvent, timeOfFeedback from feedback natural join feedback_event natural join event natural join questions_event natural join questions_feedback natural join user natural join questions natural join user_event group by feedbackid order by userid");
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("typeOfEvent");
        		row[4] = resultset.getString("typeOfQuestion");
        		row[5] = resultset.getString("question").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[6] = resultset.getString("response").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[7] = resultset.getString("timeOfEvent");
        		row[8] = resultset.getString("timeOfFeedback");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export(absolutePath, "QuestionData", new String[]{"First Name", "Last Name", "Email", "Time of Event", "Type of Question", "Question", "Response", "Time of Event", "Time of Feedback"}, info)){
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
