package jreader.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
/**
 * Główny kompozyt zawierający Previwe, Items, Filters, Tags.
 *  
 * @author Karol
 *
 */
public class MainSash {

	Composite sashComp;
	Sash vSash;
	Sash hSash;
	Sash smallSash;
	int SASH_LIMIT = 27;	//ogranicza z gory i z dolu
	int SASH_WIDTH = 3;
	int SMALL_SASH_LIMIT = 108; //ogranicza z gory
	int folderFilterHeight = 80;
	int folderItemHeight = 200;
	static Filters folderFilter;
	static Items folderItem;
	static Preview folderPreview;
	static Subscriptions folderSubs;
	static Tags folderTag;
	
	public MainSash(Composite shell) {
		
		sashComp = new Composite(shell, SWT.NONE);
		sashComp.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		
		/* Tworze nowe taby */
		folderFilter = new Filters(sashComp); 
		folderItem = new Items(sashComp);
		folderPreview = new Preview(sashComp);
		folderSubs = new Subscriptions(sashComp);
		folderTag = new Tags(sashComp);
		
		/* Trzy sashes */
		vSash = new Sash (sashComp, SWT.VERTICAL | SWT.SMOOTH);
		hSash = new Sash (sashComp, SWT.HORIZONTAL | SWT.SMOOTH);
		smallSash = new Sash(sashComp, SWT.HORIZONTAL | SWT.SMOOTH);
		
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
				//math.max ((event.x, OGRANICZENIE_Z_LEWEJ), rect.width - OGRANICZENIE_Z_PRAWEJ)
				event.x = Math.min (Math.max (event.x, SMALL_SASH_LIMIT), rect.width - SMALL_SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					vSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		smallSash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.y = Math.min (Math.max (event.y, SMALL_SASH_LIMIT), rect.height - SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					smallSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		sashComp.addControlListener (new ControlAdapter () {
			public void controlResized (ControlEvent event) {
				resized ();
			}
		});
		/*//'wyzerowanie' statusline
		sashComp.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              GUI.statusText = "";
              GUI.statusLine.setText(GUI.statusText);
            }
          });*/
	}
		
	void layout () {
		/*
		 * Tu sie dzieje taka masakra ze nawet nie chce mi sie tego komentowac...
		 * Jak masz odwage to sam to rozkmin.
		 */
		
		Rectangle clientArea = sashComp.getClientArea ();
		Rectangle hSashBounds = hSash.getBounds ();
		Rectangle vSashBounds = vSash.getBounds ();
		Rectangle smallSashBounds = smallSash.getBounds();
		
		folderFilter.setBounds (0, 0, vSashBounds.x, folderFilterHeight);
		folderItem.setBounds (vSashBounds.x + vSashBounds.width, 0, clientArea.width - (vSashBounds.x + vSashBounds.width), hSashBounds.y);
		folderPreview.setBounds (vSashBounds.x + vSashBounds.width, hSashBounds.y + hSashBounds.height, clientArea.width - (vSashBounds.x + vSashBounds.width), clientArea.height - (hSashBounds.y + hSashBounds.height));
		folderSubs.setBounds (0, folderFilterHeight + SASH_WIDTH, vSashBounds.x, smallSashBounds.y - folderFilterHeight - smallSashBounds.height);
		folderTag.setBounds(0, smallSashBounds.y + smallSashBounds.height, vSashBounds.x, clientArea.height - smallSashBounds.y - smallSashBounds.height);
		
		hSashBounds.width = clientArea.width - vSashBounds.x;
		hSashBounds.x = vSashBounds.x;
		smallSashBounds.width = vSashBounds.x;
		
		hSash.setBounds(hSashBounds);
		smallSash.setBounds(smallSashBounds);
	}
	
	void resized () {
		
		Rectangle clientArea = sashComp.getClientArea ();
		
		/*
		* Tworzy bounds dla tabu 'filters'. Umieszcza go po lewej stronie.
		* Wysoko�� z g�ry ustalona.
		* (x, y, width, height)
		*/
		Rectangle list1Bounds = new Rectangle (0, 0, (clientArea.width - SASH_WIDTH) / 5, folderFilterHeight);
		folderFilter.setBounds (list1Bounds);
		/*
		* Tworzy tab Items gora z prawej.
		*/
		folderItem.setBounds (list1Bounds.width + SASH_WIDTH, 0, clientArea.width - (list1Bounds.width + SASH_WIDTH), folderItemHeight);
		/*
		* Tworzy tab Preview tak samo jak items tyle ze na dole
		*/
		folderPreview.setBounds (list1Bounds.width + SASH_WIDTH, folderItemHeight + SASH_WIDTH, clientArea.width - (list1Bounds.width + SASH_WIDTH), clientArea.height - (folderItemHeight + SASH_WIDTH));
		/*
		 * Tworzy tab Subscryptions z lewej
		 */
		folderSubs.setBounds(0, folderFilterHeight + SASH_WIDTH, (clientArea.width - SASH_WIDTH) / 5, ((clientArea.height - folderFilterHeight) /3) *2 );
		list1Bounds = folderSubs.getBounds();
		/*
		 * Tworzy tab Tags z lewej w dolnym rogu
		 */
		folderTag.setBounds(0, list1Bounds.height + folderFilterHeight + 2*SASH_WIDTH, (clientArea.width - SASH_WIDTH) / 5, clientArea.height - folderFilterHeight - list1Bounds.height - 2*SASH_WIDTH);
		
		/* Pozycjonowanie sashes */
		vSash.setBounds (list1Bounds.width, 0, SASH_WIDTH, clientArea.height);
		hSash.setBounds (list1Bounds.width, folderItemHeight, clientArea.width - list1Bounds.width, SASH_WIDTH);
		smallSash.setBounds(0, list1Bounds.height + folderFilterHeight + SASH_WIDTH, (clientArea.width - SASH_WIDTH) / 5, SASH_WIDTH);
	}
	
	
}
