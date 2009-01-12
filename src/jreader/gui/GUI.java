package jreader.gui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Klasa główna GUI. Zbiera wszystkie widgety wyświetlając je w shellu.
 */
public class GUI {

	public static final Display display = new Display();
	public static Color gray = new Color (display, 240, 250, 250);
	public static Color white = new Color (display, 255, 255, 254);
	public static String statusText = "Status Line";
	public static Label statusLine;
	public static String version = "JReader 1.0";
	public static boolean issimple = false;
	public static Shell shell;
	public static Image jreader = new Image(display, "data" + File.separator + "icons" + File.separator + "small" + File.separator + "jreader2.png");
	public static Image preview = new Image(display, "data" + File.separator + "icons" + File.separator + "preview" + File.separator + "previewTab.png");
	public static ProgressBar progressBar;
	public static final DateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


	/**
	 * Uruchamia GUI.
	 */
	public static void run() { 

		shell = new Shell (display);
		shell.setMaximized(true);
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
		new SysTray();
		Composite status = new Composite(shell, SWT.NONE);
		status.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		status.setLayout(gridLayout);
		statusLine = new Label(status, SWT.NONE);
		statusLine.setSize(500, 23);
		statusLine.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		
		statusLine.setText(statusText);
		//new WebProgress(status);
		progressBar = new ProgressBar(status, SWT.SMOOTH);
		progressBar.setSize(100, 20);
		progressBar.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		progressBar.setVisible(false);
		
		shell.open();

		//Listenery do skrótów klawiaturówych
		shell.getDisplay().addFilter(SWT.KeyUp, new Listener() {
			public void handleEvent(Event event) {
				//System.out.println(event.keyCode);
				if (event.character == 'n') {
					System.out.println("next unread");
					MainToolBar.showNextUnread();
				}
			}
		});
		
		
		
		// Aktualizowanie wszystkich kanałów przy starcie, jeśli wybrano
		// taką opcję
		if (JReader.getConfig().getUpdateAllOnStartup()) {
			new UpdateThread();
		}

		// Automatyczne aktualizowanie kanałów co określoną ilość minut,
		// jeśli wybrano taką opcję
		if (JReader.getConfig().getAutoUpdateMinutes() > 0) {
			new UpdateDaemon();
		}
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
		jreader.dispose();
	}
	
	/**
	 * Tworzy nową zakładkę w Preview
	 * 
	 * @param title Tytuł zakładki.
	 * @return Obiekt PreviewItem.
	 */
	public static PreviewItem openTab(String title) {
		PreviewItem newPreviewItem = new PreviewItem(title, preview);
		Preview.previewItemList.add(newPreviewItem);
		return newPreviewItem;
	}
}