package database;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import application.Connector;
import application.ExportToCSV;


public class CSVQuestion {
	private static final String getQuestionsQuery = "select questionID, groupID, question from questions where typeOfQuestion = ? and groupid = 1";
	private static final String getQuestionResponsesToCSVQuery = "select first_name, last_name, email, typeOfEvent, typeOfQuestion, question, response, timeOfEvent, timeOfFeedback from feedback natural join feedback_event natural join event natural join questions_event natural join questions_feedback natural join user natural join questions natural join user_event group by feedbackid order by userid";
	private static final String getChoicesQuery = "select choiceID, choice from choices where questionID = ?";
	private static final String updateQuestionTextQuery = "update questions set question = ? where questionID = ?";
	private static final String updateChoiceTextQuery = "update choices set choice = ? where choiceID = ? and questionID = ?";
	private static final String addQuestionQuery = "insert into questions(typeOfQuestion, question, groupID) values(?, ?, ?)";
	private static final String addChoiceQuery = "insert into choices(questionID, choice) values(?, ?)";
	
	private static final String deleteQuestionSelectionQuery = "select * from questions natural join questions_event where questionID = ?";
	private static final String deleteQuestionQuery = "delete from questions where questionID = ?";
	private static final String deleteChoiceByQuestionQuery = "delete from choices where questionID = ?"; 
	private static final String deleteQuestionSelectFeedbackQuery = "select first_name, last_name, email, typeOfEvent, typeOfQuestion, question, response, timeOfEvent, timeOfFeedback , questionID from feedback natural join feedback_event natural join event natural join questions_event natural join questions_feedback natural join user natural join questions natural join user_event where questionid = ?";
	
	private static final String deleteChoiceQuery = "delete from choices where choiceID = ?";
	
	private static final String MULTI_CHOICE = "mc";
	private static final String WHO = "who";
	private static final String ANTICIPICATED = "anticipated";
	private static final String FREE_RESPONSE = "fr";
	
	
	public static String write(String absolutePath){
		ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(getQuestionResponsesToCSVQuery);
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("typeOfEvent");
        		row[4] = resultset.getString("typeOfQuestion");
        		row[5] = resultset.getString("question").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[6] = resultset.getString("response").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[7] = resultset.getString("timeOfEvent");
        		row[8] = resultset.getString("timeOfFeedback");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				if(!ExportToCSV.export(absolutePath, "QuestionData", new String[]{"First Name", "Last Name", "Email", "Time of Event", "Type of Question", "Question", "Response", "Time of Event", "Time of Feedback"}, info)){
					return "The number of columns provided does not match the number of columns in the data"; 
				}
			}catch(FileNotFoundException f){
				return "File Creation Error";
			}
		return "Export Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
	
	public static List<Question> getQuestions(Question.QuestionType type){
		
		List<Question> questions = new ArrayList<Question>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(getQuestionsQuery);
			
			String typeString = null;
			
			switch(type){
			case ANTICIPIATED: typeString = ANTICIPICATED;
				break;
			case FREE_RESPONSE: typeString = FREE_RESPONSE;
				break;
			case MUTI_CHOICE: typeString = MULTI_CHOICE;
				break;
			case WHO_WITH: typeString = WHO;
				break;
			}
			
			prepare.setString(1, typeString);
			
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return questions;
			}
			
