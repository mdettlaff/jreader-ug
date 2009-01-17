package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
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

/**
 * Tworzy okno 'About'
 * 
 * @author Karol
 *
 */
public class AboutShell {

	private static Shell aboutShell;
	private static String about = "<html>" + 
			"<body>" +
			"<div style='font-size: 11px'><h2>JReader 1.1</h2>" +
			"RSS and Atom feed reader<br />" +
			"Copyright © 2009<br /><br />" +
			"<b>Authors:</b><br />" +
			"Michał Dettlaff<br />" +
			"Karol Domagała<br />" +
			"Łukasz Draba<br />" +
			"<br />" +
			"Homepage:<br />" +
			"<a href='http://code.google.com/p/jreader-ug/'>http://code.google.com/p/jreader-ug/</a>" +
			"<br /><br />" +
			"<p>" +
			"This program is free software: you can redistribute it and/or modify " +
			"it under the terms of the GNU General Public License as published by " +
			"the Free Software Foundation, either version 3 of the License, or " +
			"(at your option) any later version." +
			"</p>" +
			"<p>" +
			"This program is distributed in the hope that it will be useful, " +
			"but WITHOUT ANY WARRANTY; without even the implied warranty of " +
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the " +
			"GNU General Public License for more details." +
			"</p>" +
			"<p>" +
			"You should have received a copy of the GNU General Public License " +
			"along with this program.  If not, see <a href='http://www.gnu.org/licenses/'>" +
			"http://www.gnu.org/licenses/</a>" +
			"</p></div>" +
			"</body>" +
			"</html>";
	private Browser browser;
	
	public AboutShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "big" + File.separator + "jreader2.png");
		aboutShell = new Shell(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.TOOL);
		aboutShell.setText("About JReader");

		aboutShell.setImage(jreader);
		//RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		//rowLayout.center = true;
		aboutShell.setLayout(new GridLayout());
		
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 250;
	    int y = bounds.height/ 2 - 250;
	    aboutShell.setLocation(x, y);
	    
	    Label pic = new Label(aboutShell, SWT.NONE);
	    pic.setImage(jreader);
	    pic.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    
	    if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			browser = new Browser(aboutShell, SWT.MOZILLA);
		} else {
			browser = new Browser(aboutShell, SWT.NONE);
		}
	    browser.setText(about);
	   // browser.setSize(500, 400);
	    browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    new Label(aboutShell, SWT.NONE).setText(" ");
	    
	    Button okButton = new Button(aboutShell, SWT.PUSH);
	    okButton.setText("  &OK  ");
	    okButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				aboutShell.close();
			}
		});
		
		aboutShell.setSize(510, 550);
	  	    
	}
	
	/**
	 * Otwiera okno
	 */
	public void open() {
		aboutShell.open();
	}
}
