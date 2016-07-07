package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.AdminUser;
import application.Connector;
import application.Main;
import application.PageType;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginController extends PageController{
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
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.MAIN_MENU));
	    	 Connector.disconnect();
	     }else{
	    	 user.setSignedIn(false);
	    	 user.setPassword("");
	    	 incorrectPasswordLabel.setVisible(true);
	     }
	}
}
