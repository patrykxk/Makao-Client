package view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.cards.Card;
import model.cards.CardsListSingleton;
import model.network.Client;
import model.network.DataFromServer;



public class GameViewController {
	@FXML
   	private ImageView card1;
	
	@FXML
	private HBox hbox;
	@FXML
	private HBox hboxCenter;
	@FXML
	private HBox buttons;
	@FXML
    private Button takeCardButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Button confirmButton;
	//private double cardsOnTableTranslateX = 0;
	//private double clientCardsTranslateX = 0;
	
	//CardsListSingleton cardsListSingleton = CardsListSingleton.getInstance();
	DataFromServer dataFromServer = null;
	
	Client client;
	
	public void setDataFromServer(DataFromServer dataFromServer){
		this.dataFromServer = dataFromServer;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	@FXML
	public void initialize(){
		buttons.setVisible(false);
		//client.startReadingData();
	}
	
	@FXML
	private void startGame(){
		buttons.setVisible(true);
		
		showClientCards();
		showCardsOnTable();
		startGameButton.setVisible(false);
	}
	
	@FXML
	private void takeCard(){
		Card card = client.takeCard();
		//cardsListSingleton.getClientCards().add(card);
		showClientCards();
		//end turn.
	}
	
	@FXML
    private void confirm() {
		
		for(Card card : cardsClicked){
			dataFromServer.getClientCards().remove(card);
			dataFromServer.getCardsOnTable().add(card);
		}
		showClientCards();
		showCardsOnTable();
		cardsClicked.clear();
		//end turn.
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
		for(Card card : dataFromServer.getClientCards()){
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
		for(Card card : dataFromServer.getCardsOnTable()){
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
