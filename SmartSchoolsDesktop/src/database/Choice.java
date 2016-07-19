package database;

public class Choice {
	private int choiceID, questionID;
	private String choice;
	
	public Choice(int choiceID, int questionID, String choice){
		this.choiceID = choiceID;
		this.questionID = questionID;
		this.choice = choice;
	}

	public int getChoiceID() {
		return choiceID;
	}

	public int getQuestionID() {
		return questionID;
	}

	public String getChoice() {
		return choice;
	}
}
