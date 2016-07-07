package controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import application.CSVLocation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class UsersController extends PageController {
	@FXML private ListView<String> userListView;
	@FXML private Label userLabel;
	@FXML private TextField searchByEmailText;
	@FXML private TextField searchByLastNameText;
	
	private String currentUserID;
	private String currentFirstName;
	private String currentLastName;
	private String currentEmail;
	
	private List<String> userList; 
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		startFillUserListThread();
		
		userListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue == null){
		    		return;
		    	}
		        String[] info = newValue.split("\\s+");
		        currentUserID = info[0].trim();
		        currentFirstName = info[1].trim();
		        currentLastName = info[2].trim();
		        currentEmail = info[3].trim();

		        userLabel.setText(String.format("%s %s\n%s", currentFirstName, currentLastName, currentEmail));
		    }
		});
    }
	
	@FXML
	protected void sortByUserIDPress(Event event) {	
		sortUserList(0);	
	}
	
	@FXML
	protected void sortByLastNamePress(Event event) {
		sortUserList(2);	
	}
	
	@FXML
	protected void sortByEmailPress(Event event) {
		sortUserList(3);	
	}
	
	@FXML
	protected void clearSearchPress(Event event) {
		searchByEmailText.setText("");
		searchByLastNameText.setText("");
		
		ObservableList<String> userNames = FXCollections.observableArrayList(userList);
		userListView.setItems(userNames);
		sortUserList(0);
	}
	
	@FXML
	protected void searchByLastNameTextChanged(Event event){
		searchByEmailText.setText("");
		
		String searchByLastName = searchByLastNameText.getText().toLowerCase();
		searchByField(searchByLastName, 2);
	}
	
	@FXML
	protected void searchByEmailTextChanged(Event event){
		searchByLastNameText.setText("");
		
		String searchByEmail = searchByEmailText.getText().toLowerCase();
		searchByField(searchByEmail, 3);
	}
	
	private void searchByField(String text, int col){
		ObservableList<String> newList = FXCollections.observableArrayList(userList.parallelStream().filter(row -> row.split("\\s+")[col].toLowerCase().contains(text)).collect(Collectors.toList()));
		userListView.setItems(newList);
	}
	
	private void sortUserList(int col){
		List<String> newList = userListView.getItems();
		newList = newList.parallelStream().sorted((String first, String second) -> 
   	 		first.split("\\s+")[col].compareTo(second.split("\\s+")[col])
		).collect(Collectors.toList());
	
		ObservableList<String> userNames = FXCollections.observableArrayList(newList);
		userListView.setItems(userNames);
	}
		
	private void startFillUserListThread(){
		Runnable userListTask = () -> {
			fillUserList();
		};
		new Thread(userListTask).start();
	}
	
	private void fillUserList(){
		List<String> list = CSVLocation.findAllUsers().stream().map((String[] row) -> Arrays.stream(row).collect(Collectors.joining(", "))).collect(Collectors.toList());
		list = list.parallelStream().map(row -> row = String.format("%-4s %s %s %s", (Object[])row.split(","))).collect(Collectors.toList());
		userList = list;
		
		ObservableList<String> userNames = FXCollections.observableArrayList (list);
		userListView.setItems(userNames);
	}
}
