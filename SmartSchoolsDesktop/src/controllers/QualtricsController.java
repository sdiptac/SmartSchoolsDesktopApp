package controllers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.Main;
import application.PageType.Type;
import database.CSVQuestion;
import database.Choice;
import database.Question;
import database.Question.QuestionType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("unused")
public class QualtricsController extends PageController {
	private final List<String> typeOfQuestions = Arrays.asList(Question.QuestionType.MUTI_CHOICE.name(), Question.QuestionType.FREE_RESPONSE.name(), Question.QuestionType.WHO_WITH.name(), Question.QuestionType.ANTICIPIATED.name());
	
	@FXML private ListView<String> typeList;
	@FXML private ListView<String> questionsList;
	@FXML private ListView<String> choicesList;
	
	@FXML private Label userLabel;
	@FXML private TextField searchByEmailText;
	@FXML private TextField searchByLastNameText;
	
	@FXML private Button addQuestionButton;
	@FXML private Button addChoiceButton;
	@FXML private Button deleteQuestionButton;
	@FXML private Button deleteChoiceButton;
	
	private Question.QuestionType currentSelectedTypeQuestion;
	private List<Question> currentQuestions;
	private List<Choice> currentChoices;
	
	private int currentQuestionID;
	private int currentChoiceID;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		
		typeList.setItems(FXCollections.observableArrayList(typeOfQuestions));
		
		typeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue == null){
		    		return;
		    	}
		    	currentSelectedTypeQuestion = Question.QuestionType.valueOf(newValue);
		    	clearListView(choicesList);
		    	clearListView(questionsList);
		    	
		    	setButtonStateTypePress();
		    	
		    	getAndSetQuestions();
		    }
		});
		
		questionsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue == null){
		    		return;
		    	}
		    	Question currentQuestion = currentQuestions.stream().filter(q -> q.getQuestion().equals(newValue)).findAny().orElse(null);
		    	if(currentQuestion != null){
		    		currentQuestionID = currentQuestion.getQuestionID();
		    		System.out.println(currentQuestionID);
		    	}else{
		    		System.out.println("null");
		    	}
		    	clearListView(choicesList);
		    	
		    	setButtonStateQuestionPress();
		    	
		    	getAndSetChoices();
		    }
		});
		
		choicesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue == null){
		    		return;
		    	}
		    	Choice currentChoice= currentChoices.stream().filter(c -> c.getChoice().equals(newValue)).findAny().orElse(null);
		    	if(currentChoice != null){
		    		currentChoiceID = currentChoice.getChoiceID();
		    		System.out.println(currentChoiceID);
		    	}else{
		    		System.out.println("null");
		    	}
				setButtonStateChoicePress();
		    }
		});
		
		questionsList.setOnEditCommit((ListView.EditEvent<String> e) -> {
			String newQuestionString = e.getNewValue();
			CSVQuestion.updateQuestionText(currentQuestionID, newQuestionString);
			getAndSetQuestions();
		});
		
		choicesList.setOnEditCommit((ListView.EditEvent<String> e) -> {
			String newChoiceString = e.getNewValue();
			CSVQuestion.updateChoiceText(currentChoiceID, currentQuestionID, newChoiceString);
			getAndSetChoices();
		});
    }
	
	private void setButtonStateTypePress(){
		if(currentSelectedTypeQuestion == QuestionType.ANTICIPIATED){
			addQuestionButton.disableProperty().set(true);
		}else{
			addQuestionButton.disableProperty().set(false);
		}
		
		addChoiceButton.disableProperty().set(true);
		deleteChoiceButton.disableProperty().set(true);
		deleteQuestionButton.disableProperty().set(true);
	}
	
	private void setButtonStateQuestionPress(){
		switch(currentSelectedTypeQuestion){
		case ANTICIPIATED:
			addQuestionButton.disableProperty().set(true);
			addChoiceButton.disableProperty().set(true);
    		deleteQuestionButton.disableProperty().set(true);
			break;
		case FREE_RESPONSE:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(true);
    		deleteQuestionButton.disableProperty().set(false);
			break;
		case MUTI_CHOICE:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(false);
    		deleteQuestionButton.disableProperty().set(false);
			break;
		case WHO_WITH:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(false);
    		deleteQuestionButton.disableProperty().set(false);
			break;
    	}
		deleteChoiceButton.disableProperty().set(true);
	}
	
	private void setButtonStateChoicePress(){
		switch(currentSelectedTypeQuestion){
		case ANTICIPIATED:
			addChoiceButton.disableProperty().set(true);
			deleteChoiceButton.disableProperty().set(true);
			break;
		case FREE_RESPONSE:
			addChoiceButton.disableProperty().set(true);
			deleteChoiceButton.disableProperty().set(true);
			break;
		case MUTI_CHOICE:
			addChoiceButton.disableProperty().set(false);
			deleteChoiceButton.disableProperty().set(false);
			break;
		case WHO_WITH:
			addChoiceButton.disableProperty().set(false);
			deleteChoiceButton.disableProperty().set(false);
			break;
    	}
	}
	
	private void getAndSetQuestions(){
		currentQuestions = CSVQuestion.getQuestions(currentSelectedTypeQuestion);
		List<String> questionsAsStrings = currentQuestions.stream().map(Question::getQuestion).collect(Collectors.toList());
		questionsList.setItems(FXCollections.observableArrayList(questionsAsStrings));		
		questionsList.getSelectionModel().clearSelection();
		
		questionsList.setCellFactory(TextFieldListCell.forListView());
		questionsList.getSelectionModel().clearSelection();
	}
	
	private void getAndSetChoices(){
		currentChoices = CSVQuestion.getChoices(currentQuestionID);
		List<String> choicesAsStrings = currentChoices.stream().map(Choice::getChoice).collect(Collectors.toList());
		if(choicesAsStrings.size() == 0){
			switch(currentSelectedTypeQuestion){
			case ANTICIPIATED:
				choicesAsStrings.add("This type of question must have Yes and No choices");
				break;
			case FREE_RESPONSE:
				choicesAsStrings.add("This type of question can't have choices");
				break;
			case WHO_WITH:
			case MUTI_CHOICE: choicesAsStrings.add("You must add a choice for this question or the mobile app will break");
				break;
	    	}
		}
		choicesList.setItems(FXCollections.observableArrayList(choicesAsStrings));
		choicesList.setCellFactory(TextFieldListCell.forListView());
		choicesList.getSelectionModel().clearSelection();
		
	}
	
	private void clearListView(ListView<String> view){
		view.setItems(null);
	}
	
	@FXML 
	private void addQuestionButtonPress(Event event) {
		if(CSVQuestion.addQuestion(currentSelectedTypeQuestion)){
			getAndSetQuestions();
		}else{
			
		}
	}
	
	@FXML 
	private void addChoicePress(Event event) {
		if(CSVQuestion.addChoice(currentQuestionID)){
			getAndSetChoices();
			System.out.println("insert succesfull");
		}else{
			System.out.println("insert not completed");
		}
	}
	
	@FXML 
	private void deleteQuestionPress(Event event) {
		if(CSVQuestion.deleteQuestion(currentQuestionID)){
			getAndSetQuestions();
			getAndSetChoices();
			System.out.println("delete succesfull");
		}else{
			System.out.println("delete not completed");
			createQuestionHasFeedbackAlert();
		}
	}
	
	@FXML 
	private void deleteChoicePress(Event event) {
		if(CSVQuestion.deleteChoice(currentChoiceID)){
			getAndSetChoices();
			System.out.println("delete succesfull");
		}else{
			System.out.println("delete not completed");
		}
	}
	
	private void createQuestionHasFeedbackAlert(){

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("Delete Questions Conflict");
		alert.setContentText("This question has already been sent to the users. If you delete it, you will delete their responses as well (permanently).");

		String exceptionText = CSVQuestion.getFeedBackFromQuestion(currentQuestionID);

		Label label = new Label("Here are the responses you'll be deleteing forever:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		Button doItAnywayButton = new Button("Do it anyway");
		doItAnywayButton.setOnAction(e -> {
			CSVQuestion.deleteFeedBackAndQuestion(currentQuestionID);
			getAndSetQuestions();
			getAndSetChoices();
			alert.close();
		});
		
		expContent.add(doItAnywayButton, 0, 2);
			
		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
