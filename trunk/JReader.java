import java.io.*;
import java.net.*;

class JReader {
  public static void main(String[] args) throws Exception {
    System.out.println("Kanal RSS ze strony Bzyla:\n");
    URL url = new URL("http://inf.univ.gda.pl:8001/rss/jp2.xml");
    BufferedReader in = new BufferedReader(
			    new InputStreamReader(
			    url.openStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      System.out.println(inputLine);

    in.close();
  }
}
