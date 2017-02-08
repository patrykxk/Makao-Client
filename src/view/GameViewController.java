package view;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.cards.Card;
import model.cards.CardValue;
import model.cards.Suit;
import model.network.Client;
import model.network.DataFromClient;
import model.network.DataFromServer;

public class GameViewController {
	@FXML private ImageView card1;
	@FXML private HBox hbox;
	@FXML private HBox hboxCenter;
	@FXML private HBox buttons;
	@FXML private Button takeCardButton;
    @FXML private Button startGameButton;
    @FXML private Button confirmButton;
    @FXML private Button endTurnButton;
    @FXML private Button takePenaltyCardsButton;
	DataFromServer clientData = null;
	Client client;
	
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
				setClientCardsClickabilityByValue("FOUR");
				showClientCards();
				showCardsOnTable();
				break;
			case 8: //take penalty cards packet
				clientData.setWhoseTurn(dataFromServer.getWhoseTurn());
				for(Card card : dataFromServer.getCardsOnTable()){
					clientData.getCardsOnTable().add(card);
				}
				takeCardButton.setVisible(true);
				setClientCardsClickability();
				showClientCards();
				showCardsOnTable();
				break;
		}
	}
	

	@FXML
	public void initialize(){
		buttons.setVisible(false);
		endTurnButton.setVisible(false);
		takePenaltyCardsButton.setVisible(false);
	}
	

	@FXML
	private void startGame(){
		checkWhosTurn();
		buttons.setVisible(true);
		confirmButton.setVisible(false);
		showClientCards();
		showCardsOnTable();
		startGameButton.setVisible(false);
	
	}
	
	@FXML
	private void takeCard(){
		client.sendEmptyPacket(3);
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
		CardValue cardOnTableValue = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getCardValue();
		Suit cardOnTableSuit = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getSuit();
		for(Card card : clientData.getClientCards()){
			if(card.getSuit().toString().equals(cardSuit)){
				card.setClickable(true);
			}else{
				card.setClickable(false);
			}
		}
	}
	private void setClientCardsClickabilityByValue(String cardValue){
		CardValue cardOnTableValue = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getCardValue();
		Suit cardOnTableSuit = clientData.getCardsOnTable().get(clientData.getCardsOnTable().size()-1).getSuit();
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
	
	private void showClientCards(){
		double clientCardsTranslateX = 0;
		hbox.getChildren().clear();
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
			hbox.getChildren().add(imageView);
		
			clientCardsTranslateX-=100;
		}
	}
	private void showCardsOnTable(){
		
		double cardsOnTableTranslateX = 0;
		hboxCenter.getChildren().clear();
		for(Card card : clientData.getCardsOnTable()){
			ImageView imageView = new ImageView();
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
}
