package jreader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;

/**
 * Główna klasa programu. Przechowuje zbiór wszystkich subskrypcji, ustawienia
 * programu oraz obiekty przeznaczone do wyświetlania w graficznym interfejsie
 * użytkownika. Posiada też metody obsługujące podstawowe funkcjonalności
 * programu, używane przy pomocy interfejsu użytkownika. 
 */
public class JReader {
	/** Ilość podglądów kanałów lub wiadomości o jaką można się cofnąć. */
	public static final int HISTORY_SIZE = 10;

	/**
	 * Zbiór wszystkich kanałów.
	 */
	private static Map<Integer, Channel> allChannels =
			new HashMap<Integer, Channel>();
	/**
	 * Ustawienia programu wybrane przez użytkownika.
	 */
	private static Config config = new Config();

	/**
	 * Lista subskrypcji do wyświetlenia w GUI.
	 */
	private static List<Channel> channels = new ArrayList<Channel>();
	/**
	 * Lista wiadomości do wyświetlenia w GUI.
	 */
	private static List<Item> items = new ArrayList<Item>();
	/**
	 * Lista treści wiadomości lub informacji o kanale do wyświetlenia w GUI.
	 * Przy pomocy przycisków Wstecz i Dalej można nawigować po liście.
	 */
	private static HistoryList<Preview> preview =
			new HistoryList<Preview>(HISTORY_SIZE);
	/**
	 * Lista tagów do wyświetlenia w GUI.
	 */
	private static List<String> tags = new LinkedList<String>();

	/** Nie można tworzyć obiektów tej klasy. */
	private JReader() {}


	public static void main(String[] args) {
		TextUI.run();
	}


	/*
	 * Metody regulujące dostęp do głównych struktur danych programu.
	 */

	/**
	 * Zwraca ustawienia programu.
	 */
	public static Config getConfig() {
		return config;
	}

	/**
	 * Zwraca kanał o wskazanym hashCode z listy wszystkich kanałów.
	 */
	public static Channel getChannelFromHash(int hashCode) {
		return allChannels.get(hashCode);
	}

	/**
	 * Zwraca kanał o wskazanym indeksie z listy kanałów do wyświetlenia w GUI.
	 */
	public static Channel getChannel(int index) {
		return channels.get(index);
	}

	/**
	 * Zwraca listę kanałów do wyświetlenia w GUI.
	 */
	public static List<Channel> getChannels() {
		return channels;
	}

	/**
	 * Zwraca listę wiadomości do wyświetlenia w GUI.
	 */
	public static List<Item> getItems() {
		return items;
	}

	/**
	 * Zwraca historię wyświetlanych wiadomości.
	 */
	public static HistoryList<Preview> getPreview() {
		return preview;
	}

	/**
	 * Zwraca listę tagów do wyświetlenia w GUI.
	 */
	public static List<String> getTags() {
		return tags;
	}


	/*
	 * Metody obsługujące główne funkcjonalności programu, wywoływane przy
	 * pomocy interfejsu użytkownika.
	 */

	/**
	 * Dodaje nowy kanał na podstawie URLa oraz listy tagów podanej przez
	 * użytkownika. URL może odnosić się do strony www lub konkretnego
	 * pliku XML z treścią kanału. Lista tagów może być pustym Stringiem.
	 *
	 * @throws LinkNotFoundException jeśli na podanej stronie nie znaleziono
	 *         żadnych odnośników do kanałów.
	 * @throws MalformedURLException jeśli podany przez użytkownika adres
	 *         nie jest prawidłowym adresem URL.
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML.
	 * @throws IOException jeśli pobieranie pliku nie powiodło się.
	 */
	public static void addChannel(String siteURL, String channelTags)
			throws LinkNotFoundException, MalformedURLException, SAXException,
				IOException	{
		Channel newChannel = ChannelFactory.getChannelFromSite(siteURL);
		newChannel.setTags(channelTags);
		// uzupełniamy listę tagów do wyświetlenia
		for (String tag : newChannel.getTags()) {
			if (!tags.contains(tag)) {
				tags.add(tag);
			}
		}
		Collections.sort(tags);
		allChannels.put(newChannel.hashCode(), newChannel);
		channels.add(newChannel);
		// sortujemy listę kanałów alfabetycznie
		Collections.sort(channels);
	}

	/**
	 * Ustawia poprzedni element z historii jako bieżący.
	 *
	 * @return <code>null</code>, jeśli nie można wrócić, w przeciwnym
	 *         wypadku podgląd elementu.
	 */
	public static Preview previousItem() {
		return preview.previous();
	}

	/**
	 * Ustawia następny element z historii jako bieżący.
	 *
	 * @return <code>null</code>, jeśli nie można przejść dalej, w przeciwnym
	 *         wypadku podgląd elementu.
	 */
	public static Preview nextItem() {
		return preview.next();
	}

