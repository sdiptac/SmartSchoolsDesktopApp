package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController extends PageController{
	@FXML
	private Label incorrectPasswordLabel;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		incorrectPasswordLabel.setVisible(false);
    }
	
	@FXML
	protected void signInPress(Event event) {
	     //Main.getInstance().changeScene("MainMenu");
	}
}
