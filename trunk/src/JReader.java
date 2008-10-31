import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Glowna klasa programu, przechowujaca obiekty przeznaczone do wyswietlania
 * w Graficznym Interfejsie Uzytkownika.
 */
class JReader {
  /** Lista subskrypcji do wyswietlenia w GUI. */
  static List<Channel> channels = new ArrayList<Channel>();
  /** Lista wiadomosci do wyswietlenia w GUI. */
  static List<Item> items = new ArrayList<Item>();
  /** Tresc wiadomosci lub informacje o kanale do wyswietlenia w GUI. */
  static Item preview;
  // TODO: Obiekt preview powinien miec wlasna klase, a nie Item, bo moga w nim
  // byc rowniez przechowywane szczegoly kanalu, a nie tylko elementow kanalu

  public static void main(String[] args) throws Exception {

    /*
     * Tekstowy interfejs uzytkownika, w celach testowych.
     */
    String command = new String();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Dostepne komendy:");
    System.out.print("show channels\t");
    System.out.print("show items\t");
    System.out.println("show preview");
    System.out.print("add channel\t");
    System.out.print("update channel\t");
    System.out.println("remove channel");
    System.out.print("select channel\t");
    System.out.println("select item");
    //System.out.println("select unread");
    //System.out.println("select all");
    System.out.println("quit\n");

    while (!(command.equals("quit"))) {
      command = in.readLine();
      if (command.equals("show channels")) {
	if (channels.size() == 0) {
	  System.out.println("Lista subskrypcji jest pusta.");
	} else {
	  Channel ch;
	  for (int i=0; i < channels.size(); i++) {
	    ch = channels.get(i);
	    System.out.println("Kanal " + (i+1) + ": " + ch.title);
	  }
	}
      } else if (command.equals("show items")) {
	if (items.size() == 0) {
	  System.out.println("Lista wiadomosci jest pusta.");
	} else {
	  for (int i=0; i < items.size(); i++) {
	    Item item = items.get(i);
	    System.out.println("Element " + (i+1) + ": " + item.title);
	    if (item.pubDate != null) {
	      System.out.println("Data publikacji: " + item.pubDate);
	    }
	  }
	}
      } else if (command.equals("show preview")) {
	if (preview == null) {
	  System.out.println("Nie wybrano zadnej wiadomosci.");
	} else {
	  System.out.println(preview.title);
	  if (preview.pubDate != null) {
	    System.out.println("Data publikacji: " + preview.pubDate);
	  }
	  System.out.println("Link: " + preview.link);
	  System.out.println("Opis: " + preview.description);
	}
      } else if (command.equals("add channel")) {
	System.out.print("Podaj adres URL kanalu: ");
	channels.add(ChannelFactory.getChannel(in.readLine()));
	System.out.println("Kanal zostal dodany");
      } else if (command.equals("update channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	channels.get(nr).update();
      } else if (command.equals("remove channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	channels.remove(nr);
      } else if (command.equals("select channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	items = channels.get(nr).getItems();
	preview = new Item(channels.get(nr));
      } else if (command.equals("select item")) {
	System.out.print("Podaj numer elementu: ");
	int nr = new Integer(in.readLine()) - 1;
	preview = items.get(nr);
      } else if (!command.equals("") && !command.equals("quit")) {
	System.out.println("Nieznane polecenie.");
      }
    }
    /*
     * Koniec tekstowego UI.
     */
  }
}


/**
 * Kanal RSS.
 */
class Channel {
  String URLString;
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

  Channel(String URLString) throws Exception {
    this.URLString = URLString;
  }

  /**
   * Parsuje zrodlo XML kanalu i uzupelnia informacje ogolne o kanale oraz
   * jego elementy.
   */
  void update() throws Exception {
    Channel ch = ChannelFactory.getChannel(URLString);
    this.title = ch.title;
    this.link = ch.link;
    this.description = ch.description;
    this.imageURL = ch.imageURL;
    this.imageTitle = ch.imageTitle;
    this.imageLink = ch.imageLink;
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

  List<Item> getItems() { return items; }
}


class Item {
  /** Czy dany element jest juz przeczytany. */
  boolean isRead;
  String title;
  String link;
  String description;
  /** Data publikacji elementu. */
  String pubDate;
  /** Unikalny identyfikator elementu. */
  String guid;

  Item() { }

  // TODO: po stworzeniu klasy Preview trzeba sie tego pozbyc
  Item(Channel ch) {
    this.title = ch.getTitle();
    this.link = ch.getLink();
    this.description = ch.getDescription();
  }

  /**
   * Porownuje dwa elementy (do sprawdzania, czy dany element jest nowy).
   */
  public boolean equals(Object obj) {
    Item it = (Item) obj;
    if (this.guid != null && it.guid != null) {
      if (this.guid.equals(it.guid)) {
	return true;
      }
    }
    if (this.title.equals(it.title) && this.pubDate.equals(it.pubDate)
	&& this.description.equals(it.description)) {
      return true;
      }
    return false;
  }
}


/**
 * Stad za pomoca metody getChannel mozna pobierac aktualny kanal.
 */
class ChannelFactory extends DefaultHandler {
  /** Zmienna, ktora zostanie zwrocona przez metode getChannel(). */
  static Channel channel;
  /** Tymczasowy element dla celow parsowania. */
  static Item item = new Item();

  boolean insideItem;
  boolean insideImage;
  String currentTag = "";

  public ChannelFactory() {
    super();
  }

  /** Zwraca aktualna postac kanalu o podanym adresie URL. */
  public static Channel getChannel(String URLString) throws Exception {
    channel = new Channel(URLString);
    URL url = new URL(URLString);

    XMLReader xr = XMLReaderFactory.createXMLReader();
    ChannelFactory handler = new ChannelFactory();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    xr.parse(new InputSource(url.openStream()));

    return channel;
  }


  /*
   * Metody oblugujace zdarzenia zwiazane z parsowaniem XML.
   */

  public void startDocument() {
    //System.out.println("Start document");
    insideItem = false;
    insideImage = false;
  }

  public void endDocument() {
    //System.out.println("End document\n");
    insideItem = false;
    insideImage = false;
  }

  public void startElement(String uri, String name,
			    String qName, Attributes atts) {
    if ("".equals(uri)) {
      //System.out.println("Start element: " + qName);
      currentTag = qName;
    } else {
      //System.out.println("Start element: {" + uri + "}" + name);
      currentTag = name;
    }
    if (currentTag.equals("item")) {
      insideItem = true;
      item = new Item();
    } else if (currentTag.equals("image")) {
      insideImage = true;
    }
  }

  public void endElement(String uri, String name, String qName) {
    String closingTag;

    if ("".equals(uri)) {
      //System.out.println("End element: " + qName);
      closingTag = qName;
    } else {
      //System.out.println("End element:   {" + uri + "}" + name);
      closingTag = name;
    }
    if (currentTag.equals(closingTag)) {
      currentTag = "";
    }
    if (closingTag.equals("item")) {
      insideItem = false;
      channel.addItem(item);
    } else if (closingTag.equals("image")) {
      insideImage = false;
    }
  }


  /**
   * Analiza tresci (body) znacznika.
   */
  public void characters(char ch[], int start, int length) {
    String chars = "";

    for (int i = start; i < start + length; i++) {
      switch (ch[i]) {
      case '\\':
	chars += "\\\\";
	break;
      case '"':
	chars += "\\\"";
	break;
      default:
	chars += ch[i];
	break;
      }
    }
    //System.out.println("Characters: " + chars);

    if (insideImage) {
      if (currentTag.equals("url")) {
	channel.imageURL = chars;
      } else if (currentTag.equals("title")) {
	channel.imageTitle = chars;
      } else if (currentTag.equals("link")) {
	channel.imageLink = chars;
      }
    } else if (!insideItem) {
      if (currentTag.equals("title")) {
	channel.title = chars;
      } else if (currentTag.equals("link")) {
	channel.link = chars;
      } else if (currentTag.equals("description")) {
	channel.description = chars;
      }
    } else {
      if (currentTag.equals("title")) {
	item.title = chars;
      } else if (currentTag.equals("link")) {
	item.link = chars;
      } else if (currentTag.equals("description")) {
	item.description = chars;
      } else if (currentTag.equals("pubDate")) {
	item.pubDate = chars;
      } else if (currentTag.equals("guid")) {
	item.guid = chars;
      }
    }
  }
}

