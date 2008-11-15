package swt.JReader;


import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/*
 * Klasa glowna. Zbiera wszystkie widgety wyswietlajac je w shellu.
 * 
 */
public class JReader {

	public static final Display display = new Display ();
	
	public static void main(String[] args) {
		
		final Image jreader = new Image(display, "c:\\icons\\small\\jreader.png");
		
	//	Shell properites
		
		final Shell shell = new Shell (display);
		shell.setSize (600, 400);
		shell.setText("JReader 0.0.1");
		shell.setImage(jreader);
		shell.setLayout(new GridLayout(1, false));
		//Wysrodkowanie shella
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    shell.setLocation(x, y);
		
		
	//	MenuBar
		new MenuBar(shell);
		
	//	SWT CoolBar
		new MainCoolBar(shell, display);
		
		
		
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}