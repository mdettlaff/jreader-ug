package jreader;

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
 * Stąd można pobrać nowo utworzony kanał lub aktualną treść kanału.
 */
public class ChannelFactory extends DefaultHandler {
	/** Zmienna, która zostanie zwrócona przez metodę getChannelFromXML(). */
	private static Channel channel;
	/** Tymczasowy element dla celów parsowania. */
	private static Item item = new Item();

	/**
	 * Definicja standardowego formatu daty stosowanego w kanałach RSS (RFC 822).
	 */
	private DateFormat RSSDateFormat =
		new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	/*
	 * Inne formaty daty.
	 */
	/** Format daty używany np&#46; w userfriendly.org. */
	private DateFormat DateFormat1 =
		new SimpleDateFormat("E, dd MMM yyyy HH:mm z", Locale.ENGLISH);
	/** Format daty uzywany np&#46; w slashdot.org. */
	private DateFormat DateFormat2 =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

	private boolean insideItem;
	private boolean insideImage;
	private boolean insideTextinput;
	private String currentTag = "";
	/**
	 * Licznik, który pozwoli na rozróżnienie dat elementów, dla których data
	 * nie jest podana w źródle XML i w konsekwencji posortowanie ich według
	 * kolejności w jakiej wystepują w źródle.
	 */
	private int counter;
	/**
	 * Data parsowania danego kanału.
	 */
	private long currentUnixTime;

	public ChannelFactory() {
		super();
	}

