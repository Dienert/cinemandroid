package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.widget.TextView;


public class AnwersReceiver extends AsyncTask<Void, Void, String[]> {
	
	public AnwersReceiver() {}
	
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String[] messages = {"n�o recebido", "n�o recebido"};
	
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
			out.writeObject("Pode mandar op��es");
			out.flush();
			//4. The two parts communicate via the input and output streams
			try{
				String message = (String)in.readObject();
				messages = message.split("#");
				out.writeObject("Recebi op��es");
				out.flush();
			}
			catch(ClassNotFoundException classnot){
				System.err.println("Data received in unknown format");
			}
		}
		catch(IOException ioException){
			try {
				out.writeObject("Falha ao receber op��es");
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
		CinemaClientActivity activity = CinemaClientActivity.getInstance();
		TextView up = activity.getUp(); 
		up.setText(result[0]);
		TextView down = activity.getDown();
		down.setText(result[1]);
		TextView messages = activity.getMessages();
		Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(600);
		messages.setText("Novas op��es recebidas");
		AnwersReceiver server = new AnwersReceiver();
		server.execute();
		
	}

}