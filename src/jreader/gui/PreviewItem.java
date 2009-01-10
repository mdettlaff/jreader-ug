package jreader.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public class PreviewItem {

	private Browser browser;
	private Label title;
	private Label author;
	private Link titleLink;
	private Link source;
	private CTabItem previewItem;
	private Composite header;
	
	/**
	 * Tworzy nową zakładkę.
	 * 
	 * @param text       Tytuł zakładki.
	 * @param itemImage  Ikona zakładki.
	 */
	public PreviewItem(String text, Image itemImage) {
		    
		previewItem = new CTabItem(Preview.folderPreview, SWT.CLOSE);
		previewItem.setText(text);
		previewItem.setImage(itemImage);
		Composite comp = new Composite(Preview.folderPreview, SWT.NONE);
		comp.setLayout(new GridLayout());
		header = new Composite(comp, SWT.NONE);
		header.setLayout(new FillLayout(SWT.VERTICAL));
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		titleLink = new Link(header, SWT.NONE);
		title = new Label(header, SWT.NONE);
		author = new Label(header, SWT.NONE);
		source = new Link(header, SWT.NONE);
		
		if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			browser = new Browser(comp, SWT.MOZILLA | SWT.BORDER);
		} else {
			browser = new Browser(comp, SWT.NONE | SWT.BORDER);
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		previewItem.setControl(comp);
		Preview.folderPreview.setSelection(previewItem);

		/* LISTENERS */
		
		// Progress bar listener 
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0) return;                            
				int ratio = event.current * 100 / event.total;
				GUI.progressBar.setSelection(ratio);
				GUI.progressBar.setVisible(true);
			}
			public void completed(ProgressEvent event) {
				GUI.progressBar.setSelection(0);
				GUI.progressBar.setVisible(false);
			}
		});
		//Status listener
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
		        GUI.statusLine.setText(event.text); 
		      }
		});
		previewItem.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				if (Preview.folderPreview.getSelectionIndex() != -1)
					Preview.previewItemList.remove(Preview.folderPreview.getSelectionIndex());
				else
					Preview.previewItemList.remove(0);
			  }
		});
		
		browser.addMouseListener(new MouseListener() {
	        public void mouseDown(MouseEvent e) {
	            //Preview.folderPreview.setFocus();
	        }
			public void mouseDoubleClick(MouseEvent arg0) {
				Preview.folderPreview.setFocus();
			}
			public void mouseUp(MouseEvent arg0) {
				//Preview.folderPreview.setFocus();
			}
	    });
		
		browser.addFocusListener(Focus.setFocus((Preview.folderPreview)));
		titleLink.addFocusListener(Focus.setFocus((Preview.folderPreview)));
	}
	public void refresh() {
		String titleText = JReader.getPreview().getCurrent().getTitle();
		Date date = JReader.getPreview().getCurrent().getDate();
		String authorText = JReader.getPreview().getCurrent().getAuthor();
		String fromText = JReader.getPreview().getCurrent().getChannelTitle();
		String sourceText = "View channel source (XML)";
		final String url = JReader.getPreview().getCurrent().getLink();

		if (!previewItem.isDisposed())
			previewItem.setText((titleText.length() > 20) ? titleText.substring(0,16).concat("...") : titleText);
		
		titleLink.setText("<a>" + titleText + "</a>");
		titleLink.setFont(SubsList.fontBold);
		
		browser.setText(JReader.getPreview().getCurrent().getHTML());
		title.setText(((date != null) ? GUI.shortDateFormat.format(date) : " "));
	
		if (authorText != null && fromText != null)
			author.setText("Author: " + authorText + "\tFrom: " + fromText);
		else if (fromText != null && authorText == null)
			author.setText("From: " + fromText);
		else if (authorText != null)
			author.setText(authorText);
		else
			author.setText("");
		
		if (!JReader.getPreview().getCurrent().isShowingItem()) {
			source.setText("<a>" + sourceText + "</a>");
		} else
			source.setText("");
		
		titleLink.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				browser.setUrl(url);
			}
		});
		source.addListener(SWT.Selection, new Listener () {
			public void handleEvent(Event e) {
				if (JReader.getPreview().getCurrent().getSource() != null)
					browser.setUrl(JReader.getPreview().getCurrent().getSource());
				else
					browser.setText("<b>Source not found.</b>");
			}
		});
		source.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove (MouseEvent e) {
				String text = JReader.getPreview().getCurrent().getSource();
				if (text != null) {
					GUI.statusLine.setText(text);
					GUI.statusText = JReader.getPreview().getCurrent().getSource();
				}
			}
		});
	}
}
