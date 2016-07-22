package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import application.Connector;
import application.Main;
import controllers.LocationController.AccessPoint;
import database.CSVLocation;
import database.CSVQuestion;
import database.UploadAccessPoint;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class LocationController extends PageController {
	@FXML private Button printButton;
	@FXML private Button uploadAcessPointsButton;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		final FileChooser fileChooser = new FileChooser();
		
		printButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = dirChooser.showDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	 CSVLocation.write(file.getAbsolutePath().toString());
		                    }
		                }
		            });
		
		uploadAcessPointsButton.setOnAction(
				 new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = fileChooser.showOpenDialog(Main.getInstance().getCurrentStage().getScene().getWindow());
		                    if (file != null) {
		                    	parseCSV(file);
		                    }
		                }
		            });
	}
	
	private void parseCSV(File file){
		//ssid, bssid, room #, building, org, ap number
		System.out.println("in here");
		InputStream is;
		try {
			is = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			List<String[]> aps = br.lines().map(l -> l.split(",")).collect(Collectors.toList());
			
			br.close();
			is.close();
			
			if(aps.size() == 0){
				createParseErrorAlert("There are no access points found in this file.");
			}else{
				createQuestionHasFeedbackAlert(aps.parallelStream().map(ap -> new AccessPoint(ap[0], ap[1], ap[2], ap[3], ap[4], ap[5])).collect(Collectors.toList()));
			}
			
		} catch (Exception e) {
			createParseErrorAlert("Exception: "+e.toString());
		}
	}
	
	
	private void createQuestionHasFeedbackAlert(List<AccessPoint> aps){

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Access Points");
		alert.setHeaderText("Uploading access points.");
	
		TableView<AccessPoint> table = new TableView<AccessPoint>();
			
		TableColumn<AccessPoint, String> ssidCol = new TableColumn<AccessPoint, String>("SSID");
		ssidCol.setMinWidth(10);
		ssidCol.setCellValueFactory(cellData -> cellData.getValue().getSsidProperty());
		
        TableColumn<AccessPoint, String> bssidCol = new TableColumn<AccessPoint, String>("BSSID");
        bssidCol.setMinWidth(17);
        bssidCol.setCellValueFactory(cellData -> cellData.getValue().getBssidProperty());
        
        TableColumn<AccessPoint, String> roomCol = new TableColumn<AccessPoint, String>("Room");
        roomCol.setMinWidth(10);
        roomCol.setCellValueFactory(cellData -> cellData.getValue().getRoomProperty());
        
        TableColumn<AccessPoint, String> buildingCol = new TableColumn<AccessPoint, String>("Building");
        buildingCol.setMinWidth(10);
        buildingCol.setCellValueFactory(cellData -> cellData.getValue().getBuildingProperty());
        
        TableColumn<AccessPoint, String> orgCol = new TableColumn<AccessPoint, String>("Organization");
        orgCol.setMinWidth(12);
        orgCol.setCellValueFactory(cellData -> cellData.getValue().getOrgProperty());
        
        TableColumn<AccessPoint, String> apCol = new TableColumn<AccessPoint, String>("AP Number");
        apCol.setMinWidth(10);
        apCol.setCellValueFactory(cellData -> cellData.getValue().getApNumberProperty());
        
        TableColumn<AccessPoint, String> uploadingCol = new TableColumn<AccessPoint, String>("Upload Status");
        uploadingCol.setMinWidth(10);
        uploadingCol.setCellValueFactory(cellData -> cellData.getValue().getIsDoneUploadingProperty());
        
        table.getColumns().addAll(ssidCol, bssidCol, roomCol, buildingCol, orgCol, apCol, uploadingCol);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(new Label("Access Points"), table);
	    
		Button uploadButton = new Button("Upload");
		uploadButton.setOnAction(e -> {
			alert.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
			uploadAPs(table);
			alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
		});
		
		vbox.getChildren().add(uploadButton);
		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(vbox);

		table.setItems(FXCollections.observableArrayList(aps));
		
		alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		
		alert.showAndWait();
	}
	
	private void uploadAPs(TableView<AccessPoint> table){
		Connector.connect();
		table.getItems().parallelStream().forEach(ap -> ap.setUploading(UploadAccessPoint.uploadAP(ap)));
		Connector.disconnect();
	}
	
	public static class AccessPoint{
		private final StringProperty ssidProperty;
		private final StringProperty bssidProperty;
		private final StringProperty roomProperty;
		private final StringProperty buildingProperty;
		private final StringProperty orgProperty;
		private final StringProperty apNumberProperty;
		private final StringProperty doneUploadingProperty;
		
		public StringProperty getSsidProperty() {
			return ssidProperty;
		}

		public StringProperty getBssidProperty() {
			return bssidProperty;
		}

		public StringProperty getRoomProperty() {
			return roomProperty;
		}

		public StringProperty getBuildingProperty() {
			return buildingProperty;
		}

		public StringProperty getOrgProperty() {
			return orgProperty;
		}

		public StringProperty getApNumberProperty() {
			return apNumberProperty;
		}
		
		public StringProperty getIsDoneUploadingProperty() {
			return doneUploadingProperty;
		}
		
		public String getSsid() {
			return ssidProperty.get();
		}

		public String getBssid() {
			return bssidProperty.get();
		}

		public String getRoom() {
			return roomProperty.get();
		}

		public String getBuilding() {
			return buildingProperty.get();
		}

		public String getOrg() {
			return orgProperty.get();
		}

		public String getApNumber() {
			return apNumberProperty.get();
		}
		
		public String getIsDoneUploading() {
			return doneUploadingProperty.get();
		}

		private AccessPoint(String ssid, String bssid, String room, String building, String org, String apNumber) {
            this.ssidProperty = new SimpleStringProperty(ssid);
            this.bssidProperty = new SimpleStringProperty(bssid);
            this.roomProperty = new SimpleStringProperty(room);
            this.buildingProperty = new SimpleStringProperty(building);
            this.orgProperty = new SimpleStringProperty(org);
            this.apNumberProperty = new SimpleStringProperty(apNumber);
            this.doneUploadingProperty = new SimpleStringProperty("");
		}
		
		public void setUploading(String status) {
			doneUploadingProperty.set(status);
        }
	}
	
	private void createParseErrorAlert(String error){

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Access Points");
		alert.setHeaderText("Upload a CSV File.");
		alert.setContentText("The format of the file is incorrect. Refer to Help for the correct format.");

		String exceptionText = error;

		Label label = new Label("Error:");

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
	
	@Override
	protected void viewHelpPress(Event event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Location Help");
		alert.setContentText("To upload access points, the access points should be in a CSV file (comma seperated values -- excel can save to that format). "
				+ "The file should not contain a header for the columns. The columns should be in this order: ssid, bssid, room, building, organization, and then access point number.\n\n"
				+ "If the access point doesn't exist in the database, it will say \"Successful\" if it uploaded. If the access point already exists, it will say \"Updated\" if it uploaded.");

		alert.showAndWait();
	}
}
