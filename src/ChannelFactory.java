import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Stad za pomoca metod getChannelFromSite i getChannelFromXML mozna pobierac
 * nowo stworzony kanal lub aktualna tresc kanalu.
 */
class ChannelFactory extends DefaultHandler {
  /** Zmienna, ktora zostanie zwrocona przez metode getChannelFromXML(). */
  static Channel channel;
  /** Tymczasowy element dla celow parsowania. */
  static Item item = new Item();

  boolean insideItem;
  boolean insideImage;
  String currentTag = "";

  public ChannelFactory() {
    super();
  }

  /**
   * Szuka adresu pliku XML z trescia kanalu w zrodle strony HTML i zwraca
   * nowy kanal o tresci z tego pliku XML.
   */
  public static Channel getChannelFromSite(String siteURL) throws Exception {
    /**
     * Znajduje URL pliku XML z trescia kanalu, ktory musimy znalezc
     * na podstawie zrodla HTML siteURL.
     */
    String channelURL = "";

    URL url = new URL(siteURL);
    BufferedReader in = new BufferedReader(
			    new InputStreamReader(
			    url.openStream()));
    // najpierw sprawdzamy czy podany adres jest adresem do pliku XML
    if (in.readLine().contains("xml version")
       	&& in.readLine().contains("rss version")) {
      channelURL = siteURL;
    }

    String inputLine;
    // jesli nie podano adresu do pliku XML tylko do strony glownej, to czytamy
    // plik HTML dopoki nie znajdziemy odnosnika do pliku XML kanalu
    if ("".equals(channelURL)) {
      while ((inputLine = in.readLine()) != null) {
	if (inputLine.matches(".*type=\"application/rss.xml\".*")) {
	  channelURL = inputLine.replaceAll("^.*href=\"", "");
	  channelURL = channelURL.replaceAll("\".*", "");
	  // sklejemy link strony i kanalu w razie potrzeby
	  if (channelURL.charAt(0) == '/') {
	    if (siteURL.charAt(siteURL.length()-1) == '/') {
	      channelURL = siteURL + channelURL.substring(1);
	    } else {
	      channelURL = siteURL + channelURL;
	    }
	  }
	  break;
	}
      }
      in.close();
    }

    return getChannelFromXML(channelURL);
  }

  /** Zwraca aktualna postac kanalu o podanym adresie URL. */
  public static Channel getChannelFromXML(String channelURL) throws Exception {
    channel = new Channel(channelURL);
    URL url = new URL(channelURL);

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

  /** Tresc (body) aktualnie parsowanego znacznika. */
  String chars;

  public void startDocument() {
    insideItem = false;
    insideImage = false;
  }

  public void endDocument() {
    insideItem = false;
    insideImage = false;
  }

  public void startElement(String uri, String name,
			    String qName, Attributes atts) {
    chars = "";

    if ("".equals(uri)) {
      currentTag = qName;
    } else {
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

    /*
     * Tutaj "wyciagamy" wlasciwa tresc ze znacznikow i wpisujemy w struktury
     */
    chars = chars.trim();
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

    if ("".equals(uri)) {
      closingTag = qName;
    } else {
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
   * UWAGA: Cala tresc danego znacznika moze byc podzielona na kilka zdarzen
   * 'characters' - w szczegolnosci, kazda linia jest innym zdarzeniem.
   */
  public void characters(char ch[], int start, int length) {
    for (int i = start; i < start + length; i++) {
      chars += ch[i];
    }
  }
}

