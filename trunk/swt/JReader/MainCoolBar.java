package swt.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainCoolBar {

	public MainCoolBar(final Shell shell, Display display) {
		
	//	Main CoolBar	
		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		
		//	Opcjonalne ikony do przyciskow na CoolBar (moze dodamy pozniej)
		
		final Image addIcon = new Image(display, "c:\\icons\\medium\\add.png");
        final Image syncIcon = new Image(display, "c:\\icons\\medium\\sync.png");
        final Image backIcon = new Image(display, "c:\\icons\\medium\\back.png");
        final Image forwardIcon = new Image(display, "c:\\icons\\medium\\forward.png");
        final Image searchIcon = new Image(display, "c:\\icons\\medium\\search.png");
        final Image optionsIcon = new Image(display, "c:\\icons\\medium\\options.png");
        final Image unreadIcon = new Image(display, "c:\\icons\\medium\\unread.png");
        
		
	//	Subskrypcje - pasek na coolbarze
        
		final CoolItem subscryptionCoolItem = new CoolItem(coolBar, SWT.NONE);
        //toolbar subskrypcje wrzucam do coolbar
        final ToolBar subscryptionToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        
        //Synchronizuj
        final ToolItem syncToolItem = new ToolItem(subscryptionToolBar, SWT.FLAT);
        syncToolItem.setImage(syncIcon);
        //syncToolItem.setText("Synch");
        syncToolItem.setToolTipText("Synchronize");
        
        //Dodaj subskrypcje 
        final ToolItem addSubToolItem = new ToolItem(subscryptionToolBar, SWT.FLAT);
        addSubToolItem.setImage(addIcon);
        //addSubToolItem.setText("Add");
        addSubToolItem.setToolTipText("Add Subscription");
        
        //nastepna nieprzeczytana wiadomsoc
        final ToolItem unreadToolItem = new ToolItem(subscryptionToolBar, SWT.PUSH);
        unreadToolItem.setImage(unreadIcon);
        //unreadToolItem.setText("Next msg");
        unreadToolItem.setToolTipText("Next unread message");
        
        subscryptionToolBar.pack(); 
        Point size = subscryptionToolBar.getSize();
        subscryptionCoolItem.setControl(subscryptionToolBar);
        subscryptionCoolItem.setSize(subscryptionCoolItem.computeSize(size.x, size.y));
        
        
   //	Back Forward NavigateBar - pasek na coolbarze
        
        final CoolItem navigateBarCoolItem = new CoolItem(coolBar, SWT.NONE);
        final ToolBar navigateToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        // Back
        final ToolItem backToolItem = new ToolItem(navigateToolBar, SWT.PUSH);
        backToolItem.setImage(backIcon);
        //backToolItem.setText("Back");
        backToolItem.setToolTipText("Back");
        
        // Forward
        final ToolItem forwardToolItem = new ToolItem(navigateToolBar, SWT.PUSH);
        forwardToolItem.setImage(forwardIcon);
        //forwardToolItem.setText("Forward");
        forwardToolItem.setToolTipText("Next");
        
        navigateToolBar.pack();
        size = navigateToolBar.getSize();
        navigateBarCoolItem.setControl(navigateToolBar);
        navigateBarCoolItem.setSize(navigateBarCoolItem.computeSize(size.x, size.y));
        
        
   //	ToolBar z pozostalymi toolsami     
        
        final CoolItem otherCoolItem = new CoolItem(coolBar, SWT.NONE);
        final ToolBar otherToolBar = new ToolBar(coolBar,SWT.FLAT);
        
        // Szukaj
        final ToolItem searchToolItem = new ToolItem(otherToolBar, SWT.PUSH);
        searchToolItem.setImage(searchIcon);
        //searchToolItem.setText("Search");
        searchToolItem.setToolTipText("Search a message");
        
        // Opcje
        final ToolItem optionsToolItem = new ToolItem(otherToolBar, SWT.PUSH);
        optionsToolItem.setImage(optionsIcon);
        //optionsToolItem.setText("Options");
        optionsToolItem.setToolTipText("Preferences");
        
        otherToolBar.pack();
        size = otherToolBar.getSize();
        otherCoolItem.setControl(otherToolBar);
        otherCoolItem.setSize(otherCoolItem.computeSize(size.x, size.y));
        
        
   //	CollBar listeners (do wypelnienia pozniej)
        
        //Add Subscryption
        addSubToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	AddSubscriptionShell addShell = new AddSubscriptionShell(shell);
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
        
        /*subscryptionToolBar.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              ToolItem item = subscryptionToolBar.getItem(new Point(e.x, e.y));
              String name = "";
              if (item != null) {
                name = item.getText();
              }
              if (!JReader.statusText.equals(name)) {
            	  JReader.statusLine.setText(name);
            	  JReader.statusText = name;
              }
            }
          });*/
        
	}
}
