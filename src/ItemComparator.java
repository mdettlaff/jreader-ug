import java.util.Comparator;


/**
 * Porownuje elementy ze wzgledu na date (od najnowszych lub od najstarszych).
 */
class ItemComparator implements Comparator<Item> {
  /**
   * Czy najnowsze maja byc najpierw; jesli false, to najstarsze najpierw.
   */
  private static boolean sortByNewest = true;

  public int compare(Item item1, Item item2) {
    if (item1.getDate().before(item2.getDate())) {
      if (sortByNewest) {
	return 1;
      } else {
	return -1;
      }
    } else if (item1.getDate().after(item2.getDate())) {
      if (sortByNewest) {
	return -1;
      } else {
	return 1;
      }
    } else {
      return 0;
    }
  }

  static void setSortByNewest(boolean b) {
    sortByNewest = b;
  }
}

