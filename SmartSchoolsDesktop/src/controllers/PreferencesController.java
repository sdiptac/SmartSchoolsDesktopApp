package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.AdminUser;
import application.Connector;
import application.Main;
import application.PageType;
import database.CSVLocation;
import database.SQLWipe;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;

public class PreferencesController extends PageController {
	@FXML
	private Label incorrectPasswordLabel;
	
	@FXML
	private PasswordField passwordField;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		incorrectPasswordLabel.setVisible(false);
    }
	
	@FXML
	protected void signInPress(Event event) {
	     AdminUser user = Main.getInstance().getAdminUser();
	     user.setPassword(passwordField.getText());
	     if(Connector.connect()){
	    	 user.setSignedIn(true);
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.WIPE));
	    	 Connector.disconnect();
	     }else{
	    	 user.setSignedIn(false);
	    	 user.setPassword("");
	    	 incorrectPasswordLabel.setVisible(true);
	     }
	}
	
	
	@Override
	protected void viewHelpPress(Event event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Preferences Help");
		alert.setContentText("This tab should only be used if you wish to delete all information and any event, feedback, accesspoint, and fitbit data pertaining to all users that are currently in the database. \n"
				+ "\n" + "Due to the sensitive nature of this function, you must authenticate yourself multiple times. \n" + "\n" +
				"There is no way to undo this operation. \n" + "\n" + "The database will, however, continue to properly store new data after the wipe. \n");
		alert.showAndWait();
	}
}