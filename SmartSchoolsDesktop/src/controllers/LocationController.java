package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LocationController extends PageController {
	@FXML
	private ListView<String> userList;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		//TODO: get usernames
    }
}
