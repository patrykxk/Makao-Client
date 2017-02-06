package model.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Stack;

import model.cards.Card;
import model.cards.CardValue;
import model.cards.CardsListSingleton;
import model.cards.Deck;
import model.cards.Suit;
import view.GameViewController;


public class Client {

	public Client(String login){
		this.login = login;
		//srunConnection();
	}
	public Client(){};
	
	private String login;
	private static Socket clientSocket = null;
	private static ObjectOutputStream objectOutputStream = null;
	private static ObjectInputStream objectInputStream = null;
	private static boolean closed = false;
	private static int portNumber = 1111;
	private static String host = "127.0.0.1";
	
	public String getLogin(){
		return login;
	}
	
	public void runConnection() {
	
		try {   
	        clientSocket = new Socket(host, portNumber);
	        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
	        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
	    } catch (UnknownHostException e) {
	    	System.err.println("Unknown host: " + host);
	    } catch (IOException e) {
	    	System.err.println(" I/O Exception " + host);
	    }
	    
		getShufledDeck();//  \\
		getCardsOnDesk();//   \\ to powinno pobieraæ z serwera
		getStartingCards();// //  

	}
	
	
	CardsListSingleton cardsListSingleton = CardsListSingleton.getInstance();
	Stack <Card>cards;
	
	private boolean isCardClickable(Card card){
		CardValue cardOnTableValue = cardsListSingleton.getCardsOnTable().get(cardsListSingleton.getCardsOnTable().size()-1).getCardValue();
		Suit cardOnTableSuit = cardsListSingleton.getCardsOnTable().get(cardsListSingleton.getCardsOnTable().size()-1).getSuit();

		if((card.getCardValue().equals(cardOnTableValue)) || 
		  (card.getSuit().equals(cardOnTableSuit))){
			return true;
		}
		return false;
	}
	
	public Card takeCard(){
		Card card = cards.pop();
		card.setClickable(isCardClickable(card));
		return card;
	}
	
	private void getShufledDeck(){
		Deck deck = new Deck();
	 	cards = deck.getDeck();
	}
	
	private void getStartingCards(){

		int j = 0;
		while(j<5){
			Card card = cards.pop();

			card.setClickable(isCardClickable(card));

			cardsListSingleton.getClientCards().add(card);
			j++;
    	}
	}
	
	
	private void getCardsOnDesk(){
		
		Card card = cards.pop();
		cardsListSingleton.getCardsOnTable().add(card);
	}
	

	public void startReadingData(GameViewController gameViewcontroller){
		new Thread(new DataReader(objectInputStream, gameViewcontroller)).start();
	}
	
}