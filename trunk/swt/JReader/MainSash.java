package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;

public class MainSash {

	Composite sashComp;
	Sash vSash;
	Sash hSash;
	int SASH_LIMIT = 25;
	int SASH_WIDTH = 3;
	int folderFilterHeight = 100;
	int folderItemHeight = 100;
	static Filters folderFilter;
	static Items folderItem;
	static Preview folderPreview;
	
	public MainSash(Composite shell) {
		
		sashComp = new Composite(shell, SWT.NONE);
		sashComp.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		
		/* Tworze nowe taby */
		folderFilter = new Filters(sashComp); 
		folderItem = new Items(sashComp);
		folderPreview = new Preview(sashComp);
	
		/* Dwie sashes */
		vSash = new Sash (sashComp, SWT.VERTICAL | SWT.SMOOTH);
		hSash = new Sash (sashComp, SWT.HORIZONTAL | SWT.SMOOTH);
		
		/* listeners */
		hSash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.y = Math.min (Math.max (event.y, SASH_LIMIT), rect.height - SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					hSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		vSash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.x = Math.min (Math.max (event.x, SASH_LIMIT), rect.width - SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					vSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		sashComp.addControlListener (new ControlAdapter () {
			public void controlResized (ControlEvent event) {
				resized ();
			}
		});
	}
		
		

	void layout () {
		
		Rectangle clientArea = sashComp.getClientArea ();
		Rectangle hSashBounds = hSash.getBounds ();
		Rectangle vSashBounds = vSash.getBounds ();
		
		folderFilter.setBounds (0, 0, vSashBounds.x, folderFilterHeight);
		folderItem.setBounds (vSashBounds.x + vSashBounds.width, 0, clientArea.width - (vSashBounds.x + vSashBounds.width), hSashBounds.y);
		folderPreview.setBounds (vSashBounds.x + vSashBounds.width, hSashBounds.y + hSashBounds.height, clientArea.width - (vSashBounds.x + vSashBounds.width), clientArea.height - (hSashBounds.y + hSashBounds.height));
	
		hSashBounds.width = clientArea.width - vSashBounds.x;
		hSashBounds.x = vSashBounds.x;
		hSash.setBounds (hSashBounds);
	}
	
	void resized () {
		
		Rectangle clientArea = sashComp.getClientArea ();
		
		/*
		* Tworzy bounds dla tabu 'filters'. Umieszcza go po lewej stronie.
		* Wysokoœæ z góry ustalona.
		* (x, y, width, height)
		*/
		Rectangle list1Bounds = new Rectangle (0, 0, (clientArea.width - SASH_WIDTH) / 4, folderFilterHeight);
		folderFilter.setBounds (list1Bounds);
	
		/*
		* Tworzy tab items gora z prawej.
		*/
		folderItem.setBounds (list1Bounds.width + SASH_WIDTH, 0, clientArea.width - (list1Bounds.width + SASH_WIDTH), folderItemHeight);
	
		/*
		* Tworzy tab preview tak samo jak items tyle ze na dole
		*/
		folderPreview.setBounds (list1Bounds.width + SASH_WIDTH, 100 + SASH_WIDTH, clientArea.width - (list1Bounds.width + SASH_WIDTH), clientArea.height - (100 + SASH_WIDTH));
	
		/* Pozycjonowanie sashes */
		vSash.setBounds (list1Bounds.width, 0, SASH_WIDTH, clientArea.height);
		hSash.setBounds (list1Bounds.width, 100, clientArea.width - list1Bounds.width, SASH_WIDTH);
	}
	
	
}
