package br.com.ufpb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.widget.TextView;


public class AnwersReceiver extends AsyncTask<Void, Void, String[]> {
	
	private ServerSocket providerSocket;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String[] messages = {CinemaClientActivity.unavailable, CinemaClientActivity.unavailable};
	private int SIMPLE_NOTFICATION_ID;
	
	public AnwersReceiver() {}
	
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
		CinemaClientActivity activity = CinemaClientActivity.getInstance();
		TextView up = activity.getUp(); 
		up.setText(result[0]);
		TextView down = activity.getDown();
		down.setText(result[1]);
		if (!up.getText().equals(CinemaClientActivity.unavailable)) {
			TextView messages = activity.getMessages();
			Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(600);
			messages.setText("Novas opções recebidas");
			if (activity.isPaused()) {
				notifyOptions();
			}
		}
		AnwersReceiver server = new AnwersReceiver();
		server.execute();
		
	}
	
	private void notifyOptions() {
		CinemaClientActivity activity = CinemaClientActivity.getInstance();
		NotificationManager noticificationMgr = (NotificationManager)activity.getSystemService(CinemaClientActivity.NOTIFICATION_SERVICE);
		
		final Notification notification = new Notification(R.drawable.icon, "Novas opções chegaram",System.currentTimeMillis());
		long[] vibrate = {100,100,200,300};
		notification.vibrate = vibrate;
		notification.defaults =Notification.DEFAULT_ALL;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		Context context = activity.getApplicationContext();
		CharSequence contentTitle = "CinemaClient - Novas opções";
		CharSequence contentText = "Faça sua escolha";
		
		Intent notifyIntent = new Intent(context, CinemaClientActivity.class);
		
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		PendingIntent intent = PendingIntent.getActivity(activity, 0, notifyIntent, 0);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, intent);
		
		noticificationMgr.notify(SIMPLE_NOTFICATION_ID, notification);

	}

}