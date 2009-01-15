package jreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Zawiera operacje dyskowe takie jak zapisywanie obiektu do pliku
 * oraz wczytanie obiektu z pliku.
 */
public class ReadWrite {
	/**
	 * Nie można tworzyć obiektów tej klasy.
	 */
	private ReadWrite() {}

	/**
	 * Odczytuje dane z dysku.
	 *
	 * @param  file Plik z którego odczytujemy dane.
	 * @return Obiekt danych odczytanych z pliku, <code>null</code> jeśli wystąpi
	 *         błąd.
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
	 * Zapisuje dane do pliku.
	 *
	 * @param  file Plik do którego zapisujemy dane.
	 * @param  object Obiekt danych, które chcemy zapisać.
	 * @return <code>true</code> jeśli operacja się powiedzie,
	 *         <code>false</code> w przeciwnym wypadku.
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

