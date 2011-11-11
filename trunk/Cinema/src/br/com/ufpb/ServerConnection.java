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
	String message;
	
	@Override
	public void run()	{
		try{
			providerSocket = new ServerSocket(2004, 10);
			while (true) {
				//1. creating a server socket
				//2. Wait for connection
				System.out.println("Waiting for connection");
				connection = providerSocket.accept();
				new Thread () {
					Socket connectionInThread = connection;
					ObjectOutputStream out;
					ObjectInputStream in;
					public void run() {
						try {
							System.out.println("Connection received from " + connectionInThread.getInetAddress().getHostName());
							//3. get Input and Output streams
							out = new ObjectOutputStream(connectionInThread.getOutputStream());
							out.flush();
							in = new ObjectInputStream(connectionInThread.getInputStream());
							String clientIp = connectionInThread.getInetAddress().getHostAddress();
							//4. The two parts communicate via the input and output streams
							try{
								message = (String)in.readObject();
								System.out.println("android> "+message);
								String[] messages = message.split("#");
								if (messages[0].equals("answer")) {
									ServerPlaylist serverPlaylist = ServerPlaylist.getInstance(); 
									serverPlaylist.addAnswer(messages[1]);
									out.writeObject("Opção recebida!");
									out.flush();
								} else if (messages[0].equals("login")) {
									if (!PlayItemAnalyzer.ips.contains(clientIp)){
										PlayItemAnalyzer.ips.add(clientIp);
										System.out.println("Cadastrei o ip: "+clientIp);
									} else {
										System.out.println("Ip já cadastrado: "+clientIp);
									}
									out.writeObject("Sessão iniciada");
									out.flush();
								}
							}catch(ClassNotFoundException classnot){
								System.err.println("Data received in unknown format");
							}
						} catch(IOException ioException){
							ioException.printStackTrace();
						} finally{
							//4: Closing connection
							try{
								in.close();
								out.close();
//								providerSocket.close();
							}
							catch(IOException ioException){
								ioException.printStackTrace();
							}
						}
					};
				}.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}