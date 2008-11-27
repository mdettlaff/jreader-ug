package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Filters {

	
	public Filters(final Shell shell, Display display) {
		
		final Image rssTab = new Image(display, "c:\\icons\\filters\\rss-tab.png");
		
		final CTabFolder folderFilter = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		folderFilter.setSimple(false);
		folderFilter.setMaximized(true);
		folderFilter.setMinimizeVisible(true);
		folderFilter.setMaximizeVisible(true);
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		  
		final CTabItem item = new CTabItem(folderFilter, SWT.NONE);
		item.setText("Filters                ");
		item.setImage(rssTab);
		Text text = new Text(folderFilter, SWT.MULTI );
		text.setText("Text for item ");
		item.setControl(text);
		folderFilter.setSelection(item);
		folderFilter.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		
		folderFilter.addCTabFolder2Listener(new CTabFolder2Adapter() {
			
			//niezamykane taby
			public void close(CTabFolderEvent event) {
				if (event.item.equals(item)) {
					event.doit = false;
				}
			}
		
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folderFilter.setMinimized(true);
				folderFilter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2));
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folderFilter.setMaximized(true);
				folderFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folderFilter.setMinimized(false);
				folderFilter.setMaximized(false);
				folderFilter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1 , 2));
				shell.layout(true);
			}
	   	});
	}
	
}
