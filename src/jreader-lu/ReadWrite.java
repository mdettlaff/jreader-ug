package jreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Klasa zawiera operacje dyskowe
 */
public class ReadWrite {
	/**
	 * Metoda odczytuje dane z dysku
	 * @param file - plik z ktorego odczytujemy dane
	 * @return zwraca obiekt danych odczytanych z pliku, null jesli wystapi blad
	 */
	public static Object read(File file) {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(ois != null)
					ois.close();
				if(fis != null)
					fis.close();
			} catch(Exception e) {}
		}
	}
	/**
	 * Metoda zapisuje dane do pliku
	 * @param file - plik do ktorego zapisujemy dane
	 * @param object - obiekt danych, ktore chcemy zapisac
	 * @return true jesli operacja sie powiedzie, false w przeciwnym wypadku
	 */
	public static boolean write(File file, Object object) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if(oos != null)
					oos.close();
				if(fos != null)
					fos.close();
			} catch(Exception e) {}
		}
		return true;
	}
}
