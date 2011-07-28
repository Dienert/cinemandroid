package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class SendAnswer extends AsyncTask<String, Void, String>{

	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	
 	public SendAnswer() {}
	
	@Override
	protected String doInBackground(String... params) {
		try{
			//1. creating a socket to connect to the server
			String ip = "192.168.0.160";
//			String ip = "150.165.132.171";
			requestSocket = new Socket(ip, 2004);
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			try{
				out.writeObject(params[0]);
				out.flush();
				message = (String)in.readObject();
			}
			catch(ClassNotFoundException classNot){
				message = "unknown format received";
			}
		} catch(UnknownHostException unknownHost){
			message = "Host desconhecido";
		} catch(IOException ioException){
			message = ioException.getMessage();
			ioException.printStackTrace();
		} catch (Exception e) {
			message = e.getMessage();
		}
		finally{
			//4: Closing connection
			try{
				if(in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (requestSocket != null && !requestSocket.isClosed()) {
					requestSocket.close();
				}
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		return message;
	}
	
	protected void onPostExecute(String result) {
		CinemaClientActivity.getInstance().getMessages().setText(result);
	}
}
