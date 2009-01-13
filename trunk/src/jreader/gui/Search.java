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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Search {

private static Shell searchShell;
	
	public Search() {
		final Image jreader = new Image(GUI.shell.getDisplay(), "data" + File.separator + "icons" + File.separator + "small" + File.separator + "search.png");
		searchShell = new Shell(GUI.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		searchShell.setText("Search");
		searchShell.setLayout(new GridLayout());
		searchShell.setImage(jreader);
		
		Monitor primary = GUI.shell.getDisplay().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    int x = bounds.width / 2 - 100;
	    int y = bounds.height/ 2 - 100;
	    searchShell.setLocation(x, y);
	
	    
	    Composite comp0 = new Composite(searchShell, SWT.NONE);
	    comp0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    comp0.setLayout(new GridLayout(2, false));
	    
	    new Label(comp0, SWT.NONE).setText("Find: ");
	    final Text phraze = new Text(comp0, SWT.BORDER);
	    phraze.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    phraze.setFocus();
	    
	    Composite comp = new Composite(searchShell, SWT.NONE);
	    comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    comp.setLayout(new GridLayout(2, true));
	
	//	Group Scope
		Group scope = new Group(comp, SWT.SHADOW_NONE);
		scope.setLayout(new RowLayout(SWT.VERTICAL));
		scope.setText("Scope:");
	    
		final Button radio0 = new Button(scope, SWT.RADIO);
		radio0.setText("Items list");
		radio0.setSelection(true);
		final Button radio1 = new Button(scope, SWT.RADIO);
		radio1.setText("Subscriptions list");
		final Button radio2 = new Button(scope, SWT.RADIO);
		radio2.setText("All");
		
	//	Group Options
		Group options = new Group(comp, SWT.SHADOW_NONE);
		options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		options.setLayout(new RowLayout(SWT.VERTICAL));
		options.setText("Options");	
		
		final Button onlyTitles = new Button(options, SWT.CHECK);
		onlyTitles.setText("Titles only");
		final Button caseSens = new Button(options, SWT.CHECK);
		caseSens.setText("Case sensitive");
		
	//	Buttons ok cancel		
		Button okButton = new Button(comp, SWT.PUSH);
		GridData gData1 = new GridData(SWT.LEFT, SWT.CENTER, false, false);
	    gData1.horizontalIndent = 55;
	    gData1.verticalIndent = 20;
		okButton.setLayoutData(gData1);
	    okButton.setText("   &OK   ");
	    
	    Button cancelButton = new Button(comp, SWT.PUSH);
	    GridData gData2 = new GridData(SWT.LEFT, SWT.CENTER, false, false);
	    gData2.horizontalIndent = 15;
	    gData2.verticalIndent = 20;
	    cancelButton.setLayoutData(gData2);
	    cancelButton.setText(" &Cancel ");
	
	//	Action Listeners (do wypelnienia pozniej)
	    
	    Listener phrazeListener = new Listener() {
			public void handleEvent(Event event) {
				int sc = -1;
				if (radio0.getSelection())
					sc = 0;
				if (radio1.getSelection())
					sc = 1;
				if (radio2.getSelection())
					sc = 2;
				
				if (JReader.search(caseSens.getSelection(), onlyTitles.getSelection(), sc, phraze.getText())) {
					ItemsTable.refresh();
				}
				else {
					final MessageBox messageBox = new MessageBox(GUI.shell, SWT.ICON_WARNING);
					messageBox.setText("Warning");
					messageBox.setMessage("Phrase not found!");
					messageBox.open();
				}
			}
		};
	    
		// ok button - powinien zebrac wszystkie wartosc z widgetow i wyslac je do metod
		okButton.addListener(SWT.Selection, phrazeListener);
		
		//phraze domyślny listener
		phraze.addListener(SWT.DefaultSelection, phrazeListener);
		
		// cancel button
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				searchShell.close();
			}
		});
		searchShell.pack();
	}
	
	public void open() {
		searchShell.open();
	}
	
}

