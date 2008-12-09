package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class AboutShell {

	private static Shell aboutShell;
	
	public AboutShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "c:\\icons\\big\\jreader2.png");
		aboutShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL |SWT.TOOL);
		aboutShell.setText("About JReader programm");

		aboutShell.setImage(jreader);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.pack = true;
		rowLayout.justify = true;
		rowLayout.marginWidth = 40;
		rowLayout.center = true;
		rowLayout.spacing = 10;
		
		aboutShell.setLayout(rowLayout);
		
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 50;
	    aboutShell.setLocation(x, y);
	    
	    
	    Label pic = new Label(aboutShell, SWT.NONE);
	    pic.setImage(jreader);
	    Label version = new Label(aboutShell, SWT.NONE);
	    version.setText(JReader.version);
	    new Label(aboutShell, SWT.NONE).setText("© All rights reserved.");
	    new Label(aboutShell, SWT.NONE).setText("2008.");
	    Button okButton = new Button(aboutShell, SWT.PUSH);
	    okButton.setText("&OK");
	    
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("AboutShell: OK was pressed.\nDisposing...");
				aboutShell.close();
			}
		});
	    
	    
	    
	    aboutShell.pack();
	  	    
	}
	
	public void open() {
		aboutShell.open();
	}
}
