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

public class Preview {

	CTabFolder folderPreview;
	private Rectangle bounds;
	private Rectangle tmpBounds = new Rectangle(0, 0, 0, 0);
	
	
	public Preview(final Composite shell) {
		
		final Image itemsTab = new Image(shell.getDisplay(), "c:\\icons\\items\\itemsTab.png");
		
		folderPreview = new CTabFolder(shell, SWT.BORDER | SWT.MULTI );
		
		folderPreview.setSimple(false);
		folderPreview.setMinimizeVisible(true);
		folderPreview.setMaximizeVisible(true);
		  
		Device device = Display.getCurrent ();
		Color bottom = new Color (device, 156, 156, 213);
		Color middle = new Color (device, 190, 190, 213);
		  
		  
		final CTabItem item = new CTabItem(folderPreview, SWT.CLOSE);
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
			
			//maxmize, minimize, restore
			public void minimize(CTabFolderEvent event) {
				folderPreview.setMinimized(true);
				
				bounds = folderPreview.getBounds();
				tmpBounds.x = bounds.x;
				tmpBounds.y = bounds.y;
				tmpBounds.width = bounds.width;
				tmpBounds.height = 24;

				folderPreview.setMaximizeVisible(false);
				
				folderPreview.setBounds(tmpBounds);
				shell.layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				folderPreview.setMaximized(true);
				
				bounds = folderPreview.getBounds();
				tmpBounds.x = bounds.x;
				tmpBounds.y = bounds.y;
				tmpBounds.width = bounds.width;
				tmpBounds.height = bounds.height;
				
				folderPreview.setMinimizeVisible(false);
				
				Rectangle clientArea = shell.getClientArea ();
				folderPreview.setBounds(clientArea);
				MainSash.folderFilter.setVisible(false);
				MainSash.folderItem.setVisible(false);
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				folderPreview.setMinimized(false);
				folderPreview.setMaximized(false);
				
				folderPreview.setMinimizeVisible(true);
				folderPreview.setMaximizeVisible(true);
				
				folderPreview.setBounds(bounds);
				MainSash.folderFilter.setVisible(true);
				MainSash.folderItem.setVisible(true);
				shell.layout(true);
			}
	   	});
	}
	public void setBounds(Rectangle rect) {
		folderPreview.setBounds(rect);
	}
	public void setBounds(int x, int y, int width, int height) {
		folderPreview.setBounds(x, y, width, height);
	}
	public void setVisible(boolean bol) {
		folderPreview.setVisible(bol);
	}
}
