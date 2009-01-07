package jreader.gui;

import jreader.JReader; 
/**
 * Wątek aktualizujący kanały co określoną ilość minut. Ilość minut jest
 * ustalana w opcjach przez użytkownika.
 */
public class UpdateDaemon implements Runnable {
	Thread t;

	UpdateDaemon() {
		t = new Thread(this, "JReaderUpdateDaemon");
		t.setDaemon(true);
		t.start();
	}

	public void run() {
		try {
			while (JReader.getConfig().getAutoUpdateMinutes() > 0) {
				Thread.sleep(JReader.getConfig().getAutoUpdateMinutes() * 1000 * 60);
				if (JReader.getConfig().getAutoUpdateMinutes() == 0) {
					break;
				}
				MainToolBar.synchronize();
			}
		} catch (InterruptedException e) {
			// wątek został przerwany
		}
	}
}

