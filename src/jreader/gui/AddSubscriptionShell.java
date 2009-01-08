package jreader.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import jreader.JReader;
import jreader.LinkNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
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
	private static Text url;
	private static Text tag;
	
	public AddSubscriptionShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "small" + File.separator + "add.png");
		addShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		addShell.setText("Add a subscription");
		addShell.setImage(jreader);
		addShell.setLayout(new GridLayout(2, true));
		
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 50;
	    addShell.setLocation(x, y);
	    
	    
	    new Label(addShell, SWT.NONE).setText("Give the URL address: ");
	    url = new Text(addShell, SWT.BORDER);
	    url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    url.setTextLimit(100);
	    url.setFocus();
	    
	    new Label(addShell, SWT.NONE).setText("Name the tags (optional): ");
	    tag = new Text(addShell, SWT.BORDER);
	    tag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    tag.setTextLimit(100);
	    
	    warning = new Label(addShell, SWT.NONE);
	    warning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	    warning.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
	    
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
            		addUrl();
                }
        });
	    caB.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	GUI.statusLine.setText("");
            	addShell.dispose();
                }
        });
	    url.addListener(SWT.DefaultSelection, new Listener() {
	        public void handleEvent(Event e) {
	        	addUrl();
	        }
	      });
	    tag.addListener(SWT.DefaultSelection, new Listener() {
	        public void handleEvent(Event e) {
	        	addUrl();
	        }
	      });
	    
	    addShell.pack();
	}

	public static void addUrl() {
		String warn = new String();
		try {
			JReader.addChannel(url.getText(), tag.getText());
		} catch (LinkNotFoundException lnfe) {
			warning.setText("Rss not found on this site.");
			return;
		} catch (MalformedURLException mue) {
			warning.setText("Wrong URL.");
			return;
		} catch (SAXParseException spe) {
			warn = "Source is not a valid XML file.\n";
			warn += "Error at line " + spe.getLineNumber() + ".\n";
			warn += "Details: " + spe.getLocalizedMessage() + ".\n";
			warning.setText(warn);
			return;
		} catch (SAXException saxe) {
			warn = "XML's parser error.\n";
			warning.setText(warn);
			return;
		} catch (SocketException se) {
			warn = "Cannot add the channel.\n";
			warn += "Detail: " + se.getLocalizedMessage();
			warning.setText(warn);
			return;
		} catch (IOException ioe) {
			warn = "Site download failed.";
			warning.setText(warn);
			return;
		} catch (IllegalArgumentException iae) {
			warn = "Wrong URL";
			warning.setText(warn);
			return;
		}
		SubsList.refresh();
		TagList.refresh();
    	addShell.dispose();
	}
	
	public void open() {
		addShell.open();
	}
}
