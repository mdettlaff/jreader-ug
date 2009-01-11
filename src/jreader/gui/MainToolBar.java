package jreader.gui;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import jreader.Channel;
import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MainToolBar {
	
	final ToolBar toolBar;

	public MainToolBar(final Shell shell) {
		
		Display display = shell.getDisplay();
		
	//	MainToolBar	
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.WRAP);
		
	//	Opcjonalne ikony do przyciskow na ToolBar
		
		final Image addIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "add.png");
        final Image syncIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "sync.png");
        final Image backIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "back.png");
        final Image forwardIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "forward.png");
        final Image searchIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "search.png");
        final Image optionsIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "options2.png");
        final Image unreadIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "unread.png");
        final Image newTabIcon = new Image(display, "data" + File.separator + "icons" + File.separator + "medium" + File.separator + "newTab2.png");
        
      //Separator0
        new ToolItem(toolBar, SWT.SEPARATOR);
        
      //Synchronizuj
        final ToolItem syncToolItem = new ToolItem(toolBar, SWT.PUSH);
        syncToolItem.setImage(syncIcon);
        //syncToolItem.setText("Synch");
        syncToolItem.setToolTipText("Synchronize");
        
      //Dodaj subskrypcje 
        final ToolItem addSubToolItem = new ToolItem(toolBar, SWT.PUSH);
        addSubToolItem.setImage(addIcon);
        //addSubToolItem.setText("Add");
        addSubToolItem.setToolTipText("Add Subscription");
        
      //nastepna nieprzeczytana wiadomsoc
        final ToolItem unreadToolItem = new ToolItem(toolBar, SWT.PUSH);
        unreadToolItem.setImage(unreadIcon);
        //unreadToolItem.setText("Next msg");
        unreadToolItem.setToolTipText("Next unread item");
       
        //separator 1
        new ToolItem(toolBar, SWT.SEPARATOR );
        
      //Back
        final ToolItem backToolItem = new ToolItem(toolBar, SWT.PUSH);
        backToolItem.setImage(backIcon);
        //backToolItem.setText("Back");
        backToolItem.setToolTipText("Previous item");
        
      //Forward
        final ToolItem forwardToolItem = new ToolItem(toolBar, SWT.PUSH);
        forwardToolItem.setImage(forwardIcon);
        //forwardToolItem.setText("Forward");
        forwardToolItem.setToolTipText("Next item");
        
      //New Tab
        final ToolItem newTabItem = new ToolItem(toolBar, SWT.PUSH);
        newTabItem.setImage(newTabIcon);
        //forwardToolItem.setText("Forward");
        newTabItem.setToolTipText("New tab");
        
        //separator 2
        new ToolItem(toolBar, SWT.SEPARATOR);
        
      //Szukaj
        final ToolItem searchToolItem = new ToolItem(toolBar, SWT.PUSH);
        searchToolItem.setImage(searchIcon);
        //searchToolItem.setText("Search");
        searchToolItem.setToolTipText("Search");
        
      //Opcje
        final ToolItem optionsToolItem = new ToolItem(toolBar, SWT.PUSH);
        optionsToolItem.setImage(optionsIcon);
        //optionsToolItem.setText("Options");
        optionsToolItem.setToolTipText("Preferences");
        
    
        
        /*TollBar listeners*/
        
        //Add Subscription
        addSubToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	GUI.statusLine.setText("Adding Subscryption");
            	AddSubscriptionShell addShell = new AddSubscriptionShell(shell);
		   		addShell.open();
                }
        });
        //synchornizuj
        syncToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                new UpdateThread();                
            }
        });
        //Back
        backToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	int tabIndex = Preview.folderPreview.getSelectionIndex();
            	if (Preview.folderPreview.getItemCount() != 0) {
            		JReader.previousItem(tabIndex);
            		Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
            	}
            }
        });
        //Forward
        forwardToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	int tabIndex = Preview.folderPreview.getSelectionIndex();
            	if (Preview.folderPreview.getItemCount() != 0) {
            		JReader.nextItem(tabIndex);
            		Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
            	}
            }
        });
        //Next unread item
        unreadToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	int tabIndex = Preview.folderPreview.getSelectionIndex();

            	if (Preview.folderPreview.getItemCount() != 0) {
	                JReader.nextUnread(tabIndex);
					Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
				} else if (JReader.getUnreadItemsCount() != 0) {
					JReader.addNewPreviewTab();
	                JReader.nextUnread(0);
					GUI.openTab(JReader.getPreview(0).getCurrent().getTitle()).refresh();
				}
				SubsList.refresh();
				Filters.refresh();
				ItemsTable.refresh();
            }
        });
        //New Tab
        newTabItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                GUI.openTab("No title");                
            }
        });
        //Search
        searchToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	Search searchShell = new Search();
            	searchShell.open();
                System.out.println("Search");
                
            }
        });
        //Options
        optionsToolItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	OptionsShell optionsShell = new OptionsShell(shell);
		   		optionsShell.open();
            }
        });   
        //Listener do wskaźnika myszy - zmienia status line
        
        toolBar.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
              ToolItem item = toolBar.getItem(new Point(e.x, e.y));
              String name = "";
              if (item != null && item.getStyle() != SWT.SEPARATOR) {
                name = item.getToolTipText();
              }
              if (!GUI.statusText.equals(name)) {
                GUI.statusLine.setText(name);
                GUI.statusText = name;
              }
            }
          });
        
        toolBar.pack();    
	}

	/*
	 * Dane tymczasowe potrzebne do wątków uruchamianych w metodzie
	 * synchronize poniżej.
	 */
	private static Channel asyncChannel;
	private static String errorMessage;
	private static int errorLine;
	
	/**
	 * Aktualizuje wszystkie kanały z listy kanałów.
	 */
	synchronized public static void synchronize() {
		for (Channel channel : JReader.getVisibleChannels()) {
			try {
				asyncChannel = channel;
				if (JReader.updateChannel(channel) > 0) {
					GUI.display.asyncExec(new Runnable() {
						public void run() {
							GUI.statusLine.setText(asyncChannel.getTitle() + " updated.");
						}
					});
				} else {
					GUI.display.asyncExec(new Runnable() {
						public void run() {
							GUI.statusLine.setText(asyncChannel.getTitle() + " has not changed.");
						}
					});
				}
				channel.setFail(false);
			} catch (SAXParseException spe) {
				asyncChannel = channel;
				errorLine = spe.getLineNumber();
				errorMessage = spe.getLocalizedMessage();
				GUI.display.asyncExec(new Runnable() {
					public void run() {
						GUI.statusLine.setText("Failed to update channel "
								+ asyncChannel.getTitle() + "."
								+ " Source is not a valid XML."
								+ " Error in line " + errorLine + "."
								+ " Details: " + errorMessage);
					}
				});
				channel.setFail(true);
			} catch (SAXException saxe) {
				GUI.display.asyncExec(new Runnable() {
					public void run() {
						GUI.statusLine.setText("Failed to update channel."
								+ " XML parser error has occured.");
					}
				});
			} catch (SocketException se) {
				asyncChannel = channel;
				errorMessage = se.getLocalizedMessage();
				GUI.display.asyncExec(new Runnable() {
					public void run() {
						GUI.statusLine.setText("Failed to update channel "
						+ asyncChannel.getTitle() + "."
						+ " Details: " + errorMessage);
					}
				});
				channel.setFail(true);
			} catch (IOException ioe) {
				asyncChannel = channel;
				GUI.display.asyncExec(new Runnable() {
					public void run() {
						GUI.statusLine.setText("Failed to update channel "
								+ asyncChannel.getTitle() + "."
								+ " Unable to connect to the site.");
					}
				});
				channel.setFail(true);
			}
		}
		GUI.display.asyncExec(new Runnable() {
			public void run() {
				GUI.statusLine.setText("All channels have been updated.");
			}
		});
		SubsList.refresh();
		ItemsTable.refresh();
		Filters.refresh();
		
	}
}
