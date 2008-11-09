import java.util.Date;

/**
 * Dane potrzebne do wyswietlenia podgladu elementu lub kanalu w GUI.
 */
class Preview {
  /** Czy pokazuje element, w przeciwnym wypadku informacje o kanale. */
  private boolean showingItem;
  private String title;
  private String link;
  private String description;
  private Date date;
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
  }

  Preview(Item item) {
    showingItem = true;
    this.title = item.getTitle();
    this.link = item.getLink();
    this.description = item.getDescription();
    this.date = item.getDate();
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  Date getDate() { return date; }

  String getImageURL() { return imageURL; }
  String getImageTitle() { return imageTitle; }
  String getImageLink() { return imageLink; }

  boolean isShowingItem() { return showingItem; }
}

