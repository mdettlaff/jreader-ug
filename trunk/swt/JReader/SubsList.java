package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;



public class SubsList {
	 
	
	private static final String[] ITEMS = { "bash.org.pl", "slashdot.org", "java2.com", "sdfkjs.com" };
	final Table subsList;
	
	 
	public SubsList(Composite comp) {
	    
		Image def = new Image(comp.getDisplay(), "c:\\icons\\default_icon.png");
		/*subsList = new List(comp, SWT.SINGLE | SWT.V_SCROLL);

	    for (int i = 0, n = ITEMS.length; i < n; i++) {
	    	subsList.add(ITEMS[i]);
	    }*/
		
	    subsList = new Table(comp, SWT.SINGLE);
	    for (int i=0; i<=5; i++) {
			TableItem item = new TableItem(subsList, SWT.NONE);
			item.setImage(def);
			item.setText("Subscription no. " + i);
			}
	    
	    
	    //Czcionka
	    Font initialFont = subsList.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setHeight(10);
	      fontData[i].setStyle(SWT.BOLD);
	    }
	    Font newFont = new Font(comp.getDisplay(), fontData);
	    subsList.setFont(newFont);
	
	}
}
