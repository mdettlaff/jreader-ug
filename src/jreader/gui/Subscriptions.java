package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Kontener zawierający listę subskrypcji.
 * 
 * @author Karol
 *
 */
public class Subscriptions {
	
	/**
	 * Kontener subskrypcji.
	 */
	public static CTabFolder folderSubs;
	SubsList lista;
	static Composite subComposite;
	
	public Subscriptions(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "data" + File.separator + "icons" + File.separator + "subscriptions" + File.separator + "subsTab2.png");
		
		folderSubs = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderSubs.setLayout(new FillLayout());
		folderSubs.setSimple(GUI.issimple);
		   
		final CTabItem item = new CTabItem(folderSubs, SWT.NONE);
		item.setText("Subscriptions");
		item.setImage(rssTab);

		subComposite = new Composite(folderSubs, SWT.NONE);
		subComposite.setLayoutData(new FillLayout());
		subComposite.setLayout(new FillLayout(SWT.VERTICAL));
		lista = new SubsList(subComposite);
		item.setControl(subComposite);

		folderSubs.setSelection(item);
		folderSubs.setSelectionBackground(new Color[]{Focus.white, Focus.midgray, Focus.gray, Focus.gray},
				new int[] {20, 40, 100}, true); 
		
		//LISTENERS
		
		/*status text*/
		/*folderSubs.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Choose a subscription.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
		
	    folderSubs.addMouseListener(new MouseListener() {
	        public void mouseDown(MouseEvent e) {
	            folderSubs.setFocus();
	          }
			public void mouseDoubleClick(MouseEvent arg0) {
			}
			public void mouseUp(MouseEvent arg0) {
			}
	    });
	}
	/**
	 * Ustawia rozmiar konteneru.
	 * 
	 * @param rect Rozmiar podany jakor typ <code>rectangle</code>
	 */
	public void setBounds(Rectangle rect) {
		folderSubs.setBounds(rect);
	}
	/**
	 * Ustawia rozmiar kontenera 'Subscriptions'.
	 * 
	 * @param x Położenie na osi x
	 * @param y Położenie na osi y
	 * @param width Szerokość
	 * @param height Wysokośc
	 */
	public void setBounds(int x, int y, int width, int height) {
		folderSubs.setBounds(x, y, width, height);
	}
	/**
	 * Ustawia czy widget ma być widoczny lub nie.
	 * 
	 * @param bol Zmienna logiczna. <code>true</code> jeśli widget ma być widoczny, 
	 * w innym przypadku <code>false</code>
	 */
	public void setVisible(boolean bol) {
		folderSubs.setVisible(bol);
	}
	/**
	 * Zwraca rozmiar konteneru.
	 * @return Rectangle rozmiar konteneru.
	 */
	public Rectangle getBounds() {
		return folderSubs.getBounds();
	}
}

