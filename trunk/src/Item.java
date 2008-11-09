import java.util.Date;

/**
 * Element kanalu.
 */
class Item {
  /** Czy dany element jest juz przeczytany. */
  private boolean isRead;
  private String title;
  private String link;
  private String description;
  /** Data publikacji elementu. */
  private Date date;
  /** Unikalny identyfikator elementu. */
  private String guid;

  /**
   * Porownuje dwa elementy (do sprawdzania, czy dany element jest nowy).
   */
  public boolean equals(Object obj) {
    Item item = (Item) obj;
    if (this.guid != null && item.guid != null) {
      if (this.guid.equals(item.guid)) {
	return true;
      }
    } else if (this.title.equals(item.title)
	&& this.description.equals(item.description)) {
      return true;
    }
    return false;
  }

  void markAsRead() {
    isRead = true;
  }

  void markAsUnread() {
    isRead = false;
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  Date getDate() { return date; }

  void setTitle(String title) { this.title = title; }
  void setLink(String link) { this.link = link; }
  void setDescription(String description) { this.description = description; }
  void setDate(Date date) { this.date = date; }
  void setGuid(String guid) { this.guid = guid; }
}

