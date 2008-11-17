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
				System.out.println("show preview");

				System.out.print("add channel\t");
				System.out.print("previous item\t");
				System.out.print("next item\t");
				System.out.print("update all\t");
				System.out.println("next unread");

				System.out.print("select item\t");
				System.out.print("select channel\t");
				System.out.print("select all\t");
				System.out.println("select unread");

				System.out.print("mark channel\t");
				System.out.print("update channel\t");
				System.out.println("remove channel");

				System.out.print("set sort\t");
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
							System.out.println("");
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

			} else if (command.equals("add channel")) {
				try {
					System.out.print("Podaj adres URL: ");
					String url = in.readLine();
					// sposob na podanie tyldy w adresie
					url = url.replaceAll("\\\\tld","~");
					addChannel(url);
					System.out.println("Kanal zostal dodany");
				} catch (LinkNotFoundException lnfe) {
					System.out.println("Nie znaleziono kanalow RSS na tej stronie.");
				} catch (SAXParseException spe) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
				} catch (MalformedURLException mue) {
					System.out.print("Nie mozna dodac kanalu.");
					System.out.println(" Podany URL jest nieprawidlowy.");
				} catch (FileNotFoundException fnfe) {
					System.out.println("Podana strona nie istnieje.");
				} catch (SocketException se) {
					System.out.println("Nie mozna dodac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
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
				updateAll();
				System.out.println("Kanaly zostaly zaktualizowane.");
			} else if (command.equals("next unread")) {
				nextUnread();
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
				} catch (FileNotFoundException fnfe) {
					System.out.print("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Brak polaczenia ze strona.");
				} catch (SocketException se) {
					System.out.println("Nie mozna zaktualizowac kanalu. Szczegoly:");
					System.out.println(se.getLocalizedMessage());
				} catch (IOException ioe) {
					System.out.print("Nie mozna zaktualizowac kanalu.");
					System.out.println(" Brak polaczenia ze strona.");
				}
			} else if (command.equals("remove channel")) {
				System.out.print("Podaj numer kanalu: ");
				int nr = new Integer(in.readLine()) - 1;
				removeChannel(nr);
			} else if (command.equals("set sort")) {
				System.out.print("Sortuj wedlug (new/old): ");
				String choice = in.readLine().trim();
				if (choice.equals("old")) {
					config.setSortByNewest(false);
				} else if (choice.equals("new")) {
					config.setSortByNewest(true);
				} else {
					System.out.println("Nieprawidlowy wybor.");
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
	static void addChannel(String siteURL) throws Exception {
		Channel newChannel = ChannelFactory.getChannelFromSite(siteURL);
		allChannels.put(newChannel.hashCode(), newChannel);
		channels.add(newChannel);
		// Sortujemy liste kanalow alfabetycznie
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

	static void updateAll() throws Exception {
		for (Channel channel : channels) {
			try {
				channel.update();
			} catch (SAXParseException spe) {
				System.out.print("Nie mozna zaktualizowac kanalu "
						+ channel.getTitle() + ".");
				System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
			} catch (FileNotFoundException fnfe) {
				System.out.print("Nie mozna zaktualizowac kanalu "
						+ channel.getTitle() + ".");
				System.out.println(" Brak polaczenia ze strona.");
			} catch (SocketException se) {
				System.out.print("Nie mozna zaktualizowac kanalu "
						+ channel.getTitle() + ".");
				System.out.println("Szczegoly: " + se.getLocalizedMessage());
			} catch (IOException ioe) {
				System.out.print("Nie mozna zaktualizowac kanalu "
						+ channel.getTitle() + ".");
				System.out.println(" Brak polaczenia ze strona.");
			}
		}
	}

	/**
	 * Ustawia nastepny pod wzgledem daty nieprzeczytany element jako biezacy.
	 * Jesli ustawione jest sortowanie od najnowszych, szuka najnowszego,
	 * w przeciwnym wypadku najstarszego.
	 */
	static void nextUnread() {
		Item nextItem = new Item(); // nastepny nieprzeczytany
		Date beginningOfTime = new Date(0); // 1 stycznia 1970
		Date endOfTime = new Date();
		try { endOfTime = new SimpleDateFormat("yyyy").parse("9999");
		} catch (ParseException pe) { }
		if (config.getSortByNewest()) { // szukamy najnowszego nieprzeczytanego
			nextItem.setDate(beginningOfTime);
			for (Channel channel : channels) {
				if (channel.getUnreadItemsCount() > 0) {
					for (Item item : channel.getItems()) {
						if (item.isUnread()) {
							if (item.getDate().after(nextItem.getDate())) {
								nextItem = item;
							}
						}
					}
				}
			}
		} else { // szukamy najstarszego nieprzeczytanego
			nextItem.setDate(endOfTime);
			for (Channel channel : channels) {
				if (channel.getUnreadItemsCount() > 0) {
					for (Item item : channel.getItems()) {
						if (item.isUnread()) {
							if (item.getDate().before(nextItem.getDate())) {
								nextItem = item;
							}
						}
					}
				}
			}
		}
		if (nextItem.getDate().equals(beginningOfTime)
				|| nextItem.getDate().equals(endOfTime)) {
			System.out.println("Nie ma nieprzeczytanych wiadomosci.");
		} else {
			nextItem.markAsRead();
			allChannels.get(nextItem.getChannelKey()).updateUnreadItemsCount();
			preview.setCurrent(new Preview(nextItem));
		}
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
	 * Sprawdza, czy w danym kanale nie pojawily sie nowe wiadomosci i jesli
	 * tak, to dodaje je do listy wiadomosci kanalu.
	 */
	static void updateChannel(Channel channel) throws Exception {
		channel.update();
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

