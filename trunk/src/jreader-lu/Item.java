package jreader;

import java.io.Serializable;
import java.util.Date;
/**
 * Przechowuje informacje o elemencie
 */
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Data utworzenia obiektu
	 */
	private Date creationDate;
	/**
	 * Unikalny idetyfikator elementu
	 */
	private String id;
	/**
	 * Unikalny identyfikator kanału do którego należy element
	 */
	private String channelId;
	/**
	 * Informacja o tym czy element został przeczytany
	 */
	private boolean isRead;
	/**
	 * Tytuł elementu
	 */
	private String title;
	/**
	 * Odnośnik do źródła skąd pochodzi element
	 */
	private String link;
	/**
	 * Opis elementu
	 */
	private String description;
	/**
	 * Autor elementu
	 */
	private String author;
	/**
	 * Data napisania elementu
	 */
	private Date date;
    /**
     * Inicjuje podstawowe wartości elementu
     * @param itemId identykikator elementu
     * @param channelId identykikator kanału do którego należy element
     */
	public Item(String itemId, String channelId) {
		this.creationDate = new Date();
		this.id = itemId;
		this.channelId = channelId;
	}
	/**
	 * Zwraca czas utworzenia obiektu
	 * @return czas utworzenia elementu
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}
	/**
	 * Zwraca identyfikator elementu
	 * @return identyfikator elementu
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Zwraca identyfikator kanału do którego nalezy element
	 * @return identyfikator kanału
	 */
	public String getChannelId() {
		return this.channelId;
	}
	/**
	 * Sprawdza czy element został przeczytany
	 * @return true, gdy element został przeczytany lub false w przeciwnym wypadku
	 */
	public boolean isRead() {
		return this.isRead;
	}
	/**
	 * Ustawia status elementu na przeczytany lub nieprzeczytany
	 * @param isRead przeczytany, gdy true lub false jeśli nieprzeczytany
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	/**
	 * Zwraca tytuł elementu
	 * @return tytuł elementu lub null, gdy tytuł nie jest ustawiony
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * Ustawia tytuł elementu
	 * @param title tytuł elementu na który chcemy zmienić 
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Zwraca odnośnik
	 * @return odnośnik lub null, gdy odnośnik nie jest ustawiony
	 */
	public String getLink() {
		return this.link;
	}
	/**
	 * Ustawia odnośnik
	 * @param link odnośnik na który chcemy zmienić
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * Zwraca opis elementu
	 * @return opis elementu lub null, gdy opis nie jest ustawiony
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Ustawia opis elementu
	 * @param description opis na który chcemy zmienić
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Zwraca autora elementu
	 * @return autor elementu lub null, gdy autor nie jest ustawiony
	 */
	public String getAuthor() {
		return this.author;
	}
	/**
	 * Ustawia autora elementu
	 * @param author autor na który chcemy zmienić
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * Zwraca datę napisania elementu
	 * @return data napisania elementu lub null, gdy data nie jest ustawiona
	 */
	public Date getDate() {
		return this.date;
	}
	/**
	 * Ustawia datę napisania elementu
	 * @param date data na którą chcemy zmienić
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
