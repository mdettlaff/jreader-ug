package jreader.gui;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class TagList {
	final List tagList;
	 
	public TagList(Composite comp) {
	    
		tagList = new List(comp, SWT.SINGLE | SWT.V_SCROLL);
		tagList.add("<All channels>");
		tagList.add("<Unread channels>");
		
	    for (String tag : JReader.getTags()) {
	      tagList.add(tag);
	    }
	 }
}

