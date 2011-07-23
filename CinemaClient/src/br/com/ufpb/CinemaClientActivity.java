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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class CinemaClientActivity extends Activity implements View.OnClickListener {
	private TextView up;
	private TextView down;
	private SensorManager sensorManager;
	private Sensor sensor;
	private float x, y, z;
	private String answerServer = "";
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
 
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
 
		up = (TextView) findViewById(R.id.up);
		down = (TextView) findViewById(R.id.down);
		
		RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(this, R.anim.myanim);
	    ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
	    down.setAnimation(ranim);
		
		down.setText("No info yet");
		up.setText("No info yet");
		
		Button btnView = (Button) findViewById(R.id.btnView);
		btnView.setOnClickListener(this);
	}
 
	private void refreshDisplay() {
//		String output = String.format("x is: %f / y is: %f / z is: %f\n%s\n%s", x, y, z, answer, answerServer);
		up.setText("Sim");
		down.setText("Não");
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
			} else if (y < -8) {
				up.setTextColor(Color.RED);
				down.setTextColor(Color.GREEN);
			} else {
				up.setTextColor(Color.GRAY);
				down.setTextColor(Color.GRAY);
			}
			refreshDisplay();
		}
 
	};

	public void onClick(View arg0) {
		String url = "http://192.168.0.160:8080/Cinema/rest/services/printSucess?answer=";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		DocumentBuilder builder;
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpClient.execute(httpGet, responseHandler);
//			HttpResponse httpResponse = httpClient.execute(httpGet);
//			int responseCode = httpResponse.getStatusLine().getStatusCode();
//			String message = httpResponse.getStatusLine().getReasonPhrase();
//			HttpEntity entity = httpResponse.getEntity();
//			InputStream insStream = entity.getContent();
//			String response = convertStreamToString(insStream);
//			insStream.close();
//			answerServer = responseCode+message+response;
			answerServer = responseBody;
		} catch (ClientProtocolException e) {
			answerServer = e.getMessage();
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (IOException e) {
			answerServer = e.getMessage();
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (FactoryConfigurationError e) {
			answerServer = e.getMessage();
			Log.e(getString(R.string.app_name), e.getMessage());
		}
		refreshDisplay();

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
}