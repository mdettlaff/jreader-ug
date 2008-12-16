package jreader.gui;

import jreader.Channel;
import jreader.Item;
import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;



public class SubsList {
	 
	
	public static Table subsList;
	public static Color gray = new Color (Subscriptions.subComposite.getDisplay(), 240, 250, 250);
	public static Image def = new Image(Subscriptions.subComposite.getDisplay(), "c:\\icons\\unread.png");
	static Font fontBold;
	 
	public SubsList(final Composite comp) {
		
	    subsList = new Table(comp, SWT.SINGLE);
	   
	  //Czcionka
	    Font initialFont = subsList.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setHeight(10);
	      fontData[i].setStyle(SWT.BOLD);
	    }
	    fontBold = new Font(comp.getDisplay(), fontData);
	    
	    for (Channel ch : JReader.getVisibleChannels()) {
	    	TableItem item = new TableItem(subsList, SWT.NONE);
	    	if (ch.getIconPath() == null)
	    		if (ch.getUnreadItemsCount() == 0)
	    			item.setImage(ItemsTable.read);
	    		else
	    			item.setImage(def);
	    	else  	item.setImage(new Image(comp.getDisplay(), ch.getIconPath()));
	    	item.setText(ch.getTitle() + " (" + ch.getUnreadItemsCount() + ")");
	    	if (ch.getUnreadItemsCount() != 0)
	    		item.setFont(fontBold);
	    	
	    }   
	    
	    subsList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int indeks = subsList.getSelectionIndex();
				System.out.println(indeks);
				JReader.selectChannel(indeks);
				ItemsTable.refresh();
            }
        });
	
	}
	
	/**
	 * Odświeża listę subskrypcji
	 */
	public static void refresh() {
		subsList.removeAll();
		for (Channel ch : JReader.getVisibleChannels()) {
	    	TableItem subs = new TableItem(SubsList.subsList, SWT.NONE);
	    	if (ch.getIconPath() == null)
	    		if (ch.getUnreadItemsCount() == 0)
	    			subs.setImage(ItemsTable.read);
	    		else
	    			subs.setImage(def);
	    	else {
	    		subs.setImage(new Image(Subscriptions.subComposite.getDisplay(), ch.getIconPath()));
	    	}
	    	subs.setText(ch.getTitle() + " (" + ch.getUnreadItemsCount() + ")");
	    	if (ch.getUnreadItemsCount() != 0)
	    		subs.setFont(fontBold);	    	
	    }
	}
	
	
	
}
