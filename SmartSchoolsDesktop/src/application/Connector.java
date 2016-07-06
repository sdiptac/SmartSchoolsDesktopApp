package application;

import java.sql.*;

public class Connector {
	static final String url = "jdbc:mysql:lusmartschools.cymnuwsxway1.us-east-1.rds.amazonaws.com";
	static final String user = "admin";
	static String password = "smartschools";
	public static Connection connection = null;
	
	public static boolean connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url,user,password);
			return true;
		} catch(Exception e){
			return false;
		}
	}
}
