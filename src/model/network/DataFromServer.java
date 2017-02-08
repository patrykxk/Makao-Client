package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromServer implements Serializable {
	
	//Client
	private static final long serialVersionUID = 840010767994965205L;
	private ArrayList<Card> cardsOnTable;
	private ArrayList<Card> clientCards = new ArrayList<Card>();
	private int whoseTurn;
	private int clientId;
	private int packetId;
	private String request;

	
	public ArrayList<Card> getCardsOnTable() {
		return cardsOnTable;
	}

	public void setCardsOnTable(ArrayList<Card> newCardsOnTable) {
		this.cardsOnTable = newCardsOnTable;
	}

	public ArrayList<Card> getClientCards() {
		return clientCards;
	}

	public void setClientCards(ArrayList<Card> newClientCards) {
		this.clientCards = newClientCards;
	}

	public int getClientId() {
		return clientId;
	}

	public int getWhoseTurn() {
		return whoseTurn;
	}
	public int getPacketId(){
		return packetId;
	}

	public void setWhoseTurn(int whoseTurn) {
		this.whoseTurn = whoseTurn;		
	}
	public String getRequest(){
		return request;
	}

}
