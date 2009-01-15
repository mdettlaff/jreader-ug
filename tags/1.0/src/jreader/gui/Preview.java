package jreader.gui;

import java.util.ArrayList;
import java.util.List;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

/**
 * Kontener zawierający zakładki z wiadomościami rss.
 * 
 * @author Karol
 *
 */
public class Preview {

	/**
	 * Kontener dla zakładek.
	 */
	public static CTabFolder folderPreview;
	public static Rectangle bounds;
	private Rectangle tmpBounds = new Rectangle(0, 0, 0, 0);
	/**
	 * Przeglądarka wyśweitlająca treść wiadomości rss.
	 */
	public static Browser browser;
	/**
	 * Tytuł zakładki.
	 */
	public static Label title;
	/**
	 * Autor wiadomości.
	 */
	public static Label author;
	/**
	 * Link wysyłający do oryginalnej treści.
	 */
	public static Link titleLink;
	/**
	 * Lista zakładek.
	 */
	public static List<PreviewItem> previewItemList = new ArrayList<PreviewItem>();
	
	public Preview(final Composite shell) {
		
		
		folderPreview = new CTabFolder(shell, SWT.BORDER | SWT.MULTI );
		folderPreview.setSimple(GUI.issimple);
		folderPreview.setMaximizeVisible(true);
		folderPreview.setUnselectedCloseVisible(false); 
		
		folderPreview.setSelectionBackground(new Color[]{Focus.white, Focus.midgray, Focus.gray, Focus.gray},
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
		/*folderPreview.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Item preview.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
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
				folderPreview.setFocus();				
			}
			
			public void mouseUp(MouseEvent e) {
			}
		});
		
		/*zamykanie zakladek */
		folderPreview.addCTabFolder2Listener(new CTabFolder2Listener() {
		      public void close(CTabFolderEvent e) {
		    	  previewItemList.remove(folderPreview.getSelectionIndex());
		    	  JReader.removePreviewTab(folderPreview.getSelectionIndex());
		      }
		      public void minimize(CTabFolderEvent arg0) {
		      }
		      public void maximize(CTabFolderEvent arg0) {
		      }
		      public void restore(CTabFolderEvent arg0) {
		      }
		      public void showList(CTabFolderEvent arg0) {
		      }
	    });
	}
	/**
	 * Ustawia rozmiar konteneru.
	 * 
	 * @param rect Rozmiar podany jakor typ <code>rectangle</code>
	 */
	public void setBounds(Rectangle rect) {
		folderPreview.setBounds(rect);
	}
	/**
	 * Ustawia rozmiar kontenera 'Preview'.
	 * 
	 * @param x Położenie na osi x
	 * @param y Położenie na osi y
	 * @param width Szerokość
	 * @param height Wysokośc
	 */
	public void setBounds(int x, int y, int width, int height) {
		folderPreview.setBounds(x, y, width, height);
	}
	/**
	 * Ustawia czy widget ma być widoczny lub nie.
	 * 
	 * @param bol Zmienna logiczna. <code>true</code> jeśli widget ma być widoczny, 
	 * w innym przypadku <code>false</code>
	 */
	public void setVisible(boolean bol) {
		folderPreview.setVisible(bol);
	}
}
