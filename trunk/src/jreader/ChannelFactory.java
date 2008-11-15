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
 * Stad za pomoca metod getChannelFromSite i getChannelFromXML mozna pobierac
 * nowo stworzony kanal lub aktualna tresc kanalu.
 */
class ChannelFactory extends DefaultHandler {
	/** Zmienna, ktora zostanie zwrocona przez metode getChannelFromXML(). */
	private static Channel channel;
	/** Tymczasowy element dla celow parsowania. */
	private static Item item = new Item();

	/**
	 * Definicja standardowego formatu daty stosowanego w kanalach RSS (RFC 822)
	 */
	private DateFormat RSSDateFormat =
		new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	/*
	 * Inne formaty daty.
	 */
	/** Format daty uzywany w userfriendly.org. */
	private DateFormat DateFormat1 =
		new SimpleDateFormat("E, dd MMM yyyy HH:mm z", Locale.ENGLISH);
	/** Format daty uzywany w slashdot.org. */
	private DateFormat DateFormat2 =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

	private boolean insideItem;
	private boolean insideImage;
	private String currentTag = "";

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
		// najpierw sprawdzamy czy podany adres jest adresem do pliku XML,
		// na podstawie 3 pierwszych linii
		String line1 = in.readLine();
		String line2 = in.readLine();
		String line3 = in.readLine();
		if (line1.contains("xml version")) {
			// wykrywanie kanalow rss
			if (line1.contains("rss") || line2.contains("rss")) {
				channelURL = siteURL;
			}
			//wykrywanie kanalow Atom
			if (line1.contains("Atom") || line2.contains("Atom")
					|| line3.contains("Atom")) {
				channelURL = siteURL;
			}
		}
		// czytamy z adresu od nowa, zeby nie pominac dwoch pierwszych linii
		in.close();
		in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;
		// jesli nie podano adresu do pliku XML tylko do strony glownej, to czytamy
		// plik HTML dopoki nie znajdziemy odnosnika do pliku XML kanalu
		if ("".equals(channelURL)) {
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("type=\"application/rss+xml\"")
						|| inputLine.contains("type=\"application/atom+xml\"")) {
					// rozbijamy na mniejsze linie, mniej problematyczne
					inputLine = inputLine.replaceAll(">", ">\n");
					String[] smallLines = inputLine.split("\n");
					for (String smallLine : smallLines) {
						if (smallLine.contains("type=\"application/rss+xml\"")
								|| smallLine.contains("type=\"application/rss+atom\"")) {
							inputLine = smallLine;
							break;
						}
					}
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
			if ("".equals(channelURL)) { // jesli nie znaleziono odnosnika do kanalu
				throw new LinkNotFoundException();
			}
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
	private String chars;

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
		if (currentTag.equals("item") || currentTag.equals("entry") /* Atom */) {
			insideItem = true;
			item = new Item();
		} else if (currentTag.equals("image")) {
			insideImage = true;
		}
		if (currentTag.equals("link")) {
			String hrefLink = "";
			hrefLink = atts.getValue("href");
			if (!"".equals(hrefLink) && !(hrefLink == null)) {
				if (insideItem) {
					item.setLink(hrefLink);
				} else {
					channel.setLink(hrefLink);
				}
			}
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
				if ("".equals(channel.getImageLink())
						|| channel.getImageLink() == null) {
					channel.setImageLink(chars);
				}
			}
		} else if (!insideItem) { // czytamy wlasciwosci kanalu
			if (currentTag.equals("title")) {
				channel.setTitle(chars);
			} else if (currentTag.equals("link")) {
				if ("".equals(channel.getLink()) || channel.getLink() == null) {
					channel.setLink(chars);
				}
			} else if (currentTag.equals("description") // nizej: Atom
					|| currentTag.equals("content") || currentTag.equals("summary")) {
				channel.setDescription(chars);
			}
		} else { // czytamy wlasciwosci elementu
			if (currentTag.equals("title")) {
				// usuwamy niepotrzebne znaczniki z tytulu (Atom)
				item.setTitle(chars.replaceAll("<.*?>", ""));
			} else if (currentTag.equals("link")) {
				if ("".equals(item.getLink()) || item.getLink() == null) {
					item.setLink(chars);
				}
			} else if (currentTag.equals("description") // nizej: Atom
					|| currentTag.equals("content") || currentTag.equals("summary")) {
				item.setDescription(chars);
			} else if (currentTag.equals("author") || currentTag.equals("name")) {
				item.setAuthor(chars);
			} else if (currentTag.equals("pubDate") || currentTag.equals("date")
					|| currentTag.equals("updated")) {
				try {
					Date parsedDate = RSSDateFormat.parse(chars);
					item.setDate(parsedDate);
				} catch (ParseException pe) {
					// jak sie nie uda ze standardowa data RFC 822, to probujemy
					// alternatyw
					try {
						Date parsedDate = DateFormat1.parse(chars);
						item.setDate(parsedDate);
					} catch (ParseException pe1) {
						try {
							Date parsedDate = DateFormat2.parse(chars);
							item.setDate(parsedDate);
						} catch (ParseException pe2) {
							// pozniej bedzie wpisana domyslnie biezaca data
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
			// data utworzenia, tj. sciagniecia
			item.setCreationDate(new Date());
			item.setChannelKey(channel.key());
			item.setChannelTitle(channel.getTitle());
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

