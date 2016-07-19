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
		    	
		    	if(currentSelectedTypeQuestion == Question.QuestionType.ANTICIPIATED){
		    		addQuestionButton.disableProperty().set(true);
		    	}else{
		    		addQuestionButton.disableProperty().set(false);
		    	}
		    	
		    	if(currentSelectedTypeQuestion == Question.QuestionType.FREE_RESPONSE){
		    		addChoiceButton.disableProperty().set(true);
		    	}else{
		    		addChoiceButton.disableProperty().set(false);
		    	}
		    	
	    		deleteChoiceButton.disableProperty().set(true);
	    		deleteQuestionButton.disableProperty().set(true);
		    	
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
		    	
		    	switch(currentSelectedTypeQuestion){
				case ANTICIPIATED:
					addChoiceButton.disableProperty().set(true);
		    		deleteChoiceButton.disableProperty().set(true);
		    		deleteQuestionButton.disableProperty().set(true);
					break;
				case FREE_RESPONSE:
					break;
				case MUTI_CHOICE:
					break;
				case WHO_WITH:
					break;
		    	}
		    	
		    	
		    	if(currentSelectedTypeQuestion == Question.QuestionType.ANTICIPIATED){
		    		addChoiceButton.disableProperty().set(true);
		    		deleteChoiceButton.disableProperty().set(true);
		    		deleteQuestionButton.disableProperty().set(true);
		    	}else{
		    		addChoiceButton.disableProperty().set(false);
		    		deleteChoiceButton.disableProperty().set(false);
		    		deleteQuestionButton.disableProperty().set(false);
		    	}
		    	
		    	if(currentSelectedTypeQuestion == Question.QuestionType.FREE_RESPONSE){
		    		addChoiceButton.disableProperty().set(true);
		    	}else{
		    		addChoiceButton.disableProperty().set(false);
		    	}
		    	
		    	getAndSetChoices();
		    }
		});
		
		questionsList.setOnEditCommit((ListView.EditEvent<String> e) -> {
			String newQuestionString = e.getNewValue();
			CSVQuestion.updateQuestionText(currentQuestionID, newQuestionString);
			getAndSetQuestions();
		});
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
			choicesAsStrings.add("This question cannot have choices or you haven't added any so far");
		}
		choicesList.setItems(FXCollections.observableArrayList(choicesAsStrings));
		choicesList.getSelectionModel().clearSelection();
	}
	
	private void clearListView(ListView<String> view){
		view.setItems(null);
	}
	
	@FXML 
	private void addQuestionButtonPress(Event event) {
		if(CSVQuestion.addQuestionText(currentSelectedTypeQuestion)){
			getAndSetQuestions();
		}else{
			
		}
	}
	
	@FXML 
	private void addChoicePress(Event event) {
		
	}
	
	@FXML 
	private void deleteQuestionPress(Event event) {
		
	}
	
	@FXML 
	private void deleteChoicePress(Event event) {
		
	}
}
