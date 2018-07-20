package codeu.controller;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

public class ActivityFeedServlet2 extends HttpServlet {

	private final String USER_AGENT = "Mozilla/5.0";
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String requestUrl = request.getRequestURI();
		String conversationTitle = requestUrl.substring("/chat/".length());
		
		String GIFsearch = request.getParameter("search");
		
		List<String> allGIFs = new ArrayList<>();
		try {
			allGIFs = sendGet(GIFsearch);
		} catch (Throwable e) {
			for(int i = 0; i < 20; i++) {
				allGIFs.add("https://i.giphy.com/bi6RQ5x3tqoSI.gif");
			}
        }
		request.setAttribute("allGIFs", allGIFs);
		// redirect to a GET request
		response.sendRedirect("/chat/" + conversationTitle);
	}
	
	// HTTP GET request
	private List<String> sendGet(String GIFsearch) throws Exception {

		String url = "http://api.giphy.com/v1/gifs/search?q="+GIFsearch+"&api_key=iDKR9vNZ9WlR2B1aqbmtentOM2wUjLyZ&limit=20";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		
		List<String> allGIFs = new ArrayList<>();
        //Read JSON response and add URLs to ArrayList
        JSONObject giphyJSONResponse = new JSONObject(response.toString());
        JSONArray JSONArray = new JSONArray(giphyJSONResponse.getString("data"));
        for(int i=0; i < JSONArray.length(); i++) {
            JSONObject JSONObject = JSONArray.getJSONObject(i);
            String id = JSONObject.getString("id");
            String gifURL = "https://i.giphy.com/"+id+".gif";
            allGIFs.add(gifURL);
        }
        return allGIFs;
         

	}
}
	