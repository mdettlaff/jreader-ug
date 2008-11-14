import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Glowna klasa programu, przechowujaca obiekty przeznaczone do wyswietlania
 * w Graficznym Interfejsie Uzytkownika i posiadajaca metody obslugujace
 * podstawowe funkcjonalnosci programu, uzywane za pomoca interfejsu
 * uzytkownika. 
 */
class JReader {
  /**
   * Lista wszystkich kanalow.
   */
  private static Map<String, Channel> allChannels =
      new HashMap<String, Channel>();
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
	  System.out.println("Opis: " + preview.getCurrent().getHTML());
	}

      } else if (command.equals("add channel")) {
	System.out.print("Podaj adres URL: ");
	addChannel(in.readLine());
	System.out.println("Kanal zostal dodany");
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
	channels.get(nr).markAllAsRead();
      } else if (command.equals("update channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	channels.get(nr).update();
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
    allChannels.put(newChannel.getKey(), newChannel);
    channels.add(newChannel);
    // Sortujemy liste kanalow alfabetycznie
    Collections.sort(channels);
  }

  static Preview previousItem() {
    return preview.previous();
  }

  static Preview nextItem() {
    return preview.next();
  }

  static void updateAll() throws Exception {
    for (Channel channel : channels) {
      channel.update();
    }
  }

  static void nextUnread() {
    Item newestItem = new Item();
    newestItem.setDate(new Date(0)); // 1 stycznia 1970
    for (Channel channel : channels) {
      if (channel.getUnreadItemsCount() > 0) {
	for (Item item : channel.getItems()) {
	  if (item.isUnread()) {
	    if (item.getDate().after(newestItem.getDate())) {
	      newestItem = item;
	    }
	  }
	}
      }
    }
    if (newestItem.getDate().equals(new Date(0))) {
      System.out.println("Nie ma nieprzeczytanych wiadomosci.");
    } else {
      // szukamy najnowszego elementu znowu, zeby go oznaczyc jako przeczytany
      boolean foundAgain = false;
      for (Channel channel : channels) {
	if (channel.getUnreadItemsCount() > 0) {
	  for (Item item : channel.getItems()) {
	    if (item.isUnread()) {
	      if (item.getDate().equals(newestItem.getDate())) {
		item.markAsRead();
		foundAgain = true;
		break;
	      }
	    }
	  }
	}
	if (foundAgain) {
	  channel.updateUnreadItemsCount();
	  break;
       	}
      }
      preview.setCurrent(new Preview(newestItem));
    }
  }

  /**
   * Efekt klikniecia na wybrany element.
   */
  static void selectItem(Item item) {
    item.markAsRead();
    preview.setCurrent(new Preview(item));
    // aktualizujemy ilosc nieprzeczytanych elementow kanalu (przerywamy petle
    // po znalezieniu odpowiedniego kanalu i zaktualizowaniu go)
    for (Channel channel : allChannels.values()) {
      if (channel.updateUnreadItemsCount() != 0) {
	break;
      }
    }
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
    allChannels.remove(channels.get(index).getKey());
    channels.remove(index);
  }

  static Config getConfig() {
    return config;
  }
}

