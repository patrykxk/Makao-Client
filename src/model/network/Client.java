package model.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;
import view.GameViewController;


public class Client {
	private String login;
	private static Socket clientSocket = null;
	private static ObjectOutputStream objectOutputStream = null;
	private static ObjectInputStream objectInputStream = null;
	private static int portNumber = 1111;
	private static String host = "127.0.0.1";
	private Thread dataReaderThread;
	
	public Client(String login){
		this.login = login;
		//runConnection();
	}
	public Client(){};
	public String getLogin(){
		return login;
	}
	public void closeStreams(){
		try {
			objectInputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void runConnection() throws IOException {
		try {   
	        clientSocket = new Socket(host, portNumber);
	        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
	        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
	    } catch (UnknownHostException e) {
	    	System.err.println("Unknown host: " + host);
	    }
	}
	
	public void sendEmptyPacket(int noOfPacket){
		DataFromClient dataFromClient = new DataFromClient(noOfPacket);
		sendPackage(dataFromClient);
	}
	public void sendPackage(DataFromClient dataFromClient){
		try {
			objectOutputStream.reset();
			objectOutputStream.writeObject(dataFromClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startReadingData(GameViewController gameViewcontroller){
		dataReaderThread = new Thread(new DataReader(objectInputStream, gameViewcontroller));
		dataReaderThread.start();
	}
	public void stopReadingData() {
		dataReaderThread.interrupt();
	}

	
}