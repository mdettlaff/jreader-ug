package jreader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

class ImportExport {

	static List<Channel> getChannelsFromFile(String fileName) throws Exception {
		List<Channel> channels = new ArrayList<Channel>();
		return channels;
	}

	static void writeChannelsToFile(List<Channel> channels, String fileName)
			throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.write("<opml version=\"1.0\">\n");
    out.write("<head>\n");
    out.write("<title>Subskrypcje kanałów</title>\n");
    out.write("</head>\n");
    out.write("<body>\n");

		for (Channel channel : channels) {
			out.write("<outline text=\"");
			out.write(channel.getTitle());
			out.write("\" title=\"");
			out.write(channel.getTitle());
			out.write("\" type=\"rss\"\n");
			out.write("    xmlUrl=\"");
			out.write(channel.getChannelURL());
			out.write("\" htmlUrl=\"");
			out.write(channel.getLink());
			out.write("\"/>\n");
		}

    out.write("</body>\n");
    out.write("</opml>\n");

		out.close();
	}
}

