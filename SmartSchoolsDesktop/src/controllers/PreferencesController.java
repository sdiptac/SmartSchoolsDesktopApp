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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
}