package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.AdminUser;
import application.CSVLocation;
import application.Connector;
import application.ExportToCSV;
import application.Main;
import application.PageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class LocationController extends PageController {
	@FXML private ListView<String> userList;
	@FXML private Button printButton;
	@FXML private Label userLabel;
	
	private String currentUserID;
	private String currentFirstName;
	private String currentLastName;
	private String currentEmail;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> list = (ArrayList<String>) CSVLocation.findAllUsers().stream().map((String[] row) -> Arrays.stream(row).collect(Collectors.joining(", "))).collect(Collectors.toList());
		ObservableList<String> userNames = FXCollections.observableArrayList (list);
		userList.setItems(userNames);
		
		userList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        userLabel.setText(newValue);
		        
		        String[] info = newValue.split(",");
		        currentUserID = info[0].trim();
		        currentFirstName = info[1].trim();
		        currentLastName = info[2].trim();
		        currentEmail = info[3].trim();
		    }
		});
    }
	
	@FXML
	protected void printButtonPress(Event event) {
	     System.out.println(CSVLocation.write(Integer.parseInt(currentUserID)));
	}
}
