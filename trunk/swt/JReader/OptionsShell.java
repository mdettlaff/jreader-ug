package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OptionsShell {

	private static Shell optionsShell;
	
	public OptionsShell(Shell shell) {
	optionsShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	optionsShell.setLocation(250, 300);
	optionsShell.setText("Preferences");
	optionsShell.setLayout(new GridLayout(1, false));
	
	
	//Checkbox Grid[1,1]
	final Button startupSync = new Button(optionsShell, SWT.CHECK);
	startupSync.setSelection(true);
	startupSync.setText("Synchronize automaticaly when the program starts.");
	
		
	//Synchronizjacja co ile minut
	Composite comp1 = new Composite(optionsShell, SWT.NONE);
	comp1.setLayout(new FillLayout(SWT.HORIZONTAL));
	new Label(comp1, SWT.NONE).setText("Symchronize every ");
	final Text autoSync = new Text(comp1, SWT.NONE);
	autoSync.setTextLimit(3);
	autoSync.setText("15");
	new Label(comp1, SWT.NONE).setText(" minutes.");
	
	
	//SortBy
	Group group2 = new Group(optionsShell, SWT.SHADOW_NONE);
	group2.setLayout(new RowLayout(SWT.VERTICAL));
	group2.setText("Sorted by:");
	
	//Radio
	Button newest = new Button(group2, SWT.RADIO);
	newest.setSelection(true);
	newest.setText("newest");
	Button latest = new Button(group2, SWT.RADIO);
	latest.setText("latest");
	
	
	Composite comp2 = new Composite(optionsShell, SWT.NONE);
	comp2.setLayout(new FillLayout(SWT.HORIZONTAL));
	new Label(comp2, SWT.NONE).setText("Remove messages older then ");
	Text remove = new Text(comp2, SWT.NONE);
	remove.setText("10");
	remove.setTextLimit(2);
	new Label(comp2, SWT.NONE).setText(" days.");
	
	
	//Action Listeners (do wypelnienia pozniej)
	startupSync.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
        	if (!startupSync.getSelection()) {
        		autoSync.setEditable(false);
        	} else {
        		autoSync.setEditable(true);
        	}
            
        }
    });
	
	optionsShell.pack();
	}
	
	public void open() {
		optionsShell.open();
	}
	
}
