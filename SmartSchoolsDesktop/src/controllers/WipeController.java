package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import application.PageType;
import database.CSVLocation;
import database.SQLWipe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

public class WipeController extends PageController {
	@FXML private Button wipeButton;
	
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		
		wipeButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                	Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.SERIOUSWIPE));
		                }
		            });
	}
}