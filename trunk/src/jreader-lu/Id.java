package jreader;

import java.util.UUID;
/**
 * Klasa generujaca unikalne identyfikatory
 */
public class Id {
	/**
	 * Metoda generuje unikalny idetyfikator
	 * @return Idetyfikator typu String
	 */
	public static String newId() {
		return UUID.randomUUID().toString();
	}
}
