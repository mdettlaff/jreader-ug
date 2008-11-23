package jreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Glowna klasa programu, przechowujaca obiekty przeznaczone do wyswietlania
 * w graficznym interfejsie uzytkownika i posiadajaca metody obslugujace
 * podstawowe funkcjonalnosci programu, uzywane za pomoca interfejsu
 * uzytkownika. 
 */
class JReader {
	/**
	 * Lista wszystkich kanalow.
	 */
	private static Map<Integer, Channel> allChannels =
			new HashMap<Integer, Channel>();
	/**
	 * Ustawienia programu wybrane przez uzytkownika.
	 */
	private static Config config = new Config();

	/**
	 * Lista subskrypcji do wyswietlenia w GUI.
	 */
	private static List<Channel> channels = new ArrayList<Channel>();
	/**
	 * Lista wiadomosci do wyswietlenia w GUI.
	 */
	private static List<Item> items = new ArrayList<Item>();
	/**
	 * Lista tresci wiadomosci lub informacji o kanale do wyswietlenia w GUI.
	 * Przy pomocy przyciskow Wstecz i Dalej mozna nawigowac po liscie.
	 */
	private static HistoryList<Preview> preview = new HistoryList<Preview>(10);
	/**
	 * Lista tagow do wyswietlenia w GUI.
	 */
	private static List<String> tags = new LinkedList<String>();


	public static void main(String[] args) {
		TextUI.run();
	}


	/*
	 * Metody obslugujace glowne funkcjonalnosci programu, wywolywane przy
	 * pomocy interfejsu uzytkownika.
	 */

	/**
	 * Dodaje nowy kanal na podstawie URLa podanego przez uzytkownika.
	 * Moze byc to URL strony lub konkretnego pliku XML z trescia kanalu.
	 */
	static void addChannel(String siteURL, String channelTags) throws Exception {
		Channel newChannel = ChannelFactory.getChannelFromSite(siteURL);
		newChannel.setTags(channelTags);
		// uzupelniamy liste tagow do wyswietlenia
		for (String tag : newChannel.getTags()) {
			if (!tags.contains(tag)) {
				tags.add(tag);
			}
		}
		Collections.sort(tags);
		allChannels.put(newChannel.hashCode(), newChannel);
		channels.add(newChannel);
		// sortujemy liste kanalow alfabetycznie
		Collections.sort(channels);
	}

	/**
	 * Ustawia poprzedni element z historii jako biezacy.
	 *
	 * @return null, jesli nie mozna wrocic, w przeciwnym wypadku podglad
	 *         elementu.
	 */
	static Preview previousItem() {
		return preview.previous();
	}

	/**
	 * Ustawia nastepny element z historii jako biezacy.
	 *
	 * @return null, jesli nie mozna przejsc dalej, w przeciwnym wypadku
	 *         podglad elementu.
	 */
	static Preview nextItem() {
		return preview.next();
	}

	/**
	 * Ustawia nastepny pod wzgledem daty nieprzeczytany element jako biezacy.
	 * Jesli ustawione jest sortowanie od najnowszych, szuka najnowszego,
	 * w przeciwnym wypadku najstarszego.
	 *
	 * @return false, jesli nie ma wiecej nieprzeczytanych wiadomosci,
	 *         true w przeciwnym wypadku
	 */
	static boolean nextUnread() {
		Item nextUnreadItem = new Item();
		Date beginningOfTime = new Date(0); // 1 stycznia 1970
		Date endOfTime = new Date();
		try { endOfTime = new SimpleDateFormat("yyyy").parse("9999");
		} catch (ParseException pe) { }
		if (config.getSortByNewest()) { // szukamy najnowszego nieprzeczytanego
			nextUnreadItem.setDate(beginningOfTime);
			for (Channel channel : channels) {
				if (channel.getUnreadItemsCount() > 0) {
					for (Item item : channel.getItems()) {
						if (item.isUnread()) {
							if (item.getDate().after(nextUnreadItem.getDate())) {
								nextUnreadItem = item;
							}
						}
					}
				}
			}
		} else { // szukamy najstarszego nieprzeczytanego
			nextUnreadItem.setDate(endOfTime);
			for (Channel channel : channels) {
				if (channel.getUnreadItemsCount() > 0) {
					for (Item item : channel.getItems()) {
						if (item.isUnread()) {
							if (item.getDate().before(nextUnreadItem.getDate())) {
								nextUnreadItem = item;
							}
						}
					}
				}
			}
		}
		if (nextUnreadItem.getDate().equals(beginningOfTime)
				|| nextUnreadItem.getDate().equals(endOfTime)) {
			return false;
		}
		nextUnreadItem.markAsRead();
		allChannels.get(nextUnreadItem.getChannelKey()).updateUnreadItemsCount();
		preview.setCurrent(new Preview(nextUnreadItem));
		return true;
	}

	/**
	 * Efekt klikniecia na wybrany element.
	 */
	static void selectItem(Item item) {
		item.markAsRead();
		preview.setCurrent(new Preview(item));
		// aktualizujemy ilosc nieprzeczytanych elementow kanalu, z ktorego
		// pochodzi wybrany item
		allChannels.get(item.getChannelKey()).updateUnreadItemsCount();
	}

