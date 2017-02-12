package model.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.application.Platform;
import view.GameViewController;

public class DataReader implements Runnable {
	private ObjectInputStream objectInputStream = null;
	private GameViewController gameViewController;
	
	public DataReader(ObjectInputStream objectInputStream, GameViewController gameViewcontroller){
		this.objectInputStream = objectInputStream;
		this.gameViewController = gameViewcontroller;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {		
			try {
				Object readObject = objectInputStream.readObject();
				DataFromServer dataFromServer = (DataFromServer) readObject;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("------------------------------------");
						System.out.println("numer pakietu od Serwera: "+dataFromServer.getPacketId());
						gameViewController.setDataFromServer(dataFromServer);				
					}
				});
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Platform.runLater(() -> gameViewController.serverNotRespondAlert());
				Thread.currentThread().interrupt();
			} 
		}
	}
}
