package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import application.Connector;
import controllers.LocationController.AccessPoint;

public class UploadAccessPoint {
	
	private static final String uploadAccessPointQuery = "insert into accesspoint(SSID, BSSID, room, building, org, APNumber) values(?,?,?,?,?,?)";
	private static final String updateAccessPointQuery = "update accesspoint set room = ?, building = ?, org = ?, APNumber = ? where ssid = ? and bssid = ?";
	
	public static String uploadAP(AccessPoint ap){
		try{
			
			PreparedStatement prepareChoice = Connector.connection.prepareStatement(uploadAccessPointQuery);
			prepareChoice.setString(1, ap.getSsid());
			prepareChoice.setString(2, ap.getBssid());
			prepareChoice.setString(3, ap.getRoom());
			prepareChoice.setString(4, ap.getBuilding());
			prepareChoice.setString(5, ap.getOrg());
			prepareChoice.setString(6, ap.getApNumber());
	
			prepareChoice.executeUpdate();

			return "Success";
		}catch (SQLIntegrityConstraintViolationException e) {
			try{
				
				PreparedStatement prepareUpdate = Connector.connection.prepareStatement(updateAccessPointQuery);
			
				prepareUpdate.setString(1, ap.getRoom());
				prepareUpdate.setString(2, ap.getBuilding());
				prepareUpdate.setString(3, ap.getOrg());
				prepareUpdate.setString(4, ap.getApNumber());
				prepareUpdate.setString(5, ap.getSsid());
				prepareUpdate.setString(6, ap.getBssid());
		
				prepareUpdate.executeUpdate();
				
				return "Updated";
			}catch (SQLException e2) {
			    return e2.toString();
			}
		} catch (SQLException e) {
		    return e.toString();
		}
	}
}
