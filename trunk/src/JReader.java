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
  static ArrayList<RSSChannel> subscriptions = new ArrayList<RSSChannel>();

  public static void main(String[] args) throws Exception {
    textUI();
  }

  static void textUI() throws Exception {
    String command = new String();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Tworzenie domyslnego kanalu");
    subscriptions.add(
	new RSSChannel("http://inf.univ.gda.pl:8001/rss/jp2.xml"));

    while (!(command.equals("quit"))) {
      command = in.readLine();
      if (command.equals("show channel source")) {
	subscriptions.get(0).printSource();
      }
    }
  }
}


class RSSChannel {
  int unreadItems;
  URL url;
  String source = new String();

  String title;
  String link;
  String description;
  ArrayList<RSSItem> items = new ArrayList<RSSItem>();

  RSSChannel(String URLString) throws Exception {
    url = new URL(URLString);
    updateSource();
    updateChannel();
    ChannelFactory.getChannel(url);
  }

  void updateSource() throws Exception {
    BufferedReader in = new BufferedReader(
			    new InputStreamReader(
			    url.openStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      this.source += inputLine + '\n';
    in.close();
  }

  /**
   * Parsuje zrodlo XML kanalu i uzupelnia informacje ogolne o kanale oraz
   * jego elementy.
   */
  void updateChannel() {
  }

  void printSource() {
    System.out.println(source);
  }
}


class RSSItem {
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

  public static void getChannel(URL url) throws Exception {
    XMLReader xr = XMLReaderFactory.createXMLReader();
    ChannelFactory handler = new ChannelFactory();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    xr.parse(new InputSource(url.openStream()));
  }

  public ChannelFactory () {
    super();
  }


  // Event handlers

  public void startDocument() {
    System.out.println("Start document");
  }

  public void endDocument() {
    System.out.println("End document");
  }

  public void startElement(String uri, String name,
			    String qName, Attributes atts) {
    if ("".equals (uri))
      System.out.println("Start element: " + qName);
    else
      System.out.println("Start element: {" + uri + "}" + name);
  }


  public void endElement(String uri, String name, String qName)
  {
    if ("".equals (uri))
      System.out.println("End element: " + qName);
    else
      System.out.println("End element:   {" + uri + "}" + name);
  }


  public void characters(char ch[], int start, int length) {
    System.out.print("Characters:    \"");
    for (int i = start; i < start + length; i++) {
      switch (ch[i]) {
      case '\\':
	System.out.print("\\\\");
	break;
      case '"':
	System.out.print("\\\"");
	break;
      case '\n':
	System.out.print("\\n");
	break;
      case '\r':
	System.out.print("\\r");
	break;
      case '\t':
	System.out.print("\\t");
	break;
      default:
	System.out.print(ch[i]);
	break;
      }
    }
    System.out.print("\"\n");
  }
}

