package jreader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Tekstowy interfejs użytkownika.
 */
public class TextUI {

	/** Nie można tworzyć obiektów tej klasy. */
	private TextUI() { }

	/**
	 * Uruchomienie tekstowego interfejsu użytkownika. Komunikacja
	 * z użytkownikiem odbywa się za pomocą standardowego wejścia i wyjścia.
	 */
	public static void run() {
		DateFormat shortDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String command = new String();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
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
					System.out.print("import\t\t");
					System.out.println("export");
					System.out.print("help\t\t");
					System.out.println("quit");
				} else if (command.equals("show channels")) {
					if (JReader.getChannels().size() == 0) {
						System.out.println("Lista subskrypcji jest pusta.");
					} else {
						Channel channel;
						for (int i=0; i < JReader.getChannels().size(); i++) {
							channel = JReader.getChannel(i);
							System.out.print("Kanal " + (i+1) + ": ");
							if (channel.isFail()) {
								System.out.print("FAIL ");
							}
						 	System.out.print(channel.getTitle());
							if (channel.getUnreadItemsCount() > 0) {
								System.out.println(" (" + channel.getUnreadItemsCount() + ")");
							} else {
								System.out.println();
							}
						}
					}
				} else if (command.equals("show items")) {
					if (JReader.getItems().size() == 0) {
						System.out.println("Lista wiadomosci jest pusta.");
					} else {
						for (int i=0; i < JReader.getItems().size(); i++) {
							Item item = JReader.getItems().get(i);
							System.out.print((i+1) + ": ");
							if (item.getDate() != null) {
								System.out.print(shortDateFormat.format(item.getDate()) + " ");
							}
							if (item.isUnread()) {
								System.out.print("N ");
							}
							if (JReader.getChannelFromHash(item.getChannelKey()).getTitle().
									length() > 12) {
								System.out.print(JReader.getChannelFromHash(
											item.getChannelKey()).getTitle().substring(0, 12) + " ");
							} else {
								System.out.print(JReader.getChannelFromHash(
											item.getChannelKey()).getTitle() + " ");
							}
							System.out.println(item.getTitle());
						}
					}
				} else if (command.equals("show preview")) {
					if (JReader.getPreview().getCurrent() == null) {
						System.out.println("Nie wybrano zadnej wiadomosci.");
					} else {
						System.out.println(JReader.getPreview().getCurrent().getTitle());
						System.out.println("Link: " + JReader.getPreview().
								getCurrent().getLink());
						if (JReader.getPreview().getCurrent().getAuthor() != null) {
							System.out.println("Autor: " + JReader.getPreview().
									getCurrent().getAuthor());
						}
						if (JReader.getPreview().getCurrent().getSource() != null) {
							System.out.println("Zrodlo: " + JReader.getPreview().
									getCurrent().getSource());
						}
						System.out.println("Opis: "+JReader.getPreview().
								getCurrent().getHTML());
					}
				} else if (command.equals("show tags")) {
					if (JReader.getTags().size() == 0) {
						System.out.println("Lista tagow jest pusta.");
					} else {
						for (String tag : JReader.getTags()) {
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
						JReader.addChannel(url, tags);
						System.out.println("Kanal zostal dodany");
					} catch (LinkNotFoundException lnfe) {
						System.out.println("Nie znaleziono kanalow RSS na tej stronie.");
					} catch (MalformedURLException mue) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Podany URL jest nieprawidlowy.");
					} catch (SAXParseException spe) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
						System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
						System.out.println("Szczegoly: " + spe.getLocalizedMessage());
					} catch (SAXException saxe) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Blad parsera XML.");
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
					} catch (Exception e) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Nastapil nieoczekiwany blad.");
					}
				} else if (command.equals("previous item")) {
					if (JReader.previousItem() == null) {
						System.out.println("Nie mozna sie cofnac.");
					}
				} else if (command.equals("next item")) {
					if (JReader.nextItem() == null) {
						System.out.println("Nie mozna przejsc dalej.");
					}
				} else if (command.equals("update all")) {
					for (Channel channel : JReader.getChannels()) {
						try {
							JReader.updateChannel(channel);
							channel.setFail(false);
						} catch (SAXParseException spe) {
							System.out.println("Nie mozna zaktualizowac kanalu "
									+ channel.getTitle() + ".");
							System.out.println("Zrodlo nie jest prawidlowym plikiem XML.");
							System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
							System.out.println("Szczegoly: " + spe.getLocalizedMessage());
							channel.setFail(true);
						} catch (SAXException saxe) {
							System.out.print("Nie mozna dodac kanalu.");
							System.out.println(" Blad parsera XML.");
						} catch (SocketException se) {
							System.out.println("Nie mozna zaktualizowac kanalu "
									+ channel.getTitle() + ".");
							System.out.println("Szczegoly: " + se.getLocalizedMessage());
							channel.setFail(true);
						} catch (FileNotFoundException fnfe) {
							System.out.println("Nie mozna zaktualizowac kanalu "
									+ channel.getTitle() + ".");
							System.out.println("Brak polaczenia ze strona.");
							channel.setFail(true);
						} catch (IOException ioe) {
							System.out.println("Nie mozna zaktualizowac kanalu "
									+ channel.getTitle() + ".");
							System.out.println("Brak polaczenia ze strona.");
							channel.setFail(true);
						} catch (Exception e) {
							System.out.print("Nie mozna zaktualizowac kanalu.");
							System.out.println(" Nastapil nieoczekiwany blad.");
							channel.setFail(true);
						}
					}
					System.out.println("Kanaly zostaly zaktualizowane.");
				} else if (command.equals("next unread")) {
					if (!JReader.nextUnread()) {
						System.out.println("Nie ma nieprzeczytanych wiadomosci.");
					}
				} else if (command.equals("select item")) {
					System.out.print("Podaj numer elementu: ");
					int nr = new Integer(in.readLine()) - 1;
					JReader.selectItem(JReader.getItems().get(nr));
				} else if (command.equals("select channel")) {
					System.out.print("Podaj numer kanalu: ");
					int nr = new Integer(in.readLine()) - 1;
					JReader.selectChannel(nr);
				} else if (command.equals("select all")) {
					JReader.selectAll();
				} else if (command.equals("select unread")) {
					JReader.selectUnread();
				} else if (command.equals("select tag")) {
					System.out.print("Wybierz tag (all - wszystkie, " +
							"untagged - nieoznaczone): ");
					JReader.selectTag(in.readLine());
				} else if (command.equals("mark channel")) {
					System.out.print("Podaj numer kanalu: ");
					int nr = new Integer(in.readLine()) - 1;
					JReader.markChannelAsRead(JReader.getChannel(nr));
				} else if (command.equals("update channel")) {
					int nr = 0;
					try {
						System.out.print("Podaj numer kanalu: ");
						nr = new Integer(in.readLine()) - 1;
						JReader.updateChannel(JReader.getChannel(nr));
						JReader.getChannel(nr).setFail(false);
						System.out.println("Kanal zostal zaktualizowany.");
					} catch (SAXParseException spe) {
						System.out.print("Nie mozna zaktualizowac kanalu.");
						System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
						System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
						System.out.println("Szczegoly: " + spe.getLocalizedMessage());
						JReader.getChannel(nr).setFail(true);
					} catch (SAXException saxe) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Blad parsera XML.");
					} catch (SocketException se) {
						System.out.println("Nie mozna zaktualizowac kanalu. Szczegoly:");
						System.out.println(se.getLocalizedMessage());
						JReader.getChannel(nr).setFail(true);
					} catch (FileNotFoundException fnfe) {
						System.out.print("Nie mozna zaktualizowac kanalu.");
						System.out.println(" Brak polaczenia ze strona.");
						JReader.getChannel(nr).setFail(true);
					} catch (IOException ioe) {
						System.out.print("Nie mozna zaktualizowac kanalu.");
						System.out.println(" Brak polaczenia ze strona.");
						JReader.getChannel(nr).setFail(true);
					} catch (Exception e) {
						System.out.print("Nie mozna zaktualizowac kanalu.");
						System.out.println(" Nastapil nieoczekiwany blad.");
						JReader.getChannel(nr).setFail(true);
					}
				} else if (command.equals("edit tags")) {
					System.out.print("Podaj numer kanalu: ");
					int nr = new Integer(in.readLine()) - 1;
					if (JReader.getChannel(nr).getTags().size() != 0) {
						System.out.println("Tagi: " + JReader.getChannel(nr).
								getTagsAsString());
					} else {
						System.out.println("Ten kanal nie ma tagow.");
					}
					System.out.print("Podaj nowe tagi: ");
					JReader.editTags(JReader.getChannel(nr), in.readLine());
				} else if (command.equals("remove channel")) {
					System.out.print("Podaj numer kanalu: ");
					int nr = new Integer(in.readLine()) - 1;
					JReader.removeChannel(nr);
				} else if (command.equals("set sort")) {
					System.out.print("Sortuj wedlug (new/old): ");
					String choice = in.readLine().trim();
					if (choice.equals("old")) {
						JReader.getConfig().setSortByNewest(false);
						if (!JReader.getConfig().write()) {
							System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
						}
					} else if (choice.equals("new")) {
						JReader.getConfig().setSortByNewest(true);
						if (!JReader.getConfig().write()) {
							System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
						}
					} else {
						System.out.println("Nieprawidlowy wybor.");
					}
				} else if (command.equals("set delete")) {
					if (JReader.getConfig().getDeleteOlderThanDays() == 0) {
						System.out.println("Stare wiadomosci nie sa usuwane.");
					} else {
						System.out.println("Wiadomosci sa usuwane po "
								+ JReader.getConfig().getDeleteOlderThanDays() + " dniach.");
					}
					System.out.print("Po ilu dniach usuwac wiadomosci (0 - wcale): ");
					JReader.getConfig().setDeleteOlderThanDays(
							new Integer(in.readLine().trim()));
					if (!JReader.getConfig().write()) {
						System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
					}
				} else if (command.equals("import")) {
					try {
						System.out.print("Podaj lokalizacje pliku OPML: ");
						JReader.importChannelList(in.readLine());
					} catch (SAXParseException spe) {
						System.out.print("Nie mozna dokonac importu.");
						System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
						System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
						System.out.println("Szczegoly: " + spe.getLocalizedMessage());
					} catch (SAXException saxe) {
						System.out.print("Nie mozna dodac kanalu.");
						System.out.println(" Blad parsera XML.");
					} catch (FileNotFoundException fnfe) {
						System.out.print("Nie mozna dokonac importu.");
						System.out.println(" Podany plik nie istnieje.");
					} catch (Exception e) {
						System.out.print("Nie mozna dokonac importu.");
						System.out.println(" Nastapil nieoczekiwany blad.");
					}
				} else if (command.equals("export")) {
					try {
						System.out.print("Podaj plik docelowy: ");
						JReader.exportChannelList(in.readLine());
					} catch (IOException ioe) {
						System.out.print("Nie mozna dokonac eksportu.");
						System.out.println(" Zapisanie pliku jest niemozliwe.");
					} catch (Exception e) {
						System.out.print("Nie mozna dokonac eksportu.");
						System.out.println(" Nastapil nieoczekiwany blad.");
					}
				} else if (!command.equals("") && !command.equals("quit")) {
					System.out.println("Nieznane polecenie.");
				}
			}
		} catch (IOException ioe) {
			System.err.println("Blad interfejsu tekstowego.");
		}
	}
}

