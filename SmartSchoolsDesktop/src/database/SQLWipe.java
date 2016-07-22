package database;

import java.sql.SQLException;
import java.sql.Statement;
import application.Connector;

public class SQLWipe {
	private static final String setForeignKey0 = "SET FOREIGN_KEY_CHECKS = 0";
	private static final String truncateSleep= "truncate table sleep";
	private static final String truncateHeartRate= "truncate table heartRate";
	private static final String truncateDailyActivity= "truncate table dailyActivity";
	private static final String truncateDeviceEvent= "truncate table device_event";
	private static final String truncateUserDevice= "truncate table user_device";
	private static final String truncateQuestionsEvent= "truncate table questions_event";
	private static final String truncateQuestionsFeedback= "truncate table questions_feedback";
	private static final String truncateUserEvent= "truncate table user_event";
	private static final String truncateFeedbackEvent= "truncate table feedback_event";
	private static final String truncateEvent= "truncate table event";
	private static final String truncateFitbit = "truncate table fitbit";
	private static final String truncateDevice = "truncate table device";
	private static final String truncateFeedback = "truncate table feedback";
	private static final String truncateAccessedAP = "truncate table accessedAP";
	private static final String truncateUser = "truncate table user";
	private static final String setForeignKey1 = "SET FOREIGN_KEY_CHECKS = 1";
	
	public static String withACloth(){
		
		try{
			Connector.connect();
			Statement truncate = Connector.connection.createStatement();
			truncate.addBatch(setForeignKey0);
			truncate.addBatch(truncateSleep);
			truncate.addBatch(truncateHeartRate);
			truncate.addBatch(truncateDailyActivity);
			truncate.addBatch(truncateDeviceEvent);
			truncate.addBatch(truncateUserDevice);
			truncate.addBatch(truncateQuestionsEvent);
			truncate.addBatch(truncateQuestionsFeedback);
			truncate.addBatch(truncateUserEvent);
			truncate.addBatch(truncateFeedbackEvent);
			truncate.addBatch(truncateEvent);
			truncate.addBatch(truncateFitbit);
			truncate.addBatch(truncateDevice);
			truncate.addBatch(truncateFeedback);
			truncate.addBatch(truncateAccessedAP);
			truncate.addBatch(truncateUser);
			truncate.addBatch(setForeignKey1);
			truncate.executeBatch();
        	
        	Connector.disconnect();
        	return "Wipe Succeeded";
			
		}catch(SQLException e){
			return e.toString();
		}
	}
}
