package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromServer implements Serializable {
	
	//Client
	private static final long serialVersionUID = 840010767994965205L;
	private ArrayList<Card> cardsOnTable;
	private ArrayList<Card> clientCards = new ArrayList<Card>();
	int whoseTurn;
	  
	public DataFromServer(ArrayList<Card> cardsOnTable, ArrayList<Card> newClientCards, int whoseTurn) {
		this.cardsOnTable = cardsOnTable;
		this.clientCards = newClientCards;
		this.whoseTurn = whoseTurn;
	}
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

	public int getWhoseTurn() {
		return whoseTurn;
	}

	public void setWhoseTurn(int whoseTurn) {
		this.whoseTurn = whoseTurn;
	}


}
