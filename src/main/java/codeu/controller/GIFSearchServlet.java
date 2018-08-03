package codeu.controller;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

public class GIFSearchServlet extends HttpServlet {

	private final String USER_AGENT = "Mozilla/5.0";
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		String GIFsearch = java.net.URLEncoder.encode(request.getParameter("search"), "UTF-8");
		GIFsearch = clean(GIFsearch);
		
		String htmlResponse = "";
		try {
			htmlResponse = sendGet(GIFsearch);
		} catch (Throwable e) {
			for(int i = 0; i < 20; i++) {
				htmlResponse = "<div class='row'>"
						+ "<div class='col'><div data-dismiss='modal'><img onclick='sendGIF(this)' width=200 src='https://i.giphy.com/bi6RQ5x3tqoSI.gif' class='img-fluid' alt='Responsive image'></div></div>"
						+ "<div class='col'><div data-dismiss='modal'><img onclick='sendGIF(this)' width=200 src='https://i.giphy.com/bi6RQ5x3tqoSI.gif' class='img-fluid' alt='Responsive image'></div></div>"
						+ "</row>";
        
			}

        }
		
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(htmlResponse);
	    
	}
	
	// HTTP GET request
	private String sendGet(String GIFsearch) throws Exception {

		String url = "http://api.giphy.com/v1/gifs/search?q="+GIFsearch+"&api_key=iDKR9vNZ9WlR2B1aqbmtentOM2wUjLyZ&limit=20";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		
        //Read JSON response and add URLs to ArrayList
        JSONObject giphyJSONResponse = new JSONObject(response.toString());
        JSONArray JSONArray = giphyJSONResponse.getJSONArray("data");
        
	    String htmlResponse = "";
	    
        for(int i=0; i < JSONArray.length(); i++) {
            JSONObject JSONObject = JSONArray.getJSONObject(i);
            String id = JSONObject.getString("id");
            String gifURL = "https://i.giphy.com/"+id+".gif";
            
            if (i%2 == 0) {
            	if (i!=0) {
            		htmlResponse+="</div>";
            	}
            	htmlResponse+="<div class='row'>";
    	    }
            htmlResponse+="<div class='col'><div data-dismiss='modal'>"
            		+ "<img onclick='sendGIF(this)' width=200 src='"+ gifURL +"' class='img-fluid' alt='Responsive image'>"
            				+ "</div></div>";
        }
        htmlResponse+="</div>";
        return htmlResponse;
         
	}
	private static String clean(String cleanedMessage) {
		Document dirty = Parser.parseBodyFragment(cleanedMessage, "");
		Cleaner cleaner = new Cleaner(Whitelist.basic());
		Document clean = cleaner.clean(dirty);
		clean.outputSettings().prettyPrint(false);
		return clean.body().html();
	}
}
	