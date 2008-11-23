package jreader;

import java.util.Date;

/**
 * Element (wiadomosc) kanalu.
 */
class Item implements Comparable<Item> {
	/** Data sciagniecia elementu. */
	private Date creationDate;
	/** Klucz (do HashMapy) kanalu, z ktorego pochodzi element. */
	private Integer channelKey;
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

	String getTitle() { return title; }
	String getLink() { return link; }
	String getDescription() { return description; }
	String getAuthor() { return author; }
	Date getDate() { return date; }
	Date getCreationDate() { return creationDate; }
	Integer getChannelKey() { return channelKey; }

	void setTitle(String title) { this.title = title; }
	void setLink(String link) { this.link = link; }
	void setDescription(String description) { this.description = description; }
	void setAuthor(String author) { this.author = author; }
	void setDate(Date date) { this.date = date; }
	void setCreationDate(Date date) { this.creationDate = date; }
	void setGuid(String guid) { this.guid = guid; }
	void setChannelKey(Integer channelKey) { this.channelKey = channelKey; }

	void markAsRead() {
		isRead = true;
	}

	void markAsUnread() {
		isRead = false;
	}

	boolean isUnread() {
		return !isRead;
	}

	/**
	 * Do wykorzystania jako klucz w HashMapie.
	 */
	public int hashCode() {
		if (guid != null && !"".equals(guid)) {
			return guid.hashCode();
		} else {
			// nie wykorzystywac pola date, bo moze byc zmienne
			return title.concat(description).hashCode();
		}
	}

	/**
	 * Sprawdza czy elementy sa identyczne (do sprawdzania, czy dany element
	 * jest nowy).
	 */
	public boolean equals(Object obj) {
		Item item = (Item) obj;
		if (this.hashCode() == item.hashCode()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Porownuje ten element z innym ze wzgledu na date.
	 */
	public int compareTo(Item item) {
		if (this.getDate().before(item.getDate())) {
			if (JReader.getConfig().getSortByNewest()) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.getDate().after(item.getDate())) {
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

