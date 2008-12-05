package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainToolBar {
	
	//static String statusText = "";
	final ToolBar toolBar;

	public MainToolBar(final Shell shell) {
		
		Display display = shell.getDisplay();
		
	//	MainToolBar	
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.WRAP);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
	//	Opcjonalne ikony do przyciskow na CoolBar (moze dodamy pozniej)
		
		final Image addIcon = new Image(display, "c:\\icons\\medium\\add.png");
        final Image syncIcon = new Image(display, "c:\\icons\\medium\\sync.png");
        final Image backIcon = new Image(display, "c:\\icons\\medium\\back.png");
        final Image forwardIcon = new Image(display, "c:\\icons\\medium\\forward.png");
        final Image searchIcon = new Image(display, "c:\\icons\\medium\\search.png");
        final Image optionsIcon = new Image(display, "c:\\icons\\medium\\options.png");
        final Image unreadIcon = new Image(display, "c:\\icons\\medium\\unread.png");
        
      //Synchronizuj
        final ToolItem syncToolItem = new ToolItem(toolBar, SWT.FLAT);
        syncToolItem.setImage(syncIcon);
        //syncToolItem.setText("Synch");
        syncToolItem.setToolTipText("Synchronize");
        
      //Dodaj subskrypcje 
        final ToolItem addSubToolItem = new ToolItem(toolBar, SWT.FLAT);
        addSubToolItem.setImage(addIcon);
        //addSubToolItem.setText("Add");
        addSubToolItem.setToolTipText("Add Subscription");
        
      //nastepna nieprzeczytana wiadomsoc
        final ToolItem unreadToolItem = new ToolItem(toolBar, SWT.PUSH);
        unreadToolItem.setImage(unreadIcon);
        //unreadToolItem.setText("Next msg");
        unreadToolItem.setToolTipText("Next unread message");
       
        //separator 1
        new ToolItem(toolBar, SWT.SEPARATOR);
        
      //Back
        final ToolItem backToolItem = new ToolItem(toolBar, SWT.PUSH);
        backToolItem.setImage(backIcon);
        //backToolItem.setText("Back");
        backToolItem.setToolTipText("Back");
        
      //Forward
        final ToolItem forwardToolItem = new ToolItem(toolBar, SWT.PUSH);
        forwardToolItem.setImage(forwardIcon);
        //forwardToolItem.setText("Forward");
        forwardToolItem.setToolTipText("Next");
        
        //separator 2
        new ToolItem(toolBar, SWT.SEPARATOR);
        
      //Szukaj
        final ToolItem searchToolItem = new ToolItem(toolBar, SWT.PUSH);
        searchToolItem.setImage(searchIcon);
        //searchToolItem.setText("Search");
        searchToolItem.setToolTipText("Search a message");
        
      //Opcje
        final ToolItem optionsToolItem = new ToolItem(toolBar, SWT.PUSH);
        optionsToolItem.setImage(optionsIcon);
        //optionsToolItem.setText("Options");
        optionsToolItem.setToolTipText("Preferences");
        
        
        //final Label statusLine = new Label(shell, SWT.NONE);
        
    //	CollBar listeners (do wypelnienia pozniej)
        
        //Add Subscryption
        addSubToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	AddSubscryptionShell addShell = new AddSubscryptionShell(shell);
		   		addShell.open();
                }
        });
        //synchornizuj
        syncToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Synchronize");
                
            }
        });
        //Back
        backToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Back");
                
            }
        });
        //Forward
        forwardToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Forward");
                
            }
        });
        //Next unread message
        unreadToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Next unread message");
                
            }
        });
        //Search
        searchToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Search message");
                
            }
        });
        //Options
        optionsToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	OptionsShell optionsShell = new OptionsShell(shell);
		   		optionsShell.open();
            }
        });   
        
        
        toolBar.pack();
        
	}

	
}
