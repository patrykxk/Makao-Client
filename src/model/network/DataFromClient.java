package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromClient implements Serializable{
	
	//Client
	private static final long serialVersionUID = 4069786321538302229L;
	int packetId;
	private ArrayList<Card> clientCards;
	private String request;
	
	public DataFromClient(int packetId, ArrayList<Card> clientCards){
		this.packetId = packetId;
		this.clientCards = clientCards;
	}
	public DataFromClient(int packetId){
		this.packetId = packetId;
	}
	public DataFromClient(int packetId, String request){
		this.packetId = packetId;
		this.request = request;
	}
	
	
}
