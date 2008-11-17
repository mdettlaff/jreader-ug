package jreader;

import java.util.Comparator;

/**
 * Porownuje elementy ze wzgledu na date (od najnowszych lub od najstarszych).
 */
class ItemComparator implements Comparator<Item> {

	public int compare(Item item1, Item item2) {
		if (item1.getDate().before(item2.getDate())) {
			if (JReader.getConfig().getSortByNewest()) {
				return 1;
			} else {
				return -1;
			}
		} else if (item1.getDate().after(item2.getDate())) {
			if (JReader.getConfig().getSortByNewest()) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}
}

