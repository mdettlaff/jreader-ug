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
	 * Katalog w którym są przechowywane ustawienia użytkownika i dane programu.
	 */
	private static File configDir = null;
	/**
	 * Plik zawierający ustawienia programu.
	 */
	private static File configFile = null;
	/**
	 * Katalog w którym znajduje się zachowany stan programu (kanały i elementy).
	 */
	private static File cacheDir = null;
	/**
	 * Katalog w którym znajdują się pobrane ikony kanałów.
	 */
	private static File shortcutIconsDir = null;
	/**
	 * Obiekt przechowywujący ustawienia programu.
	 */
	private static ConfigData cd = null;

	/**
	 * Automatycznie wczytuje ustawienia z pliku lub używa domyślnych ustawień.
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
				Config.configDir = new File("data");
			else
				Config.configDir = new File("data");
			Config.configDir.mkdirs();
		}
		if(Config.configFile == null) {
			Config.configFile = new File(Config.configDir.getPath() + File.separator + "config");
		}
		if(Config.cacheDir == null) {
			Config.cacheDir = new File(Config.configDir.getPath() + File.separator + "cache");
			Config.cacheDir.mkdirs();
		}
		if(Config.shortcutIconsDir == null) {
			Config.shortcutIconsDir = new File(Config.cacheDir.getPath() + File.separator + "shortcut_icons");
			Config.shortcutIconsDir.mkdirs();
		}
		if(Config.cd == null) {
			if(!read()) {
				Config.cd = new ConfigData();
				write();
			}
		}
	}

	/**
	 * Zwraca ścieżkę do głównego katalogu konfiguracyjnego, gdzie przechowywane
	 * są ustawienia, stan oraz dane programu.
	 *
	 * @return Ścieżka do katalogu, <code>null</code> gdy ścieżka jest
	 *         niezdefiniowana.
	 */
	public File getConfigDir() {
		return Config.configDir;
	}

	/**
	 * Zwraca ścieżkę do pliku z ustawieniami programu.
	 *
	 * @return Ścieżka do pliku, <code>null</code> gdy ścieżka jest
	 *         niezdefiniowana.
	 */
	public File getConfigFile() {
		return Config.configFile;
	}

	/**
	 * Zwraca ścieżkę do katalogu w którym przechowywany jest stan programu.
	 *
	 * @return Ścieżka do katalogu z zapisanymi kanałami i elementami.
	 */
	public File getCacheDir() {
		return Config.cacheDir;
	}

	/**
	 * Zwraca ścieżkę do katalogu w którym są przechowywane ikony kanałów.
	 *
	 * @return Ścieżka do katalogu z pobranymi ikonami kanałów.
	 */
	public File getShortcutIconsDir() {
		return Config.shortcutIconsDir;
	}

	/**
	 * Zwraca informację czy elementy mają być wyswietlone od najnowszych.
	 *
	 * @return <code>true</code>, gdy elementy mają być wyświetlane
	 *         od najnowszych, <code>false</code> - od najstarszych.
	 */
	public boolean getSortByNewest() {
		return Config.cd.sortByNewest;
	}

	/**
	 * Zwraca informację czy kanały podczas uruchomienia programu mają pobrać
	 * nowe elementy.
	 *
	 * @return <code>true</code>, gdy kanały mają pobrać nowe elementy lub
	 *         <code>false</code>, gdy nie.
	 */
	public boolean getUpdateAllOnStartup() {
		return Config.cd.updateAllOnStartup;
	}

	/**
	 * Zwraca informację co ile minut kanały mają automatycznie pobierać
	 * nowe elementy.
	 *
	 * @return Ilość minut co ile kanały mają automatycznie pobierać nowe
	 *         elementy, 0 (zero) - nigdy.
	 */
	public int getAutoUpdateMinutes() {
		return Config.cd.autoUpdateMinutes;
	}

	/**
	 * Zwraca informację po ilu dniach od dodania element zostanie automatycznie
	 * usunięty.
	 *
	 * @return Ilość dni po których element zostanie automatycznie usunięty,
	 *         0 (zero) - nigdy.
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
	 * Zapisuje bieżące ustawienia programu do pliku.
	 *
	 * @return <code>true</code>, gdy operacja się powiedzie lub
	 *         <code>false</code> w przeciwnym wypadku.
	 */
	public boolean write() {
		return ReadWrite.write(Config.configFile, Config.cd);
	}

	/**
	 * Wczytuje ustawienia programu z pliku.
	 *
	 * @return <code>true</code>, gdy operacja się powiedzie lub
	 *         <code>false</code> w przeciwnym wypadku.
	 */
	public boolean read() {
		Config.cd = (ConfigData) ReadWrite.read(Config.configFile);
		return Config.cd != null;
	}

	/**
	 * Klasa prywatna przechowująca ustawienia programu.
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
			 * Konfiguracja domyślna programu.
			 */
			this.sortByNewest = true;
			this.updateAllOnStartup = false;
			this.autoUpdateMinutes = 0;
			this.deleteOlderThanDays = 30;
		}
	}
}

