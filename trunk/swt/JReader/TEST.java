package swt.JReader;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class TEST {
	public static void main (String [] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText ("Parent Shell");
		shell.addMouseListener (new MouseAdapter() {
			public void mouseDown (MouseEvent e) {
				Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				Point pt = display.getCursorLocation ();
				dialog.setLocation (pt.x, pt.y);
				dialog.setText ("Dialog Shell");
				dialog.setSize (100, 100);
				dialog.open (); 
			}});
		shell.setSize (400, 400);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}

