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

public class Preview {

	
	public Preview(final Shell shell) {
		
		final Image itemsTab = new Image(shell.getDisplay(), "c:\\icons\\items\\itemsTab.png");
		
		final CTabFolder folderPreview = new CTabFolder(shell, SWT.BORDER | SWT.MULTI );
		folderPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		folderPreview.setSimple(false);
		folderPreview.setMaximized(true);
		folderPreview.setMinimizeVisible(true);
		folderPreview.setMaximizeVisible(true);
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		  
		final CTabItem item = new CTabItem(folderPreview, SWT.NONE);
		item.setText("Preview");
		item.setImage(itemsTab);
		Text text = new Text(folderPreview, SWT.MULTI );
		text.setText("Text for item lsjklf\nklsdj\nklsdjflkjsdlfj\n");
		item.setControl(text);
		
		folderPreview.setSelection(item);
		folderPreview.setSelectionBackground(new Color[]{shell.getDisplay().getSystemColor(SWT.COLOR_WHITE), middle, bottom, bottom},
				new int[] {20, 40, 100}, true);

	//	LISTENERS
		
			
		folderPreview.addCTabFolder2Listener(new CTabFolder2Adapter() {
			
			//niezamykane taby
			public void close(CTabFolderEvent event) {
				if (event.item.equals(item)) {
					event.doit = false;
				}
			}
		
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folderPreview.setMinimized(true);
				folderPreview.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folderPreview.setMaximized(true);
				folderPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folderPreview.setMinimized(false);
				folderPreview.setMaximized(false);
				folderPreview.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				shell.layout(true);
			}
	   	});
	}
	
}