	/**
	 * Efekt klikniecia na wybrany kanal.
	 */
	static void selectChannel(int index) {
		items = channels.get(index).getItems();
		Collections.sort(items);
		preview.setCurrent(new Preview(channels.get(index)));
	}

	/**
	 * Oznacza wszystkie wiadomosci w kanale jako przeczytane.
	 */
	static void markChannelAsRead(Channel channel) {
		channel.markAllAsRead();
	}

	/**
	 * Wybiera wszystkie elementy z listy kanalow.
	 */
	static void selectAll() {
		items = new ArrayList<Item>(); // nie uzywac items.clear()
		for (Channel channel : channels) {
			for (Item item : channel.getItems()) {
				items.add(item);
			}
		}
		Collections.sort(items);
	}

	/**
	 * Wybiera nieprzeczytane elementy z listy kanalow.
	 */
	static void selectUnread() {
		items = new ArrayList<Item>(); // nie uzywac items.clear()
		for (Channel channel : channels) {
			if (channel.getUnreadItemsCount() > 0) {
				for (Item item : channel.getItems()) {
					if (item.isUnread()) {
						items.add(item);
					}
				}
			}
		}
		Collections.sort(items);
	}

	/**
	 * Ogranicza liste kanalow do kanalow oznaczonych wybranym tagiem.
	 */
	static void selectTag(String tag) {
		tag = tag.trim();
		channels = new ArrayList<Channel>();
		if (tag.equals("all")) {
			channels = new ArrayList<Channel>(allChannels.values());
		} else if (tag.equals("untagged")) {
			for (Channel channel : allChannels.values()) {
				if ("".equals(channel.getTagsAsString())) {
					channels.add(channel);
				}
			}
		} else {
			for (Channel channel : allChannels.values()) {
				if (channel.containsTag(tag)) {
					channels.add(channel);
				}
			}
		}
		Collections.sort(channels);
	}

	/**
	 * Sprawdza, czy w danym kanale nie pojawily sie nowe wiadomosci i jesli
	 * tak, to dodaje je do listy wiadomosci kanalu.
	 */
	static void updateChannel(Channel channel) throws Exception {
		channel.update();
	}

	static void editTags(Channel channel, String channelTags) {
		channel.setTags(channelTags);
		// uzupelniamy liste tagow do wyswietlenia
		for (String tag : channel.getTags()) {
			if (!tags.contains(tag)) {
				tags.add(tag);
			}
		}
		Collections.sort(tags);
	}

	static void removeChannel(int index) {
		// najpierw usuwamy z listy elementow te pochadzace z usuwanego kanalu
		List<Integer> indToRemove = new ArrayList<Integer>();
		for (int i=0; i < items.size(); i++) {
			for (Item channelItem : channels.get(index).getItems()) {
				if (items.get(i).equals(channelItem)) {
					indToRemove.add(i);
				}
			}
		}
		for (int i=0; i < indToRemove.size(); i++) {
			items.remove((int)indToRemove.get(i));
			for (int j=i; j < indToRemove.size(); j++) {
				indToRemove.set(j, indToRemove.get(j)-1);
			}
		}
		allChannels.remove(channels.get(index).hashCode());
		channels.remove(index);
	}

	static void importChannelList(String fileLocation) throws Exception {
		for (Channel channel : ImportExport.getChannelsFromFile(fileLocation)) {
			allChannels.put(channel.hashCode(), channel);
			channels.add(channel);
			// uzupelniamy liste tagow do wyswietlenia
			for (String tag : channel.getTags()) {
				if (!tags.contains(tag)) {
					tags.add(tag);
				}
			}
		}
		Collections.sort(tags);
	}

	static void exportChannelList(String fileLocation) throws Exception {
		ImportExport.writeChannelsToFile(channels, fileLocation);
	}

	/*
	 * KONIEC metod obslugujacych glowne funkcjonalnosci programu.
	 */

	/*
	 * Metody regulujace dostep do glownych struktur danych programu.
	 */

	static Config getConfig() {
		return config;
	}

	/**
	 * Zwraca kanal z listy wszystkich kanalow o wskazanym hashCode.
	 */
	static Channel getChannelFromHash(int hashCode) {
		return allChannels.get(hashCode);
	}

	/**
	 * Zwraca kanal z listy kanalow do wyswietlenia w GUI o wskazanym indeksie.
	 */
	static Channel getChannel(int index) {
		return channels.get(index);
	}

	/**
	 * Zwraca liste kanalow do wyswietlenia w GUI.
	 */
	static List<Channel> getChannels() {
		return channels;
	}

	/**
	 * Zwraca liste wiadomosci do wyswietlenia w GUI.
	 */
	static List<Item> getItems() {
		return items;
	}

	/**
	 * Zwraca liste wiadomosci do wyswietlenia w GUI.
	 */
	static HistoryList<Preview> getPreview() {
		return preview;
	}

	/**
	 * Zwraca liste tagow do wyswietlenia w GUI.
	 */
	static List<String> getTags() {
		return tags;
	}
}

