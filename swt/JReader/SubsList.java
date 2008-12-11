package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;



public class SubsList {
	 
	
	private static final String[] ITEMS = { "bash.org.pl", "slashdot.org", "java2.com", "sdfkjs.com" };
	final List subsList;
	
	 
	public SubsList(Composite comp) {
	    
		subsList = new List(comp, SWT.SINGLE | SWT.V_SCROLL);

	    for (int i = 0, n = ITEMS.length; i < n; i++) {
	      subsList.add(ITEMS[i]);
	    }
	    
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
