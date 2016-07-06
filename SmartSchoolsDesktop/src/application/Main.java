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
	private static boolean adminLoggedIn;
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		this.currentStage = primaryStage;
		
		changeScene("Login");
	}
	
	public void changeScene(String sceneName){		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view/" + sceneName + ".fxml"));
			Parent root = (Parent)loader.load();     
			Scene scene = new Scene(root, 1280, 720);
			scene.getStylesheets().add(getClass().getResource("/resources/view/application.css").toExternalForm());
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
	
	public Stage getCurrentStage(){
		return this.currentStage;
	}
}
