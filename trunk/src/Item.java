/**
 * Element kanalu.
 */
class Item {
  /** Czy dany element jest juz przeczytany. */
  boolean isRead;
  String title;
  String link;
  String description;
  /** Data publikacji elementu. */
  String pubDate;
  /** Unikalny identyfikator elementu. */
  String guid;

  /**
   * Porownuje dwa elementy (do sprawdzania, czy dany element jest nowy).
   */
  public boolean equals(Object obj) {
    Item it = (Item) obj;
    if (this.guid != null && it.guid != null) {
      if (this.guid.equals(it.guid)) {
	return true;
      }
    }
    if (this.title.equals(it.title) && this.pubDate.equals(it.pubDate)
	&& this.description.equals(it.description)) {
      return true;
      }
    return false;
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  String getPubDate() { return pubDate; }
}

