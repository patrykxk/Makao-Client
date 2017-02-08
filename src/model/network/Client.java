package model.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import view.GameViewController;


public class Client {

	public Client(String login){
		this.login = login;
		//runConnection();
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
		new Thread(new DataReader(objectInputStream, gameViewcontroller)).start();
	}
	
	
}