package jreader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXParseException;

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

	/** Lista subskrypcji do wyswietlenia w GUI. */
	private static List<Channel> channels = new ArrayList<Channel>();
	/** Lista wiadomosci do wyswietlenia w GUI. */
	private static List<Item> items = new ArrayList<Item>();
	/**
	 * Lista tresci wiadomosci lub informacji o kanale do wyswietlenia w GUI.
	 * Przy pomocy przyciskow Wstecz i Dalej mozna nawigowac po liscie.
	 */
	private static HistoryList<Preview> preview = new HistoryList<Preview>(10);
	/** Lista tagow do wyswietlenia w GUI. */
	private static List<String> tags = new LinkedList<String>();


	public static void main(String[] args) throws Exception {

		/*
		 * Tekstowy interfejs uzytkownika, w celach testowych.
		 */
		DateFormat shortDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String command = new String();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		boolean justStarted = true;
		while (!(command.equals("quit"))) {
			if (justStarted) {
				command = "help";
				justStarted = false;
			} else {
				command = in.readLine().trim();
			}
			if (command.equals("help")) {
				System.out.println("Dostepne komendy:");
				System.out.print("show channels\t");
				System.out.print("show items\t");
				System.out.print("show preview\t");
				System.out.println("show tags");

				System.out.print("add channel\t");
				System.out.print("previous item\t");
				System.out.print("next item\t");
				System.out.print("update all\t");
				System.out.println("next unread");

				System.out.print("select item\t");
				System.out.print("select channel\t");
				System.out.print("select all\t");
				System.out.print("select unread\t");
				System.out.println("select tag");

				System.out.print("mark channel\t");
				System.out.print("update channel\t");
				System.out.print("edit tags\t");
				System.out.println("remove channel");

				System.out.print("set sort\t");
				System.out.print("set delete\t");
				System.out.print("help\t\t");
				System.out.println("quit");
			} else if (command.equals("show channels")) {
				if (channels.size() == 0) {
					System.out.println("Lista subskrypcji jest pusta.");
				} else {
					Channel channel;
					for (int i=0; i < channels.size(); i++) {
						channel = channels.get(i);
						System.out.print("Kanal " + (i+1) + ": " + channel.getTitle());
						if (channel.getUnreadItemsCount() > 0) {
							System.out.println(" (" + channel.getUnreadItemsCount() + ")");
						} else {
							System.out.println();
						}
					}
				}
			} else if (command.equals("show items")) {
				if (items.size() == 0) {
					System.out.println("Lista wiadomosci jest pusta.");
				} else {
					for (int i=0; i < items.size(); i++) {
						Item item = items.get(i);
						System.out.print((i+1) + ": ");
						if (item.getDate() != null) {
							System.out.print(shortDateFormat.format(item.getDate()) + " ");
						}
						if (item.isUnread()) {
							System.out.print("N ");
						}
						if (allChannels.get(item.getChannelKey()).getTitle().length()>12) {
							System.out.print(allChannels.get(item.getChannelKey()).
									getTitle().substring(0, 12) + " ");
						} else {
							System.out.print(allChannels.get(item.getChannelKey()).
									getTitle() + " ");
						}
						System.out.println(item.getTitle());
					}
				}
			} else if (command.equals("show preview")) {
				if (preview.getCurrent() == null) {
					System.out.println("Nie wybrano zadnej wiadomosci.");
				} else {
					System.out.println(preview.getCurrent().getTitle());
					System.out.println("Link: " + preview.getCurrent().getLink());
					if (preview.getCurrent().getAuthor() != null) {
						System.out.println("Autor: " + preview.getCurrent().getAuthor());
					}
					if (preview.getCurrent().getSource() != null) {
						System.out.println("Zrodlo: " + preview.getCurrent().getSource());
					}
					System.out.println("Opis: " + preview.getCurrent().getHTML());
				}
			} else if (command.equals("show tags")) {
				if (tags.size() == 0) {
					System.out.println("Lista tagow jest pusta.");
				} else {
					for (String tag : tags) {
						System.out.print(tag.concat(" "));
					}
					System.out.println();
				}

			} else if (command.equals("add channel")) {
				try {
					System.out.print("Podaj adres URL: ");
					String url = in.readLine();
					// sposob na podanie tyldy w adresie: \tld
					url = url.replace("\\tld","~");
					System.out.print("Podaj tagi: ");
					String tags = in.readLine();
					addChannel(url, tags);
					System.out.println("Kanal zostal dodany");
				} catch (LinkNotFoundException lnfe) {
					System.out.println("Nie znaleziono kanalow RSS na tej stronie.");
				} catch (MalformedURLException mue) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Podany URL jest nieprawidlowy.");
				} catch (SAXParseException spe) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
				} catch (SocketException se) {
					System.out.println("Nie mozna dodac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
				} catch (FileNotFoundException fnfe) {
					System.out.println("Podana strona nie istnieje.");
				} catch (IOException ioe) {
					System.out.println("Podana strona nie istnieje.");
				} catch (IllegalArgumentException iae) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Podany URL jest nieprawidlowy.");
				}
			} else if (command.equals("previous item")) {
				if (previousItem() == null) {
					System.out.println("Nie mozna sie cofnac.");
				}
			} else if (command.equals("next item")) {
				if (nextItem() == null) {
					System.out.println("Nie mozna przejsc dalej.");
				}
			} else if (command.equals("update all")) {
				for (Channel channel : channels) {
					try {
						updateChannel(channel);
					} catch (SAXParseException spe) {
						System.out.println("Nie mozna zaktualizowac kanalu "
								+ channel.getTitle() + ".");
						System.out.println("Zrodlo nie jest prawidlowym plikiem XML.");
					} catch (SocketException se) {
						System.out.println("Nie mozna zaktualizowac kanalu "
								+ channel.getTitle() + ".");
						System.out.println("Szczegoly: " + se.getLocalizedMessage());
					} catch (FileNotFoundException fnfe) {
						System.out.println("Nie mozna zaktualizowac kanalu "
								+ channel.getTitle() + ".");
						System.out.println("Brak polaczenia ze strona.");
					} catch (IOException ioe) {
						System.out.println("Nie mozna zaktualizowac kanalu "
								+ channel.getTitle() + ".");
						System.out.println("Brak polaczenia ze strona.");
					}
				}
				System.out.println("Kanaly zostaly zaktualizowane.");
			} else if (command.equals("next unread")) {
				if (!nextUnread()) {
					System.out.println("Nie ma nieprzeczytanych wiadomosci.");
				}
			} else if (command.equals("select item")) {
				System.out.print("Podaj numer elementu: ");
				int nr = new Integer(in.readLine()) - 1;
				selectItem(items.get(nr));
			} else if (command.equals("select channel")) {
				System.out.print("Podaj numer kanalu: ");
				int nr = new Integer(in.readLine()) - 1;
				selectChannel(nr);
			} else if (command.equals("select all")) {
				selectAll();
			} else if (command.equals("select unread")) {
				selectUnread();
			} else if (command.equals("select tag")) {
				System.out.print("Wybierz tag (all - wszystkie, " +
						"untagged - nieoznaczone): ");
				selectTag(in.readLine());
			} else if (command.equals("mark channel")) {
				System.out.print("Podaj numer kanalu: ");
				int nr = new Integer(in.readLine()) - 1;
				markChannelAsRead(channels.get(nr));
			} else if (command.equals("update channel")) {
				try {
					System.out.print("Podaj numer kanalu: ");
					int nr = new Integer(in.readLine()) - 1;
					updateChannel(channels.get(nr));
					System.out.println("Kanal zostal zaktualizowany.");
				} catch (SAXParseException spe) {
					System.out.print("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
				} catch (SocketException se) {
					System.out.println("Nie mozna zaktualizowac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
				} catch (FileNotFoundException fnfe) {
					System.out.print("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Brak polaczenia ze strona.");
				} catch (IOException ioe) {
					System.out.print("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Brak polaczenia ze strona.");
				}
			} else if (command.equals("edit tags")) {
				System.out.print("Podaj numer kanalu: ");
				int nr = new Integer(in.readLine()) - 1;
				if (channels.get(nr).getTagsAsString() != null) {
					System.out.println("Tagi: " + channels.get(nr).getTagsAsString());
				} else {
					System.out.println("Ten kanal nie ma tagow.");
				}
				System.out.print("Podaj nowe tagi: ");
				editTags(channels.get(nr), in.readLine());
			} else if (command.equals("remove channel")) {
				System.out.print("Podaj numer kanalu: ");
				int nr = new Integer(in.readLine()) - 1;
				removeChannel(nr);
			} else if (command.equals("set sort")) {
				System.out.print("Sortuj wedlug (new/old): ");
				String choice = in.readLine().trim();
				if (choice.equals("old")) {
					config.setSortByNewest(false);
					if (!config.write()) {
						System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
					}
				} else if (choice.equals("new")) {
					config.setSortByNewest(true);
					if (!config.write()) {
						System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
					}
				} else {
					System.out.println("Nieprawidlowy wybor.");
				}
			} else if (command.equals("set delete")) {
        if (config.getDeleteOlderThanDays() == 0) {
					System.out.println("Stare wiadomosci nie sa usuwane.");
				} else {
					System.out.println("Wiadomosci sa usuwane po "
							+ config.getDeleteOlderThanDays() + " dniach.");
				}
				System.out.print("Po ilu dniach usuwac wiadomosci (0 - wcale): ");
				config.setDeleteOlderThanDays(new Integer(in.readLine().trim()));
				if (!config.write()) {
					System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
				}
			} else if (!command.equals("") && !command.equals("quit")) {
				System.out.println("Nieznane polecenie.");
			}
		}
		/*
		 * Koniec tekstowego UI.
		 */
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
		Collections.sort(items, new ItemComparator());
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
		Collections.sort(items, new ItemComparator());
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
		Collections.sort(items, new ItemComparator());
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

	static Config getConfig() {
		return config;
	}
}

