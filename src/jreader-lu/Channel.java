package jreader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Klasa przechowuje informacje o kanale
 */
public class Channel implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Lista identyfikatorów elementów należących do kanału
	 */
	private List<String> items;
    /**
     * Tagi; jeśli kanał nie jest oznaczony tagami, lista jest pusta
     */
	private List<String> tags;
	/**
	 * URL pliku XML z treścią kanału.
	 */
	private String channelURL;
    /**
     * Unikalny identyfikator kanału
     */
	private String id;
    /** 
     * Informacja o nieudanej aktualizacji kanału
     */
	private boolean isFail;
	/**
	 * Tytuł kanału
	 */
	private String title;
	/**
	 * Odnośnik do strony z której został pobrany kanał
	 */
	private String link;
	/**
	 * Opis kanału
	 */
	private String description;
	/* nie wiem co to ma byc */
	private String imageURL;
	/* nie wiem co to ma byc */
	private String imageTitle;
	/* nie wiem co to ma byc */
	private String imageLink;
	/**
	 * Ilość nieprzeczytanych elementów
	 */
	private int unreadItemsCount;
	/**
	 * Inicjuje podstawowe wartości kanału
	 * @param channelURL adres kanału
	 */
	public Channel(String channelURL) {
		this.id = channelURL;
		this.items = new ArrayList<String>();
		this.tags = new ArrayList<String>();
		this.channelURL = channelURL;
		this.isFail = false;
		this.unreadItemsCount = 0;
	}
	/**
	 * Dodaje identyfikator element do kanału
	 * @param itemId idetyfikator elementu
	 */
	public void addItem(String itemId) {
		this.items.add(itemId);
	}
	/**
	 * Zwraca listę wszystkich identyfikatorów elementów w kanale
	 * @return zwraca listę identyfikatorów elementów
	 */
	public List<String> getItems() {
		return this.items;
	}
	/**
	 * Ustawia nową listę idetyfikatorów elementów, które mają należeć do kanału
	 * @param items lista elementow
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
	/**
	 * Usuwa idetyfikator elementu z listy
	 * @param itemId identyfikator elementu
	 */
	public void removeItem(String itemId) {
		this.items.remove(itemId);
	}
	/**
	 * Dodaje tag do listy tagów
	 * @param tag dodawany tag
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	/**
	 * Zwraca listę wszystkich tagów
	 * @return lista tagów,
	 */
	public List<String> getTags() {
		return this.tags;
	}
	/**
	 * Ustawia nową listę tagów
	 * @param tags lista tagów
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	/**
	 * Usuwa tag z listy tagów
	 * @param tag usuwany tag
	 */
	public void removeTag(String tag) {
		this.tags.remove(tag);
	}
	/**
	 * Zwraca adres kanału
	 * @return zwraca adres kanału
	 */
	public String getChannelURL() {
		return this.channelURL;
	}
	/**
	 * Zwraca identyfikator kanału
	 * @return zwraca identyfikator kanału
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Sprawdza, czy ostatnia aktualizacja kanału się nie powiodła
	 * @return zwraca true, gdy ostatnia aktualizacja się nie powidła, false w przeciwnym wypadku
	 */
	public boolean isFail() {
		return this.isFail;
	}
	/**
	 * Ustawia informację o nieudanej aktualizacji
	 * @param isFail true, gdy się nie powiodło, false w przeciwnym wypadku
	 */
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	/**
	 * Zwraca tytuł kanału
	 * @return tytuł kanału lub null, gdy tytuł nie jest ustawiony
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * Ustawia tytuł kanału
	 * @param title tytuł kanału na który chcemy zmienić
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Zwraca odnośnik do strony z której został pobrany kanał
	 * @return odnośnik do strony lub null, gdy odnośnik nie jest ustawiony
	 */
	public String getLink() {
		return this.link;
	}
	/**
	 * Ustawia odnośnik do strony z której został pobrany kanał
	 * @param link odnościk do strony na który chcemy zmienić
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * Zwraca opis kanału
	 * @return opis kanału lub null, gdy opis nie jest ustawiony
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Usawia opis kanału
	 * @param description opis kanału na który chcemy zmienić
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/* nie wiem co to ma byc */
	public String getImageURL() {
		return this.imageURL;
	}
	/* nie wiem co to ma byc */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	/* nie wiem co to ma byc */
	public String getImageTitle() {
		return this.imageTitle;
	}
	/* nie wiem co to ma byc */
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}
	/* nie wiem co to ma byc */
	public String getImageLink() {
		return this.imageLink;
	}
	/* nie wiem co to ma byc */
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	/**
	 * Zwraca ilość nieprzeczytanych elementów w kanale
	 * @return ilość nieprzeczytanych elementów
	 */
	public int getUnreadItemsCount() {
		return this.unreadItemsCount;
	}
	/**
	 * Ustawia ilość nieprzeczytanych elementów w kanale
	 * @param unreadItemsCount - ilość nieprzeczytanych elementów
	 */
	public void setUnreadItemsCount(int unreadItemsCount) {
		this.unreadItemsCount = unreadItemsCount;
	}
}
