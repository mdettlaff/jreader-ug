import java.util.LinkedList;


/**
 * Przechowuje historie przegladanych elementow, po ktorych mozna nawigowac
 * za pomoca przyciskow Wstecz i Dalej. Ilosc pamietanych elementow jest
 * ograniczona.
 */
class HistoryList<Preview> {
  /** Ilosc pamietanych elementow. */
  private int size;
  private LinkedList<Preview> history;

  HistoryList(int size) {
    this.size = size;
    history = new LinkedList<Preview>();
    history.add(null);
  }

  void setCurrent(Preview preview) {
    if (history.size() > this.size) {
      int i;
      for (i=0; history.get(i) != null; i++) { }
      if (i == 0) {
	history.removeLast();
      } else {
	history.remove(i-1);
      }
    }
    history.addFirst(preview);
  }

  Preview getCurrent() {
    return history.getFirst();
  }

  Preview previous() {
    if (history.size() > 1) {
      if (history.get(1) != null) {
	history.addLast(history.removeFirst());
	return history.getFirst();
      }
    }
    return null;
  }

  Preview next() {
    if (history.size() > 1) {
      if (history.getLast() != null) {
	history.addFirst(history.removeLast());
	return history.getFirst();
      }
    }
    return null;
  }
}

