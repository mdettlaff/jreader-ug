package jreader.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class WebProgress {

	public WebProgress(Composite comp) { 
		final ProgressBar progressBar = new ProgressBar(comp, SWT.SMOOTH);
		progressBar.setSize(100, 20);
		progressBar.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		progressBar.setVisible(false);
		
		Preview.browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0) return;                            
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
				progressBar.setVisible(true);
			}
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
				progressBar.setVisible(false);
			}
    	});
	}
}
