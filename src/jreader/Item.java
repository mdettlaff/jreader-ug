package jreader;

import java.util.Date;

/**
 * Element (wiadomość) kanału.
 */
public class Item implements Comparable<Item> {
	/** Data ściągnięcia elementu. */
	private Date creationDate;
	/** Klucz (do HashMapy) kanału, z którego pochodzi element. */
	private String channelKey;
	/** Czy dany element jest już przeczytany. */
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
	 * Tworzy nowy, pusty element.
	 */
	public Item() { }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }

	public String getDescription() { return description; }
	public void setDescription(String description) {
	 	this.description = description;
 	}

	public String getAuthor() { return author; }
	public void setAuthor(String author) { this.author = author; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }

	public Date getCreationDate() { return creationDate; }
	public void setCreationDate(Date date) { this.creationDate = date; }

	public String getChannelKey() { return channelKey; }
	public void setChannelKey(String channelKey) {
	 	this.channelKey = channelKey;
 	}

	public void setGuid(String guid) { this.guid = guid; }

	public boolean isUnread() {
		return !isRead;
	}

	public void markAsRead() {
		isRead = true;
	}

	public void markAsUnread() {
		isRead = false;
	}

	/**
	 * Porównuje ten element z innym ze względu na datę.
	 */
	public int compareTo(Item item) {
		if (this.date.before(item.date)) {
			if (JReader.getConfig().getSortByNewest()) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.date.after(item.date)) {
			if (JReader.getConfig().getSortByNewest()) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Sprawdza czy elementy są identyczne (do sprawdzania, czy dany element
	 * jest nowy).
	 */
	public boolean equals(Object obj) {
		Item item = (Item) obj;
		if (this.guid != null && !"".equals(this.guid)
				&& item.guid != null && !"".equals(item.guid)) {
			if (this.guid.equals(item.guid)) {
				return true;
			}
			return false;
		} else {
			if (!this.title.equals(item.title)) {
				return false;
			} else if (!this.description.equals(item.description)) {
				return false;
			}
			return true;
		}
	}
}

