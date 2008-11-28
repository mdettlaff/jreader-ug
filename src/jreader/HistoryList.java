package jreader;

import java.util.LinkedList;

/**
 * Przechowuje historię przeglądanych elementów, po których można nawigować
 * za pomocą przycisków Wstecz i Dalej. Ilość pamiętanych elementów jest
 * ograniczona.
 */
public class HistoryList<T> {
	/** Ilość pamiętanych elementów. */
	private int size;
	private int currentPosition;
	private LinkedList<T> history;

	/**
	 * Tworzy nową historię pamiętającą określoną ilość elementów.
	 */
	public HistoryList(int size) {
		this.size = size;
		currentPosition = 0;
		history = new LinkedList<T>();
	}

	/**
	 * Ustawia podany element jako bieżący.
	 */
	public void setCurrent(T preview) {
		if (currentPosition > 0) {
			for (int i=0; i < currentPosition; i++) {
				history.removeFirst();
			}
		}
		history.addFirst(preview);
		currentPosition = 0;
		if (history.size() > size) {
			history.removeLast();
		}
	}

	/**
	 * Zwraca bieżący element.
	 *
	 * @return Aktualnie wyświetlany element, lub <code>null</code>
	 * jeśli lista jest pusta.
	 */
	public T getCurrent() {
		if (history.size() == 0) {
			return null;
		}
		return history.get(currentPosition);
	}

	/**
	 * Przesuwa historię o jedno miejsce wstecz.
	 *
	 * @return Poprzedni element, lub <code>null</code> jeśli nie ma takiego.
	 */
	public T previous() {
		if (currentPosition < history.size()-1) {
			currentPosition++;
			return this.getCurrent();
		} else {
			return null;
		}
	}

	/**
	 * Przesuwa historię o jedno miejsce naprzód.
	 *
	 * @return Następny element, lub <code>null</code> jeśli nie ma takiego.
	 */
	public T next() {
		if (currentPosition > 0) {
			currentPosition--;
			return this.getCurrent();
		} else {
			return null;
		}
	}
}

