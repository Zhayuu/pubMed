package pubMed.service;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import pubMed.article.Article;

public class Search {
	/**
	 * search for a given keyword from file and present the results 
	 */

		
	public static void searchKeyword(Document doc,String givenword) {
		/**
		 * This method is to search a given keyword in a document.
		 * @param doc
		 * @param givenword
		 */
		Element root = doc.getRootElement(); 
		int num = 0;
		// iterate through child elements of root
		 for (Iterator<Element> pumbedarticle = root.elementIterator(); pumbedarticle.hasNext();) {
			 Element p = pumbedarticle.next(); 
			 Element keywordlist = p.element("MedlineCitation").element("KeywordList");
			 if(keywordlist!=null) {
			// iterate through keyword elements of keywordlist 
			 for (Iterator<Element> keyword = keywordlist.elementIterator(); keyword.hasNext();) {
				 	Element k = keyword.next(); 	
		        	//System.out.println(k.getName());	
		        	//System.out.println(k.getText());	
		        	if(k.getText().toLowerCase().contains(givenword.toLowerCase())){
		        		//System.out.println("d");	
		        		//System.out.println(p.asXML());
		        		num++;
		        		present(p,num);
		        		break;  //return to the previous loop
			 		}
		      }}
		 }
	}
	public static void searchKeyword(Document doc, String givenword, int hitsPerpage) {
		/**
		 * This method is to search a given keyword in a document.
		 * 
		 * @param doc
		 * @param givenword
		 */
		Element root = doc.getRootElement();
		int num = 0;
		// iterate through child elements of root
		for (Iterator<Element> pumbedarticle = root.elementIterator(); pumbedarticle.hasNext() && num < hitsPerpage;) {
			Element p = pumbedarticle.next();
			Article a = new Article();
			if (a.getKeywords(p).toLowerCase().contains(givenword.toLowerCase())) {
				num++;
				present(p, num);
				
			}
		}
	}
	
	public static void searchKeyword(Document doc, String givenword, int hitsPerpage, boolean ifDisplay) {
		/**
		 * This method is to search a given keyword in a document.
		 * 
		 * @param doc
		 * @param givenword
		 */
		Element root = doc.getRootElement();
		int num = 0;
		// iterate through child elements of root
		for (Iterator<Element> pumbedarticle = root.elementIterator(); pumbedarticle.hasNext() && num < hitsPerpage;) {
			Element p = pumbedarticle.next();
			//Article a = new Article();
			if (p.toString().toLowerCase().contains(givenword.toLowerCase())) {

				num++;
				if(ifDisplay) {
					present(p, num);
				}
			}
		}
	}
	
	public static void present(Element p, int num) {
		/**
		 * This method presents the venue, the title and the year of search results
		 * 
		 * @param p
		 * @param num
		 */
		System.out.println("------------------------------------------");
		System.out.println("Num:    " + num);
		Article a = new Article();
		System.out.println("Title:  " + a.getTitle(p));
		System.out.println("Year :  " + a.getYear(p));
		System.out.println("Venue:  " + a.getVenue(p));

	}
}
