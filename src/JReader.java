import java.util.ArrayList;
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
  static ArrayList<Channel> channels = new ArrayList<Channel>();
  /** Lista wiadomosci do wyswietlenia w GUI. */
  static ArrayList<Item> items = new ArrayList<Item>();
  /** Tresc wiadomosci do wyswietlenia w GUI. */
  static Item itemPreview;

  public static void main(String[] args) throws Exception {
    System.out.println("Tworzenie domyslnego kanalu "
	+ "(http://inf.univ.gda.pl:8001/rss/jp2.xml)");
    channels.add(ChannelFactory.getChannel(
	  "http://inf.univ.gda.pl:8001/rss/jp2.xml"));

    textUI();
  }

  /**
   * Interakcja z uzytkownikiem za pomoca komend tekstowych, w celach
   * testowych.
   */
  static void textUI() throws Exception {
    String command = new String();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Dostepne komendy:");
    System.out.println("show channels");
    System.out.println("show items");
    System.out.println("show preview");
    //System.out.println("add channel <URL>");
    System.out.println("update channel");
    //System.out.println("remove channel");
    //System.out.println("select channel");
    //System.out.println("select item");
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
	    System.out.println("Link: " + ch.link);
	    System.out.println("Opis: " + ch.description);
	  }
	}
      } else if (command.equals("show items")) {
	if (items.size() == 0) {
	  System.out.println("Lista wiadomosci jest pusta.");
	}
      } else if (command.equals("show preview")) {
	if (itemPreview == null) {
	  System.out.println("Nie wybrano zadnej wiadomosci.");
	}
      } else if (command.equals("update channel")) {
	System.out.print("Podaj numer kanalu: ");
	Integer nr = new Integer(in.readLine()) - 1;
	channels.get(nr).update();
      } else if (!command.equals("") && !command.equals("quit")) {
	System.out.println("Nieznane polecenie.");
      }
    }
  }
}


class Channel {
  String URLString;
  /** Liczba nieprzeczytanych elementow. */
  int unreadItems;

  String title;
  String link;
  String description;
  /* Zawartosc elementu image (obrazek bedacy czescia opisu kanalu) */
  String imageURL;
  String imageTitle;
  String imageLink;
  /* Lista elementow (wiadomosci) kanalu */
  ArrayList<Item> items = new ArrayList<Item>();

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
    // TODO: dodawanie nowych elementow
  }
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

    XMLReader xr = XMLReaderFactory.createXMLReader();
    ChannelFactory handler = new ChannelFactory();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    xr.parse(new InputSource(new URL(URLString).openStream()));

    return channel;
  }


  /*
   * Metody oblugujace zdarzenia zwiazane z parsowaniem XML.
   */

  public void startDocument() {
    System.out.println("Start document");
    insideItem = false;
    insideImage = false;
  }

  public void endDocument() {
    System.out.println("End document\n");
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
    } else if (closingTag.equals("image")) {
      insideImage = false;
    }
  }


  /**
   * Analiza tresci znacznika.
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
      // TODO: parsowanie elementow
    }
  }
}

