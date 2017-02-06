package model.cards;


import java.util.ArrayList;


public class CardsListSingleton {

	private ArrayList <Card> clientCards = new ArrayList<Card>();
	private ArrayList <Card> cardsOnTable= new ArrayList<Card>();
	private static CardsListSingleton instance;
    
    private CardsListSingleton(){}
    
    public static synchronized CardsListSingleton  getInstance(){
        if(instance == null){
            instance = new CardsListSingleton ();
        }
        return instance;
    }

	public synchronized ArrayList<Card> getClientCards() {
		return clientCards;
	}

	public synchronized void setClientCards(ArrayList<Card> clientCards) {
		this.clientCards = clientCards;
	}

	public synchronized ArrayList<Card> getCardsOnTable() {
		return cardsOnTable;
	}

	public synchronized void setCardsOnTable(ArrayList<Card> cardsOnTable) {
		this.cardsOnTable = cardsOnTable;
	}


	
}