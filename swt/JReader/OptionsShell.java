package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OptionsShell {

	private static Shell optionsShell;
	
	public OptionsShell(Shell shell) {
		final Image jreader = new Image(shell.getDisplay(), "c:\\icons\\small\\jreader.png");
		optionsShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		optionsShell.setText("Preferences");
		optionsShell.setLayout(new GridLayout(1, false));
		optionsShell.setImage(jreader);
		//Wysrodkowanie shella
		Rectangle bounds = shell.getBounds();
	    int x = bounds.width - 200;
	    int y = bounds.height - 150;
	    optionsShell.setLocation(x, y);
	
	
	//	Checkbox
		final Button startupSync = new Button(optionsShell, SWT.CHECK);
		startupSync.setSelection(true);
		startupSync.setText("Synchronize automaticaly when the program starts.");
	
		
	//	Label Synchronizjacja co ile minut
		Composite comp1 = new Composite(optionsShell, SWT.NONE);
		comp1.setLayout(new FillLayout(SWT.HORIZONTAL));
		new Label(comp1, SWT.NONE).setText("Symchronize every ");
		final Text autoSync = new Text(comp1, SWT.NONE);
		autoSync.setTextLimit(3);
		autoSync.setText("15");
		new Label(comp1, SWT.NONE).setText(" minutes.");
	
	
	//	Group SortBy
		Group group2 = new Group(optionsShell, SWT.SHADOW_NONE);
		group2.setLayout(new RowLayout(SWT.VERTICAL));
		group2.setText("Sorted by:");
	
		//Radio
		Button newest = new Button(group2, SWT.RADIO);
		newest.setSelection(true);
		newest.setText("newest");
		Button latest = new Button(group2, SWT.RADIO);
		latest.setText("oldest");
	
	//	Label - remove by 
		Composite comp2 = new Composite(optionsShell, SWT.NONE);
		comp2.setLayout(new FillLayout(SWT.HORIZONTAL));
		new Label(comp2, SWT.NONE).setText("Remove messages older then ");
		Text remove = new Text(comp2, SWT.NONE);
		remove.setText("10");
		remove.setTextLimit(2);
		new Label(comp2, SWT.NONE).setText(" days.");
		
	//	Buttons ok cancel
		Composite comp3 = new Composite(optionsShell, SWT.NONE);
		comp3.setLayout(new RowLayout());
		
		Button okButton = new Button(comp3, SWT.PUSH);
	    okButton.setText("&OK");
	    Button cancelButton = new Button(comp3, SWT.PUSH);
	    cancelButton.setText("&Cancel");
	    okButton.setFocus();
	
	//	Action Listeners (do wypelnienia pozniej)
	    //wsynchronizacja pszy starcie -checkbox
		startupSync.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!startupSync.getSelection()) {
					autoSync.setEditable(false);
				} else {
					autoSync.setEditable(true);
				}
            
			}
		});
		
		// ok button - powinien zebrac wszystkie wartosc z widgetow i wyslac je do metod
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Ok - get data to the method");           
			}
		});
		
		// cancel button
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Cancel was pressed.\nDisposing...");
				optionsShell.close();
			}
		});
		optionsShell.pack();
	}
	
	public void open() {
		optionsShell.open();
	}
	
}
