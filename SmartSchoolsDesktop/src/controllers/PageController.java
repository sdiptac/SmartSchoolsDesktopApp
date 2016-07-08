package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.PageType;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class PageController implements Initializable{
	@FXML
	protected void preferencesQuit(Event event) {
	     System.out.println("ap press");
	}
	
	@FXML
	protected void quitPress(Event event) {
		Platform.exit();
	    System.exit(0);
	}
	
	@FXML
	protected void usersPress(Event event) {
		if(Main.getInstance().getAdminUser().isSignedIn()){
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.USER));
	     }
	}
	
	@FXML
	protected void questionPress(Event event) {
		if(Main.getInstance().getAdminUser().isSignedIn()){
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.QUESTION));
	     }
	}
	
	@FXML
	protected void locationsPress(Event event) {
		if(Main.getInstance().getAdminUser().isSignedIn()){
	    	 Main.getInstance().changeScene(PageType.pageMap.get(PageType.Type.LOCATION));
	     }
	}
	
	@FXML
	protected void fitbitPress(Event event) {
	     System.out.println("fitbit press");
	}
	
	@FXML
	protected void viewHelpPress(Event event) {
	     System.out.println("view help press");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
}
