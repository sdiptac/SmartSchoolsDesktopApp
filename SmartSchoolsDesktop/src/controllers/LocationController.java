package controllers;

import application.CSVLocation;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LocationController extends PageController {
	@FXML private Button printButton;
	
	@FXML
	protected void printButtonPress(Event event) {
	     System.out.println(CSVLocation.write());
	}
}
