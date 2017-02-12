package view;

import java.util.ArrayList;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.cards.Card;
import model.cards.CardValue;
import model.cards.Suit;
import model.network.Client;
import model.network.DataFromClient;
import model.network.DataFromServer;

public class GameViewController {
	@FXML private HBox clientCardsHBox;
	@FXML private HBox hboxCenter;
	@FXML private HBox buttonsHBox;
	@FXML private Button takeCardButton;
    @FXML private Button startGameButton;
    @FXML private Button confirmButton;
    @FXML private Button endTurnButton;
    @FXML private Button takePenaltyCardsButton;
    
	private DataFromServer clientData = null;
	private Client client;
	
	public void setClient(Client client){
		this.client = client;
	}
	
	public void setDataFromServer(DataFromServer dataFromServer){
		switch(dataFromServer.getPacketId()){
			case 1: //start packet
				clientData = dataFromServer;
				break;
			case 2: //endTurn packet
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				checkWhosTurn();
				break;
			case 3: //takeCard packet
				for(Card card : dataFromServer.getClientCards())
					clientData.getClientCards().add(card);
				setClientCardsClickability();
				showClientCards();
				takeCardButton.setVisible(false);
				endTurnButton.setVisible(true);
				break;
			case 4: //normal queue packet
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				if(!(dataFromServer.getCardsOnTable()==null)){
					for(Card card : dataFromServer.getCardsOnTable()){
						System.out.println("Karta od gracza drugiego: " + card.getCardValue() + ' ' + card.getSuit());
						clientData.getCardsOnTable().add(card);
					}
				}
				takeCardButton.setVisible(true);
				setClientCardsClickability();
				showClientCards();
				showCardsOnTable();
				checkWhosTurn();
				break;
			case 5: // TWO is on Table
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				for(Card card : dataFromServer.getCardsOnTable()){
					clientData.getCardsOnTable().add(card);
				}
				takePenaltyCardsButton.setVisible(true);
				takeCardButton.setVisible(true);
				confirmButton.setDisable(false);
				setClientCardsClickability("TWO", "THREE");
				showClientCards();
				showCardsOnTable();
				break;
			case 6: // THREE is on Table
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				for(Card card : dataFromServer.getCardsOnTable()){
					clientData.getCardsOnTable().add(card);
				}
				takePenaltyCardsButton.setVisible(true);
				takeCardButton.setVisible(true);
				confirmButton.setDisable(false);
				setClientCardsClickability("THREE", "TWO");
				showClientCards();
				showCardsOnTable();
				break;
			case 7://FOUR is on Table
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				for(Card card : dataFromServer.getCardsOnTable()){
					clientData.getCardsOnTable().add(card);
				}
				takeCardButton.setVisible(true);
				endTurnButton.setVisible(true);
				setClientCardsClickabilityByValue("FOUR");
				showClientCards();
				showCardsOnTable();
				break;
			case 8: //take penalty cards packet
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				for(Card card : dataFromServer.getCardsOnTable()){
					clientData.getClientCards().add(card);
				}
				takeCardButton.setVisible(true);
				setClientCardsClickability();
				showClientCards();
				showCardsOnTable();
				break;
			case 9:
				jackChooseReqest();
				break;
			case 10://ACE : give request
				aceChooseReqest();
				break;
			case 11://ACE on the table with reqest
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				if(!(dataFromServer.getCardsOnTable()==null)){
					for(Card card : dataFromServer.getCardsOnTable()){
						clientData.getCardsOnTable().add(card);
					}
				}
				takeCardButton.setVisible(true);
				System.out.println(dataFromServer.getRequest());
				setClientCardsClickabilityBySuit(dataFromServer.getRequest());
				showClientCards();
				showCardsOnTable();
				checkWhosTurn();
				break;
			case 12://JACK on the table with reqest
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				if(!(dataFromServer.getCardsOnTable()==null)){
					for(Card card : dataFromServer.getCardsOnTable()){
						clientData.getCardsOnTable().add(card);
					}
				}
				takeCardButton.setVisible(true);
				System.out.println(dataFromServer.getRequest());
				setClientCardsClickabilityByValue(dataFromServer.getRequest());
				showClientCards();
				showCardsOnTable();
				checkWhosTurn();
				break;
		}
	}
	

	@FXML
	public void initialize(){
		buttonsHBox.setVisible(false);
		endTurnButton.setVisible(false);
		takePenaltyCardsButton.setVisible(false);
	}
	

	@FXML
	private void startGame(){
		checkWhosTurn();
		buttonsHBox.setVisible(true);
		confirmButton.setVisible(false);
		showClientCards();
		showCardsOnTable();
		startGameButton.setVisible(false);
		
	}
	
