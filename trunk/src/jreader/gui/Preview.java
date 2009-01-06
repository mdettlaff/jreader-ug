package jreader.gui;

import java.io.File;
import java.util.Date;
import java.util.List;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public class Preview {

	public static CTabFolder folderPreview;
	private Rectangle bounds;
	private Rectangle tmpBounds = new Rectangle(0, 0, 0, 0);
	public static Browser browser;
	public static Label title;
	public static Label author;
	public static Link titleLink;
	public static CTabItem item;
	//lista zakladek w folderPreview
	public static List<PreviewItem> previewItemList;
	
	public Preview(final Composite shell) {
		
		final Image itemsTab = new Image(shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "preview" + File.separator + "previewTab.png");
		
		folderPreview = new CTabFolder(shell, SWT.BORDER | SWT.MULTI );
		folderPreview.setSimple(GUI.issimple);
		folderPreview.setMaximizeVisible(true);
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);  
		  
		item = new CTabItem(folderPreview, SWT.NONE);
		item.setText("Preview");
		item.setImage(itemsTab);
		Composite comp = new Composite(folderPreview, SWT.NONE);
		comp.setLayout(new GridLayout());
		Composite header = new Composite(comp, SWT.NONE);
		header.setLayout(new FillLayout(SWT.VERTICAL));
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		titleLink = new Link(header, SWT.NONE);
		title = new Label(header, SWT.NONE);
		author = new Label(header, SWT.NONE);
		
		
		if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			browser = new Browser(comp, SWT.MOZILLA | SWT.BORDER);
		} else {
			browser = new Browser(comp, SWT.BORDER);
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		item.setControl(comp);
		
		folderPreview.setSelection(item);
		folderPreview.setSelectionBackground(new Color[]{shell.getDisplay().getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

	//	LISTENERS
	
		/* Maksymalizacja i retostore okna */
		folderPreview.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void maximize(CTabFolderEvent event) {
				folderPreview.setMaximized(true);
				
				bounds = folderPreview.getBounds();
				tmpBounds.x = bounds.x;
				tmpBounds.y = bounds.y;
				tmpBounds.width = bounds.width;
				tmpBounds.height = bounds.height;
				
				//folderPreview.setMinimizeVisible(false);
				
				Rectangle clientArea = shell.getClientArea ();
				folderPreview.setBounds(clientArea);
				MainSash.folderFilter.setVisible(false);
				MainSash.folderItem.setVisible(false);
				MainSash.folderSubs.setVisible(false);
				MainSash.folderTag.setVisible(false);
				shell.layout(true);
			}
			public void restore(CTabFolderEvent event) {
				//folderPreview.setMinimized(false);
				folderPreview.setMaximized(false);
				
				folderPreview.setMaximizeVisible(true);
				
				folderPreview.setBounds(bounds);
				MainSash.folderFilter.setVisible(true);
				MainSash.folderItem.setVisible(true);
				MainSash.folderSubs.setVisible(true);
				MainSash.folderTag.setVisible(true);
				shell.layout(true);
			}
	   	});
		/* zmienia treść Status Line */
		folderPreview.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Item preview.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });
		/* Podwójne kliknięcie maksymalizuje lub przywraca rozmiar okna */
		folderPreview.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent me) {
				if (folderPreview.getMaximized()) {
					//folderPreview.setMinimized(false);
					folderPreview.setMaximized(false);
					
					folderPreview.setMaximizeVisible(true);
					
					folderPreview.setBounds(bounds);
					MainSash.folderFilter.setVisible(true);
					MainSash.folderItem.setVisible(true);
					MainSash.folderSubs.setVisible(true);
					MainSash.folderTag.setVisible(true);
				} else {
					folderPreview.setMaximized(true);
					
					bounds = folderPreview.getBounds();
					tmpBounds.x = bounds.x;
					tmpBounds.y = bounds.y;
					tmpBounds.width = bounds.width;
					tmpBounds.height = bounds.height;
					
					//folderPreview.setMinimizeVisible(false);
					
					Rectangle clientArea = shell.getClientArea ();
					folderPreview.setBounds(clientArea);
					MainSash.folderFilter.setVisible(false);
					MainSash.folderItem.setVisible(false);
					MainSash.folderSubs.setVisible(false);
					MainSash.folderTag.setVisible(false);
				}
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
		      public void changed(StatusTextEvent event) {
		        GUI.statusLine.setText(event.text); 
		      }
		    });
	}
	public void setBounds(Rectangle rect) {
		folderPreview.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderPreview.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderPreview.setVisible(bol);
	}
	
	public static void refresh() {
		String titleText = JReader.getPreview().getCurrent().getTitle();
		Date date = JReader.getPreview().getCurrent().getDate();
		String authorText = JReader.getPreview().getCurrent().getAuthor();
		String fromText = JReader.getPreview().getCurrent().getChannelTitle();
		titleLink.setText("<a>" + titleText + "</a>");
		
		browser.setText(JReader.getPreview().getCurrent().getHTML());
		title.setText(((date != null) ? date.toString() : " "));
		if (authorText != null && fromText != null)
			author.setText("Author: " + authorText + "\tFrom: " + fromText);
		else if (fromText != null && authorText == null)
			author.setText("From: " + fromText);
		else if (authorText != null)
			author.setText(authorText);
		else
			author.setText("");
		
		titleLink.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				//System.out.println("Selection: " + event.text);
				browser.setUrl(JReader.getPreview().getCurrent().getLink());
			}
		});
	}
}
