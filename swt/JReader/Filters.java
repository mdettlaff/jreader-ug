package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Text;

public class Filters {

	public static CTabFolder folderFilter;
	public static Button allButton;
	public static Button unreadButton;
	public String allButtonLabel = "All messages ";
	public String unreadButtonLabel = "Unread messages ";
	public int unread = 0;
	public int msgs = 0;

	public Filters(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "c:\\icons\\filters\\rss-tab.png");
		
		folderFilter = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderFilter.setSimple(JReader.issimple);
		
		  
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
		allButton.setText(allButtonLabel + "(" + msgs + ")");
		allButton.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		
		unreadButton = new Button(filterComposite, SWT.TOGGLE | SWT.FLAT);
		unreadButton.setAlignment(SWT.LEFT);
		unreadButton.setText(unreadButtonLabel +  "(" + unread + ")");
		unreadButton.setSelection(true);
		
		item.setControl(filterComposite);
		/*
		 * koniec zawartosci ctaba
		 */
		
		
		folderFilter.setSelection(item);
		folderFilter.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		folderFilter.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              JReader.statusText = "Choose the type of messages filter.";
              JReader.statusLine.setText(JReader.statusText);
            }
          });
		allButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (allButton.getSelection()) {
					unreadButton.setSelection(false);
					allButton.setSelection(true);
				}
			}
		});
		unreadButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (unreadButton.getSelection()) {
					allButton.setSelection(false);
					unreadButton.setSelection(true);
				}
			}
		});
	}
	
	public void setBounds(Rectangle rect) {
		folderFilter.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderFilter.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderFilter.setVisible(bol);
	}
}
