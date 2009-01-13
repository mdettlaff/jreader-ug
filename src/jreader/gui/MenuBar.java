package jreader.gui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;
/**
 * Tworzy menu programu z elementami: File, Edit, Window, Help
 */
public class MenuBar {

	Text textWidget;
	
	public MenuBar(final Shell shell) {
		
		//tworze pasek menu
		Menu menu = new Menu(shell,SWT.BAR);
		
	 // File wrzusam do menu
		final MenuItem file = new MenuItem(menu, SWT.CASCADE);
        file.setText("&File");
        //Menu file wrzucam do File
        final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(filemenu);
        
        final MenuItem importMenuItem = new MenuItem(filemenu, SWT.PUSH);
        importMenuItem.setText("&Import subscriptions\tCTRL+I");
        importMenuItem.setAccelerator(SWT.CTRL+'I');
        final MenuItem exportMenuItem = new MenuItem(filemenu, SWT.PUSH);
        exportMenuItem.setText("&Export subscriptions\tCTRL+E");
        exportMenuItem.setAccelerator(SWT.CTRL+'E');
        final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
        final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
        exitMenuItem.setText("E&xit");
        
     // Window menu
        final MenuItem window = new MenuItem(menu, SWT.CASCADE);
        window.setText("&Window");
        final Menu windowmenu = new Menu(shell, SWT.DROP_DOWN);
        window.setMenu(windowmenu);
        final MenuItem maxMenuItem = new MenuItem(windowmenu, SWT.PUSH);
        maxMenuItem.setText("&Restore");
        final MenuItem minMenuItem = new MenuItem(windowmenu, SWT.PUSH);
        minMenuItem.setText("Mi&nimize");
        final MenuItem simpleView = new MenuItem(windowmenu, SWT.CHECK);
        simpleView.setText("&Simple windows");
        simpleView.setSelection(false);
        
        
     // Help Menu
        final MenuItem help = new MenuItem(menu, SWT.CASCADE);
        help.setText("&Help");
        final Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
        help.setMenu(helpmenu);
        final MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
        aboutMenuItem.setText("&About");    
        final MenuItem helpMenuItem = new MenuItem(helpmenu, SWT.PUSH);
        helpMenuItem.setText("&Help");
        helpMenuItem.setAccelerator(SWT.F1);
        
        
     // ActionListeners do MenuItems (do wypelnienia pozniej)
        
        //File menu listeners
        importMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Import");
                FileDialog dialog = new FileDialog (shell, SWT.OPEN);
    			dialog.setFileName("*.xml");
    			dialog.setFilterNames (new String[] {"*.xml", "*.opml", "*.txt", "All"});
    			dialog.setFilterExtensions (new String[] {"*.xml", "*.opml", "*.txt", "*" });
    			dialog.setText("Import subscriptions");
    			String result = dialog.open();
    			
    			try {
					JReader.importChannelList(result);
					SubsList.refresh();
					Filters.refresh();
					TagList.refresh();
				} catch (IOException e1) {
					// TODO okienko z trescia: brak pliku
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO okienko z treścią: plik jest niepoprawny
					e1.printStackTrace();
				}
			
    			return;
            }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
        
        exportMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Export");
                FileDialog dialog = new FileDialog (shell, SWT.SAVE);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    			dialog.setFileName("jreader_subscriptions_" + date + ".xml");
    			dialog.setFilterNames (new String[] {"*.xml", "All"});
    			dialog.setFilterExtensions (new String[] {"*.xml", "*" });
    			dialog.setText("Export subscriptions");
    			String result = dialog.open();
    			
    			try {
					JReader.exportChannelList(result);
					GUI.statusLine.setText("Channels list exported to " + result + ".xml");
				} catch (IOException e1) {
					// TODO tresc: nie mozna zapisac pliku
					e1.printStackTrace();
				}
    		
    			return;
           }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
        
        exitMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                System.exit(0);
            }
            public void widgetDefaultSelected(SelectionEvent e) {                
            }
        });
              
        //Window menu listeners
        maxMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Shell parent = (Shell)maxMenuItem.getParent().getParent();
                parent.setMaximized(true);
            }
            public void widgetDefaultSelected(SelectionEvent e) {                
            }
        });
    
        minMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Shell parent = (Shell)minMenuItem.getParent().getParent();
                parent.setMaximized(false);
            }
            public void widgetDefaultSelected(SelectionEvent e) {                
            }
        });
        simpleView.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
            	Items.folderItem.setSimple(simpleView.getSelection());
            	Filters.folderFilter.setSimple(simpleView.getSelection());
            	Preview.folderPreview.setSimple(simpleView.getSelection());
            	Subscriptions.folderSubs.setSimple(simpleView.getSelection());
            	Tags.folderTag.setSimple(simpleView.getSelection());
            }
            public void widgetDefaultSelected(SelectionEvent e) { 
            }
        });
        
        //Help Menu Listeners
        helpMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Help");
          }
            public void widgetDefaultSelected(SelectionEvent e) {                
            }
        });
        
        aboutMenuItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                AboutShell s = new AboutShell(shell);
                s.open();
          }
            public void widgetDefaultSelected(SelectionEvent e) {                
            }
            });      
        
        shell.setMenuBar(menu);
        
	}
}
