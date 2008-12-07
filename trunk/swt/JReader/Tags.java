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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class Tags {
	public static CTabFolder folderTag;

	public Tags(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "c:\\icons\\tags\\tagTab2.png");
		
		folderTag = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderTag.setSimple(JReader.issimple);
				  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		//wlasciwy Ctab 
		final CTabItem item = new CTabItem(folderTag, SWT.NONE);
		item.setText("Tags");
		item.setImage(rssTab);
		Text text = new Text(folderTag, SWT.MULTI );
		text.setText("Text for item ");
		item.setControl(text);
		folderTag.setSelection(item);
		folderTag.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		text.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              JReader.statusText = "Choose a tag to filtrate.";
              JReader.statusLine.setText(JReader.statusText);
            }
          });
	}
	
	public void setBounds(Rectangle rect) {
		folderTag.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderTag.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderTag.setVisible(bol);
	}
	public Rectangle getBounds() {
		return folderTag.getBounds();
	}
}
