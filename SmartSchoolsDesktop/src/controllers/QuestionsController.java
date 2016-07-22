package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import application.PageType;
import database.CSVQuestion;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;

public class QuestionsController extends PageController {
	@FXML private Button printButton;
	
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		
		printButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = dirChooser.showDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	 CSVQuestion.write(file.getAbsolutePath().toString());
		                    }
		                }
		            });
	}
	
	@FXML
	protected void editButtonPress(Event event) {
		if(Main.getInstance().getAdminUser().isSignedIn()){
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.QUALTRICS));
	     }
	}
	
	@Override
	protected void viewHelpPress(Event event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Questions Help");
		alert.setContentText("For help concerning editing questions, first click the edit questions button and then click View->Help.");

		alert.showAndWait();
	}
}
