import java.util.Date;


/**
 * Element (wiadomosc) kanalu.
 */
class Item {
  /** Data sciagniecia elementu. */
  private Date creationDate;
  /** Czy dany element jest juz przeczytany. */
  private boolean isRead;
  private String title;
  private String link;
  private String description;
  private String author;
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

  boolean isUnread() {
    return !isRead;
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  String getAuthor() { return author; }
  Date getDate() { return date; }
  Date getCreationDate() { return creationDate; }

  void setTitle(String title) { this.title = title; }
  void setLink(String link) { this.link = link; }
  void setDescription(String description) { this.description = description; }
  void setAuthor(String author) { this.author = author; }
  void setDate(Date date) { this.date = date; }
  void setCreationDate(Date date) { this.creationDate = date; }
  void setGuid(String guid) { this.guid = guid; }

  // Klucz do uzycia w HashMapie
  String getKey() {
    if (guid != null) {
      return guid;
    } else {
      // TODO: sam tytul nie jest zbyt dobry, trzeba wymyslic sprytniejszy hash
      return title;
    }
  }
}

