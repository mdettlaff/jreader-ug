package jreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Kanal RSS.
 */
class Channel implements Comparable<Channel> {
	/** URL pliku XML z trescia kanalu. */
	private String channelURL;
	/** Liczba nieprzeczytanych elementow. */
	private int unreadItemsCount;
	/** Tagi; jesli kanal nie jest oznaczony tagami, lista jest pusta. */
	private List<String> tags = new LinkedList<String>();

	private String title;
	private String link;
	private String description;
	/** Zawartosc elementu image (obrazek bedacy czescia opisu kanalu). */
	private String imageURL;
	private String imageTitle;
	private String imageLink;
	/** Elementy (wiadomosci) kanalu. */
	private Map<Integer, Item> items = new HashMap<Integer, Item>();

	Channel(String channelURL) throws Exception {
		this.channelURL = channelURL;
	}

	/**
	 * Pobiera i parsuje zrodlo XML kanalu i uzupelnia informacje ogolne
	 * o kanale oraz jego elementy.
	 */
	void update() throws Exception {
		Channel ch = ChannelFactory.getChannelFromXML(channelURL);
		this.title = ch.getTitle();
		this.link = ch.getLink();
		this.description = ch.getDescription();
		this.imageURL = ch.getImageURL();
		this.imageTitle = ch.getImageTitle();
		this.imageLink = ch.getImageLink();
		// dodawanie nowych elementow do kanalu
		for (Item updatedItem : ch.getItems()) {
			boolean itemAlreadyExists = false;
			for (Item item : items.values()) {
				if (updatedItem.equals(item)) {
					itemAlreadyExists = true;
					break;
				}
			}
			if (!itemAlreadyExists) {
				this.addItem(updatedItem);
			}
		}
		this.updateUnreadItemsCount();
	}

	/**
	 * Liczy na nowo i aktualizuje ilosc nieprzeczytanych elementow kanalu.
	 *
	 * @return 1, jesli ilosc nieprzeczytanych elementow sie zmienila;
	 *         w przeciwnym przypadku 0.
	 */
	int updateUnreadItemsCount() {
		int oldCount = unreadItemsCount;
		unreadItemsCount = 0;
		for (Item item : items.values()) {
			if (item.isUnread()) {
				unreadItemsCount++;
			}
		}
		if (oldCount == unreadItemsCount) {
			return 0;
		} else {
			return 1;
		}
	}

	void addItem(Item item) {
		items.put(item.hashCode(), item);
	}

	void markAllAsRead() {
		for (Item item : items.values()) {
			item.markAsRead();
		}
		this.updateUnreadItemsCount();
	}

	/**
	 * Oznacza wszystkie elementy kanalu jako przeczytane.
	 */
	void markAllAsUnread() {
		for (Item item : items.values()) {
			item.markAsUnread();
		}
		this.updateUnreadItemsCount();
	}

	String getTitle() { return title; }
	String getLink() { return link; }
	String getDescription() { return description; }
	String getChannelURL() { return channelURL; }
	List<Item> getItems() {
		return new ArrayList<Item>(items.values());
	}

	String getImageURL() { return imageURL; }
	String getImageTitle() { return imageTitle; }
	String getImageLink() { return imageLink; }

	/**
	 * @return Liczba nieprzeczytanych elementow.
	 */
	int getUnreadItemsCount() {
		return unreadItemsCount;
	}

	/**
	 * @return Lista tagow jako string w formacie 'tag1 tag2 tag3 [...]';
	 *         jesli nie ma zadnych tagow, zwraca pusty string ""
	 */
	List<String> getTags() {
		return tags;
	}

	/**
	 * @return Lista tagow jako string w formacie 'tag1 tag2 tag3 [...]';
	 *         jesli nie ma zadnych tagow, zwraca pusty string ""
	 */
	String getTagsAsString() {
		String tagsList = "";
		for (String tag : tags) {
			tagsList += tag.concat(" ");
		}
		return tagsList;
	}

	/**
	 * Ustawia liste tagow na podstawie lancucha znakow wprowadzonego przez
	 * uzytkownika, np. 'blog linux tech science'. Tagi powinny byc oddzielone
	 * spacjami, ale dozwolone sa tez przecinki.
	 */
	void setTags(String newTags) {
		if (newTags == null) {
			this.tags = new LinkedList<String>();
		} else {
			newTags = newTags.trim();
			this.tags = new LinkedList<String>();
			if (!"".equals(newTags)) {
				newTags = newTags.replace(", ", ",");
				newTags = newTags.replace(",", " ");
				String[] newTagsArray = newTags.split(" ");
				for (String tag : newTagsArray) {
					this.tags.add(tag);
				}
			}
		}
	}

	boolean containsTag(String tag) {
		for (String tagInThisChannel : tags) {
			if (tag.equals(tagInThisChannel)) {
				return true;
			}
		}
		return false;
	}

	void setTitle(String title) { this.title = title; }
	void setLink(String link) { this.link = link; }
	void setDescription(String description) { this.description = description; }
	void setImageURL(String imageURL) { this.imageURL = imageURL; }
	void setImageTitle(String imageTitle) { this.imageTitle = imageTitle; }
	void setImageLink(String imageLink) { this.imageLink = imageLink; }

	/**
	 * Porownywanie alfabetyczne kanalow wedlug ich tytulow.
	 */
	public int compareTo(Channel channel) {
		return title.compareToIgnoreCase(channel.getTitle());
	}

	/**
	 * Do uzycia jako klucz w HashMapie.
	 */
	public int hashCode() {
		return channelURL.hashCode();
	}
}

