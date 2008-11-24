package jreader;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Klasa zawierajaca metody potrzebne do eksportowania i importowania kanalow
 * do plikow zapisanych w standardzie OPML.
 */
class ImportExport extends DefaultHandler {
	/** Lista kanalow, ktora zwraca metoda getChannelsFromFile. */
	private static List<Channel> channels;

	public ImportExport() {
		super();
	}

	static List<Channel> getChannelsFromFile(String fileName) throws Exception {
		FileReader fr = new FileReader(fileName);
		channels = new ArrayList<Channel>();

		XMLReader xr = XMLReaderFactory.createXMLReader();
		ImportExport handler = new ImportExport();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);

		xr.parse(new InputSource(fr));

		return channels;
	}

	/**
	 * Zapisuje podane kanaly do pliku.
	 */
	static void writeChannelsToFile(List<Channel> channels, String fileName)
			throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.write("<opml version=\"1.0\">\n");
    out.write("<head>\n");
    out.write("<title>Subskrypcje kanałów</title>\n");
    out.write("</head>\n");
    out.write("<body>\n");

		Collections.sort(channels);
		// kanaly ktore nie sa oznaczone zadnym tagiem
		for (Channel channel : channels) {
			if (channel.getTags().size() == 0) {
				out.write("<outline text=\"");
				out.write(channel.getTitle());
				out.write("\" title=\"");
				out.write(channel.getTitle());
				out.write("\" type=\"rss\"\n");
				out.write("    xmlUrl=\"");
				out.write(channel.getChannelURL().replace("&", "&amp;"));
				out.write("\" htmlUrl=\"");
				out.write(channel.getLink());
				out.write("\"/>\n");
			}
		}
		// kanaly oznaczone tagiem
		for (String tag : JReader.getTags()) {
			out.write("<outline title=\"" + tag + "\" text=\"" + tag + "\">\n");
			for (Channel channel : channels) {
				if (channel.containsTag(tag)) {
					out.write("    <outline text=\"");
					out.write(channel.getTitle());
					out.write("\" title=\"");
					out.write(channel.getTitle());
					out.write("\" type=\"rss\"\n");
					out.write("        xmlUrl=\"");
					out.write(channel.getChannelURL().replace("&", "&amp;"));
					out.write("\" htmlUrl=\"");
					out.write(channel.getLink());
					out.write("\"/>\n");
				}
			}
			out.write("</outline>\n");
		}

    out.write("</body>\n");
    out.write("</opml>\n");

		out.close();
	}

	/*
	 * Metody oblugujace zdarzenia zwiazane z parsowaniem XML.
	 */

	/** Nazwa aktualnie parsowanego znacznika. */
	private String currentTag = "";
	private Channel channel;
	/** Nazwa aktualnego taga do oznaczenia kanalu. */
	private String channelTag;

	public void startDocument() { }

	public void endDocument() { }

	public void startElement(String uri, String name,
			String qName, Attributes atts) {
		if ("".equals(uri)) {
			currentTag = qName;
		} else {
			currentTag = name;
		}

		if (currentTag.equals("outline")) {
			if (atts.getValue("type") != null) {
				if ("rss".equals(atts.getValue("type"))) {
					if (atts.getValue("xmlUrl") != null) {
						if (!"".equals(atts.getValue("xmlUrl"))) {
							channel = new Channel(atts.getValue("xmlUrl"));
							channel.setTitle(atts.getValue("title"));
							if (channel.getTitle() == null) {
								channel.setTitle(atts.getValue("text"));
							}
							channel.setLink(atts.getValue("htmlUrl"));
							int index = indexOf(channel, channels);
							if (index == -1) { // nie ma aktualnego kanalu na liscie
								if (channelTag != null) {
									channel.setTags(channelTag);
								}
								if (channel.getTitle() != null) {
									channels.add(channel);
								}
							} else { // jesli jest juz na liscie, dodajemy tylko tagi
								channels.get(index).addTags(channelTag);
							}
						}
					}
				}
			// ustawiamy aktualny tag (w sensie tag do oznaczenia kanalu)
			} else if (atts.getValue("xmlUrl") == null) {
				channelTag = atts.getValue("title");
				if (channelTag == null) {
					channelTag = atts.getValue("text");
				}
			}
		}
	}

	/*
	 * Funkcja pomocnicza dla startElement(). Chodzi o to, zeby ignorowac tagi
	 * przy porownywaniu kanalow.
	 */
	private int indexOf(Channel channel, List<Channel> channels) {
		int index = -1;
		for (int i=0; i < channels.size(); i++) {
			if (channel.getChannelURL().equals(channels.get(i).getChannelURL())) {
				return i;
			}
		}
		return index;
	}

	public void endElement(String uri, String name, String qName) { }

	public void characters(char ch[], int start, int length) { }

}

