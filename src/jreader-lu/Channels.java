package jreader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channels implements Serializable {
	private static final long serialVersionUID = 1L;
	private Config config = null;
	private Map<String, Channel> channels = null;
	private Map<String, Item> items = null;
	
	public Channels() {
		this.config = new Config();
		if(!readChannels()) {
			this.channels = new HashMap<String, Channel>();
			writeChannels();
		}
		if(!readItems()) {
			this.items = new HashMap<String, Item>();
			writeItems();
		}
	}
	
	public void addChannel(String channelURL) {
		/* 
		 * metoda nie jest skonczona
		 */
		Channel channel = new Channel(channelURL);
		this.channels.put(channel.getId(), channel);
		writeChannels();
	}
	
	public Channel getChannel(String channelId) {
		return this.channels.get(channelId);
	}
	
	public Collection<Channel> getChannels() {
		return this.channels.values();
	}
	
	public Item getItem(String itemId) {
		return this.items.get(itemId);
	}
	
	public Collection<Item> getItems() {
		return this.items.values();
	}
	
	public void removeChannel(String channelId) {
		Channel channel = this.channels.get(channelId);
		for(String itemId: channel.getItems())
			this.items.remove(itemId);
		this.channels.remove(channelId);
		writeChannels();
		writeItems();
	}
	
	public void removeItem(String itemID) {
		this.channels.get(this.items.get(itemID).getChannelId()).removeItem(itemID);
		this.items.remove(itemID);
		writeChannels();
		writeItems();
	}
	
	public void removeOldItems() {
		Calendar currentDate = Calendar.getInstance();
		Calendar itemDate = Calendar.getInstance();
		List<Item> removeItems = new ArrayList<Item>();
		for(Item item: this.items.values()) {
			itemDate.setTime(item.getCreationDate());
			itemDate.set(Calendar.DATE, itemDate.get(Calendar.DATE) + this.config.getDeleteOlderThanDays());
			if(currentDate.compareTo(itemDate) > 0)
				removeItems.add(item);
		}
		for(Item item: removeItems) {
			this.channels.get(item.getChannelId()).removeItem(item.getId());
			this.items.remove(item.getId());
		}
		writeChannels();
		writeItems();
	}
	
	public void selectItem(String itemId) {
		this.items.get(itemId).setRead(true);
		writeItems();
	}
	
	public void selectItems(List<String> itemsId) {
		for(String itemId: itemsId)
			this.items.get(itemId).setRead(true);
		writeItems();
	}
	
	public boolean writeChannels() {
		return ReadWrite.write(
				new File(config.getCacheDir().getPath() + File.separator + "channels"), this.channels);
	}

	@SuppressWarnings("unchecked")
	public boolean readChannels() {
		this.channels = (Map<String, Channel>) ReadWrite.read(
				new File(config.getCacheDir().getPath() + File.separator + "channels"));
		return this.channels != null;
	}
	
	public boolean writeItems() {
		return ReadWrite.write(
				new File(config.getCacheDir().getPath() + File.separator + "items"), this.items);
	}

	@SuppressWarnings("unchecked")
	public boolean readItems() {
		this.items = (Map<String, Item>) ReadWrite.read(
				new File(config.getCacheDir().getPath() + File.separator + "items"));
		return this.items != null;
	}
}
