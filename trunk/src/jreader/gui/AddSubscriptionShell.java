package jreader.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import jreader.JReader;
import jreader.LinkNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AddSubscriptionShell {

	private static Shell addShell;
	private static Label warning;
	
	public AddSubscriptionShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "small" + File.separator + "add.png");
		addShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		addShell.setText("Add a subscription");
		//aboutShell.setSize(200, 150);
		addShell.setImage(jreader);
		addShell.setLayout(new GridLayout(2, true));
		
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 50;
	    addShell.setLocation(x, y);
	    
	    
	    new Label(addShell, SWT.NONE).setText("Give the URL address: ");
	    final Text url = new Text(addShell, SWT.BORDER);
	    url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    url.setTextLimit(100);
	    url.setText("http://");
	    
	    new Label(addShell, SWT.NONE).setText("Name the tags (optional): ");
	    final Text tag = new Text(addShell, SWT.BORDER);
	    tag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    tag.setTextLimit(100);
	    
	    warning = new Label(addShell, SWT.NONE);
	    warning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	    
	    Button okB = new Button(addShell, SWT.PUSH);
	    okB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    okB.setText("OK");
	    Button caB = new Button(addShell, SWT.PUSH);
	    caB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    caB.setText("Cancel");
	    
	    /*
	     * Listeners
	     */
	    
	    //OK button
	    okB.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	GUI.statusLine.setText("");
            	System.out.println(url.getText());
            	System.out.println(tag.getText());
				try {
					JReader.addChannel(url.getText(), tag.getText());
				} catch (LinkNotFoundException lnfe) {
					System.out.println("Nie znaleziono kanalow na tej stronie.");
					warning.setText("Rss not found on this site.");
					return;
				} catch (MalformedURLException mue) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" URL jest nieprawidlowy.");
				} catch (SAXParseException spe) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
					System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
					System.out.println("Szczegoly: " + spe.getLocalizedMessage());
				} catch (SAXException saxe) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Blad parsera XML.");
				} catch (SocketException se) {
					System.out.println("Nie mozna dodac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
				} catch (IOException ioe) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Pobieranie strony nie powiodlo sie.");
				} catch (IllegalArgumentException iae) {
					System.out.println("URL jest nieprawidłowy");
					warning.setText("Wrong URL.");
					return;
				}
				SubsList.refresh();
				TagList.refresh();
            	addShell.dispose();
                }
        });
	    caB.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	GUI.statusLine.setText("");
            	addShell.dispose();
                }
        });
	    
	    
	    addShell.pack();
	}

	public void open() {
		addShell.open();
	}
}
