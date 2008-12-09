package swt.JReader;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/*
 * Klasa glowna. Zbiera wszystkie widgety wyswietlajac je w shellu.
 * 
 */
public class JReader {

	public static final Display display = new Display ();
	public static String statusText = "Status Line";
	public static Label statusLine;
	public static String version = "JReader v. 0.0.8";
	public static boolean issimple = false;
	public static Shell shell;
	
	
	public static void main(String[] args) {
		final Image jreader = new Image(display, "c:\\icons\\small\\jreader2.png");
		
	//	Shell properites	
		shell = new Shell (display);
		//shell.setMinimumSize(400,300);
		shell.setSize (800, 600);
		shell.setText(version);
		shell.setImage(jreader);
		shell.setLayout(new GridLayout());
		/* Wysrodkowanie shella */
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    shell.setLocation(x, y);
		
	//	MenuBar
		new MenuBar(shell);
		
	//	SWT ToolBar
		new MainToolBar(shell);
	
	//	SASH
		new MainSash(shell);
		
	//	status Line
		statusLine = new Label(shell, SWT.NONE);
		statusLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		statusLine.setText(statusText);

		//shell.pack();
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}