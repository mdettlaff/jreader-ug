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
	 * Katalog w którym są przechowywane ustawanienia użytkownika
	 */
	private static File configDir = null;
	/**
	 * Plik zawierający ustawienia programu
	 */
	private static File configFile = null;
	/**
	 * Katalog w którym znajdują się dane programu
	 */
	private static File cacheDir = null;
	/**
	 * Obiekt przechowywujący ustawienia programu
	 */
	private static ConfigData cd = null;
	/**
	 * Konstruktor automatycznie wczytuje ustawienia z pliku lub używa domyślnych ustawień
	 */
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
	 * Metoda zwraca ścieżkę do katalogu konfiguracyjnego
	 * @return ścieżka do katalogu, null gdy ścieżka jest niezdefiniowana
	 */
	public File getConfigDir() {
		return Config.configDir;
	}
	/**
	 * Metoda zwraca ścieżkę do pliku z ustawieniami programu
	 * @return ścieżka do pliku, null gdy ścieżka jest niezdefiniowana
	 */
	public File getConfigFile() {
		return Config.configFile;
	}
	/**
	 * Zwraca ścieżkę do katalogu w którym są przechowywane dane programu.
	 * @return Ścieżka do katalogu z danymi programu.
	 */
	public File getCacheDir() {
		return Config.cacheDir;
	}
	/**
	 * Metoda zwraca informacje czy elementy mają być wyswietlone od najnowszych 
	 * @return true, gdy elementy mają być wyświetlane od najnowszych, false - odnajstarszych
	 */
	public boolean getSortByNewest() {
		return Config.cd.sortByNewest;
	}
	/**
	 * Metoda zwraca informacje czy kanały podczas uruchomienia programu mają pobrać nowe elementy
	 * @return true, gdy kanały mają pobrać nowe elementy lub false, gdy nie
	 */
	public boolean getUpdateAllOnStartup() {
		return Config.cd.updateAllOnStartup;
	}
	/**
	 * Metoda zwraca informacje co ile minut kanały mają automatycznie pobierać nowe elementy
	 * @return ilość minut co ile kanały mają automatycznie pobierać nowe elementy, 0 (zero) - nigdy
	 */
	public int getAutoUpdateMinutes() {
		return Config.cd.autoUpdateMinutes;
	}
	/**
	 * Metoda zwraca informacje po ilu dniach od dodania element zostanie automatycznie usunięty
	 * @return ilość dni po których element zostanie automatycznie usunięty, 0 (zero) - zaraz po zamknięciu programu
	 */
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
	 * Metoda zapisuje bieżace ustawienia programu do pliku 
	 * @return zwraca true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	public boolean write() {
		return ReadWrite.write(Config.configFile, Config.cd);
	}
	/**
	 * Metoda wczytuje ustawienia programu z pliku
	 * @return zwraca true, gdy operacja się powiedzie lub false w przeciwnym wypadku
	 */
	public boolean read() {
		Config.cd = (ConfigData) ReadWrite.read(Config.configFile);
		return Config.cd != null;
	}
	/**
	 * Klasą prywatna przechowywująca ustawienia programu
	 */
	private class ConfigData implements Serializable {
		private static final long serialVersionUID = 1L;
		/**
		 * Kolejność sortowania elementów.
		 * Czy najnowsze mają być najpierw; jeśli false, to najstarsze najpierw.
		 */
		public boolean sortByNewest;
		/**
		 * Czy aktualizować wszystkie kanały automatycznie przy starcie programu.
		 */
		public boolean updateAllOnStartup;
		/**
		 * Co ile minut automatycznie aktualizować wszystkie kanaly.
		 * Wartość 0 wyłącza tą opcje.
		 */
		public int autoUpdateMinutes;
		/**
		 * Usuwa wiadomości starsze niż dana ilość dni.
		 * Wartość 0 wyłącza tą opcje.
		 */
		public int deleteOlderThanDays;
		/**
		 * Konstruktor ustawia wartości domyślne dla poszczególnych opcji
		 */
		public ConfigData() {
			super();
			/**
			 * Konfiguracja domyślna programu
			 */
			this.sortByNewest = true;
			this.updateAllOnStartup = false;
			this.autoUpdateMinutes = 0;
			this.deleteOlderThanDays = 10;
		}
	}
}
