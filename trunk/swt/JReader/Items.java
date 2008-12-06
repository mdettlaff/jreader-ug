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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class Items {

	private CTabFolder folderItem;
	
	public Items(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image itemsTab = new Image(display, "c:\\icons\\items\\itemsTab.png");
			
		folderItem = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderItem.setSimple(false);
				
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		  
		final CTabItem item = new CTabItem(folderItem, SWT.NONE);
		item.setText("Items");
		item.setImage(itemsTab);
		Text text = new Text(folderItem, SWT.MULTI );
		text.setText("Text for item lsjklf\nklsdj\nklsdjflkjsdlfj\n");
		item.setControl(text);
		
		folderItem.setSelection(item);
		folderItem.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

	//	LISTENERS
		
			
		folderItem.addCTabFolder2Listener(new CTabFolder2Adapter() {
			
		
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folderItem.setMinimized(true);
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folderItem.setMaximized(true);
				
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folderItem.setMinimized(false);
				folderItem.setMaximized(false);
				
				shell.layout(true);
			}
	   	});
		text.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              JReader.statusText = "Select the topic you want to read.";
              JReader.statusLine.setText(JReader.statusText);
            }
          });
		
	}
	
	public void setBounds(Rectangle rect) {
		folderItem.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderItem.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderItem.setVisible(bol);
	}
	
}
