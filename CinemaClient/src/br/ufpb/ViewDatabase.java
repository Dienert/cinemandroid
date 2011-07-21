package br.ufpb;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ViewDatabase extends Activity implements View.OnClickListener {
	String url = "http://203.247.166.88:8000/NoteWS/notes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		retrieveData(ll);
		this.setContentView(sv);
	}

	private void retrieveData(LinearLayout ll) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		DocumentBuilder builder;
		try {

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpClient.execute(httpGet, responseHandler);
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
					responseBody)));

			NodeList ids = doc.getElementsByTagName("noteId");

			String[] allIds = new String[ids.getLength()];

			for (int i = 0; i < ids.getLength(); i++) {
				Element elem = (Element) ids.item(i);
				allIds[i] = elem.getFirstChild().getNodeValue();
			}

			NodeList contents = doc.getElementsByTagName("content");

			String[] allContents = new String[contents.getLength()];

			for (int i = 0; i < contents.getLength(); i++) {
				Element elem = (Element) contents.item(i);
				allContents[i] = elem.getFirstChild().getNodeValue();
			}

			NodeList createddates = doc.getElementsByTagName("createdDate");

			String[] allCreateddates = new String[createddates.getLength()];

			for (int i = 0; i < createddates.getLength(); i++) {
				Element elem = (Element) createddates.item(i);
				allCreateddates[i] = elem.getFirstChild().getNodeValue();
			}

			for (int i = 0; i < allIds.length; i++) {
				TextView id = new TextView(this);
				id.setText(allIds[i]);
				ll.addView(id);

				TextView content = new TextView(this);
				content.setText(allContents[i]);
				ll.addView(content);

				TextView createddate = new TextView(this);
				createddate.setText(allCreateddates[i]);
				ll.addView(createddate);

				View view = new View(this);
				view.setBackgroundColor(Color.WHITE);
				view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						2));
				ll.addView(view);
			}

			Button b = new Button(this);
			b.setOnClickListener(this);
			b.setText("Post New");

			ll.addView(b);
		} catch (ClientProtocolException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (IOException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (SAXException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		} catch (FactoryConfigurationError e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, DatabaseAndroid.class));
	}
}
