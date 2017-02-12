package view;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    	Client client = new Client(login);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));
		AnchorPane anchorPane = null;
    	try {
    		anchorPane = (AnchorPane) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	GameViewController gameViewcontroller = fxmlLoader.<GameViewController>getController();
    	try {
			client.runConnection();
			gameViewcontroller.setClient(client);
	        client.startReadingData(gameViewcontroller);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			      public void handle(WindowEvent we) {
			          client.stopReadingData();
			          System.exit(0);
			      }
			  }); 
			
			Scene scene = new Scene(anchorPane);
			stage.setResizable(false);
	        stage.setScene(scene);
	        stage.show();
		} catch (IOException e) {
	    	Platform.runLater(() -> gameViewcontroller.serverConnectionAlert());
	    	System.err.println(" I/O Exception");
		}
    }

}