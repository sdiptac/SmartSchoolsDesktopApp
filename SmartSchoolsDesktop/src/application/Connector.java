package application;

import java.sql.*;

public class Connector {
	private static final String url = "jdbc:mysql:lusmartschools.cymnuwsxway1.us-east-1.rds.amazonaws.com";
	private static final String user = "admin";
	public static Connection connection = null;
	
	public static boolean connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url,user,Main.getInstance().getAdminUser().getPassword());
			return true;
		} catch(Exception e){
			return false;
		}
	}
}
