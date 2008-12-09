package jreader;

import java.io.File;
import java.io.Serializable;

/**
 * Przechowuje ustawienia programu oraz umożliwia zapisywanie i odczytywanie
 * ich z pliku.
 */
public class Config implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Katalog w ktorym sa przechowywane ustawanienia urzytkownika
	 */
	private static File configDir = null;
	/**
	 * Plik zawierajacy ustawienia programu
	 */
	private static File configFile = null;
	/**
	 * Katalog w ktorym znajduja sie dane programu
	 */
	private static File cacheDir = null;
	/**
	 * Obiekt przechowywujacy ustawienia programu
	 */
	private static ConfigData cd = null;
	
	Config() { 
		if(Config.configDir == null) {
			String osName = System.getProperty("os.name");
			if(osName.equalsIgnoreCase("Linux"))
				Config.configDir = new File(System.getProperty("user.home") + File.separator + ".jreader");
			else if(osName.equalsIgnoreCase("Windows Xp") || osName.equalsIgnoreCase("Windows Vista") ||
					osName.equalsIgnoreCase("Windows 2000") || osName.equalsIgnoreCase("Windows NT"))
				Config.configDir = new File(System.getProperty("user.home") + File.separator + "JReader");
			else if(osName.equalsIgnoreCase("Windows 95") || osName.equalsIgnoreCase("Windows 98"))
				Config.configDir = new File("Config");
			else
				Config.configDir = new File("Config");
			Config.configDir.mkdirs();
		}
		if(Config.configFile == null) {
			Config.configFile = new File(Config.configDir.getPath() + File.separator + "config");
		}
		if(Config.cacheDir == null) {
			Config.cacheDir = new File(Config.configDir.getPath() + File.separator + "cache");
			Config.cacheDir.mkdirs();
		}
		if(Config.cd == null) {
			if(!read()) {
				Config.cd = new ConfigData();
				write();
			}
		}
	}
	/**
	 * metoda zwraca sciezke do katalogu konfiguracyjnego
	 * @return sciezka do katalogu typu File, null gdy sciezka jest niezdefiniowana
	 */
	public File getConfigDir() {
		return Config.configDir;
	}
	/**
	 * metoda zwraca sciezke do pliku z ustawienia programu
	 * @return sciezka do pliku typu File, null gdy sciezka jest niezdefiniowana
	 */
	public File getConfigFile() {
		return Config.configFile;
	}
	
	public File getCacheDir() {
		return Config.cacheDir;
	}

	public boolean getSortByNewest() {
		return Config.cd.sortByNewest;
	}
	
	public boolean getUpdateAllOnStartup() {
		return Config.cd.updateAllOnStartup;
	}
	
	public int getAutoUpdateMinutes() {
		return Config.cd.autoUpdateMinutes;
	}
	
	public int getDeleteOlderThanDays() {
		return Config.cd.deleteOlderThanDays;
	}
	
	public int getHistorySize() {
		return Config.cd.historySize;
	}
	
	public void setSortByNewest(boolean b) {
		Config.cd.sortByNewest = b;
	}
	
	public void setUpdateAllOnStartup(boolean b) {
		Config.cd.updateAllOnStartup = b;
	}
	
	public void setAutoUpdateMinutes(int m) {
		Config.cd.autoUpdateMinutes = m;
	}
	
	public void setDeleteOlderThanDays(int d) {
		Config.cd.deleteOlderThanDays = d;
	}
	
	public void setHistorySize(int i) {
		Config.cd.historySize = i;
	}
	/**
	 * metoda zapisuje biezace ustawienia programu do pliku 
	 * @return zwraca true, gdy operacja sie powiedzie lub false w przeciwnym wypadku
	 */
	public boolean write() {
		return ReadWrite.write(Config.configFile, Config.cd);
	}
	/**
	 * metoda wczytuje ustawienia programu z pliku
	 * @return zwraca true, gdy operacja sie powiedzie lub false w przeciwnym wypadku
	 */
	public boolean read() {
		Config.cd = (ConfigData) ReadWrite.read(Config.configFile);
		return Config.cd != null;
	}
	/**
	 * Klasa przechowywujaca ustawienia programu
	 */
	private class ConfigData implements Serializable {
		private static final long serialVersionUID = 1L;
		/**
		 * Kolejnosc sortowania elementow.
		 * Czy najnowsze maja byc najpierw; jesli false, to najstarsze najpierw.
		 */
		public boolean sortByNewest;
	    /**
	     * Czy aktualizowac wszystkie kanaly automatycznie przy starcie programu.
	     */
	    public boolean updateAllOnStartup;
	    /**
	     * Co ile minut automatycznie aktualizowac wszystkie kanaly.
	     * Wartosc 0 wylacza ta opcje.
	     */
	    public int autoUpdateMinutes;
	    /**
	     * Usuwa wiadomosci starsze niz dana ilosc dni.
	     * Wartosc 0 wylacza ta opcje.
	     */
	    public int deleteOlderThanDays;
	    /**
	     * Wielkosc historii przegladanych elementow
	     */
	    public int historySize;
		
		public ConfigData() {
			super();
			/**
			 * Konfiguracja domyślna
			 */
			this.sortByNewest = true;
			this.updateAllOnStartup = false;
			this.autoUpdateMinutes = 0;
			this.deleteOlderThanDays = 10;
			this.historySize = 10;
		}
	}
}
