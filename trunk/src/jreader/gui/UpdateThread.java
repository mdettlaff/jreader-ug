package jreader.gui;

/**
 * Wątek aktualizujący wszystkie kanały z listy kanałów do wyświetlenia w GUI.
 */
public class UpdateThread implements Runnable {
	Thread t;

	UpdateThread() {
		t = new Thread(this, "JReaderUpdateThread");
		t.start();
	}

	public void run() {
		MainToolBar.synchronize();
	}
}

