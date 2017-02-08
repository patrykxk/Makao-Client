package model.network;

import java.io.IOException;
import java.io.ObjectInputStream;

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
				Object readObject = objectInputStream.readObject();
				DataFromServer dataFromServer = (DataFromServer) readObject;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
							System.out.println("------------------------------------");
							System.out.println("numer pakietu: "+dataFromServer.getPacketId());
							gameViewcontroller.setDataFromServer(dataFromServer);
							
					}
				});
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} 
		}
	}
}
