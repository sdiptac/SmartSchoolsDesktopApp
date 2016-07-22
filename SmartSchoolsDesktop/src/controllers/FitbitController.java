package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import database.CSVDailyActivity;
import database.CSVHeartRate;
import database.CSVSleep;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;


public class FitbitController extends PageController {
	@FXML private Button printHeartRateButton;
	@FXML private Button printDailyActivityButton;
	@FXML private Button printSleepButton;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		
		printHeartRateButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = dirChooser.showDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	 CSVHeartRate.write(file.getAbsolutePath().toString());
		                    }
		                }
		            });
		printDailyActivityButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = dirChooser.showDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	 CSVDailyActivity.write(file.getAbsolutePath().toString());
		                    }
		                }
		            });
		printSleepButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = dirChooser.showDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	 CSVSleep.write(file.getAbsolutePath().toString());
		                    }
		                }
		            });
	}
}

