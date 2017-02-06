package model.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javafx.application.Platform;
import model.cards.Card;
import view.GameViewController;

public class DataReader implements Runnable {
	private ObjectInputStream objectInputStream = null;
	private Card card;
	private GameViewController gameViewcontroller;
	
	public DataReader(ObjectInputStream objectInputStream, GameViewController gameViewcontroller){
		this.objectInputStream = objectInputStream;
		this.gameViewcontroller = gameViewcontroller;
	}
	
	@Override
	public void run() {
		
		while (true) {		
			try {

				//int read = objectInputStream.readInt();
				
				System.out.println("bedzie czytac");
				Object readObject = objectInputStream.readObject();
				DataFromServer dataFromServer = (DataFromServer) readObject;
				gameViewcontroller.setDataFromServer(dataFromServer);
				System.out.println("odczytal");
//				ArrayList<Card> card = (ArrayList<Card>) readObject;
//				System.out.println( card.get(0).getCardValue() + " of " +  card.get(0).getSuit());
//				System.out.println(card.size());
//				if(read<5){
//					System.out.println(read);
//				}
				
				
//				if (card != null) {
//					Platform.runLater(new Runnable() {
//						@Override
//						public void run() {
//							System.out.println("runLater");
//							System.out.println(" kupa" + card.getCardValue() + " of " + card.getSuit());
//						}
//					});
//				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} 
		}
	}
}
