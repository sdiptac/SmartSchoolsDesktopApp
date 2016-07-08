package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import application.Connector;

public class CSVUser {
	
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
}
