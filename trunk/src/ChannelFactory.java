import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

  /**
   * Definicja formatu daty stosowanego w kanalach RSS (standard RFC 822)
   */
  DateFormat RSSDateFormat =
    new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
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
     * URL pliku XML z trescia kanalu, ktory musimy znalezc na podstawie
     * zrodla HTML siteURL.
     */
    String channelURL = "";

    // dopisujemy na poczatku protokol (http), jesli go nie ma
    if (!siteURL.startsWith("http://")) {
      siteURL = "http://" + siteURL;
    }
    URL url = new URL(siteURL);
    BufferedReader in = new BufferedReader(
			    new InputStreamReader(
			    url.openStream()));
    // najpierw sprawdzamy czy podany adres jest adresem do pliku XML
    if (in.readLine().contains("xml version")
       	&& in.readLine().contains("rss")) {
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

    channel.markAllAsUnread();
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
	channel.setImageURL(chars);
      } else if (currentTag.equals("title")) {
	channel.setImageTitle(chars);
      } else if (currentTag.equals("link")) {
	channel.setImageLink(chars);
      }
    } else if (!insideItem) {
      if (currentTag.equals("title")) {
	channel.setTitle(chars);
      } else if (currentTag.equals("link")) {
	channel.setLink(chars);
      } else if (currentTag.equals("description")) {
	channel.setDescription(chars);
      }
    } else {
      if (currentTag.equals("title")) {
	item.setTitle(chars);
      } else if (currentTag.equals("link")) {
	item.setLink(chars);
      } else if (currentTag.equals("description")) {
	item.setDescription(chars);
      } else if (currentTag.equals("pubDate") || currentTag.equals("date")) {
	try {
	  Date parsedDate = RSSDateFormat.parse(chars);
	  item.setDate(parsedDate);
	} catch (ParseException pe) { }
      } else if (currentTag.equals("guid")) {
	item.setGuid(chars);
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
      // jesli data nie byla okreslona lub parsowanie nie powiodlo sie,
      // stosujemy biezaca date
      if (item.getDate() == null) {
	// TODO: ponizsze 3 linijki ustawiaja przykladowa date zamiast
	// biezacej, dla celow testowych. Docelowo zmienic na:
	//item.setDate(new Date());
	try {
	  item.setDate(RSSDateFormat.parse("Sun, 9 Nov 2008 19:30:00 +0100"));
       	} catch (ParseException pe) { }
      }
      channel.addItem(item);
    } else if (closingTag.equals("image")) {
      insideImage = false;
    }
  }

  /**
   * Analiza tresci znacznika.
   * UWAGA: Cala tresc danego znacznika moze byc podzielona na kilka zdarzen
   * 'characters' - w szczegolnosci, kazda linia jest innym zdarzeniem.
   */
  public void characters(char ch[], int start, int length) {
    for (int i = start; i < start + length; i++) {
      chars += ch[i];
    }
  }
}

