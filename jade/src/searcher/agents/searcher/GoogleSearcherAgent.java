package searcher.agents.searcher;

import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import searcher.Article;

public class GoogleSearcherAgent extends SearcherAgent {

	@Override
	public List<Article> search(ACLMessage msg) {
		String query = msg.getContent();
		
		JSONObject json =   request(query);

		JSONObject contResponseData;
		try {
			contResponseData = json.getJSONObject("responseData");
			JSONArray arrayRes = contResponseData.getJSONArray("results");
			List<Article> result = new ArrayList<Article>();
			for(int i = 0; i < arrayRes.length(); i++){
				String url = arrayRes.getJSONObject(i).getString("url");
				result.add(new Article(url));
			}
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Проблемки'c c Googl'ом :(");
			e.printStackTrace();
		}
		return null;
	}
	
	private static JSONObject request(String guery) {
		URL url;
		JSONObject json = null;
		try {
			url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + guery);
			URLConnection connection = url.openConnection();
			//connection.addRequestProperty("Referer",
			//		"http://www.mysite.com/index.html");

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			json = new JSONObject(builder.toString());
			// now have some fun with the results...
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}


}
