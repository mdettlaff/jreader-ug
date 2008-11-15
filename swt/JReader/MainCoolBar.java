package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainCoolBar {

	public MainCoolBar(final Shell shell) {
		
	//	Main CoolBar	
		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setLocation(0,0);
		
		//	Opcjonalne ikony do przyciskow na CoolBar (moze dodamy pozniej)
		/* 
		final Image addIcon = new Image(d, "c:\\icons\\add.jpg");
        final Image syncIcon = new Image(d, "c:\\icons\\sync.jpg");
        final Image backIcon = new Image(d, "c:\\icons\\back.ico");
        final Image forwardIcon = new Image(d, "c:\\icons\\forward.jpg");
        final Image searchIcon = new Image(d, "c:\\icons\\search.jpg");
        final Image optionsIcon = new Image(d, "c:\\icons\\options.jpg");
        */
		
	//	Subskrypcje - pasek na coolbarze
        
		final CoolItem subscryptionCoolItem = new CoolItem(coolBar, SWT.NONE);
        //toolbar subskrypcje wrzucam do coolbar
        final ToolBar subscryptionToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        //Dodaj subskrypcje 
        final ToolItem addSubToolItem = new ToolItem(subscryptionToolBar, SWT.FLAT);
        //addSubToolItem.setImage(openIcon);
        addSubToolItem.setText("Add");
        addSubToolItem.setToolTipText("Add Subscription");
        
        //Synchronizuj
        final ToolItem syncToolItem = new ToolItem(subscryptionToolBar, SWT.FLAT);
        //syncToolItem.setImage(syncIcon);
        syncToolItem.setText("Synch");
        syncToolItem.setToolTipText("Synchronize");
        
        subscryptionToolBar.pack(); 
        Point size = subscryptionToolBar.getSize();
        subscryptionCoolItem.setControl(subscryptionToolBar);
        subscryptionCoolItem.setSize(subscryptionCoolItem.computeSize(size.x, size.y));
        
        
   //	Back Forward NavigateBar - pasek na coolbarze
        
        final CoolItem navigateBarCoolItem = new CoolItem(coolBar, SWT.NONE);
        final ToolBar navigateToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        // Back
        final ToolItem backToolItem = new ToolItem(navigateToolBar, SWT.PUSH);
        //backToolItem.setImage(backIcon);
        backToolItem.setText("Back");
        backToolItem.setToolTipText("Back");
        
        // Forward
        final ToolItem forwardToolItem = new ToolItem(navigateToolBar, SWT.PUSH);
        //forwardToolItem.setImage(forwardIcon);
        forwardToolItem.setText("Forward");
        forwardToolItem.setToolTipText("Forward");
        
        navigateToolBar.pack();
        size = navigateToolBar.getSize();
        navigateBarCoolItem.setControl(navigateToolBar);
        navigateBarCoolItem.setSize(navigateBarCoolItem.computeSize(size.x, size.y));
        
        
   //	ToolBar z pozostalymi toolsami     
        final CoolItem otherCoolItem = new CoolItem(coolBar, SWT.NONE);
        final ToolBar otherToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        //nastepna nieprzeczytana wiadomsoc
        final ToolItem unreadToolItem = new ToolItem(otherToolBar, SWT.PUSH);
        //unreadToolItem.setImage(unreadIcon);
        unreadToolItem.setText("Next msg");
        unreadToolItem.setToolTipText("Next unread message");
        
        // Szukaj
        final ToolItem searchToolItem = new ToolItem(otherToolBar, SWT.PUSH);
        //searchToolItem.setImage(searchIcon);
        searchToolItem.setText("Search");
        searchToolItem.setToolTipText("Search a message");
        
        // Opcje
        final ToolItem optionsToolItem = new ToolItem(otherToolBar, SWT.PUSH);
        //optionsToolItem.setImage(optionsIcon);
        optionsToolItem.setText("Options");
        optionsToolItem.setToolTipText("Options & preferences");
        
        otherToolBar.pack();
        size = otherToolBar.getSize();
        Point p = shell.getSize();
        otherCoolItem.setControl(otherToolBar);
        otherCoolItem.setSize(otherCoolItem.computeSize(p.x, size.y));
        
        
   //	CollBar listeners (do wypelnienia pozniej)
        
        //Add Subscryption
        addSubToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("Add Subscryption");
                
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
        
	}
}
