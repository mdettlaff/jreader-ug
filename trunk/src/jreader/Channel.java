package jreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Kanał RSS lub Atom.
 */
public class Channel implements Comparable<Channel> {
	/** URL pliku XML z treścią kanału. */
	private String channelURL;
	/** Liczba nieprzeczytanych elementów. */
	private int unreadItemsCount;
	/** true, jeśli ostatnia próba aktualizowania kanału nie powiodła się. */
	private boolean fail;
	/** Tagi; jeśli kanał nie jest oznaczony tagami, lista jest pusta. */
	private List<String> tags = new LinkedList<String>();

	private String title;
	private String link;
	private String description;
	/** Zawartość elementu image (obrazek będący częścią opisu kanału). */
	private String imageURL;
	private String imageTitle;
	private String imageLink;
	/** Elementy (wiadomości) kanału. */
	private Map<Integer, Item> items = new HashMap<Integer, Item>();

	/**
	 * Tworzy nowy kanał o podanym adresie źródła XML.
	 */
	public Channel(String channelURL) {
		this.channelURL = channelURL;
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }

	public String getDescription() { return description; }
	public void setDescription(String description) {
	 	this.description = description;
 	}

	public String getChannelURL() { return channelURL; }

	public List<Item> getItems() {
		return new ArrayList<Item>(items.values());
	}

	public String getImageURL() { return imageURL; }
	public void setImageURL(String imageURL) { this.imageURL = imageURL; }

	public String getImageTitle() { return imageTitle; }
	public void setImageTitle(String imageTitle) {
	 	this.imageTitle = imageTitle;
 	}

	public String getImageLink() { return imageLink; }
	public void setImageLink(String imageLink) { this.imageLink = imageLink; }

	/**
	 * @return Liczba nieprzeczytanych elementów kanału.
	 */
	public int getUnreadItemsCount() {
		return unreadItemsCount;
	}

	public boolean isFail() { return fail; }
	public void setFail(boolean b) { fail = b; }

	/**
	 * @return Lista tagów.
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @return Lista tagów jako string w formacie 'tag1 tag2 tag3 [...]';
	 *         jeśli nie ma żadnych tagów, zwraca pusty string ""
	 */
	public String getTagsAsString() {
		String tagsList = "";
		for (String tag : tags) {
			tagsList += tag.concat(" ");
		}
		return tagsList;
	}

	/**
	 * Ustawia listę tagów na podstawie łańcucha znaków wprowadzonego przez
	 * użytkownika, np&#46; 'blog linux tech science'. Tagi powinny być
	 * oddzielone spacjami, ale dozwolone są też przecinki.
	 */
	public void setTags(String newTags) {
		this.tags = new LinkedList<String>();
		addTags(newTags);
	}

	/**
	 * Dodaje podane tagi do już istniejących.
	 */
	public void addTags(String newTags) {
		if (newTags != null) {
			newTags = newTags.trim().toLowerCase();
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

	public boolean containsTag(String tag) {
		for (String tagInThisChannel : tags) {
			if (tag.toLowerCase().equals(tagInThisChannel.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Pobiera i parsuje źródło XML kanału i uzupełnia informacje ogólne
	 * o kanale oraz jego elementy.
	 *
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML
	 * @throws Exception różne wyjątki związane z niepowodzeniem pobierania
	 *         pliku.
	 */
	public void update() throws Exception {
		Channel ch = ChannelFactory.getChannelFromXML(channelURL);
		this.title = ch.getTitle();
		this.link = ch.getLink();
		this.description = ch.getDescription();
		this.imageURL = ch.getImageURL();
		this.imageTitle = ch.getImageTitle();
		this.imageLink = ch.getImageLink();
		// dodawanie nowych elementów do kanału
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
	 * Liczy na nowo i aktualizuje ilość nieprzeczytanych elementów kanału.
	 *
	 * @return 1, jeśli ilość nieprzeczytanych elementów się zmieniła;
	 *         w przeciwnym wypadku 0.
	 */
	public int updateUnreadItemsCount() {
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

	public void addItem(Item item) {
		items.put(item.hashCode(), item);
	}

	public void markAllAsRead() {
		for (Item item : items.values()) {
			item.markAsRead();
		}
		this.updateUnreadItemsCount();
	}

	/**
	 * Oznacza wszystkie elementy kanału jako przeczytane.
	 */
	public void markAllAsUnread() {
		for (Item item : items.values()) {
			item.markAsUnread();
		}
		this.updateUnreadItemsCount();
	}

	/**
	 * Porównywanie alfabetyczne kanałów według ich tytułów.
	 */
	public int compareTo(Channel channel) {
		return title.compareToIgnoreCase(channel.getTitle());
	}

	/**
	 * Do użycia jako klucz w HashMapie.
	 */
	public int hashCode() {
		return channelURL.hashCode();
	}
}

