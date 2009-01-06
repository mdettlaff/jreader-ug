package jreader.gui;

import java.util.Date;

import jreader.JReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;

public class PreviewItem {

	private Browser browser;
	private Label title;
	private Label author;
	private Link titleLink;
	
	/**
	 * Tworzy nową zakładkę.
	 * 
	 * @param text       Tytuł zakładki.
	 * @param itemImage  Ikona zakładki.
	 */
	public PreviewItem(String text, Image itemImage) {
		    
		final CTabItem previewItem = new CTabItem(Preview.folderPreview, SWT.CLOSE);
		previewItem.setText(text);
		previewItem.setImage(itemImage);
		Composite comp = new Composite(Preview.folderPreview, SWT.NONE);
		comp.setLayout(new GridLayout());
		Composite header = new Composite(comp, SWT.NONE);
		header.setLayout(new FillLayout(SWT.VERTICAL));
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		titleLink = new Link(header, SWT.NONE);
		title = new Label(header, SWT.NONE);
		author = new Label(header, SWT.NONE);
		
		if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			browser = new Browser(comp, SWT.MOZILLA | SWT.BORDER);
		} else {
			browser = new Browser(comp, SWT.NONE | SWT.BORDER);
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		previewItem.setControl(comp);
		Preview.folderPreview.setSelection(previewItem);
	
		String titleText = JReader.getPreview().getCurrent().getTitle();
		Date date = JReader.getPreview().getCurrent().getDate();
		String authorText = JReader.getPreview().getCurrent().getAuthor();
		String fromText = JReader.getPreview().getCurrent().getChannelTitle();
		final String url = JReader.getPreview().getCurrent().getLink();
		
		titleLink.setText("<a>" + titleText + "</a>");
		browser.setText(JReader.getPreview().getCurrent().getHTML());
		title.setText(((date != null) ? date.toString() : " "));
		if (authorText != null && fromText != null)
			author.setText("Author: " + authorText + "\tFrom: " + fromText);
		else if (fromText != null && authorText == null)
			author.setText("From: " + fromText);
		else if (authorText != null)
			author.setText(authorText);
		else
			author.setText("");
	
		titleLink.addListener (SWT.Selection, new Listener () {
		public void handleEvent(Event event) {
			browser.setUrl(url);
		}
		});

	}
}
