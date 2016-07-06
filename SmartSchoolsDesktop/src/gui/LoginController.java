package gui;

import application.Main;
import javafx.event.Event;
import javafx.fxml.FXML;

public class LoginController {
	@FXML
	private void mousePress(Event event) {
	     Main.getInstance().changeScene("MainMenu", 400, 400);
	}
}
