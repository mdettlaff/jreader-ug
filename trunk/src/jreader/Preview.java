package jreader;

import java.util.Date;

/**
 * Dane potrzebne do wyświetlenia podglądu elementu lub kanału w GUI.
 */
public class Preview {
	/** Czy pokazuje element, w przeciwnym wypadku informacje o kanale. */
	private boolean showingItem;
	private String title;
	private String link;
	private Date date;
	private String description;
	private String author;
	private String source;
	/** Tytuł kanału, z którego pochodzi element. */
	private String channelTitle;
	/** Obrazek będący częścią opisu kanału. */
	private String imageURL;
	private String imageTitle;
	private String imageLink;

	/**
	 * Tworzy nowy podgląd kanału.
	 */
	public Preview(Channel ch) {
		showingItem = false;
		this.title = ch.getTitle();
		this.link = ch.getLink();
		this.description = ch.getDescription();
		this.imageURL = ch.getImageURL();
		this.imageTitle = ch.getImageTitle();
		this.imageLink = ch.getImageLink();
		this.source = ch.getChannelURL();
	}

	/**
	 * Tworzy nowy podgląd elementu.
	 */
	public Preview(Item item) {
		showingItem = true;
		this.title = item.getTitle();
		this.link = item.getLink();
		this.date = item.getDate();
		this.description = item.getDescription();
		this.author = item.getAuthor();
		this.source = null;
		this.channelTitle = JReader.getChannel(item.getChannelId()).getTitle();
	}

	public String getTitle() { return title; }
	public String getLink() { return link; }
	public String getAuthor() { return author; }
	/**
	 * Zwraca URL źródła XML kanału lub <code>null</code> dla wiadomości.
	 */
	public String getSource() { return source; }
	/**
	 * Zwraca tytuł kanału, z którego pochodzi dany element.
	 */
	public String getChannelTitle() { return channelTitle; }
	/**
	 * Zwraca datę publikacji elementu lub <code>null</code> dla kanału.
	 */
	public Date getDate() { return date; }

	/**
	 * Zwraca właściwą treść wiadomości lub opis kanału.
	 *
	 * @return Opis, w którym mogą pojawić się znaczniki HTML.
	 */
	public String getHTML() {
		if (description == null) {
			return "Brak opisu.";
		}
		if (showingItem) {
			return description;
		} else { // konstruujemy opis kanału, z obrazkiem jeśli istnieje
			String HTML = "";
			if (imageURL != null) {
				if (imageTitle != null) {
					HTML += "<img alt=\"" + imageTitle + "\" align=\"right\"" +
						" src=\"" + imageURL + "\">\n";
				} else {
					HTML += "<img align=\"right\"" + " src=\"" + imageURL + "\">\n";
				}
				if (imageLink != null) {
					HTML = "<a href=\"" + imageLink + "\">\n" + HTML + "</a>\n";
				}
			}
			HTML += description;
			return HTML;
		}
	}
}

