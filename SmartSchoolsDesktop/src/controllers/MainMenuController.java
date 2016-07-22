package controllers;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainMenuController extends PageController{
	@Override
	protected void viewHelpPress(Event event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Main Menu Help");
		alert.setContentText("No help on this page.");

		alert.showAndWait();
	}
}
