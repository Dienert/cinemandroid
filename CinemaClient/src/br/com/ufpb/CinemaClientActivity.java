package br.com.ufpb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class CinemaClientActivity extends Activity implements View.OnClickListener {
	private TextView up;
	private TextView down;
	private TextView messages;
	private Button send;
	private SensorManager sensorManager;
	private Sensor sensor;
	private float x, y, z;
	private String answer = "";
	private boolean paused = false;
	private static boolean sessionStarted = false;
	private static 	String ip =
//					 "189.71.26.51"
					 "192.168.0.160"
//					 "150.165.132.171"
			;
	
	public static String unavailable = "ainda n�o dispon�vel";
	
	private static AnwersReceiver answersReceiver;
	
	private static CinemaClientActivity instance;
	
	public static CinemaClientActivity getInstance() {
		return instance;
	}
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		
		if (!sessionStarted) {
			RequestAccess reqAccess = new RequestAccess();
			reqAccess.execute();
		}
		
		setContentView(R.layout.main);
 
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
 
		up = (TextView) findViewById(R.id.up);
		down = (TextView) findViewById(R.id.down);
		messages = (TextView) findViewById(R.id.messages);
		down.setText(unavailable);
		up.setText(unavailable);
		
		messages.setTextColor(Color.RED);
		if (answersReceiver == null) {
			answersReceiver = new AnwersReceiver();
			answersReceiver.execute();
		}
		
		send = (Button) findViewById(R.id.btnSend);
		
		RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(this, R.anim.myanim);
	    ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
	    down.setAnimation(ranim);
		
		
	}
 
	@Override
	protected void onResume() {
		super.onResume();
		paused = false;
		sensorManager.registerListener(accelerationListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		if (answersReceiver == null) {
			answersReceiver = new AnwersReceiver();
			answersReceiver.execute();
		}
	}
 
	@Override
	protected void onStop() {
		sensorManager.unregisterListener(accelerationListener);
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		paused = true;
	}
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		paused = false;
		
	}
 
	private SensorEventListener accelerationListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) {
		}
 
		public void onSensorChanged(SensorEvent event) {
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
			if (y > 8) {
				if (!up.getText().equals(unavailable)) {
					up.setTextColor(Color.GREEN);
					down.setTextColor(Color.RED);
					answer = up.getText().toString();
					send.setOnClickListener(instance);
				}
			} else if (y < -8) {
				if (!up.getText().equals(unavailable)) {
					up.setTextColor(Color.RED);
					down.setTextColor(Color.GREEN);
					answer = down.getText().toString();
					send.setOnClickListener(instance);
				}
			} else {
				up.setTextColor(Color.GRAY);
				down.setTextColor(Color.GRAY);
				answer = "neutro";
				send.setOnClickListener(null);
			}
		}
 
	};

	public void onClick(View arg0) {
		messages.setText("Enviando op��es...");
		SendAnswer sendAnswer = new SendAnswer();
		sendAnswer.execute("answer#"+answer);
		up.setText(unavailable);
		down.setText(unavailable);
		
	}
	
	public TextView getUp() {
		return up;
	}

	public void setUp(TextView up) {
		this.up = up;
	}

	public TextView getDown() {
		return down;
	}

	public void setDown(TextView down) {
		this.down = down;
	}

	public TextView getMessages() {
		return messages;
	}

	public void setMessages(TextView messages) {
		this.messages = messages;
	}
    
	public boolean isPaused() {
		return paused;
	}
	
	public static boolean isSessionStarted() {
		return sessionStarted;
	}
	
	public static void setSessionStarted(boolean sessionStarted) {
		CinemaClientActivity.sessionStarted = sessionStarted;
	}
	
	public static String getIp() {
		return ip;
	}
}