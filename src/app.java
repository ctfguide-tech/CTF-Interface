import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class app {
	static Scanner sc = new Scanner(System.in);
	static boolean running;
	static String input, sessionToken;
	static boolean debug;
	
	public static void main(String[] args)
	{
		System.setProperty("http.agent", "Chrome");
		String email, password;
		running = true;
		app.debug = false;
		
		//login parameters:
		String fail = "bad";

		ctfapi api = new ctfapi();
		
		System.out.println("Please Login into CTFGuide: ");
		email = getUserEmail();
		password = getUserPassword();
		
		sessionToken = api.getLoginToken(email, password);
		
		if(app.debug == true)
		{
			System.out.println("------------------------------");
			System.out.println("DEBUG MODE IS ON, EXPECT EXTRA TEXT");
			System.out.println("Session Token: " + sessionToken);

		}
		
		
		if(sessionToken.equals(fail) == true)
		{
			System.out.println("------------------------------");
			System.out.println("There was an error while logging in:");
			System.out.println(ctfapi.recentReason);
			System.out.println("Check server status at: https://api.ctfguide.com/");
			
			running = false;
		}else
		{
			System.out.println("Welcome, " + email + "!" + " To view the commands, type help.");
		}
		
		while(running == true)
		{
			System.out.println("------------------------------");
			input = sc.nextLine();
			if(app.debug == true)
			{
				System.out.println("USER INPUT: " + input);
			}
			
			if(input.equals("help") == true)
			{
				System.out.println("");
				System.out.println("------------------------------");
				System.out.println("Commands (Case Sensative:");
				
				System.out.println("");
				System.out.println("> check flag: Check your flag with CTFGuide");
				System.out.println("Usage: input challenge ID and then your flag");

				
				System.out.println("");
				System.out.println("> list: Get the list of challenges and challenge ID");
				System.out.println("Usage: lists all the challenges available");
			}
			if(input.equals("check flag") == true)
			{
				String challengeID, flagSubmission;
				System.out.println("Input ChallengeID:");
				challengeID = sc.nextLine();
				challengeID = challengeID.replaceAll("\\s", "");
				System.out.println("Input Flag:");
				flagSubmission = sc.nextLine();
			
				boolean flagSolved;
				try {
					flagSolved= api.checkFlag(challengeID, sessionToken, flagSubmission);
				}catch (Exception e) {
					flagSolved = false;
					e.printStackTrace();
				}
				
				if(app.debug == true)
				{
					System.out.println(flagSolved);
				}
				
				if(flagSolved == true)
				{
					System.out.println("Congrats, you've solved the challenge!");
				}else
				{
					System.out.println("Incorrect flag");
				}
				
			}
		}
	}
	
	public static String getUserEmail() {
		System.out.println("Enter your email:");
		String email = sc.nextLine();
		
		return email;
	}
	
	public static String getUserPassword()
	{
		Console console = System.console();
		System.out.println("Enter Your Password (it will be invisible):");
		
		//TODO HIDE PASSWORD FOR FINAL BUILD
		/* USE THIS
		char[] passwordchar = console.readPassword();	
		String password = new String(passwordchar);
		*/
		String password = sc.nextLine();
		//clear console
		for (int i = 0; i < 50; ++i) System.out.println();

		return password;
	}
}
