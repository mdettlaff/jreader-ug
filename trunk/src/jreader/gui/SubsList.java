package jreader.gui;

import java.io.IOException;
import java.net.SocketException;

import jreader.Channel;
import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



public class SubsList {
	 
	
	public static Table subsList;
	public static Color gray = new Color (Subscriptions.subComposite.getDisplay(), 240, 250, 250);
	public static Color white = new Color (Subscriptions.subComposite.getDisplay(), 255, 255, 255);
	public static Image def = new Image(Subscriptions.subComposite.getDisplay(), "c:\\icons\\unread.png");
	static Font fontBold;
	 
	public SubsList(final Composite comp) {
		
	    subsList = new Table(comp, SWT.SINGLE);
	   
	  //Czcionka
	    Font initialFont = subsList.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setHeight(10);
	      fontData[i].setStyle(SWT.BOLD);
	    }
	    fontBold = new Font(comp.getDisplay(), fontData);
	    
	    /*for (Channel ch : JReader.getVisibleChannels()) {
	    	TableItem item = new TableItem(subsList, SWT.NONE);
	    	if (ch.getIconPath() == null)
	    		if (ch.getUnreadItemsCount() == 0)
	    			item.setImage(ItemsTable.read);
	    		else
	    			item.setImage(def);
	    	else  	item.setImage(new Image(comp.getDisplay(), ch.getIconPath()));
	    	item.setText(ch.getTitle() + " (" + ch.getUnreadItemsCount() + ")");
	    	if (ch.getUnreadItemsCount() != 0)
	    		item.setFont(fontBold);
	    	
	    }   */
	    refresh();
	    
	    Menu popupMenu = new Menu(subsList);
	    MenuItem markRead = new MenuItem(popupMenu, SWT.NONE);
	    markRead.setText("Mark as read");
	    MenuItem synchronize = new MenuItem(popupMenu, SWT.NONE);
	    synchronize.setText("Synchronize");
	    MenuItem changeTag = new MenuItem(popupMenu, SWT.NONE);
	    changeTag.setText("Change tag");
	    MenuItem delete = new MenuItem(popupMenu, SWT.NONE);
	    delete.setText("Delete");
	    subsList.setMenu(popupMenu);
	    
	    
	    
	    subsList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int indeks = subsList.getSelectionIndex();
				System.out.println(indeks);
				JReader.selectChannel(indeks);
				ItemsTable.refresh();
            }
        });
	    
	    /*
	     * Obsługuje popupMenu markRead.
	     */
	    markRead.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("Mark as read");
            	int indeks = subsList.getSelectionIndex();
            	if (indeks == -1) return;
            	JReader.markChannelAsRead(JReader.getChannel(indeks));
            	SubsList.refresh();
            	ItemsTable.refresh();
           }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
	    
	    /*
	     * Obsługuje popupMenu synchronize.
	     */
	    synchronize.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("Synchronize");
            	int indeks = subsList.getSelectionIndex();
            	if (indeks == -1) return;
            	try {
            		JReader.updateChannel(JReader.getChannel(indeks));
					JReader.getChannel(indeks).setFail(false);
					JReader.statusLine.setText("Kanal zostal zaktualizowany.");//status
				} catch (SAXParseException spe) {
					JReader.statusLine.setText("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
					System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
					System.out.println("Szczegoly: " + spe.getLocalizedMessage());
					JReader.getChannel(indeks).setFail(true);
				} catch (SAXException saxe) {
					JReader.statusLine.setText("Nie mozna dodac kanalu.");
					System.out.println(" Blad parsera XML.");
				} catch (SocketException se) {
					JReader.statusLine.setText("Nie mozna zaktualizowac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
					JReader.getChannel(indeks).setFail(true);
				} catch (IOException ioe) {
					JReader.statusLine.setText("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Brak polaczenia ze strona.");
					JReader.getChannel(indeks).setFail(true);
				}
            	SubsList.refresh();
            	ItemsTable.refresh();
           }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
	
	    /*
	     * Obsługuje popupMenu changeTag.
	     */
	    changeTag.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("Change Tag");
            	final int indeks = subsList.getSelectionIndex();
            	if (indeks == -1) return;
            	final Shell changeShell = new Shell(comp.getDisplay(), SWT.DIALOG_TRIM);
            	changeShell.setText("Change tag: " + subsList.getItem(indeks).getText());
            	RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        		rowLayout.pack = true;
        		rowLayout.justify = true;
        		rowLayout.marginWidth = 40;
        		rowLayout.center = true;
        		rowLayout.spacing = 10;
        		changeShell.setLayout(rowLayout);
            	
            	new Label(changeShell, SWT.NONE).setText("Give the tags: ");
            	final Text tags = new Text(changeShell, SWT.BORDER);
            	tags.setText(JReader.getChannel(indeks).getTagsAsString());
            	Button okBut = new Button(changeShell, SWT.PUSH);
            	okBut.setText("OK");
            	okBut.addListener(SWT.Selection, new Listener() {
        			public void handleEvent(Event event) {
        				JReader.editTags(JReader.getChannel(indeks), tags.getText());
        				TagList.refresh();
        				changeShell.close();
        			}
        		});
            	
            	changeShell.pack();
            	changeShell.open();
            	TagList.refresh();
           }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
	
	    /*
	     * Obsługuje popupMenu delete.
	     */
	    delete.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("Delete channel");
            	int indeks = subsList.getSelectionIndex();
            	if (indeks == -1) return;
            	JReader.removeChannel(indeks);
            	SubsList.refresh();
            	ItemsTable.refresh();
           }
            public void widgetDefaultSelected(SelectionEvent e) {                
           }
        });
	
	
	}
	
	/**
	 * Odświeża listę subskrypcji
	 */
	public static void refresh() {
		subsList.removeAll();
		for (Channel ch : JReader.getVisibleChannels()) {
	    	TableItem subs = new TableItem(SubsList.subsList, SWT.NONE);
	    	if (ch.getIconPath() == null)
	    		if (ch.getUnreadItemsCount() == 0)
	    			subs.setImage(ItemsTable.read);
	    		else
	    			subs.setImage(def);
	    	else {
	    		subs.setImage(new Image(Subscriptions.subComposite.getDisplay(), ch.getIconPath()));
	    	}
	    	subs.setText(ch.getTitle() + " (" + ch.getUnreadItemsCount() + ")");
	    	if (ch.getUnreadItemsCount() != 0)
	    		subs.setFont(fontBold);
	    }
	}
	
	
	
}
