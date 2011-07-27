package br.com.ufpb;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;
public class RequestAccess extends AsyncTask<Void, Void, String>{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	
	RequestAccess(){}
	
	protected String doInBackground(Void... v) {
		try{
			//1. creating a socket to connect to the server
			requestSocket = new Socket("192.168.0.160", 2004);
			System.out.println("Connected to 192.168.0.160 in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			try{
				message = "login";
				out.writeObject(message);
				out.flush();
				System.out.println("server>" + message);
				message = (String)in.readObject();
			}
			catch(ClassNotFoundException classNot){
				System.err.println("data received in unknown format");
			}
		} catch(UnknownHostException unknownHost){
			message = "Host desconhecido";
			System.err.println("You are trying to connect to an unknown host!");
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
