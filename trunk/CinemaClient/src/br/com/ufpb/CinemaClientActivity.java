package br.com.ufpb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import br.com.ufpb.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
	
	private static CinemaClientActivity instance;
	
	public static CinemaClientActivity getInstance() {
		return instance;
	}
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		
		RequestAccess reqAccess = new RequestAccess();
		reqAccess.execute();
		
		setContentView(R.layout.main);
 
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
 
		up = (TextView) findViewById(R.id.up);
		down = (TextView) findViewById(R.id.down);
		messages = (TextView) findViewById(R.id.messages);
		
		messages.setTextColor(Color.RED);
		
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.execute();
		send = (Button) findViewById(R.id.btnSend);
		send.setOnClickListener(this);
		
		RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(this, R.anim.myanim);
	    ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
	    down.setAnimation(ranim);
		
		down.setText("Ainda não disponível");
		down.setGravity(Gravity.CENTER_HORIZONTAL);
		up.setText("Ainda não disponível");
		
	}
 
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(accelerationListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}
 
	@Override
	protected void onStop() {
		sensorManager.unregisterListener(accelerationListener);
		super.onStop();
	}
 
	private SensorEventListener accelerationListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) {
		}
 
		public void onSensorChanged(SensorEvent event) {
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
			if (y > 8) {
				up.setTextColor(Color.GREEN);
				down.setTextColor(Color.RED);
				answer = up.getText().toString();
			} else if (y < -8) {
				up.setTextColor(Color.RED);
				down.setTextColor(Color.GREEN);
				answer = down.getText().toString();
			} else {
				up.setTextColor(Color.GRAY);
				down.setTextColor(Color.GRAY);
				answer = "neutro";
			}
		}
 
	};

	public void onClick(View arg0) {
		if (!answer.equals("neutro")) {
			SendAnswer sendAnswer = new SendAnswer();
			sendAnswer.execute("answer#"+answer);
		}
	}
	
    private static String convertStreamToString(InputStream is) {
    	 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
    
}