        	do {        		
        		Question question = new Question(resultset.getInt("questionID"), resultset.getInt("groupID"), resultset.getString("question"), type);

        		questions.add(question);
        	} while (resultset.next());
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		Connector.disconnect();
		return questions;
	}
	
	public static List<Choice> getChoices(int questionID){
		
		List<Choice> choices = new ArrayList<Choice>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(getChoicesQuery);
			
			prepare.setInt(1, questionID);
			
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				System.out.println("get choices is empty");
				return choices;
			}
			
        	do {        		
        		Choice choice = new Choice(resultset.getInt("choiceID"), questionID, resultset.getString("choice"));

        		choices.add(choice);
        	} while (resultset.next());
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		Connector.disconnect();
		return choices;
	}
	
	public static boolean updateQuestionText(int questionID, String newQuestion){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(updateQuestionTextQuery);
			
			prepare.setString(1, newQuestion);
			prepare.setInt(2, questionID);
			
			int rows = prepare.executeUpdate();
			Connector.disconnect();
			if (rows > 0){ 
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("updateQuestionText" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static boolean updateChoiceText(int choiceID, int questionID, String newChoiceText){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(updateChoiceTextQuery);
			
			System.out.println(choiceID);
			System.out.println(newChoiceText);
			
			prepare.setString(1, newChoiceText);
			prepare.setInt(2, choiceID);
			prepare.setInt(3, questionID);
			
			int rows = prepare.executeUpdate();
			Connector.disconnect();
			if (rows > 0){ 
				System.out.println("updated");
				return true;
			}else{
				System.out.println("not updated ");
				return false;
			}
		}catch(Exception e){
			System.out.println("updateChoiceText" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static boolean addQuestion(Question.QuestionType type){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(addQuestionQuery);
			
			String typeString = null;
			
			switch(type){
			case ANTICIPIATED: typeString = ANTICIPICATED;
				break;
			case FREE_RESPONSE: typeString = FREE_RESPONSE;
				break;
			case MUTI_CHOICE: typeString = MULTI_CHOICE;
				break;
			case WHO_WITH: typeString = WHO;
				break;
			}
			
			prepare.setString(1, typeString);
			prepare.setString(2, "Placeholder Text " + (int)(Math.random()*10000));
			prepare.setInt(3, 1);
			
			int rows = prepare.executeUpdate();
			Connector.disconnect();
			if (rows > 0){ 
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("add qusetion" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static boolean addChoice(int questionID){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(addChoiceQuery);
						
			prepare.setInt(1, questionID);
			prepare.setString(2, "Placeholder Text "+ (int)(Math.random()*10000));
			
			int rows = prepare.executeUpdate();
			Connector.disconnect();
			if (rows > 0){ 
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("add choice" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static boolean deleteQuestion(int questionID){
		try{
			Connector.connect();
			PreparedStatement preparedSelect = Connector.connection.prepareStatement(deleteQuestionSelectionQuery);
			preparedSelect.setInt(1, questionID);
			
			ResultSet resultset  = preparedSelect.executeQuery();
			if (!resultset.next()){ 
			
				PreparedStatement prepareChoice = Connector.connection.prepareStatement(deleteChoiceByQuestionQuery);
				prepareChoice.setInt(1, questionID);
				
				prepareChoice.executeUpdate();

				PreparedStatement prepare = Connector.connection.prepareStatement(deleteQuestionQuery);
				prepare.setInt(1, questionID);
				
				int rows = prepare.executeUpdate();
				Connector.disconnect();
				if (rows > 0){ 
					System.out.println("rows greater than 0");
					return true;
				}else{
					System.out.println("rows less than 0");
					return false;
				}
			}else{
				Connector.disconnect();
				System.out.println("here in else");
				return false;
			}
		}catch(Exception e){
			System.out.println("delete question" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static boolean deleteChoice(int choiceID){
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(deleteChoiceQuery);
			prepare.setInt(1, choiceID);
			
			int rows = prepare.executeUpdate();
			Connector.disconnect();
			if (rows > 0){ 
				System.out.println("rows greater than 0");
				return true;
			}else{
				System.out.println("rows less than 0");
				return false;
			}
		}catch(Exception e){
			System.out.println("delete question" + e.toString());
			Connector.disconnect();
			return false;
		}
	}
	
	public static String getFeedBackFromQuestion(int questionID){
	ArrayList<String[]> info = new ArrayList<String[]>();
		
		try{
			Connector.connect();
			PreparedStatement prepare = Connector.connection.prepareStatement(deleteQuestionSelectFeedbackQuery);
			prepare.setInt(1, questionID);
			
			ResultSet resultset  = prepare.executeQuery();
			if (!resultset.next()){ 
				return "No entries returned";
			}
			
        	do {
        		String[] row = new String[9];
        		row[0] = resultset.getString("first_name");
        		row[1] = resultset.getString("last_name");
        		row[2] = resultset.getString("email");
        		row[3] = resultset.getString("typeOfEvent");
        		row[4] = resultset.getString("typeOfQuestion");
        		row[5] = resultset.getString("question").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[6] = resultset.getString("response").replaceAll(",", "\",\"").replace('\n', Character.MIN_VALUE);
        		row[7] = resultset.getString("timeOfEvent");
        		row[8] = resultset.getString("timeOfFeedback");
        		info.add(row);
        	} while (resultset.next());
        	
        	Connector.disconnect();

			try{
				StringBuilder builder = new StringBuilder();
				builder.append(info.stream().map(row -> Arrays.stream(row).collect(Collectors.joining(", "))).collect(Collectors.joining("\n")));
				return builder.toString();
			}catch(Exception e){
				return e.toString();
			}			
		}catch(SQLException e){
			return e.toString();
		}
	}
	
	public static boolean deleteFeedBackAndQuestion(int questionID){
		try{
			Connector.connect();
			PreparedStatement prepareChoice = Connector.connection.prepareStatement(deleteChoiceByQuestionQuery);
			prepareChoice.setInt(1, questionID);
			
	
			int rowsChoice = prepareChoice.executeUpdate();
			
			final String deleteQuestionsEvent = "delete from questions_event where questionID = ?";

			PreparedStatement prepareQuestionEvent  = Connector.connection.prepareStatement(deleteQuestionsEvent);
			prepareQuestionEvent.setInt(1, questionID);
			
			int rowsQuestionEvent  = prepareQuestionEvent.executeUpdate();
			
			final String deleteQuestionsFeedback = "delete from questions_feedback where questionID = ?";

			PreparedStatement prepareQuestionFeedback  = Connector.connection.prepareStatement(deleteQuestionsFeedback);
			prepareQuestionFeedback.setInt(1, questionID);
			
			int rowsQuestionFeedback  = prepareQuestionFeedback.executeUpdate();
			
			Connector.disconnect();
			
			return 	deleteQuestion(questionID);
		}catch(Exception e){
			Connector.disconnect();
			return false;
		}
	}
}


