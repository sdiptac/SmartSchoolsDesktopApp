package controllers;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import application.Connector;
import application.Main;
import application.PageType;
import database.CSVQuestion;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;

public class QuestionsController extends PageController {
	@FXML private Button printButton;
	@FXML private TextField minTimesField;
	@FXML private TextField maxTimesField;
	@FXML private TextField endTimeField;
	@FXML private TextField startTimeField;
	
	
	private final String minTimesSetQuery = "update questions_time_number set minQuestions = ? where placeholderID = 0";
	private final String maxTimesSetQuery = "update questions_time_number set  maxQuestions = ? where placeholderID = 0";
	private final String endTimeSetQuery = "update questions_time_number set endTimeOfQuestions = ? where placeholderID = 0";
	private final String startTimeSetQuery = "update questions_time_number set startTimeOfQuestions = ? where placeholderID = 0";
	private final String minTimesGetQuery = "select minQuestions from questions_time_number where placeholderID = 0";
	private final String maxTimesGetQuery = "select maxQuestions from questions_time_number where placeholderID = 0";
	private final String endTimeGetQuery = "select endTimeOfQuestions from questions_time_number where placeholderID = 0";
	private final String startTimeGetQuery = "select startTimeOfQuestions from questions_time_number where placeholderID = 0";
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		
		getAndSetQuestionsCount(maxTimesField, maxTimesGetQuery);
		getAndSetQuestionsCount(minTimesField, minTimesGetQuery);
		getAndSetTimes(startTimeField, startTimeGetQuery);
		getAndSetTimes(endTimeField, endTimeGetQuery);
		
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
		
		minTimesField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
				updateQuestionsCountAsked(minTimesField, minTimesSetQuery);
				getAndSetQuestionsCount(minTimesField, minTimesGetQuery);
			}
		});
		
		maxTimesField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
				updateQuestionsCountAsked(maxTimesField, maxTimesSetQuery);
				getAndSetQuestionsCount(maxTimesField, maxTimesGetQuery);
			}
		});
		
		endTimeField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
				updateTimes(endTimeField, endTimeSetQuery);
				getAndSetTimes(endTimeField, endTimeGetQuery);
			}
		});
		
		startTimeField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
				updateTimes(startTimeField, startTimeSetQuery);
				getAndSetTimes(startTimeField, startTimeGetQuery);
			}
		});
	}
	
	private void updateQuestionsCountAsked(TextField field, String query){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(query);
			
			prepare.setInt(1, Integer.parseInt(field.getText()));
			
			prepare.executeUpdate();
			Connector.disconnect();

		}catch(Exception ex){
			System.out.println("update min auestions count " + ex.toString());
			Connector.disconnect();
		}
	}
	
	private void getAndSetQuestionsCount(TextField field, String query){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(query);
			
			ResultSet resultSet = prepare.executeQuery();
			
			if(resultSet.next()){
				field.textProperty().set(""+resultSet.getInt(1));
			}
		
			Connector.disconnect();

		}catch(Exception ex){
			System.out.println("get and set questions count" + ex.toString());
			Connector.disconnect();
		}
	}
	
	private void updateTimes(TextField field, String query){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(query);
			
			prepare.setTime(1, java.sql.Time.valueOf(field.getText()));
			
			prepare.executeUpdate();
			Connector.disconnect();

		}catch(Exception ex){
			System.out.println("update times " + ex.toString());
			Connector.disconnect();
		}
	}
	
	private void getAndSetTimes(TextField field, String query){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(query);
			
			ResultSet resultSet = prepare.executeQuery();
			
			if(resultSet.next()){
				field.textProperty().set(resultSet.getTime(1).toString());
			}
		
			Connector.disconnect();

		}catch(Exception ex){
			System.out.println("get and set times" + ex.toString());
			Connector.disconnect();
		}
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
