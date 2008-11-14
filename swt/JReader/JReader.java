package swt.JReader;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class JReader {

	private static Control createToolBar(Composite composite, final Shell shell){
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT);
		ToolItem item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Adda subscription");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Back");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Forward");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Synchronize");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Next unread message");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Search");
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setText("Preferences");
		item.addListener(SWT.Selection, new Listener() {
		  	public void handleEvent(Event E) {
		   		OptionsShell optionsShell = new OptionsShell(shell);
		   		optionsShell.open();
		   	}
		});
		return toolBar;
	}

	public static void main(String[] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setSize (650, 600);
		shell.setText("JReader 0.0.1");
		shell.setLayout(new GridLayout(1, false));
		Label separator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
	    separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		/*
		 * SWT BAR MENU
		 */
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		
				
		//Bar menu items (file, edit, etc..)
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("File");
		MenuItem infoItem = new MenuItem (bar, SWT.CASCADE);
		infoItem.setText("Info");
		
		//BarMenu's items's manus
		Menu fileMenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (fileMenu);
		Menu infoMenu = new Menu (shell, SWT.DROP_DOWN);
		infoItem.setMenu(infoMenu);
		
		
		//FileMenu Items
		//item one - import subscription
		MenuItem itemImport = new MenuItem (fileMenu, SWT.PUSH);
		itemImport.setText ("Import a Subscription List");
		//item two
		MenuItem itemExport = new MenuItem (fileMenu, SWT.PUSH);
		itemExport.setText("Export a Subscription List");
		//item three = close
		MenuItem itemClose = new MenuItem (fileMenu, SWT.PUSH);
		itemClose.setText("Close");
		itemClose.addListener (SWT.Selection, new Listener() {
			public void handleEvent (Event e) {
				shell.dispose();
			}
		});
		
		//infoMenu Items
		//item one
		MenuItem itemHelp = new MenuItem (infoMenu, SWT.PUSH);
		itemHelp.setText("Help");
		//item two
		MenuItem itemAbout = new MenuItem (infoMenu, SWT.PUSH);
		itemAbout.setText("About the Program");
		
		
		/*
		 * SWT CoolBar
		 */
		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
	    coolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
	    //cool item
	    final CoolItem item = new CoolItem(coolBar, SWT.DROP_DOWN);
	    item.setControl(createToolBar(coolBar, shell));
	    
	    Control control = item.getControl();
	    Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    pt =item.computeSize(pt.x, pt.y);
	    item.setSize(pt);
		coolBar.pack();
		
		
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}