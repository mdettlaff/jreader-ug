package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddSubscryptionShell {

	private static Shell addShell;
	
	public AddSubscryptionShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "c:\\icons\\small\\add.png");
		addShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		addShell.setText("Add a subscryption");
		//aboutShell.setSize(200, 150);
		addShell.setImage(jreader);
		addShell.setLayout(new GridLayout(2, true));
		
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 50;
	    addShell.setLocation(x, y);
	    
	    
	    new Label(addShell, SWT.NONE).setText("Give the URL address: ");
	    Text url = new Text(addShell, SWT.BORDER);
	    url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    url.setTextLimit(100);
	    url.setText("http://");
	    
	    new Label(addShell, SWT.NONE).setText("Name the tags (optional): ");
	    Text tag = new Text(addShell, SWT.BORDER);
	    tag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    tag.setTextLimit(100);
	    
	    Button okB = new Button(addShell, SWT.PUSH);
	    okB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    okB.setText("OK");
	    Button caB = new Button(addShell, SWT.PUSH);
	    caB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	    caB.setText("Cancel");
	    
	    
	    addShell.pack();
	}

	public void open() {
		addShell.open();
	}
}
