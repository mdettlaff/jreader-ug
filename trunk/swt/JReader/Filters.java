package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class Filters {

	private CTabFolder folderFilter;

	public Filters(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "c:\\icons\\filters\\rss-tab.png");
		
		folderFilter = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderFilter.setSimple(false);
		
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		//wlasciwy Ctab 
		final CTabItem item = new CTabItem(folderFilter, SWT.NONE);
		item.setText("Filters");
		item.setImage(rssTab);
		Text text = new Text(folderFilter, SWT.MULTI );
		text.setText("Text for item ");
		item.setControl(text);
		folderFilter.setSelection(item);
		folderFilter.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		
		folderFilter.addCTabFolder2Listener(new CTabFolder2Adapter() {
		
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folderFilter.setMinimized(true);
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folderFilter.setMaximized(true);
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folderFilter.setMinimized(false);
				folderFilter.setMaximized(false);
				shell.layout(true);
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
