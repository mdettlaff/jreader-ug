package swt.JReader;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Subscriptions {
	
	public static CTabFolder folderSubs;
	SubsList lista;
	
	public Subscriptions(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "c:\\icons\\subscriptions\\subsTab2.png");
		
		folderSubs = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderSubs.setLayout(new FillLayout());
		folderSubs.setSimple(JReader.issimple);
				  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		//wlasciwy Ctab 
		final CTabItem item = new CTabItem(folderSubs, SWT.NONE);
		item.setText("Subscriptions");
		item.setImage(rssTab);
		/*
		 * Tresc ctaba
		 */
		Composite subComposite = new Composite(folderSubs, SWT.NONE);
		subComposite.setLayoutData(new FillLayout());
		subComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		lista = new SubsList(subComposite);
		
		item.setControl(subComposite);
		/*
		 * koniec zawartosci ctaba
		 */
		
		folderSubs.setSelection(item);
		folderSubs.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		
		folderSubs.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              JReader.statusText = "Choose a subscription.";
              JReader.statusLine.setText(JReader.statusText);
            }
          });
	}
	
	public void setBounds(Rectangle rect) {
		folderSubs.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderSubs.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderSubs.setVisible(bol);
	}
	public Rectangle getBounds() {
		return folderSubs.getBounds();
	}
}

