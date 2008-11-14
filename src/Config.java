import java.io.Serializable;


/**
 * Zawiera ustawienia programu, wybrane przez uzytkownika.
 */
class Config implements Serializable {
  /**
   * Kolejnosc sortowania elementow.
   * Czy najnowsze maja byc najpierw; jesli false, to najstarsze najpierw.
   */
  private static boolean sortByNewest;
  /*
   * Czy aktualizowac wszystkie kanaly automatycznie przy starcie programu.
   */
  // TODO
  private static boolean updateAllOnStartup;
  /*
   * Co ile minut automatycznie aktualizowac wszystkie kanaly.
   * Wartosc 0 wylacza ta opcje.
   */
  // TODO
  private static int autoUpdateMinutes;
  /*
   * Usuwa wiadomosci starsze niz dana ilosc dni.
   * Wartosc 0 wylacza ta opcje.
   */
  // TODO
  private static int deleteOlderThanDays;

  Config() {
    sortByNewest = true;
    updateAllOnStartup = false;
    autoUpdateMinutes = 0;
    deleteOlderThanDays = 0;
  }


  static boolean getSortByNewest() {
    return sortByNewest;
  }

  static boolean getUpdateAllOnStartup() {
    return updateAllOnStartup;
  }

  static int getAutoUpdateMinutes() {
    return autoUpdateMinutes;
  }

  static int getDeleteOlderThanDays() {
    return deleteOlderThanDays;
  }


  static void setSortByNewest(boolean b) {
    sortByNewest = b;
  }

  static void setUpdateAllOnStartup(boolean b) {
    updateAllOnStartup = b;
  }

  static void setAutoUpdateMinutes(int m) {
    autoUpdateMinutes = m;
  }

  static void setDeleteOlderThanDays(int d) {
    deleteOlderThanDays = d;
  }
}

