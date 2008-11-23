package jreader;

import java.util.Date;

/**
 * Dane potrzebne do wyswietlenia podgladu elementu lub kanalu w GUI.
 */
class Preview {
	/** Czy pokazuje element, w przeciwnym wypadku informacje o kanale. */
	private boolean showingItem;
	private String title;
	private String link;
	private Date date;
	private String description;
	private String author;
	private String source;
	/** Obrazek bedacy czescia opisu kanalu. */
	private String imageURL;
	private String imageTitle;
	private String imageLink;

	Preview(Channel ch) {
		showingItem = false;
		this.title = ch.getTitle();
		this.link = ch.getLink();
		this.description = ch.getDescription();
		this.imageURL = ch.getImageURL();
		this.imageTitle = ch.getImageTitle();
		this.imageLink = ch.getImageLink();
		this.source = ch.getChannelURL().replace("\n", "");
	}

	Preview(Item item) {
		showingItem = true;
		this.title = item.getTitle();
		this.link = item.getLink();
		this.date = item.getDate();
		this.description = item.getDescription();
		this.author = item.getAuthor();
		this.source = null;
	}

	String getTitle() { return title; }
	String getLink() { return link; }
	String getAuthor() { return author; }
	String getSource() { return source; }
	Date getDate() { return date; }

	String getHTML() {
		if (description == null) {
			return "Brak opisu.";
		}
		if (showingItem) {
			return description;
		} else { // konstruujemy opis kanalu, z obrazkiem jesli istnieje
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

