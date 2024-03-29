package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Okno zawierające tabelę wiadomości.
 * 
 * @author Karol
 *
 */
public class Items {

	/**
	 * kontener dla tabeli itemów.
	 */
	public static CTabFolder folderItem;
	ItemsTable itemsTable;
	public static Composite tableComposite;
	
	public Items(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image itemsTab = new Image(display, "data" + File.separator + "icons" + File.separator + "items" + File.separator + "itemsTab2.png");
			
		folderItem = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderItem.setLayout(new FillLayout());
		folderItem.setSimple(GUI.issimple);
				
		final CTabItem item = new CTabItem(folderItem, SWT.NONE);
		item.setText("Items");
		item.setImage(itemsTab);
		
		/*
		 * Zawartosc ctaba
		 */
		tableComposite = new Composite(folderItem, SWT.NONE);
		tableComposite.setLayoutData(new FillLayout());
		tableComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		itemsTable = new ItemsTable(tableComposite);
		
		item.setControl(tableComposite);
		/*
		 * koniec zawartosci ctaba
		 */
		folderItem.setSelection(item);
		folderItem.setSelectionBackground(new Color[]{Focus.white, Focus.midgray, Focus.gray, Focus.gray},
				new int[] {20, 40, 100}, true);

	//	LISTENERS
		
			
		/*folderItem.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Select the topic you want to read.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
		folderItem.addMouseListener(new MouseListener() {
	        public void mouseDown(MouseEvent e) {
	            folderItem.setFocus();
	          }
			public void mouseDoubleClick(MouseEvent arg0) {
			}
			public void mouseUp(MouseEvent arg0) {
			}
	    });
	}
	/**
	 * Ustawia rozmiar okna 'Items'.
	 * 
	 * @param x Położenie na osi x
	 * @param y Położenie na osi y
	 * @param width Szerokość
	 * @param height Wysokośc
	 */
	public void setBounds(int x, int y, int width, int height) {
		folderItem.setBounds(x, y, width, height);
	}
	/**
	 * Ustawia czy widget ma być widoczny lub nie.
	 * 
	 * @param bol Zmienna logiczna. <code>true</code> jeśli widget ma być widoczny, 
	 * w innym przypadku <code>false</code>
	 */
	public void setVisible(boolean bol) {
		folderItem.setVisible(bol);
	}
	
}
