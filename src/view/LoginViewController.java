package view;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.network.Client;

public class LoginViewController {

    @FXML
    private GridPane loginPanel;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private TextField loginField;

    @FXML
    void login(ActionEvent event) {
    	
    	String login = loginField.getText();
    	System.out.println("login: " + login);
    	//Client client = new Client(login);
    	
    	
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
    	try {
        	//Parent root;
    		//AnchorPane anchorPane = (AnchorPane);
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));
    		AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
    		Client client = new Client(login);
    		client.runConnection();
    		
    		
    		GameViewController gameViewcontroller = fxmlLoader.<GameViewController>getController();
    		gameViewcontroller.setClient(client);
            client.startReadingData(gameViewcontroller);
    		
			Scene scene = new Scene(anchorPane);
	        stage.setScene(scene);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
        

    }

}