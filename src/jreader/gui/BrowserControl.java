package jreader.gui;

import java.io.IOException;

/**
 * Klasa służąca do otwierania zewnętrznej przeglądarki internetowej.
 * @author Karol
 *
 */
public class BrowserControl {

	// zmienna dla windowsa
	private static final String WIN_ID = "Windows";
	// domyślna przeglądarka pod windowsem
	private static final String WIN_PATH = "rundll32";
	// flaga z jąką wyświetlna jest url (win)
	private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
	// domyślna przeglądarka pod unixem
	private static final String UNIX_PATH = "firefox";
	// flaga z jąką wyświetlna jest url (unix)
	private static final String UNIX_FLAG = "";
	
	
	/**
	* Otwiera link w domyślnej przeglądarce systemu operacyjnego. 
	*
	* @param url link do strony (musi zaczynać się od "http://" lub "file://")
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
				// Domyślnie próbuję odpalić w Firefoksie.
				// Wysyłam komendę (cmd) i sprawdzam wartość na wyjściu.
				// Jeśli jest 0 to działa jeśli nie to trzeba odpalić
				// inną przeglądarkę.
				cmd = UNIX_PATH + " " + UNIX_FLAG + url;
				Process p = Runtime.getRuntime().exec(cmd);
				
				try	{
					// Czekam na 0 - jeśli jest 0 to działa,
					// jeśli nie to próbuję inaczej.
					int exitCode = p.waitFor();
					if (exitCode != 0) {
						// nie udało się, próbuję z Operą
						cmd = "opera" + " " + url;
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
	* @return <code>true</code> jeśli rozpoznany system to windows, w innym przyadku </code>false</code>
	*/
	public static boolean isWindowsPlatform() {
		String os = System.getProperty("os.name");
		
		if ( os != null && os.startsWith(WIN_ID))
			return true;
		else
			return false;
	}
}
