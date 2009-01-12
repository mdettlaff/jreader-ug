package jreader.gui;

import java.io.File;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
		final Image jreader = new Image(shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "small" + File.separator + "options.png");
		optionsShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		optionsShell.setText("Preferences");
		optionsShell.setLayout(new GridLayout(1, false));
		optionsShell.setImage(jreader);
		//Wysrodkowanie shella
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 50;
	    optionsShell.setLocation(x, y);
	
	
	//	Checkbox
		final Button startupSync = new Button(optionsShell, SWT.CHECK);
		startupSync.setSelection(JReader.getConfig().getUpdateAllOnStartup());
		startupSync.setText("Synchronize automatically when the program starts.");
	
		
	//	Label Synchronizjacja co ile minut
		Composite comp1 = new Composite(optionsShell, SWT.NONE);
		comp1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp1.setLayout(new GridLayout(3, false));
		Label lab1 = new Label(comp1, SWT.NONE);
		lab1.setText("Synchronize every ");
		lab1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		final Text autoSync = new Text(comp1, SWT.BORDER);
		autoSync.setTextLimit(3);
		autoSync.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		autoSync.setText((new Integer(JReader.getConfig().getAutoUpdateMinutes())).toString());
		Label lab2 = new Label(comp1, SWT.NONE);
		lab2.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
		lab2.setText(" minutes (0 - never).");
	
	
	//	Group SortBy
		Group group2 = new Group(optionsShell, SWT.SHADOW_NONE);
		group2.setLayout(new RowLayout(SWT.VERTICAL));
		group2.setText("Sort by:");
	
		//Radio
		final Button newest = new Button(group2, SWT.RADIO);
		newest.setText("newest");
		Button latest = new Button(group2, SWT.RADIO);
		latest.setText("oldest");
		if (JReader.getConfig().getSortByNewest())
			newest.setSelection(true);
		else
			latest.setSelection(true);
				
	
	//	Label - remove by 
		Composite comp2 = new Composite(optionsShell, SWT.NONE);
		comp2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp2.setLayout(new GridLayout(3, false));
		Label lab3 = new Label(comp2, SWT.NONE);
		lab3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		lab3.setText("Remove items older than ");
		final Text remove = new Text(comp2, SWT.BORDER);
		remove.setText(new Integer(JReader.getConfig().getDeleteOlderThanDays()).toString());
		remove.setTextLimit(2);
		remove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label lab4 = new Label(comp2, SWT.NONE);
		lab4.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
		lab4.setText(" days (0 - never).");
		
	//	Buttons ok cancel
		Composite comp3 = new Composite(optionsShell, SWT.NONE);
		comp3.setLayout(new GridLayout(4, true));
		
		Button okButton = new Button(comp3, SWT.PUSH);
		okButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
	    okButton.setText("&OK");
	    Button cancelButton = new Button(comp3, SWT.PUSH);
	    cancelButton.setText("&Cancel");
	    okButton.setFocus();
	
	//	Action Listeners (do wypelnienia pozniej)
	    		
		// ok button - powinien zebrac wszystkie wartosc z widgetow i wyslac je do metod
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				JReader.getConfig().setUpdateAllOnStartup(startupSync.getSelection());
				JReader.getConfig().setAutoUpdateMinutes(Integer.parseInt(autoSync.getText()));
				JReader.getConfig().setSortByNewest(newest.getSelection());
				JReader.getConfig().setDeleteOlderThanDays(Integer.parseInt(remove.getText()));
				if (!JReader.getConfig().write()) {
					System.out.println("Write error");
				}
				optionsShell.dispose();
			}
		});
		
		// cancel button
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				optionsShell.close();
			}
		});
		optionsShell.pack();
	}
	
	public void open() {
		optionsShell.open();
	}
	
}
