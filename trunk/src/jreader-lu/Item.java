package jreader;

import java.io.Serializable;
import java.util.Date;

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
	 * Unikalny identyfikator kanalu do ktorego nalezy element
	 */
    private String channelId;

    private boolean isRead;
    private String title;
    private String link;
    private String description;
    private String author;
    private Date date;
    /**
     * Konstruktor inicjuje podstawowe wartosci elementu
     * @param channelId - identykikator kanalu do ktorego nalezy element
     */
	public Item(String channelId) {
		this.creationDate = new Date();
		this.id = Id.newId();
		this.channelId = channelId;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public String getId() {
		return this.id;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public boolean isRead() {
		return this.isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
