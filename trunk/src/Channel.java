import java.util.ArrayList;
import java.util.List;


/**
 * Kanal RSS.
 */
class Channel {
  /** URL strony HTML zawierajacej kanal. */
  String siteURL;
  /** URL pliku XML z trescia kanalu. */
  String channelURL;
  /** Liczba nieprzeczytanych elementow. */
  int unreadItems;

  String title;
  String link;
  String description;
  /** Zawartosc elementu image (obrazek bedacy czescia opisu kanalu). */
  String imageURL;
  String imageTitle;
  String imageLink;
  /** Lista elementow (wiadomosci) kanalu. */
  List<Item> items = new ArrayList<Item>();

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
	}
      }
      if (!itemAlreadyExists) {
	items.add(updatedItem);
      }
    }
  }

  void addItem(Item item) { items.add(item); }

  String getTitle() { return title; }
  String getLink() { return link; }
  String getDescription() { return description; }

  String getImageURL() { return imageURL; }
  String getImageTitle() { return imageTitle; }
  String getImageLink() { return imageLink; }

  List<Item> getItems() { return items; }
}

