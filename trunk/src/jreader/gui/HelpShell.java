package jreader.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Tworzy okno z pomocą.
 */
public class HelpShell {

	Shell helpShell;
	private Browser helpBrowser;
	
	public HelpShell() {
		
		final Image backIconSmall = new Image(GUI.display, "data" + File.separator + "icons" + File.separator + "small" + File.separator + "back.png");
        final Image forwardIconSmall = new Image(GUI.display, "data" + File.separator + "icons" + File.separator + "small" + File.separator + "forward.png");
        final Image homeIconSmall = new Image(GUI.display, "data" + File.separator + "icons" + File.separator + "small" + File.separator + "home.png");
        final String homeUrl = "file://" + System.getProperty("user.dir") 
		+ File.separator + "data" + File.separator + "help" + File.separator + "index.html";
		
		helpShell = new Shell(GUI.display);
		helpShell.setSize(700, 600);
		helpShell.setImage(GUI.jreader);
		helpShell.setText("Help - Jreader");
		helpShell.setLayout(new FillLayout());
		Monitor primary = GUI.display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = helpShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		helpShell.setLocation(x, y);
		
		CTabFolder helpFolder = new CTabFolder(helpShell, SWT.SINGLE);
		helpFolder.setSimple(true);
		helpFolder.setBorderVisible(true);
		
		CTabItem helpItem = new CTabItem(helpFolder, SWT.NONE);
		Composite helpComp = new Composite(helpFolder, SWT.NONE);
		helpComp.setLayout(new FillLayout());
		if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			helpBrowser = new Browser(helpComp, SWT.MOZILLA);
		} else {
			helpBrowser = new Browser(helpComp, SWT.NONE);
		}
		//helpBrowser.setUrl(homeUrl);
		helpItem.setControl(helpComp);
		
		helpFolder.setSelection(helpItem);	
		helpFolder.setSelectionBackground(new Color[]{Focus.gray, Focus.gray, Focus.gray, Focus.gray},
				new int[] {20, 40, 100}, true);
		
		ToolBar browseToolBar = new ToolBar(helpFolder, SWT.FLAT );
	    ToolItem goBack = new ToolItem(browseToolBar, SWT.PUSH );
	    goBack.setImage(backIconSmall);
	    goBack.setToolTipText("Go back");
	    ToolItem goForward = new ToolItem(browseToolBar, SWT.PUSH );
	    goForward.setImage(forwardIconSmall);
	    goForward.setToolTipText("Go forward");
	    ToolItem home = new ToolItem(browseToolBar, SWT.PUSH );
	    home.setImage(homeIconSmall);
	    home.setToolTipText("Welcome");
	    browseToolBar.pack();
	    helpFolder.setTabHeight(Math.max(browseToolBar.computeSize(SWT.DEFAULT,	SWT.DEFAULT).y, helpFolder.getTabHeight()));
		helpFolder.setTopRight(browseToolBar);
		
		goBack.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				helpBrowser.back();
			}
		});
		goForward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				helpBrowser.forward();
			}
		});
		home.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				helpBrowser.setUrl(homeUrl);
			}
		});
		
	}
	/**
	 * Otwiera okno pomocy.
	 */
	public void open() {
		helpShell.open();
	}
}
