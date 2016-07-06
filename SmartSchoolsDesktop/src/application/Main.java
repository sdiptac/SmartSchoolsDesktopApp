package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static Main instance;
	private Stage currentStage;
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		this.currentStage = primaryStage;
		
		changeScene("Login", 400, 400);
	}
	
	public void changeScene(String sceneName, int width, int height){		
		try {
			Parent root;
			root = FXMLLoader.load(getClass().getResource("/gui/" + sceneName + ".fxml"));
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("/gui/application.css").toExternalForm());
			currentStage.setScene(scene);
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Main getInstance(){
		return instance;
	}
}