	@FXML
	private void takeCard(){
		client.sendEmptyPacket(3);
		hBoxTranslateX -= 10;
	}
	@FXML
    void takePenaltyCards() {
		client.sendEmptyPacket(4);
		takePenaltyCardsButton.setVisible(false);
    }
	
	@FXML
    private void confirm() {
		for(Card card : cardsClicked){
			clientData.getClientCards().remove(card);
		}
		System.out.println(cardsClicked.get(0).getCardValue() +" "+ cardsClicked.get(0).getSuit() );
		DataFromClient dataFromClient = new DataFromClient(1, cardsClicked);
		client.sendPackage(dataFromClient);
		checkWhosTurn();
		showClientCards();
		System.out.println("cardsClicked.size(): "+cardsClicked.size());
		cardsClicked.clear();
		confirmButton.setVisible(false);
		endTurnButton.setVisible(false);
		takePenaltyCardsButton.setVisible(false);
		if(clientData.getClientCards().isEmpty()){
			gameWonAlert();
		}
		hBoxTranslateX += 14;
    }
	@FXML
    void endTurn() {
		client.sendEmptyPacket(2);
		endTurnButton.setVisible(false);
		takeCardButton.setVisible(true);
    }
	private void checkWhosTurn(){
		int clientId = clientData.getClientId();
		System.out.println("ID: " + clientId);
		System.out.println("whosetrn: " + clientData.getWhoseTurn());
		if(clientData.getWhoseTurn()!=clientId){
			takeCardButton.setDisable(true);
			confirmButton.setDisable(true);
		}else{
			takeCardButton.setDisable(false);
			confirmButton.setDisable(false);
		}
	}
	
	private void setClientCardsClickability(){
		CardValue cardOnTableValue = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getCardValue();
		Suit cardOnTableSuit = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getSuit();
		for(Card card : clientData.getClientCards()){
			if((card.getCardValue().equals(cardOnTableValue)) || (card.getSuit().equals(cardOnTableSuit))){
				card.setClickable(true);
			}else{
				card.setClickable(false);
			}
		}
	}
	private void setClientCardsClickability(String cardValue, String secondCardValue){
		CardValue cardOnTableValue = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getCardValue();
		Suit cardOnTableSuit = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getSuit();
		for(Card card : clientData.getClientCards()){
			if(((card.getCardValue().toString().equals(cardValue)) 
					|| ((card.getCardValue().toString().equals(secondCardValue))&& card.getSuit().equals(cardOnTableSuit)))){
				card.setClickable(true);
			}else{
				card.setClickable(false);
			}
		}
	}
	private void setClientCardsClickabilityBySuit(String cardSuit){
		for(Card card : clientData.getClientCards()){
			System.out.println("SUIT: " + card.getSuit().toString());
			if(card.getSuit().toString().equalsIgnoreCase(cardSuit)){
				System.out.println("TRUE");
				card.setClickable(true);
			}else{
				card.setClickable(false);
			}
		}
	}
	private void setClientCardsClickabilityByValue(String cardValue){
		for(Card card : clientData.getClientCards()){
			if(card.getCardValue().toString().equals(cardValue)){
				card.setClickable(true);
			}else{
				card.setClickable(false);
			}
		}
	}
	
	ArrayList<Card> cardsClicked = new ArrayList<Card>();
	
	private void cardClicked(ImageView imageView, Card card){
		if(cardsClicked.isEmpty()){
			confirmButton.setVisible(true);
			imageView.setTranslateY(-20);
			cardsClicked.add(card);
		}else if(cardsClicked.contains(card)){
			confirmButton.setVisible(false);
			cardsClicked.remove(card);
			imageView.setTranslateY(0);	
		}else{
			for(Card cardClicked : cardsClicked){
				if(cardClicked.getCardValue().equals(card.getCardValue())){
					imageView.setTranslateY(-20);
					cardsClicked.add(card);
				}
			}
		}
	}
	private double hBoxTranslateX = 0;
	private void showClientCards(){
		double clientCardsTranslateX = 0;
		System.out.println("SHOW");
		clientCardsHBox.getChildren().clear();
		for(Card card : clientData.getClientCards()){
			ImageView imageView = new ImageView();
			String path = "../resources/images/" + card.getCardValue() + "_of_" + card.getSuit().toString().toLowerCase() +".png";
			Image image = new Image(getClass().getResourceAsStream(path));
			imageView.setImage(image);
			imageView.setFitHeight(181.5);
			imageView.setFitWidth(125);
			imageView.setTranslateX(clientCardsTranslateX);
			if(card.getClickable()){
				imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				    public void handle(MouseEvent me) {
				    	cardClicked(imageView, card);
				    }
				});
			}
			clientCardsHBox.getChildren().add(imageView);
			
