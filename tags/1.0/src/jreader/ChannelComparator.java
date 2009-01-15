package jreader;

import java.util.Comparator;

/**
 * Porównuje kanały pod względem alfabetycznej kolejności ich tytułów.
 */
public class ChannelComparator implements Comparator<Channel> {
	public int compare(Channel channel1, Channel channel2) {
		return channel1.getTitle().compareToIgnoreCase(channel2.getTitle());
	}
}

