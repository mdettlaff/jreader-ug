package jreader.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class SysTray {

	public static TrayItem trayItem;
	private Menu popupMenu;
	
	public SysTray() {
		
		createMenu();
		
		final Tray tray = GUI.display.getSystemTray();
	    if (tray == null) {
	      System.out.println("The system tray is not available");
	    } else {
	      trayItem = new TrayItem(tray, SWT.NONE);
	      trayItem.setToolTipText(GUI.version);
	      trayItem.setImage(GUI.jreader);
	      
	      trayItem.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	            if (GUI.shell.isVisible())
	            	GUI.shell.setVisible(false);
	            else
	            	GUI.shell.setVisible(true);
	          }
	        });
	      trayItem.addListener(SWT.MenuDetect, new Listener() {
	          public void handleEvent(Event event) {
	            popupMenu.setVisible(true);
	          }
	        });
	    }
	}
	
	private void createMenu() {
		popupMenu = new Menu(GUI.shell, SWT.POP_UP);
	    MenuItem about = new MenuItem(popupMenu, SWT.NONE);
	    about.setText("About");
	    MenuItem open = new MenuItem(popupMenu, SWT.NONE);
	    open.setText("Open");
	    new MenuItem(popupMenu, SWT.SEPARATOR);
	    MenuItem options = new MenuItem(popupMenu, SWT.NONE);
	    options.setText("Preferences");
	    MenuItem synchronize = new MenuItem(popupMenu, SWT.NONE);
	    synchronize.setText("Synchronize");
	    new MenuItem(popupMenu, SWT.SEPARATOR);
	    MenuItem close = new MenuItem(popupMenu, SWT.NONE);
	    close.setText("Exit");
	    
	    about.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	        	  AboutShell s = new AboutShell(GUI.shell);
	              s.open();
	          }
	        });
	    open.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	        	  GUI.shell.setVisible(true);
	          }
	        });
	    options.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	        	  OptionsShell optionsShell = new OptionsShell(GUI.shell);
	        	  optionsShell.open();
	          }
	        });
	    synchronize.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	          }
	        });
	    close.addListener(SWT.Selection, new Listener() {
	          public void handleEvent(Event event) {
	        	  GUI.display.dispose();
	          }
	        });
	}
}
