package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class Tags {
	public static CTabFolder folderTag;
	TagList tagList;

	public Tags(final Composite shell) {
		
		Display display = shell.getDisplay();
		final Image rssTab = new Image(display, "data" + File.separator + "icons" + File.separator + "tags" + File.separator + "tagTab2.png");
		
		folderTag = new CTabFolder(shell, SWT.BORDER | SWT.SINGLE );
		folderTag.setLayout(new FillLayout());
		folderTag.setSimple(GUI.issimple);
				  
		  
		//wlasciwy Ctab 
		final CTabItem item = new CTabItem(folderTag, SWT.NONE);
		item.setText("Tags");
		item.setImage(rssTab);
		
		/*
		 * tresc ctaba
		 */
		Composite tagComposite = new Composite(folderTag, SWT.NONE);
		tagComposite.setLayoutData(new FillLayout());
		tagComposite.setLayout(new FillLayout(SWT.VERTICAL));		
		tagList = new TagList(tagComposite);
		item.setControl(tagComposite);
		/*
		 * koniec tresci ctaba
		 */
		folderTag.setSelection(item);
		folderTag.setSelectionBackground(new Color[]{Focus.white, Focus.midgray, Focus.gray, Focus.gray},
				new int[] {20, 40, 100}, true);

		  
//		LISTENERS
		
		/*tagComposite.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "Choose a tag to filter.";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
		
		folderTag.addMouseListener(new MouseListener() {
	        public void mouseDown(MouseEvent e) {
	            folderTag.setFocus();
	          }
			public void mouseDoubleClick(MouseEvent arg0) {
			}
			public void mouseUp(MouseEvent arg0) {
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
