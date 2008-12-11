package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class TagList {
	private static final String[] ITEMS = { "< all >", "komputery", "java", "sdfds" };
	final List tagList;
	 
	public TagList(Composite comp) {
	    
		tagList = new List(comp, SWT.SINGLE | SWT.V_SCROLL);

	    for (int i = 0, n = ITEMS.length; i < n; i++) {
	      tagList.add(ITEMS[i]);
	    }
	 }
}

