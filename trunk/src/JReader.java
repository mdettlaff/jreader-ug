import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Glowna klasa programu, przechowujaca obiekty przeznaczone do wyswietlania
 * w Graficznym Interfejsie Uzytkownika.
 */
class JReader {
  /** Lista subskrypcji do wyswietlenia w GUI. */
  static List<Channel> channels = new ArrayList<Channel>();
  /** Lista wiadomosci do wyswietlenia w GUI. */
  static List<Item> items = new ArrayList<Item>();
  /** Tresc wiadomosci lub informacje o kanale do wyswietlenia w GUI. */
  static Preview preview;

  public static void main(String[] args) throws Exception {

    /*
     * Tekstowy interfejs uzytkownika, w celach testowych.
     */
    String command = new String();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    boolean justStarted = true;
    while (!(command.equals("quit"))) {
      if (justStarted) {
	command = "help";
	justStarted = false;
      } else {
	command = in.readLine();
      }
      if (command.equals("help")) {
	System.out.println("Dostepne komendy:");
	System.out.print("show channels\t");
	System.out.print("show items\t");
	System.out.println("show preview");
	System.out.print("add channel\t");
	System.out.print("update channel\t");
	System.out.println("remove channel");
	System.out.print("select channel\t");
	System.out.println("select item");
	//System.out.println("select unread");
	//System.out.println("select all");
	System.out.println("quit\n");
      } else if (command.equals("show channels")) {
	if (channels.size() == 0) {
	  System.out.println("Lista subskrypcji jest pusta.");
	} else {
	  Channel ch;
	  for (int i=0; i < channels.size(); i++) {
	    ch = channels.get(i);
	    System.out.println("Kanal " + (i+1) + ": " + ch.title);
	  }
	}
      } else if (command.equals("show items")) {
	if (items.size() == 0) {
	  System.out.println("Lista wiadomosci jest pusta.");
	} else {
	  for (int i=0; i < items.size(); i++) {
	    Item item = items.get(i);
	    System.out.println("Element " + (i+1) + ": " + item.title);
	    if (item.pubDate != null) {
	      System.out.println("Data publikacji: " + item.pubDate);
	    }
	  }
	}
      } else if (command.equals("show preview")) {
	if (preview == null) {
	  System.out.println("Nie wybrano zadnej wiadomosci.");
	} else {
	  if (preview.showingItem()) { // podglad elementu
	    System.out.println(preview.getTitle());
	    if (preview.getPubDate() != null) {
	      System.out.println("Data publikacji: " + preview.getPubDate());
	    }
	    System.out.println("Link: " + preview.getLink());
	    System.out.println("Opis: " + preview.getDescription());
	  } else { // podglad kanalu
	    System.out.println(preview.getTitle());
	    System.out.println("Link: " + preview.getLink());
	    System.out.println("Opis: " + preview.getDescription());
	    if (preview.getImageTitle() != null) {
	      System.out.println("Obrazek: " + preview.getImageTitle());
	    }
	    if (preview.getImageURL() != null) {
	      System.out.println("    URL: " + preview.getImageURL());
	    }
	    if (preview.getImageLink() != null) {
	      System.out.println("   link: " + preview.getImageLink());
	    }
	  }
	}
      } else if (command.equals("add channel")) {
	System.out.print("Podaj adres URL: ");
	channels.add(ChannelFactory.getChannelFromSite(in.readLine()));
	System.out.println("Kanal zostal dodany");
      } else if (command.equals("update channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	channels.get(nr).update();
      } else if (command.equals("remove channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	channels.remove(nr);
      } else if (command.equals("select channel")) {
	System.out.print("Podaj numer kanalu: ");
	int nr = new Integer(in.readLine()) - 1;
	items = channels.get(nr).getItems();
	preview = new Preview(channels.get(nr));
      } else if (command.equals("select item")) {
	System.out.print("Podaj numer elementu: ");
	int nr = new Integer(in.readLine()) - 1;
	preview = new Preview(items.get(nr));
      } else if (!command.equals("") && !command.equals("quit")) {
	System.out.println("Nieznane polecenie.");
      }
    }
    /*
     * Koniec tekstowego UI.
     */
  }
}

