package swt.JReader;

import java.text.DateFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ItemsTable {

	public static Table itemsTable;
	String[] titles = {"Title", "Date"};
	private int DATE_WIDTH = 200;

	public ItemsTable(final Composite comp) {
		Calendar cal = Calendar.getInstance();
	    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);
		
		Image unread = new Image(comp.getDisplay(), "c:\\icons\\unread.png");
		final Image read = new Image(comp.getDisplay(), "c:\\icons\\read.png");
		itemsTable = new Table(comp, SWT.SINGLE | SWT.FULL_SELECTION);
		itemsTable.setLinesVisible(true);
		itemsTable.setHeaderVisible(true);
		Color gray = new Color (comp.getDisplay(), 240, 250, 250);
		
		final TableColumn column1 = new TableColumn(itemsTable, SWT.NONE);
		column1.setText(titles[0]);
		final TableColumn column2 = new TableColumn(itemsTable, SWT.NONE);
		column2.setText(titles[1]);
		
		itemsTable.getColumn(1).setWidth(DATE_WIDTH);
		
		for (int i=0; i<=20; i++) {
			TableItem item = new TableItem(itemsTable, SWT.NONE);
			item.setImage(unread);
			item.setText(0, "Table Item " + i);
			item.setText(1, df.format(cal.getTime()));
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
	    for (int i=0; i<=20; i++) {
	    	itemsTable.getItem(i).setFont(newFont);
	    }
		
	    /*
	     * Listeners
	     */
	    /**
	     * Dopasowuje szerokość kolumn tabeli wzgędem wielkości okna.
	     */
	    comp.addControlListener(new ControlAdapter() {
	        public void controlResized(ControlEvent e) {
	          Rectangle area = comp.getClientArea();
	          Point preferredSize = itemsTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	          int width = area.width - 2 * itemsTable.getBorderWidth();
	          if (preferredSize.y > area.height + itemsTable.getHeaderHeight()) {
	        	  Point vBarSize = itemsTable.getVerticalBar().getSize();
	        	  width -= vBarSize.x;
	          }
	          Point oldSize = itemsTable.getSize();
	          if (oldSize.x > area.width) {
	            // zmniejszenie tabeli - zmniejszenie kolumn
	            column1.setWidth((width /3)*2);
	            column2.setWidth(width - column1.getWidth());
	            itemsTable.setSize(area.width, area.height);
	          } else {
	            // zwiekszenie tabeli - zwiekszenie kolumn
	            itemsTable.setSize(area.width, area.height);
	            column1.setWidth((width/3)*2);
	            column2.setWidth(width - column1.getWidth());
	          }
	        }
	      });
	
		/**
		 * Zaznacza wiersze jako przeczytane - zmienia czcionkę z BOLD na normal
		 */
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
              item[0].setImage(read);
            }
        });
		/**
		 * Podświetla kolorowym gradientem wybrany wiersz tabeli.
		 */
		itemsTable.addListener(SWT.EraseItem, new Listener() {
		      public void handleEvent(Event event) {
		        if ((event.detail & SWT.SELECTED) != 0) {
		          GC gc = event.gc;
		          Rectangle area = itemsTable.getClientArea();
		          int columnCount = itemsTable.getColumnCount();
		          if (event.index == columnCount - 1 || columnCount == 0) {
		            int width = area.x + area.width - event.x;
		            if (width > 0) {
		              Region region = new Region();
		              gc.getClipping(region);
		              region.add(event.x, event.y, width, event.height);
		              gc.setClipping(region);
		              region.dispose();
		            }
		          }
		          gc.setAdvanced(true);
		          if (gc.getAdvanced())
		            gc.setAlpha(95);
		          Rectangle rect = event.getBounds();
		          Color foreground = gc.getForeground();
		          Color background = gc.getBackground();
		          gc.setForeground(comp.getDisplay().getSystemColor(SWT.COLOR_RED));
		          gc.setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		          gc.fillGradientRectangle(0, rect.y, itemsTable.getClientArea().width, rect.height, false);
		          // przywracanie domyślynch kolorów
		          gc.setForeground(foreground);
		          gc.setBackground(background);
		          event.detail &= ~SWT.SELECTED;
		        }
		      }
		    });
		
		
	}
	
	
	
	
	
}
