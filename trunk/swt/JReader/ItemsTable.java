package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ItemsTable {

	public static Table itemsTable;
	String[] titles = {"Title", "Date"};
	private int DATE_WIDTH = 120;

	public ItemsTable(final Composite comp) {
		Image jreader = new Image(comp.getDisplay(), "c:\\icons\\small\\jreader2.png");
		itemsTable = new Table(comp, SWT.SINGLE | SWT.BORDER);
		itemsTable.setLinesVisible(true);
		itemsTable.setHeaderVisible(true);
		Color gray = new Color (comp.getDisplay(), 240, 250, 250);
		
		TableColumn column1 = new TableColumn(itemsTable, SWT.NONE);
		column1.setText(titles[0]);
		TableColumn column2 = new TableColumn(itemsTable, SWT.NONE);
		column2.setText(titles[1]);
		
		itemsTable.getColumn(1).setWidth(DATE_WIDTH);
		
		for (int i=0; i<=20; i++) {
			TableItem item = new TableItem(itemsTable, SWT.NONE);
			item.setImage(jreader);
			item.setText(0, "Table Item " + i);
			item.setText(1, "Table Item " + i);
			if (i%2==0) {
				item.setBackground(gray);
			}
		}
		
		
		Font initialFont = itemsTable.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setStyle(SWT.BOLD);
	    }
	    Font newFont = new Font(comp.getDisplay(), fontData);
	    itemsTable.setFont(newFont);
		
	    /*
	     * Listeners
	     */
	    
	    /**
	     * Dopasowuje szerokość kolumn tabeli wzgędem wielkości okna.
	     */
		comp.getShell().addControlListener (new ControlAdapter () {
			public void controlResized (ControlEvent event) {
				itemsTable.getColumn(0).setWidth(comp.getShell().getBounds().width-(comp.getShell().getBounds().width/5+DATE_WIDTH+43));
			}
		});
		
		itemsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
             
				TableItem[] item = itemsTable.getSelection();
             
              Font initialFont = item[0].getFont();
              FontData[] fontData = initialFont.getFontData();
              
              for (int i = 0; i < fontData.length; i++) {            	  
      	    		fontData[i].setStyle(SWT.NORMAL);
              }
              
              Font newFont = new Font(comp.getDisplay(), fontData);
              item[0].setFont(newFont);
            }
        });
		
	}
	
	
	
	
	
}
