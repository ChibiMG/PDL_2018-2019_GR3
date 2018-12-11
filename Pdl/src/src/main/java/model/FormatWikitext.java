package src.main.java.model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */
// ET SI ON METTAIT DES FORMATWIKITEXT EN PARAMETRE ?? PAS BESOIN DE CREER NOUVEAU FW DANS TEST ?
public class FormatWikitext
{

	public String wikitext;
	public ArrayList<Character> urlTitle = new ArrayList<Character>();
	public String urlfinal = new String("");
	public int nbcolonnes;
	public int nblignes;
	public int nbtab;
	public int tabCourant;
	public String titleCSV;



	public FormatWikitext(){
		this.wikitext = new String();
	}

	public FormatWikitext(String wikitext){
		this.wikitext = wikitext;
	}

	/******************** DEBUT DE LA RECUPERATION DE L URL ***************/

	/**
	 * <!-- begin-user-doc -->
	 * Split sur le title : renvoi ce qu'il y a après le title
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	public FormatWikitext recupererURL() {
		String[] separateur = this.wikitext.split("title");
		FormatWikitext fw = new FormatWikitext(separateur[1]);
		char[] chars = separateur[1].toCharArray();
		for(int i =0; i<separateur[1].length(); i++) {
			urlTitle.add(i, chars[i]);
		}
		urlTitle.remove(0); //remove le ">"
		for(int j=separateur[1].length()-2; j>separateur[1].length()-16;j--) {
			urlTitle.remove(j);
		}
		for(int i =0;i<urlTitle.size();i++) {
			urlfinal += urlTitle.get(i); 
		}
		fw.wikitext = urlfinal;
		FormatWikitext result = new FormatWikitext("https://en.wikipedia.org/w/index.php?title=" + fw.wikitext + "&action=edit");
		return result;
	}
	

	/******************** FIN DE LA RECUPERATION DE L URL ***************/


	/******************* DEBUT DE LA RECUPERATION DU TABLEAU ****************/

	/**
	 * <!-- begin-user-doc -->
	 * Split sur le debut du tableau, tab[1] correspond au debut du tableau + fin de la page
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	public FormatWikitext getTableau() {
		String[] wikiseparateur = this.wikitext.split("\\{\\|");
		FormatWikitext premiersplit = new FormatWikitext(wikiseparateur[1]);
		FormatWikitext clone = premiersplit;
		//FormatWikitext secondsplit.wikitext = new FormatWikitext(wikiseparateur2[0]);
		String[] wikiseparateur2 = clone.wikitext.split("\\|}");
		FormatWikitext secondsplit = new FormatWikitext(wikiseparateur2[0]);
		return secondsplit;
	}


	/******************* FIN DE LA RECUPERATION DU TABLEAU ****************/


	/******************* DEBUT DU HEAD *****************************/