			//hBoxTranslateX += 25;
			clientCardsTranslateX-=100;
		}
		
		clientCardsHBox.setTranslateX(hBoxTranslateX);
		
	}
	private void showCardsOnTable(){
		double cardsOnTableTranslateX = 0;
		hboxCenter.getChildren().clear();
		int i = 0;
		if(clientData.getCardsOnTable().size()>3){
			i = clientData.getCardsOnTable().size()-3;
		}
			
		for(i=i; i<clientData.getCardsOnTable().size();i++){
			ImageView imageView = new ImageView();
			Card card = clientData.getCardsOnTable().get(i);
			String path = "../resources/images/" + card.getCardValue() + "_of_" + card.getSuit().toString().toLowerCase() +".png";
			Image image = new Image(getClass().getResourceAsStream(path));
			imageView.setImage(image);
			imageView.setFitHeight(181.5);
			imageView.setFitWidth(125);
			imageView.setLayoutX(200);
			imageView.setTranslateX(cardsOnTableTranslateX);
			
			hboxCenter.getChildren().add(imageView);
		
			cardsOnTableTranslateX-=100;
		}
		

	}
	void aceChooseReqest() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Rz¹danie");
		alert.setHeaderText("Wybierz jaki kolor kart rz¹dasz:");
		alert.initModality(Modality.APPLICATION_MODAL);
		
		ButtonType diamondsButton = new ButtonType("Karo");
		ButtonType heartsButton = new ButtonType("Kier");
		ButtonType clubsButton = new ButtonType("Trefl");
		ButtonType spadesButton = new ButtonType("Pik");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStyleClass().remove("alert");
		dialogPane.getStyleClass().remove("context");
		
		alert.getButtonTypes().setAll(diamondsButton, clubsButton, spadesButton, heartsButton);
		alert.initStyle(StageStyle.UTILITY);
		
		String choose = null;
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == diamondsButton){
		    System.out.println("Wybrano Karo");
		    choose = "DIAMONDS";
		} else if (result.get() == clubsButton) {
			 System.out.println("Wybrano Trefl");
			 choose = "CLUBS";
		} else if (result.get() == spadesButton) {
			 System.out.println("Wybrano Pik");
			 choose = "SPADES";
		} else if(result.get() == heartsButton){
			 System.out.println("Wybrano Kier");
			 choose = "HEART";
		}
		System.out.println("Wybrano: "+choose);

		DataFromClient dataFromClient = new DataFromClient(5, choose);
		client.sendPackage(dataFromClient);
		System.out.println("po wyslaniu");
    }
	
	void jackChooseReqest() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Rz¹danie");
		alert.setHeaderText("Wybierz wartoœæ karty której rz¹dasz:");
		alert.initModality(Modality.APPLICATION_MODAL);
		
		ButtonType button5 = new ButtonType("5");
		ButtonType button6 = new ButtonType("6");
		ButtonType button7 = new ButtonType("7");
		ButtonType button8 = new ButtonType("8");
		ButtonType button9 = new ButtonType("9");
		ButtonType button10 = new ButtonType("10");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStyleClass().remove("alert");
		dialogPane.getStyleClass().remove("context");
		
		alert.getButtonTypes().setAll(button5,button6,button7,button8,button9,button10);
		alert.initStyle(StageStyle.UTILITY);
		
		String choose = null;
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == button5){
		    System.out.println("Wybrano 5");
		    choose = "FIVE";
		} else if (result.get() == button6) {
			 System.out.println("Wybrano 6");
			 choose = "SIX";
		} else if (result.get() == button7) {
			 System.out.println("Wybrano 7");
			 choose = "SEVEN";
		} else if(result.get() == button8){
			 System.out.println("Wybrano 8");
			 choose = "EIGHT";
		} else if (result.get() == button9) {
			 System.out.println("Wybrano 9");
			 choose = "NINE";
		} else if (result.get() == button10) {
			 System.out.println("Wybrano 10");
			 choose = "TEN";
		}
		System.out.println("Wybrano: "+choose);

		DataFromClient dataFromClient = new DataFromClient(6, choose);
		client.sendPackage(dataFromClient);
    }
	public void serverNotRespondAlert(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("B³¹d!");
		alert.setHeaderText("Server nie odpowiada lub zakoñczy³ pracê.");
		alert.initStyle(StageStyle.UTILITY);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			client.closeStreams();
		   	System.exit(0);
		}
	}
	public void serverConnectionAlert(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("B³¹d!");
		alert.setHeaderText("Nie mo¿na po³¹czyæ siê z serwerem!");
		alert.initStyle(StageStyle.UTILITY);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		   	System.exit(0);
		}
	}
	public void gameWonAlert(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Makao");
		alert.setHeaderText("Gratulacje, wygra³eœ!");
		alert.setContentText("Makao, po makale");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			client.closeStreams();
		   	System.exit(0);
		}
	}
}
