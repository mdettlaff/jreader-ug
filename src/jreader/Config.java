package jreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Przechowuje ustawienia programu oraz umożliwia zapisywanie i odczytywanie
 * ich z pliku.
 */
public class Config implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Sciezka do katalogu konfiguracyjnego
	 */
	private static File configDir = null;
	/**
	 * Sciezka do pliku zawierajacego ustawienia programu
	 */
	private static File configFile = null;
	/**
	 * Obiekt zawierajacy ustawienia programu
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
		if(Config.cd == null) {
			if(!read()) {
				Config.cd = new ConfigData();
				write();
			}
		}
	}
	/**
	 * metoda zwraca sciezke do katalogu konfiguracyjnego
	 * @return sciezka do katalogu typu String lub pusty String, gdy sciezki nie zdefiniowano
	 */
	public String getConfDir() {
		return Config.configDir.getPath();
	}
	/**
	 * metoda zwraca sciezke do pliku z ustawienia programu
	 * @return sciezka do pliku typu String lub pusty String, gdy sciezka nie jest zdefiniowana
	 */
	public String getConfFile() {
		return Config.configFile.getPath();
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
	/**
	 * metoda zapisuje biezace ustawienia programu do pliku 
	 * @return zwraca true, gdy operacja sie powiedzie lub false w przeciwnym wypadku
	 */
	public boolean write() {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(Config.configFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(Config.cd);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if(oos != null)
					oos.close();
				if(fos != null)
					fos.close();
			} catch(Exception e) {}
		}
		return true;
	}
	/**
	 * metoda wczytuje ustawienia programu z pliku
	 * @return zwraca true, gdy operacja sie powiedzie lub false w przeciwnym wypadku
	 */
	public boolean read() {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Config.configFile);
			ois = new ObjectInputStream(fis);
			Config.cd = (ConfigData) ois.readObject();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if(ois != null)
					ois.close();
				if(fis != null)
					fis.close();
			} catch(Exception e) {}
		}
		return true;
	}
	
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
		
		public ConfigData() {
			super();
			/**
			 * Konfiguracja domyślna
			 */
			this.sortByNewest = true;
			this.updateAllOnStartup = false;
			this.autoUpdateMinutes = 0;
			this.deleteOlderThanDays = 10;
		}
	}
}
