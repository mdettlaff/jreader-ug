package jreader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Przechowuje informacje o wszystkich kanałach i elementach oraz umożliwia
 * zapisanie ich do pliku i wczytanie ich z pliku.
 */
public class Channels implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Channel> channels = null;
	private Map<String, Item> items = null;
	/**
	 * Inicjuje podstawowe wartości obiektu
	 */
	public Channels() {
		if(!readChannels()) {
			this.channels = new HashMap<String, Channel>();
			writeChannels();
		}
		if(!readItems()) {
			this.items = new HashMap<String, Item>();
			writeItems();
		}
	}
	/**
	 * Dodaje kanał oraz elementy
	 * Uwaga! Dodawane identyfikatory elementów nie są dodawane do kanału
	 * @param channel dodawany kanał
	 * @param items lista dodawanych elementów
	 */
	public void add(Channel channel, List<Item> items) {
		try {
			this.channels.put(channel.getId(), channel);
			for(Item item: items) {
				try {
					this.items.put(item.getId(), item);
				} catch(NullPointerException e) {
					continue;
				}
			}
		} catch(NullPointerException e) {
			return;
		}
	}
	/**
	 * Dodaje kanał, jeśli element istnieje, zostanie nadpisany
	 * @param channel dodawany kanał
	 */
	public void add(Channel channel) {
		try {
			this.channels.put(channel.getId(), channel);
		} catch(NullPointerException e) {
			return;
		}
	}
	/**
	 * Dodaje element, jeśli element istnieje, zostanie nadpisany
	 * Uwaga! Nie dodaje identyfikatora elementu do kanału do którego należy
	 * @param item dodawany element
	 */
	public void addItem(Item item) {
		try {
			this.items.put(item.getId(), item);
		} catch(NullPointerException e) {
			return;
		}
	}
	/**
	 * Sprawdza czy kanał o podanym identyfikatorze istnieje
	 * @param channelId identyfikator kanału
	 * @return true jeśli istnieje, false w przeciwnym wypadku
	 */
	public boolean containsChannel(String channelId) {
		return this.channels.containsKey(channelId);
	}
	/**
	 * Sprawdza czy element o podanym identyfikatorze istnieje
	 * @param itemId identyfikator elementu
	 * @return true jeśli istnieje, false w przeciwnym wypadku
	 */
	public boolean containsItem(String itemId) {
		return this.items.containsKey(itemId);
	}
	/**
	 * Zwraca kanał o podanym identyfikatorze
	 * @param channelId identyfikator kanału
	 * @return kanał lub null, gdy kanał o podanym identyfikatorze nie istnieje
	 */
	public Channel getChannel(String channelId) {
		return this.channels.get(channelId);
	}
	/**
	 * Zwraca listę wszystkich kanałów
	 * @return lista kanałów lub pusta lista, gdy nie ma żadnych kanałów
	 */
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(this.channels.values());
	}
	/**
	 * Zwraca listę kanałów, które posiadają podany tag
	 * @param tag
	 * @return lista kanałów lub pusta lista, gdy żaden kanał nie posiada podanego tagu
	 */
	public List<Channel> getChannels(String tag) {
		List<Channel> channels = new ArrayList<Channel>();
		for(Channel channel: this.channels.values())
			for(String t: channel.getTags())
				if(tag.equalsIgnoreCase(t)) {
					channels.add(channel);
					break;
				}
		return channels;
	}
	/**
	 * Zwraca element o podanym identyfikatorze
	 * @param itemId identyfikator elementu
	 * @return element lub null, gdy element o podanym identyfikatorze nie istnieje
	 */
	public Item getItem(String itemId) {
		return this.items.get(itemId);
	}
	/**
	 * Zwraca listę elementów z kanału o podanym identyfikatorze
	 * @param channelId identyfikator kanału
	 * @return lista elementów, gdy kanał o podanym identyfikatorze nie istnieje, zwróci pustą liste
	 */
	public List<Item> getItems(String channelId) {
		List<Item> items = new ArrayList<Item>();
		try {
			for(String itemId: this.channels.get(channelId).getItems())
				items.add(this.items.get(itemId));
		} catch(NullPointerException e) {}
		return items;
	}
	/**
	 * Usuwa kanał o podanym identyfikatorze
	 * Uwaga! Nie usuwa elementów
	 * @param channelId identyfikator kanału
	 */
	public void removeChannel(String channelId) {
		this.channels.remove(channelId);
	}
	/**
	 * Usuwa element o podanym identyfikatorze
	 * Uwaga! Nie usuwa identyfikatora elementu z kanału, który go posiada
	 * @param itemId identyfikator kanału
	 */
	public void removeItem(String itemId) {
		this.items.remove(itemId);
	}
	/**
	 * Usuwa wszystkie elementy, które są starsze niż podana ilość dni w obiekcie Config
	 * Usuwa także z kanału
	 */
	public void removeItems() {
		Config config = new Config();
		if(config.getDeleteOlderThanDays() > 0) {
			Calendar currentDate = Calendar.getInstance();
			Calendar itemDate = Calendar.getInstance();
			List<Item> removeItems = new ArrayList<Item>();
			for(Item item: this.items.values()) {
				itemDate.setTime(item.getCreationDate());
				itemDate.set(Calendar.DATE, itemDate.get(
						Calendar.DATE) + config.getDeleteOlderThanDays());
				if(currentDate.compareTo(itemDate) > 0)
					removeItems.add(item);
			}
			for(Item item: removeItems) {
				try {
					this.channels.get(item.getChannelId()).removeItem(item.getId());
					this.items.remove(item.getId());
				} catch(NullPointerException e) {
					continue;
				}
			}
		}
	}
	/**
	 * Zapisuję kanały i elementy do pliku
	 * @return true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	public boolean write() {
		return writeChannels() && writeItems();
	}
	/**
	 * Zapisuję obiekt przechowujący kanały do pliku
	 * @return true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	private boolean writeChannels() {
		Config config = new Config();
		return ReadWrite.write(
				new File(config.getCacheDir().getPath() + File.separator + "channels"), this.channels);
	}
	/**
	 * Wczytuję z pliku obiekt przechowujący kanały
	 * @return true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	@SuppressWarnings("unchecked")
	private boolean readChannels() {
		Config config = new Config();
		this.channels = (Map<String, Channel>) ReadWrite.read(
				new File(config.getCacheDir().getPath() + File.separator + "channels"));
		return this.channels != null;
	}
	/**
	 * Zapisuję obiekt przechowujący elementy do pliku
	 * @return true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	private boolean writeItems() {
		Config config = new Config();
		return ReadWrite.write(
				new File(config.getCacheDir().getPath() + File.separator + "items"), this.items);
	}
	/**
	 * Wczytuję z pliku obiekt przechowujący elementy
	 * @return true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	@SuppressWarnings("unchecked")
	private boolean readItems() {
		Config config = new Config();
		this.items = (Map<String, Item>) ReadWrite.read(
				new File(config.getCacheDir().getPath() + File.separator + "items"));
		return this.items != null;
	}
}
