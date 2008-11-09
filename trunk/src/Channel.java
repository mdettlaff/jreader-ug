import java.util.ArrayList;
import java.util.List;


/**
 * Kanal RSS.
 */
class Channel {
  /** URL strony HTML zawierajacej kanal. */
  private String siteURL;
  /** URL pliku XML z trescia kanalu. */
  private String channelURL;
  /** Liczba nieprzeczytanych elementow. */
  private int unreadItems;

  private String title;
  private String link;
  private String description;
  /** Zawartosc elementu image (obrazek bedacy czescia opisu kanalu). */
  private String imageURL;
  private String imageTitle;
  private String imageLink;
  /** Lista elementow (wiadomosci) kanalu. */
  private List<Item> items = new ArrayList<Item>();

  Channel(String channelURL) throws Exception {
    this.channelURL = channelURL;
  }

  /**
   * Parsuje zrodlo XML kanalu i uzupelnia informacje ogolne o kanale oraz
   * jego elementy.
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
      for (Item item : items) {
	if (updatedItem.equals(item)) {
	  itemAlreadyExists = true;
	  break;
	}
      }
      if (!itemAlreadyExists) {
	items.add(updatedItem);
      }
    }
  }

  void addItem(Item item) { items.add(item); }

  void markAllAsRead() {
    for (Item item : items) {
      item.markAsRead();
    }
    unreadItems = 0;
  }

  void markAllAsUnread() {
    for (Item item : items) {
      item.markAsUnread();
    }
    unreadItems = items.size();
  }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }
  List<Item> getItems() { return items; }

  String getImageURL() { return imageURL; }
  String getImageTitle() { return imageTitle; }
  String getImageLink() { return imageLink; }

  void setTitle(String title) { this.title = title; }
  void setLink(String link) { this.link = link; }
  void setDescription(String description) { this.description = description; }
  void setImageURL(String imageURL) { this.imageURL = imageURL; }
  void setImageTitle(String imageTitle) { this.imageTitle = imageTitle; }
  void setImageLink(String imageLink) { this.imageLink = imageLink; }
}

