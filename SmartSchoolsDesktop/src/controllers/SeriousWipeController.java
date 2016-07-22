package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.AdminUser;
import application.Connector;
import application.Main;
import database.SQLWipe;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class SeriousWipeController extends PageController {
	@FXML
	private Label incorrectPasswordLabel;
	@FXML
	private Label incorrectPasswordLabel2;
	@FXML
	private Label incorrectPasswordLabel3;
	@FXML
	private Label errorLabel;
	
	
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField passwordField2;
	@FXML
	private PasswordField passwordField3;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		incorrectPasswordLabel.setVisible(false);
		incorrectPasswordLabel2.setVisible(false);
		incorrectPasswordLabel3.setVisible(false);
    }
	
	@FXML
	protected void signInPress(Event event) {
	     AdminUser user = Main.getInstance().getAdminUser();
	     user.setPassword(passwordField3.getText());
	     
	     if(passwordField.getText().equals(passwordField2.getText()) 
	    		 && passwordField.getText().equals(passwordField3.getText()) 
	    		 && Connector.connect()){
	    	 
	    	 user.setSignedIn(true);
	    	 String error = SQLWipe.withACloth();
	    	 errorLabel.setText(error);
	    	 errorLabel.setVisible(true);
	    	 Connector.disconnect();
	     
	     }else{
	    	 user.setSignedIn(false);
	    	 user.setPassword("");
	    	 incorrectPasswordLabel.setVisible(true);
	    	 incorrectPasswordLabel2.setVisible(true);
	    	 incorrectPasswordLabel3.setVisible(true);
	     }
	}
}