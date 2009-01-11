package jreader.gui;

import java.io.IOException;

public class BrowserControl {

	// zmienna dla windowsa
	private static final String WIN_ID = "Windows";
	// domyślna przeglądarka pod windowsem
	private static final String WIN_PATH = "rundll32";
	// flaga z jąką wyświetlna jest url (win)
	private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
	// domyślna przeglądarka pod unixem
	private static final String UNIX_PATH = "netscape";
	// flaga z jąką wyświetlna jest url (unix)
	private static final String UNIX_FLAG = "-remote openURL";
	
	
	/**
	* Otwiera link w domyślnej przeglądarce sytemu operaccyjnego. 
	*
	* @param url link do strony (musi zaczynać się od "http://" lub "file://"
	*/
	public static void displayURL(String url) {
		boolean windows = isWindowsPlatform();
		String cmd = null;
	
		try {
			if (windows) {
				// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
				cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
				Process p = Runtime.getRuntime().exec(cmd);
			}
			else {
				// Pod linuxem, Netscape musi być odpalany z "-remote"
				// żeby działać. Wysylam comende (cmd)
				// i sprawdzam wartosc na wyjsciu. Jesli jest 0
				// to działa jeśli nie to trzeba odpalić przeglądare.
				// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
				cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
				Process p = Runtime.getRuntime().exec(cmd);
				
				try	{
					// czekam na 0 -- jeśli jest 0 to działa
					// jeśli nie to odpala przeglądarkę.
					int exitCode = p.waitFor();
					if (exitCode != 0) {
						// nie udało się, odpalam ręcznie
						// cmd = 'netscape http://www.javaworld.com'
						cmd = UNIX_PATH + " " + url;
						p = Runtime.getRuntime().exec(cmd);
					}
				}
				catch(InterruptedException x) {
					System.err.println("Error bringing up browser, cmd='" + cmd + "'");
					System.err.println("Caught: " + x);
				}
			}
		}
		catch(IOException x) {
			System.err.println("Could not invoke browser, command=" + cmd);
			System.err.println("Caught: " + x);
		}
	}
	
	/**
	* Rozpoznaje system na jakim uruchomiony jest program.
	*
	* @return true jeśli rozpoznany system to windows, w innym przyadku false
	*/
	public static boolean isWindowsPlatform() {
		String os = System.getProperty("os.name");
		
		if ( os != null && os.startsWith(WIN_ID))
			return true;
		else
			return false;
	}
}
