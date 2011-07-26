package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendOptions extends Thread {
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	String ip;
 	String up;
 	String down;
 	
	public SendOptions(String ip, String up, String down){
		this.up = up;
		this.down = down;
		this.ip = ip;
	}
	
	@Override
	public void run() {
		try{
			//1. creating a socket to connect to the server
			requestSocket = new Socket(ip, 2004);
			System.out.println("Connected to "+ip+" in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			try{
				message = (String)in.readObject();
				System.out.println("server android>" + message);
				message = up+"#"+down;
				out.writeObject(message);
				out.flush();
				System.out.println("Opções enviadas");
				message = (String)in.readObject();
				System.out.println("server android>" + message);
			}
			catch(ClassNotFoundException classNot){
				System.err.println("data received in unknown format");
			}
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
}