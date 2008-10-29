import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


class JReader {
  /** Lista subskrypcji do wyswietlenia w GUI. */
  static ArrayList<Channel> channels = new ArrayList<Channel>();
  /** Lista wiadomosci do wyswietlenia w GUI. */
  static ArrayList<Item> items = new ArrayList<Item>();
  /** Tresc wiadomosci do wyswietlenia w GUI. */
  static Item itemPreview;

  public static void main(String[] args) throws Exception {
    textUI();
  }

  /**
   * Interakcja z uzytkownikiem za pomoca komend tekstowych, w celach
   * testowych.
   */
  static void textUI() throws Exception {
    String command = new String();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    // TODO
    System.out.println("Tworzenie domyslnego kanalu (http://inf.univ.gda.pl:8001/rss/jp2.xml)");
    Channel ch = new Channel("http://inf.univ.gda.pl:8001/rss/jp2.xml");
    ch.update();
    channels.add(ch);

    System.out.println("Dostepne komendy:");
    System.out.println("show channels");
    System.out.println("show items");
    System.out.println("show preview");
    System.out.println("quit\n");

    while (!(command.equals("quit"))) {
      command = in.readLine();
      /*if (command.equals("show channel source")) {
	channels.get(0).printSource();
      } else */if (command.equals("show channels")) {
	if (channels.size() == 0) {
	  System.out.println("Lista subskrypcji jest pusta.");
	} else {
	  for (Channel chan : channels) {
	    System.out.println("Tytul: " + chan.title);
	    System.out.println(chan.description);
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
      }
    }
  }
}


class Channel {
  int unreadItems;
  String URLString;
  // deprecated
  //String source = new String();

  String title;
  String link;
  String description;
  ArrayList<Item> items = new ArrayList<Item>();

  Channel(String URLString) throws Exception {
    this.URLString = URLString;
    //this.updateSource();
  }

  // deprecated
  /*void updateSource() throws Exception {
    BufferedReader in = new BufferedReader(
			    new InputStreamReader(
			    new URL(URLString).openStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      this.source += inputLine + '\n';
    in.close();
  }*/

  /**
   * Parsuje zrodlo XML kanalu i uzupelnia informacje ogolne o kanale oraz
   * jego elementy.
   */
  void update() throws Exception {
    Channel ch = ChannelFactory.getChannel(URLString);
    this.title = ch.title;
    this.description = ch.description;
  }

  /*void printSource() {
    System.out.println(source);
  }*/
}


class Item {
  boolean isRead;
  String title;
  String link;
  String description;
  String pubDate;

  /**
   * Porownuje dwa elementy (do sprawdzania, czy dany element jest nowy).
   */
  boolean equals() {
    return false;
  }
}


class ChannelFactory extends DefaultHandler {
  /** Zmienna, ktora zostanie zwrocona przez metode getChannel(). */
  static Channel channel;
  /** Tymczasowy element dla celow parsowania. */
  static Item item = new Item();

  boolean insideItem;
  String currentTag = "";


  public static Channel getChannel(String URLString) throws Exception {
    channel = new Channel(URLString);
    XMLReader xr = XMLReaderFactory.createXMLReader();
    ChannelFactory handler = new ChannelFactory();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    xr.parse(new InputSource(new URL(URLString).openStream()));

    return channel;
  }

  public ChannelFactory () {
    super();
  }


  /*
   * Metody oblugujace zdarzenia zwiazane z parsowaniem XML.
   */

  public void startDocument() {
    System.out.println("Start document");
    insideItem = false;
  }

  public void endDocument() {
    System.out.println("End document\n");
  }

  public void startElement(String uri, String name,
			    String qName, Attributes atts) {
    if ("".equals (uri)) {
      //System.out.println("Start element: " + qName);
      currentTag = qName;
      if (currentTag.equals("item")) {
	insideItem = true;
      }
    } else {
      //System.out.println("Start element: {" + uri + "}" + name);
      currentTag = name;
      if (currentTag.equals("item")) {
	insideItem = true;
      }
    }
  }


  public void endElement(String uri, String name, String qName)
  {
    if ("".equals (uri)) {
      //System.out.println("End element: " + qName);
      if (currentTag.equals(qName)) {
	currentTag = "";
      }
    } else {
      //System.out.println("End element:   {" + uri + "}" + name);
      if (currentTag.equals(name)) {
	currentTag = "";
      }
    }
  }


  public void characters(char ch[], int start, int length) {
    String chars = "";

    //System.out.print("Characters:    \"");
    for (int i = start; i < start + length; i++) {
      switch (ch[i]) {
      case '\\':
	chars += "\\\\";
	break;
      case '"':
	chars += "\\\"";
	break;
      case '\n':
	chars += "\\n";
	break;
      case '\r':
	chars += "\\r";
	break;
      case '\t':
	chars += "\\t";
	break;
      default:
	chars += ch[i];
	break;
      }
    }
    //System.out.print(chars);
    //System.out.print("\"\n");

    if (!insideItem) {
      if (currentTag.equals("title")) {
	channel.title = chars;
      } else if (currentTag.equals("description")) {
	channel.description = chars;
      }
    } else {
      // TODO: parsowanie elementow
    }
  }
}

