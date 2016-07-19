package database;

public class Question {
	public enum QuestionType {
		MUTI_CHOICE, 
		FREE_RESPONSE,
		WHO_WITH,
		ANTICIPIATED
	}
	
	private int questionID;
	private int groupID;
	private String question;
	private QuestionType type;
	
	public Question(int questionID, int groupID, String question, QuestionType type){
		this.questionID = questionID;
		this.groupID = groupID;
		this.question = question;
		this.type = type;
	}

	public int getQuestionID() {
		return questionID;
	}

	public int getGroupID() {
		return groupID;
	}

	public String getQuestion() {
		return question;
	}

	public QuestionType getType() {
		return type;
	}
}
