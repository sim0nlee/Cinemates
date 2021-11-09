package data;

import control.Controller;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;

public class RTDBStatisticsAccess implements StatisticsDAO {
	
	private static final String STATS_URL =
			"https://cinemates-e0934-default-rtdb.europe-west1.firebasedatabase.app/Statistiche.json";

	private static final OkHttpClient client = new OkHttpClient.Builder().build();


	
	public int[] fetchStats () {
		
		Request jsonRequest = new Request.Builder().url(STATS_URL).build();
		
		try {
			
			Response jsonResponse = client.newCall(jsonRequest).execute();

			assert jsonResponse.body() != null;
			JSONObject stats = new JSONObject(jsonResponse.body().string());

			
			return parseJSON(stats);
			
		}
		catch (IOException | JSONException ex) {
			handleRTDBConnectionException(ex);
			return null;
		}

	}
	private int[] parseJSON (JSONObject stats) {

		try {
			int utenti = stats.isNull("Utenti") 						  ? 0 : stats.getInt("Utenti");
			int accessi = stats.isNull("Accessi") 						  ? 0 : stats.getInt("Accessi");
			int collegamenti = stats.isNull("Collegamenti") 			  ? 0 : stats.getInt("Collegamenti");
			int ricerche = stats.isNull("Ricerche") 					  ? 0 : stats.getInt("Ricerche");
			int avventura = stats.isNull("Film di Avventura nelle liste") ? 0 : stats.getInt("Film di Avventura nelle liste");
			int azione = stats.isNull("Film di Azione nelle liste") 	  ? 0 : stats.getInt("Film di Azione nelle liste");
			int commedie = stats.isNull("Commedie nelle liste") 		  ? 0 : stats.getInt("Commedie nelle liste");
			int thriller = stats.isNull("Thriller nelle liste") 		  ? 0 : stats.getInt("Thriller nelle liste");
			int horror = stats.isNull("Horror nelle liste")				  ? 0 : stats.getInt("Horror nelle liste");

			return new int[]{utenti, accessi, collegamenti, ricerche, avventura, azione, commedie, thriller, horror};
		}
		catch (JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	private static void handleRTDBConnectionException(Exception e) {
		JOptionPane.showMessageDialog(Controller.getActiveFrame(),
									  "Impossibile visualizzare i dati",
									  "Errore di connessione",
				                      JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}

}
