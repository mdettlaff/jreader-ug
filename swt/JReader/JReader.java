package swt.JReader;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class JReader {

	public static void main(String[] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setSize (650, 600);
		shell.setText("JReader 0.0.1");
		shell.setLayout(new GridLayout(1, false));
		
		Label separator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
	    separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
	//	MenuBar
		new MenuBar(shell);
		
	//	SWT CoolBar
		new MainCoolBar(shell);
		
		
		
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}