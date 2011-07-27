package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.TextView;


public class ServerConnection extends AsyncTask<Void, Void, String[]> {
	
	public ServerConnection() {}
	
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String[] messages = {"não recebido", "não recebido"};
	
	protected String[] doInBackground(Void... v) {
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
			out.writeObject("Pode mandar opções");
			out.flush();
			//4. The two parts communicate via the input and output streams
			try{
				String message = (String)in.readObject();
				messages = message.split("#");
				out.writeObject("Recebi opções");
				out.flush();
			}
			catch(ClassNotFoundException classnot){
				System.err.println("Data received in unknown format");
			}
		}
		catch(IOException ioException){
			try {
				out.writeObject("Falha ao receber opções");
				out.flush();
			} catch (IOException e) {
				ioException.printStackTrace();
			}
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (providerSocket != null) {
					providerSocket.close();
				}
				return messages;
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		return messages;
	}
	
	protected void onPostExecute(String[] result) {
		TextView up = CinemaClientActivity.getInstance().getUp(); 
		up.setText(result[0]);
		TextView down = CinemaClientActivity.getInstance().getDown();
		down.setText(result[1]);
		ServerConnection server = new ServerConnection();
		server.execute();
		
	}

}