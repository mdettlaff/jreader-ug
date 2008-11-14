package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OptionsShell {

	private static Shell optionsShell;
	
	public OptionsShell(Shell shell) {
	optionsShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	optionsShell.setLocation(200, 200);
	optionsShell.setText("Preferences");
	optionsShell.setLayout(new GridLayout(3, false));
	
	
	//Checkbox Grid[1,1]
	Button startupSync = new Button(optionsShell, SWT.CHECK);
	startupSync.setSelection(true);
	startupSync.setText("Synchronize automaticaly when the program starts.");
	
	//nadpisanie dwoch pol pustym label Grid[2:,1]
	Label tmplabel1 = new Label(optionsShell, SWT.NONE);
	Label tmplabel2 = new Label(optionsShell, SWT.NONE);
	
	//Synchronizjacja co ile minut
	Label sync = new Label(optionsShell, SWT.NONE);
	sync.setText("Synchronize every: ");
	Text autoSync = new Text(optionsShell, SWT.BORDER);
	autoSync.setTextLimit(3);
	autoSync.setText("15");
	new Label(optionsShell, SWT.LEFT).setText("minutes.");
	
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
	
	
	optionsShell.pack();
	}
	
	public void open() {
		optionsShell.open();
	}
	
}
