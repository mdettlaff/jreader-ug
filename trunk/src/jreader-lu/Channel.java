package jreader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Lista ID elementow nalezace do kanalu
	 */
	private List<String> items;
    /**
     * Tagi; jeśli kanał nie jest oznaczony tagami, lista jest pusta.
     */
    private List<String> tags;
	/**
	 * URL pliku XML z treścią kanału.
	 */
    private String channelURL;
    /**
     * Unikalny identyfikator kanalu
     */
    private String id;
    /** 
     * true, jeśli ostatnia próba aktualizowania kanału nie powiodła się.
     */
    private boolean isFail;

    private String title;
    private String link;
    private String description;
    private String imageURL;
    private String imageTitle;
    private String imageLink;
	/**
	 * Konstruktor inicjuje podstawowe wartosci kanalu
	 * @param channelURL - URL kanalu
	 */
	public Channel(String channelURL) {
		this.id = channelURL;
		this.items = new ArrayList<String>();
		this.tags = new ArrayList<String>();
		this.channelURL = channelURL;
		this.isFail = false;
	}
	/**
	 * Metoda dodaje elementu (identyfikator) do kanalu
	 * @param itemId - idetyfikator elementu
	 */
	public void addItem(String itemId) {
		this.items.add(itemId);
	}
	/**
	 * Metoda zwraca liste wszystkich elementow (identyfikatory) w kanale
	 * @return zwraca liste elementow (identyfikatory)
	 */
	public List<String> getItems() {
		return this.items;
	}
	/**
	 * Metoda ustawia elementy, ktore maja nalezec do kanalu
	 * @param items - lista elementow
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
	/**
	 * Metoda usuwa element o podanym identyfikatorze
	 * @param itemId - identyfikator elementu
	 */
	public void removeItem(String itemId) {
		this.items.remove(itemId);
	}
	/**
	 * Metoda dodaje tag do kanalu
	 * @param tag - dodawany tag
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	/**
	 * Metoda zwraca liste wszystkich tagow
	 * @return lista tagow,
	 */
	public List<String> getTags() {
		return this.tags;
	}
	/**
	 * Metoda ustawia tagi
	 * @param tags - lista tagow
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	/**
	 * Metoda usuwa tag
	 * @param tag - usuwany tag
	 */
	public void removeTag(String tag) {
		this.tags.remove(tag);
	}
	/**
	 * Metoda zwraca URL kanalu
	 * @return zwraca URL kanalu
	 */
	public String getChannelURL() {
		return this.channelURL;
	}
	/**
	 * Metoda zwraca identyfikator kanalu
	 * @return zwraca identyfikator kanalu
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Metoda sprawdza czy ostatnia aktualizacja kanalu sie nie powiodła
	 * @return zwraca true gdy ostatnia aktualizacja sie nie powidła, false w przeciwnym wypadku
	 */
	public boolean isFail() {
		return this.isFail;
	}
	/**
	 * Metoda ustawi stan kanalu podczas aktualizacji
	 * @param isFail true, gdy sie nie powiodło, false w przeciwnym wypadku
	 */
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	/**
	 * Metoda zwraca tytul kanalu
	 * @return tytul kanalu lub null, gdy tytul nie jest ustawiony
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * Metoda ustawia tytul kanalu
	 * @param title - tytul kanalu na ktory chcemy zmienic
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Metoda zwraca odnosnik do strony z ktorej zostal pobrany kanal
	 * @return odnosnik do strony lub null, gdy odnosnik nie jest ustawiony
	 */
	public String getLink() {
		return this.link;
	}
	/**
	 * Metoda ustawia odnosnik do strony z ktorej zostal pobrany kanal
	 * @param link - odnoscik do strony na ktory chcemy zmienic
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * Metoda zwraca opis kanalu
	 * @return opis kanalu lub null, gdy opis nie jest ustawiony
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Metoda usawia opis kanalu
	 * @param description - opis kanalu na ktory chcemy zmienic
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
}