	/**
	 * <!-- begin-user-doc -->
	 * Renvoi le head
	 * 
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	public FormatWikitext getHead() {
		String[]separateur = this.wikitext.split("\\|\\-");
		String[]tabfinal = new String[separateur.length-1];
		ArrayList<String> list = new ArrayList<String>();
		if(separateur[0].contains("!")) { /* cas spécial rencontré */
			for(int i=0; i<separateur.length;i++) {
				list.add(separateur[i]); //Si le premier split contiens des "!" qui représente une colonne, alors on l'ajoute dans la liste !
			}
		}
		else {
			for(int i=1; i<separateur.length;i++) {
				list.add(separateur[i]);
			}
		}
		tabfinal = list.toArray(tabfinal); //Converti la liste (qui contient le tableau entier, spliter sur les |--) en tableau
		String head = tabfinal[0];
		
		this.nbcolonnes = head.split("!").length-1;
		String[]separateur1 = head.split("!");
		String[]tabfinal1 = new String[nbcolonnes];
		for(int i=0; i<nbcolonnes;i++) {
			tabfinal1[i] = separateur1[i+1]; //on ne prend pas le split[0] car vide
			if(tabfinal1[i].contains("|")) { //Permet de supprimer les balises avant les noms des cols
				String[]separateur2 = tabfinal1[i].split("\\|");
				tabfinal1[i] = separateur2[1];
			}
			tabfinal1[i] = tabfinal1[i].replaceAll("[^\\wàâäÄÀÂéèêëÈÊËìîïÌÏÎòöôÒÖÔùüûÙÜÛç!#$€%&'`(),;:/@...]", " ");
		}
		FormatWikitext result = new FormatWikitext(Arrays.toString(tabfinal1));
		result.wikitext = result.wikitext.replaceAll("[^\\wàâäÄÀÂéèêëÈÊËìîïÌÏÎòöôÒÖÔùüûÙÜÛç!#$€%&'`(),;:/@...]", " "); //enlève les corchets en trop
		return result;
	}


	public ProductionCSV headToCSV(FormatWikitext fw) {
		FormatWikitext wikitext = fw;
		ProductionCSV prod  = new ProductionCSV("");
		String[] res = wikitext.wikitext.split(",");
		String stringCSV = "";
		for(String str : res){
			if(str != null)
				stringCSV += str + "," ;
		}
		prod.csv = stringCSV;
		prod.csv = prod.csv.replaceAll("  ", " ");
		try {
			prod.generateCSV("zd", 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prod;
	}
	/*************************** FIN TRAVAIL SUR HEAD ****************************/


	//<th[^>]*>
	/**************************DEBUT TRAVAIL SUR LIGNES *********************/
	public FormatWikitext wikiRowPremierSplit() {
		FormatWikitext result = new FormatWikitext();
		String[] separateur = this.wikitext.split("\\|\\-");
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < separateur.length; i++) {
			list.add(separateur[i]);
		}
		
		if(separateur[0].contains("class")) {
			list.remove(0);
			list.remove(0);
		}
		
		else {
			list.remove(0);
		}
		
		String[] lignes = new String[list.size()];

		String nb_elems = separateur[1];
		int nbelements = nb_elems.split("[\n]").length-1;

		String[] elements = new String[nbelements];
		
		for(int i=0; i<list.size();i++) {
			lignes[i] = list.get(i);
			for(int j=0; j<nbelements;j++) {
				String[] split = lignes[i].split("\n");
				elements[j] = split[j+1];
				if(elements[j].contains(",")) { //si il y a des virgules dans le txt on les supprime
					elements[j] = elements[j].replaceAll(",", " ");
				}
				if(elements[j].contains("&lt;")) {
					elements[j] = elements[j].replaceAll("&lt[^>]*ref", "");
					elements[j] = elements[j].replaceAll("name[^>]*/", "");
					elements[j] = elements[j].replaceAll("&gt;", "");
					//elements[j] = elements[j].replaceAll("&lt.*ref>", "");

				}
				elements[j] = elements[j].replaceAll("[^\\wàâäÄÀÂéèêëÈÊËìîïÌÏÎòöôÒÖÔùüûÙÜÛç!#$€%&'`(),;:/@...]", " ");
				elements[j] = elements[j].replaceAll("!", "");

				result.wikitext += elements[j] + ",";
			}
			result.wikitext = result.wikitext + "\n";
		}
		return result;
	}

	public ProductionCSV rowToCSV(FormatWikitext fw) {
		FormatWikitext wikitext = fw;
		ProductionCSV prod  = new ProductionCSV("");
		String[] res = wikitext.wikitext.split(",");
		String stringCSV = "";
		for(String str : res){
			if(str != null)
				stringCSV += str + "," ;
		}
		prod.csv = stringCSV;
		prod.csv = prod.csv.replaceAll("  ", " ");
		try {
			prod.generateCSV("lignes", 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prod;
	}

	/************************* FIN TRAITEMENT LIGNES ********************/

	public void ToCSV(FormatWikitext head, FormatWikitext row) throws IOException {
		String result = "";
		ProductionCSV header = headToCSV(head);
		ProductionCSV body = rowToCSV(row);
		result = (header.csv + "\n" +body.csv);
		ProductionCSV prod = new ProductionCSV(result);
		prod.generateCSV("test", 0);
		System.out.println("Fichier créé avec succès.");
	}





	public int wikiNombreLigne() {
		int result=0;
		String[] nbligne = this.wikitext.split("! scope\\=row \\|");
		result = nbligne.length-1;
		return result;
	}



	public int wikiCountTabs() {
		Matcher matcher = Pattern.compile("wikitable").matcher(wikitext);
		int occur = 0;
		while(matcher.find()) {
			occur ++;
		}
		return occur;
	}


}
