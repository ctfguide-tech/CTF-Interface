import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ctfapi {
	

	ArrayList<challenge> challenges;
	static String recentReason;
	
	//Constructor
	public ctfapi() 
	{
		challenges = new ArrayList<>();
	}
	
	//Access and download html from URL
	private String accessLink(String address)
	{
		String data;
		
		URL url;
		try {
			url = new URL(address);
			InputStream html = null;
			html = url.openStream();
			
			int c = 0;
		    StringBuffer buffer = new StringBuffer("");

		    while(c != -1) {
		    	c = html.read();
		    	
		    	if(c != -1)
		    	{
			    	buffer.append((char)c);
			    	
			    	if(app.debug == true)
			    	{
			    		System.out.print((char)c);
			    	}
		    	}
		     }
		    data = buffer.toString();
		} catch (Exception e) {
			data = null;
			e.printStackTrace();
			System.out.println("Error while accessing the API. Check your have an internet connection, or check https://api.ctfguide.com/");
		}
		
		return data;
	}
	
	//Returns session token for authentication
	public String getLoginToken(String email, String password){
		String token, text;
		
		String address = "https://api.ctfguide.com/v1/login?";
		//LINK FORMAT: https://api.ctfguide.com/v1/login?email=EMAIL&password=PASSWORD
		
		address = address +"email=" + email +"&" + "password=" + password;
		System.out.println("Logging you in...");
		
		if(app.debug == true)
		{
		    System.out.println("------------------------------");
			System.out.println("API LINK: " + address);
			System.out.print("HTML RETURNED FROM LOGIN API: ");
		}

		token = this.accessLink(address);
		
		text = token;

		//extract token from html
		String[] tokenarray = token.split("\"", 9);
		token = tokenarray[3];
		
		//extract reason (errors/login fails)
		String[] reasonarray = text.split("\"", 9);
		this.recentReason = reasonarray[7];
		
		//print api information and array
		if(app.debug == true)
		{
		    System.out.println();
		    System.out.println("------------------------------");
			System.out.println("API SPLIT INTO ARRAY");
			for (String a: tokenarray) System.out.println(a);
		}
		

		return token;
	}
		
	//checks the submitted flag
	public boolean checkFlag(String challengeID, String sessionToken, String flagSubmission){
		//https://api.ctfguide.com/v1/checkflag?cid= challenge id&sesskey = session key&solution = user solution
		//test cid: nEweQDS9oJCIouQX3rEv answer: "Joe Mama"
		//https://api.ctfguide.com/v1/checkflag?cid=nEweQDS9oJCIouQX3rEv&sesskey=SESSIONKEYHERE&solution=Joe%20Mama
		
		String address = "https://api.ctfguide.com/v1/checkflag?cid=";	
		String output;
		Boolean solved;
		
		flagSubmission = flagSubmission.replaceAll(" ", "%20");
		
		address = address + challengeID + "&sesskey=" + sessionToken + "&solution=" + flagSubmission;
		//make sure space = %20
		if(app.debug == true)
		{
			System.out.println("------------------------------");
			System.out.println(address);
		}
		
		
		output = this.accessLink(address);
		
		if(app.debug == true)
		{
			System.out.println();
			System.out.println(output);
		}
		
		String[] resultarray = output.split("\"", 9);
		output = resultarray[7];
		
		if(app.debug == true)
		{
			System.out.println(output);
		
		}
		
		if(output.equals("points awarded") == true)
		{
			solved = true;
		}else
		{
			solved = false;
		}
			
		return solved;
	}
	
	//returns an arraylist of challenges
	public ArrayList<challenge> getChallenges(){
		String address = "https://api.ctfguide.com/v1/challenges";
		
		String rawlist = this.accessLink(address);
		
		
		return null;
	}
}
