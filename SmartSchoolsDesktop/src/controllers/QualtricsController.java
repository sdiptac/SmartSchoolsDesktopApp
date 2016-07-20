package controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.PageType.Type;
import database.CSVQuestion;
import database.Choice;
import database.Question;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.Alert

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
		    	
		    	setButtonState();
		    	
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
		    	
		    	setButtonState();
		    	
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
	
	private void setButtonState(){
		switch(currentSelectedTypeQuestion){
		case ANTICIPIATED:
			addQuestionButton.disableProperty().set(true);
			addChoiceButton.disableProperty().set(true);
    		deleteChoiceButton.disableProperty().set(true);
    		deleteQuestionButton.disableProperty().set(true);
			break;
		case FREE_RESPONSE:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(true);
			deleteChoiceButton.disableProperty().set(true);
    		deleteQuestionButton.disableProperty().set(false);
			break;
		case MUTI_CHOICE:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(false);
			deleteChoiceButton.disableProperty().set(false);
    		deleteQuestionButton.disableProperty().set(false);
			break;
		case WHO_WITH:
			addQuestionButton.disableProperty().set(false);
			addChoiceButton.disableProperty().set(false);
			deleteChoiceButton.disableProperty().set(false);
    		deleteQuestionButton.disableProperty().set(false);
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
			case MUTI_CHOICE: choicesAsStrings.add("You must add a choice for this question or the app will break");
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
		}
	}
	
	@FXML 
	private void deleteChoicePress(Event event) {
		
	}
	
	private void createQuestionHasFeedbackAlert(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Look, an Exception Dialog");
		alert.setContentText("Could not find file blabla.txt!");

		Exception ex = new FileNotFoundException("Could not find file blabla.txt");

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

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

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
