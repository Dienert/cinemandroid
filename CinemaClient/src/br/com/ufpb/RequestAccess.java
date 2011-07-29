package br.com.ufpb;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;
public class RequestAccess extends AsyncTask<Void, Void, String>{
	
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
 	
	RequestAccess(){}
	
	protected String doInBackground(Void... v) {
		try{
			//1. creating a socket to connect to the server

			requestSocket = new Socket(CinemaClientActivity.getIp(), 2004);
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			try{
				message = "login";
				out.writeObject(message);
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
		if (result.equals("Sess�o inciada")) {
			CinemaClientActivity.setSessionStarted(true);
		} else {
			CinemaClientActivity.setSessionStarted(false);
		}
	}
}
