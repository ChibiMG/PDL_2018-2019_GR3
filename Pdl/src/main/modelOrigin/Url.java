package modelOrigin;



import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */

/**
 * @author Romiche
 *
 */
public class Url extends FormatHTML {

	public String url;
	public Url urltest;
	//public URL Url;
	

	public Url() {
		this.url = new String();
	}
	

	public Url(String url) {
		this.url = url;
	}
	
	/*public Url(URL Url) {
		this.Url = Url;
	}*/
	
	public Url getUrltest() {
		return this.urltest;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

		
	/**
	 * 
	 * Methode booleen isValidUrl()
	 * 
	 * 
	 * @return true si Url est valide
	 * @return false si Url n'est pas valide
	 * @generated
	 */
	public boolean isValidUrl() { 
		// int ch;
		try {
			URL urlt = new URL(url);
			HttpURLConnection connexion = (HttpURLConnection) urlt.openConnection();
			InputStream flux = connexion.getInputStream();
			//System.out.println("Status de la connexion de " + urlt + ": " + connexion.getResponseMessage());
			if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK)
				// while ((ch = flux.read()) != -1)
				// System.out.print((char) ch); //Affiche le code HTML de la page
				//System.out.println("--> Url valide");
			flux.close();
			connexion.disconnect();
			return true;
		} catch (Exception e) {
			System.out.println(e + "\n--> Url non valide");
			return false;
		}
	}

	//
	public int[] UrlToHTML() throws IOException {
		int [] nbTab = new int[2];
		if(isValidUrl()) {
			try {
				Document doc = Jsoup.connect(getUrl()).get();
				FormatHTML fhtml =new FormatHTML(doc.html());
				fhtml.ToCSV();
				nbTab[0] = fhtml.getNbTab();
				nbTab[1] = fhtml.getNbTabSucces();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		return nbTab;
	}
	

	public String HTML() throws IOException {
		if(isValidUrl()) {
		Document doc = Jsoup.connect(getUrl()).get();
		return doc.html();
		}
		else return null;
	}


	/*@Override
	public String toString() {
		return "Url [Url=" + Url + "]";
	}*/
	
}