	/**
	 * Szuka adresu pliku XML z treścią kanału w źródle strony HTML znajdującej
	 * się pod podanym adresem.
	 *
	 * @return Nowy kanał o treści ze znalezionego pliku XML.
	 * @throws LinkNotFoundException jeśli na podanej stronie nie znaleziono
	 *         żadnych odnośników do kanałów.
	 * @throws MalformedURLException jeśli podany przez użytkownika adres
	 *         nie jest prawidłowym adresem URL.
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML
	 * @throws Exception różne wyjątki związane z niepowodzeniem pobierania
	 *         pliku.
	 */
	public static Channel getChannelFromSite(String siteURL) throws Exception {
		/**
		 * URL pliku XML z treścią kanału, który musimy znaleźć na podstawie
		 * źródła HTML znajdującego się pod adresem siteURL.
		 */
		String channelURL = "";

		// dopisujemy na początku protokół (http), jeśli go nie ma
		if (!siteURL.startsWith("http://")) {
			siteURL = "http://" + siteURL;
		}
		URL url = new URL(siteURL);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
					url.openStream()));
		// najpierw sprawdzamy czy podany adres jest adresem do pliku XML,
		// na podstawie 3 pierwszych linii
		String[] lines = new String[3];
		for (int i=0; i < lines.length; i++) {
			if ((lines[i] = in.readLine()) == null) {
				lines[i] = "";
				break;
			}
		}
		if (lines[0].contains("xml version")) {
			// wykrywanie plików źródłowych rss
			if (lines[0].contains("rss") || lines[1].contains("rss")) {
				channelURL = siteURL;
			}
			// wykrywanie plików źródłowych Atom
			if (lines[0].contains("Atom") || lines[1].contains("Atom")
					|| lines[2].contains("Atom")) {
				channelURL = siteURL;
			}
		}
		// czytamy z adresu od nowa, żeby nie pominąć początkowych linii
		in.close();
		in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;
		// jeśli nie podano adresu do pliku XML tylko do strony głównej, to czytamy
		// plik HTML dopóki nie znajdziemy odnośnika do pliku XML kanału
		if ("".equals(channelURL)) {
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("type=\"application/rss+xml\"")
						|| inputLine.contains("type=\"application/atom+xml\"")) {
					// jeśli linia nie zawiera odnośnika href (np. na thedailywtf.com),
					// tylko jest on przeniesiony do dalszej linii, szukamy go dalej
					if (!inputLine.contains("href=")) {
						while ((inputLine = in.readLine()) != null) {
							if (inputLine.contains("href=")) {
								break;
							}
						}
					}
					// rozbijamy na mniejsze linie, mniej problematyczne
					inputLine = inputLine.replace(">", ">\n");
					String[] smallLines = inputLine.split("\n");
					for (String smallLine : smallLines) {
						if (smallLine.contains("type=\"application/rss+xml\"")
								|| smallLine.contains("type=\"application/atom+xml\"")) {
							inputLine = smallLine;
							break;
						}
					}
					channelURL = inputLine.replaceAll("^.*href=\"", "");
					channelURL = channelURL.replaceAll("\".*", "");
					// sklejemy link strony i kanału w razie potrzeby
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
			if ("".equals(channelURL)) { // jeśli nie znaleziono odnośnika do kanału
				throw new LinkNotFoundException();
			}
		}

		return getChannelFromXML(channelURL.trim());
	}

	/**
	 * Pobiera i parsuje kanał o źródle w podanym adresie URL.
	 *
	 * @return Aktualna postać kanału o podanym adresie URL.
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML
	 * @throws Exception różne wyjątki związane z niepowodzeniem pobierania
	 *         pliku.
	 */
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
	 * Metody obsługujące zdarzenia związane z parsowaniem XML.
	 */

	/** Treść aktualnie parsowanego znacznika. */
	private String chars;

	public void startDocument() {
		insideItem = false;
		insideImage = false;
		insideTextinput = false;
		counter = 0;
		currentUnixTime = new Date().getTime();
	}

	public void endDocument() {
		insideItem = false;
		insideImage = false;
		insideTextinput = false;
		counter = 0;
	}

	/**
	 * Wywoływana kiedy parser natrafia na początek znacznika.
	 */
	public void startElement(String uri, String name,
			String qName, Attributes atts) {
		chars = "";

		if ("".equals(uri)) {
			currentTag = qName;
		} else {
			currentTag = name;
		}
		if (currentTag.equals("item") || currentTag.equals("entry") /* Atom */) {
			insideItem = true;
			item = new Item();
			counter++;
		} else if (currentTag.equals("image")) {
			insideImage = true;
		} else if (currentTag.equals("textinput")) {
			insideTextinput = true;
		}
		if (currentTag.equals("link")) {
			String hrefLink = "";
			hrefLink = atts.getValue("href");
			if (!"".equals(hrefLink) && !(hrefLink == null)) {
				if (insideItem) {
					if ("alternate".equals(atts.getValue("rel"))) {
						if (item.getLink() == null || "".equals(item.getLink())) {
							item.setLink(hrefLink);
						}
					}
				} else {
					if ("alternate".equals(atts.getValue("rel"))) {
						if (channel.getLink() == null || "".equals(channel.getLink())) {
							channel.setLink(hrefLink);
						}
					}
				}
			}
		}
	}

	/**
	 * Wywoływana kiedy parser natrafia na koniec znacznika.
	 */
	public void endElement(String uri, String name, String qName) {
		String closingTag;

		/*
		 * Tutaj wyciągamy właściwą treść ze znaczników i wpisujemy w struktury.
		 */
		chars = chars.trim();
		if (insideImage) {
			if (currentTag.equals("url")) {
				channel.setImageURL(chars);
			} else if (currentTag.equals("title")) {
				channel.setImageTitle(chars);
			} else if (currentTag.equals("link")) {
				if ("".equals(channel.getImageLink())
						|| channel.getImageLink() == null) {
					channel.setImageLink(chars);
				}
			}
		} else if (insideTextinput) {
			// wyszukiwarka albo coś podobnego, np. na slashdot.org
		} else if (!insideItem) { // czytamy właściwości kanału
			if (currentTag.equals("title")) {
				channel.setTitle(chars.replaceAll("<.*?>", "").replace("\n", " ").
						replaceAll(" +", " "));
			} else if (currentTag.equals("link")) {
				if ("".equals(channel.getLink()) || channel.getLink() == null) {
					channel.setLink(chars);
				}
			} else if (currentTag.equals("description") // niżej: Atom
					|| currentTag.equals("content") || currentTag.equals("summary")) {
				channel.setDescription(chars);
			} else if (currentTag.equals("subtitle")) {
				if (channel.getDescription() == null) {
					channel.setDescription(chars);
				}
			}
		} else { // czytamy właściwości elementu
			if (currentTag.equals("title") && item.getTitle() == null) {
				// usuwamy niepotrzebne znaczniki z tytułu (Atom)
				item.setTitle(chars.replaceAll("<.*?>", "").replace("\n", " ").
						replaceAll(" +", " "));
			} else if (currentTag.equals("link")) {
				if ("".equals(item.getLink()) || item.getLink() == null) {
					item.setLink(chars);
				}
			} else if (currentTag.equals("description") // niżej: Atom
					|| currentTag.equals("content") || currentTag.equals("summary")) {
				item.setDescription(chars);
			} else if (currentTag.equals("author") || currentTag.equals("name")
					|| currentTag.equals("creator")) {
				item.setAuthor(chars);
			} else if (currentTag.equals("email")) { // Atom; email autora
				if (insideItem && item.getAuthor() != null) {
					item.setAuthor(item.getAuthor() + " (" + chars + ")");
				}
			} else if (currentTag.equals("pubDate") || currentTag.equals("date")
					|| currentTag.equals("updated")) {
				try {
					Date parsedDate = RSSDateFormat.parse(chars);
					item.setDate(parsedDate);
				} catch (ParseException pe) {
					// jak się nie uda ze standardową datą RFC 822, to próbujemy
					// alternatyw
					try {
						Date parsedDate = DateFormat1.parse(chars);
						item.setDate(parsedDate);
					} catch (ParseException pe1) {
						try {
							Date parsedDate = DateFormat2.parse(chars);
							item.setDate(parsedDate);
						} catch (ParseException pe2) {
							// później będzie wpisana domyślnie bieżąca data
						}
					}
				}
			} else if (currentTag.equals("guid") || currentTag.equals("id")) { //Atom
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
		if (closingTag.equals("item") || closingTag.equals("entry") /* Atom */) {
			insideItem = false;
			// jeśli data nie była określona lub parsowanie nie powiodło się,
			// stosujemy bieżącą datę
			if (item.getDate() == null) {
				// TODO: poniższe 3 linijki ustawiają przykładową datę zamiast
				// bieżącej, dla celów testowych. Docelowo zmienić na:
				//item.setDate(new Date(currentUnixTime - counter));
				try {
					item.setDate(RSSDateFormat.parse("Sun, 9 Nov 2008 19:30:00 +0100"));
				} catch (ParseException pe) { }
			}
			// data utworzenia, tj. ściągnięcia
			item.setCreationDate(new Date());
			item.setChannelKey(channel.hashCode());
			channel.addItem(item);
		} else if (closingTag.equals("image")) {
			insideImage = false;
		} else if (closingTag.equals("textinput")) {
			insideTextinput = false;
		}
	}

	/**
	 * Analiza treści znacznika.<br>
	 * UWAGA: Cała treść danego znacznika może być podzielona na kilka zdarzeń
	 * "characters" - w szczególności, każda linia jest innym zdarzeniem.
	 */
	public void characters(char ch[], int start, int length) {
		for (int i = start; i < start + length; i++) {
			chars += ch[i];
		}
	}
}