	/**
	 * Ustawia następny pod względem daty nieprzeczytany element jako bieżący.
	 * Jeśli ustawione jest sortowanie od najnowszych, szuka najnowszego,
	 * w przeciwnym wypadku najstarszego.
	 *
	 * @return <code>false</code>, jeśli nie ma więcej nieprzeczytanych
	 *         wiadomości, <code>true</code> w przeciwnym wypadku.
	 */
	public static boolean nextUnread() {
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
	 * Efekt kliknięcia na wybrany element. Zostaje on pokazany w podglądzie,
	 * oznaczony jako przeczytany, a ilość elementów nieprzeczytanych kanału
	 * z którego pochodzi zostaje zaktualizowana.
	 */
	public static void selectItem(Item item) {
		item.markAsRead();
		preview.setCurrent(new Preview(item));
		// aktualizujemy ilość nieprzeczytanych elementów kanału, z którego
		// pochodzi wybrany item
		allChannels.get(item.getChannelKey()).updateUnreadItemsCount();
	}

	/**
	 * Efekt kliknięcia na wybrany kanał. Jego opis zostaje pokazany
	 * w podglądzie, a na listę elementów zostają wpisane jego elementy.
	 *
	 * @param index Indeks kanału na liście kanałów do wyświetlenia.
	 */
	public static void selectChannel(int index) {
		items = channels.get(index).getItems();
		Collections.sort(items);
		preview.setCurrent(new Preview(channels.get(index)));
	}

	/**
	 * Oznacza wszystkie wiadomości w kanale jako przeczytane.
	 */
	public static void markChannelAsRead(Channel channel) {
		channel.markAllAsRead();
	}

	/**
	 * Wybiera wszystkie elementy z listy kanałów.
	 */
	public static void selectAll() {
		items = new ArrayList<Item>(); // nie uzywać items.clear()
		for (Channel channel : channels) {
			for (Item item : channel.getItems()) {
				items.add(item);
			}
		}
		Collections.sort(items);
	}

	/**
	 * Wybiera nieprzeczytane elementy z listy kanałów.
	 */
	public static void selectUnread() {
		items = new ArrayList<Item>(); // nie używać items.clear()
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
	 * Ogranicza listę kanałów do kanałów oznaczonych wybranym tagiem.
	 */
	public static void selectTag(String tag) {
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
	 * Sprawdza, czy w danym kanale pojawiły się nowe wiadomości i jeśli tak,
	 * to dodaje je do listy wiadomości kanału.
	 *
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML.
	 * @throws IOException jeśli pobieranie pliku nie powiodło się.
	 */
	public static void updateChannel(Channel channel)
			throws SAXException, IOException {
		channel.update();
	}

	/**
	 * Zmienia tagi kanału na podane przez użytkownika.
	 */
	public static void editTags(Channel channel, String channelTags) {
		channel.setTags(channelTags);
		// uzupełniamy listę tagów do wyświetlenia
		for (String tag : channel.getTags()) {
			if (!tags.contains(tag)) {
				tags.add(tag);
			}
		}
		Collections.sort(tags);
	}

	/**
	 * Usuwa kanał na stałe.
	 *
	 * @param index Indeks kanału na liście kanałów do wyświetlenia.
	 */
	public static void removeChannel(int index) {
		// najpierw usuwamy z listy elementów te pochodzące z usuwanego kanału
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

	/**
	 * Importuje listę kanałów z pliku. Kanały są dodawane na koniec listy
	 * wyświetlanych subskrypcji.
	 *
	 * @return Ilość zaimportowanych kanałów.
	 * @throws FileNotFoundException jeśli podany plik nie istnieje.
	 * @throws IOException jeśli nie można odczytać pliku.
	 * @throws SAXParseException jeśli parsowanie podanego pliku OPML
	 *         nie powiodło się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML.
	 */
	public static int importChannelList(String fileLocation)
			throws IOException, SAXException {
		List<Channel> importedChannels =
				ImportExport.getChannelsFromFile(fileLocation);
		for (Channel channel : importedChannels) {
			if (!allChannels.containsKey(channel.hashCode())) {
				allChannels.put(channel.hashCode(), channel);
			}
			channels.add(channel);
			// uzupełniamy listę tagów do wyświetlenia
			for (String tag : channel.getTags()) {
				if (!tags.contains(tag)) {
					tags.add(tag);
				}
			}
		}
		Collections.sort(tags);
		return importedChannels.size();
	}

	/**
	 * Eksportuje wszystkie kanały (nie tylko z listy do wyświetlenia) do pliku.
	 *
	 * @throws IOException jeśli zapisanie pliku jest niemożliwe.
	 */
	public static void exportChannelList(String fileLocation)
			throws IOException {
		ImportExport.writeChannelsToFile(new LinkedList<Channel>(
					allChannels.values()), fileLocation);
	}
}

