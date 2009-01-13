package jreader.gui;

import java.io.File;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Okno filtrów
 * 
 * @author Karol
 *
 */
public class Filters {

	/**
	 * Kontener filtrów
	 */
	public static CTabFolder folderFilter;
	/**
	 * Przycisk do wyświetlania wszystkich itemów.
	 */
	public static Button allButton;
	/**
	 * Przycisk do wyświetlania nieprzeczytanych itemów.
	 */
	public static Button unreadButton;
	private static String allButtonLabel = "All items ";
	private static String unreadButtonLabel = "Unread items ";

	public Filters(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "data" + File.separator + "icons" + File.separator + "filters" + File.separator + "rss-tab.png");
		
		folderFilter = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderFilter.setSimple(GUI.issimple);
		
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		//Ctab 
		final CTabItem item = new CTabItem(folderFilter, SWT.NONE);
		item.setText("Filters");
		item.setImage(rssTab);
		
		/*
		 * Zawartosc ctaba
		 */
		Composite filterComposite = new Composite(folderFilter, SWT.NONE);
		filterComposite.setLayoutData(new FillLayout());
		filterComposite.setLayout(new FillLayout(SWT.VERTICAL));
		allButton = new Button(filterComposite, SWT.TOGGLE | SWT.FLAT);
		allButton.setAlignment(SWT.LEFT);
		allButton.setText(allButtonLabel + "(" + JReader.getItemsCount() + ")");
		
		unreadButton = new Button(filterComposite, SWT.TOGGLE | SWT.FLAT);
		unreadButton.setAlignment(SWT.LEFT);
		unreadButton.setText(unreadButtonLabel +  "(" + JReader.getUnreadItemsCount() + ")");
		unreadButton.setSelection(true);
		
		item.setControl(filterComposite);
		/*
		 * koniec zawartosci ctaba
		 */
		
		
		folderFilter.setSelection(item);
		folderFilter.setSelectionBackground(new Color[]{Focus.white, Focus.midgray, Focus.gray, Focus.gray},
		new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		/*folderFilter.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Choose a type of items filter.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
		
		folderFilter.addMouseListener(new MouseListener() {
	        public void mouseDown(MouseEvent e) {
	            folderFilter.setFocus();
	          }
			public void mouseDoubleClick(MouseEvent arg0) {
			}
			public void mouseUp(MouseEvent arg0) {
			}
	    });
		allButton.addFocusListener(Focus.setFocus((Filters.folderFilter)));
		unreadButton.addFocusListener(Focus.setFocus((Filters.folderFilter)));
		
		allButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (allButton.getSelection()) {
					unreadButton.setSelection(false);
					allButton.setSelection(true);
				}
				JReader.selectAll();
				ItemsTable.refresh();
			}
		});
		unreadButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (unreadButton.getSelection()) {
					allButton.setSelection(false);
					unreadButton.setSelection(true);
				}
				JReader.selectUnread();	
				ItemsTable.refresh();
			}
		});
	}
	/**
	 * Ustawia rozmiar konteneru.
	 * 
	 * @param rect Rozmiar podany jakor typ <code>rectangle</code>
	 */
	public void setBounds(Rectangle rect) {
		folderFilter.setBounds(rect);
	}
	/**
	 * Ustawia rozmiar konteneru.
	 * 
	 * @param x Położenie na osi x
	 * @param y Położenie na osi y
	 * @param width Szerokość
	 * @param height Wysokość
	 */
	public void setBounds(int x, int y, int width, int height) {
		folderFilter.setBounds(x, y, width, height);
	}
	/**
	 * Ustawia czy widget ma być widoczny lub nie.
	 * 
	 * @param bol Zmienna logiczna. <code>true</code> jeśli widget ma być widoczny, 
	 * w innym przypadku <code>false</code>
	 */
	public void setVisible(boolean bol) {
		folderFilter.setVisible(bol);
	}
	/**
	 * Odśiweża zawartośc kontenera.
	 */
	public static void refresh() {
		GUI.display.asyncExec(new Runnable() {
			public void run() {
				allButton.setText(allButtonLabel + "(" + JReader.getItemsCount() + ")");
				unreadButton.setText(unreadButtonLabel +  "(" + JReader.getUnreadItemsCount() + ")");
			}
		});
	}
}
