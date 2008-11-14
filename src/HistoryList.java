import java.util.LinkedList;


/**
 * Przechowuje historie przegladanych elementow, po ktorych mozna nawigowac
 * za pomoca przyciskow Wstecz i Dalej. Ilosc pamietanych elementow jest
 * ograniczona.
 */
class HistoryList<T> {
  /** Ilosc pamietanych elementow. */
  private int size;
  private int currentPosition;
  private LinkedList<T> history;

  HistoryList(int size) {
    this.size = size;
    currentPosition = 0;
    history = new LinkedList<T>();
    history.add(null);
  }

  void setCurrent(T preview) {
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

  T getCurrent() {
    return history.get(currentPosition);
  }

  T previous() {
    if (currentPosition < history.size()-1) {
      currentPosition++;
      return this.getCurrent();
    } else {
      return null;
    }
  }

  T next() {
    if (currentPosition > 0) {
      currentPosition--;
      return this.getCurrent();
    } else {
      return null;
    }
  }
}

