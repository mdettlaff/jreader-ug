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

public class Items {

	
	public Items(final Shell shell, Display display) {
		
		final Image itemsTab = new Image(display, "c:\\icons\\items\\itemsTab.png");
		
		final CTabFolder folder = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		folder.setSimple(false);
		folder.setMaximized(true);
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		  
		final CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText("Items");
		item.setImage(itemsTab);
		Text text = new Text(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		text.setText("Text for item lsjklf\nklsdj\nklsdjflkjsdlfj\n");
		item.setControl(text);
		
		folder.setSelection(item);
		folder.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

	//	LISTENERS
		
			
		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			
			//niezamykane taby
			public void close(CTabFolderEvent event) {
				if (event.item.equals(item)) {
					event.doit = false;
				}
			}
		
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folder.setMinimized(true);
				folder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folder.setMaximized(true);
				folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				//shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folder.setMinimized(false);
				folder.setMaximized(false);
				folder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				shell.layout(true);
			}
	   	});
	}
	
}
