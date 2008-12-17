package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * Klasa główna GUI. Zbiera wszystkie widgety wyświetlając je w shellu.
 */
public class GUI {

	public static final Display display = new Display ();
	public static String statusText = "Status Line";
	public static Label statusLine;
	public static String version = "JReader v. 0.81";
	public static boolean issimple = false;
	public static Shell shell;

	/**
	 * Uruchamia GUI.
	 */
	public static void run() {
		final Image jreader = new Image(display, "data" + File.separator + "icons" + File.separator + "small" + File.separator + "jreader2.png");

		shell = new Shell (display);
		shell.setSize (800, 600);
		shell.setText(version);
		shell.setImage(jreader);
		shell.setLayout(new GridLayout());
		/* Wyśrodkowanie shella */
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    shell.setLocation(x, y);

		new MenuBar(shell);
		new MainToolBar(shell);
		new MainSash(shell);
		statusLine = new Label(shell, SWT.NONE);
		statusLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		statusLine.setText(statusText);
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}