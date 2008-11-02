/**
 * Dane potrzebne do wyswietlenia podgladu elementu lub kanalu w GUI.
 */
class Preview {
  /** Czy pokazuje element, w przeciwnym wypadku informacje o kanale. */
  boolean showingItem;
  String title;
  String link;
  String description;
  String pubDate;
  /** Obrazek bedacy czescia opisu kanalu. */
  String imageURL;
  String imageTitle;
  String imageLink;

  Preview(Channel ch) {
    showingItem = false;
    this.title = ch.getTitle();
    this.link = ch.getLink();
    this.description = ch.getDescription();
    this.imageURL = ch.getImageURL();
    this.imageTitle = ch.getImageTitle();
    this.imageLink = ch.getImageLink();
  }

  Preview(Item item) {
    showingItem = true;
    this.title = item.getTitle();
    this.link = item.getLink();
    this.description = item.getDescription();
    this.pubDate = item.getPubDate();
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  String getPubDate() { return pubDate; }

  String getImageURL() { return imageURL; }
  String getImageTitle() { return imageTitle; }
  String getImageLink() { return imageLink; }

  boolean showingItem() { return showingItem; }
}

