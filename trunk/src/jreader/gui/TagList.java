package jreader.gui;

import jreader.JReader;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

public class TagList {
	static Table tagList;
	 
	public TagList(Composite comp) {
	    
		tagList = new Table(comp, SWT.SINGLE | SWT.FULL_SELECTION);
		refresh();
		
		tagList.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			int indeks = tagList.getSelectionIndex();
			if (indeks == 0)
				JReader.selectTag("all");
			else if (indeks == 1)
				JReader.selectTag("");
			else {
				JReader.selectTag(tagList.getItem(indeks).getText());
			}
			SubsList.refresh();
        }
		});
		
		
	}


	
	
	public static void refresh() {
		tagList.removeAll();
		TableItem all = new TableItem(tagList, SWT.NONE);
		all.setText("<All channels>");
		TableItem unreadCh = new TableItem(tagList, SWT.NONE);
		unreadCh.setText("<Untagged channels>");
	    for (String tag : JReader.getTags()) {
	    	TableItem item = new TableItem(tagList, SWT.NONE);
	    	item.setText(tag);
	    }
	}
}

