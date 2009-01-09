package jreader.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Focus {

	final static Color bottom = new Color (GUI.display, 156, 156, 213);
	final static Color middle = new Color (GUI.display, 190, 190, 213);
	final static Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	final static Color midgray = new Color (GUI.display, 233, 231, 226);
	final static Color gray = new Color (GUI.display, 212, 208, 200);

	
	public static FocusListener setFocus(final CTabFolder control) {
		FocusListener listener = new FocusListener() {
	        public void focusGained(FocusEvent event) {
	        	control.setSelectionBackground(new Color[]{white, middle, bottom, bottom},
						new int[] {20, 40, 100}, true);	
	        }

	        public void focusLost(FocusEvent event) {
	        	control.setSelectionBackground(new Color[]{white, midgray, gray, gray},
						new int[] {20, 40, 100}, true);
	        }
	      };
	      return listener;
	}
}
