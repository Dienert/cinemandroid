package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.ufpb.utils.PlayItemAnalyzer;

public class ServerConnection extends Thread {
	
	public ServerConnection() {}
	
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	@Override
	public void run()	{
		while (true) {
			try{
				//1. creating a server socket
				providerSocket = new ServerSocket(2004, 10);
				//2. Wait for connection
				System.out.println("Waiting for connection");
				connection = providerSocket.accept();
				System.out.println("Connection received from " + connection.getInetAddress().getHostName());
				//3. get Input and Output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());
				String clientIp = connection.getInetAddress().getHostAddress();
				//4. The two parts communicate via the input and output streams
				try{
					message = (String)in.readObject();
					System.out.println("android> "+message);
					String[] messages = message.split("#");
					if (messages[0].equals("answer")) {
						ServerPlaylist serverPlaylist = ServerPlaylist.getInstance(); 
						serverPlaylist.setAnswer(messages[1]);
						serverPlaylist.start(true);
					} else if (messages[0].equals("login")) {
						PlayItemAnalyzer.ips.add(clientIp);
						System.out.println("Cadastrei o ip: "+clientIp);
						out.writeObject("Sessão iniciada");
						out.flush();
					}
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//4: Closing connection
				try{
					in.close();
					out.close();
					providerSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
		}
	}
	
}