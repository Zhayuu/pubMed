package pubMed.article;

import java.util.Iterator;

import org.dom4j.Element;

public class Article {
	/*
	 * this class is to get the content of different field in a article element. 
	 */

	public String getPMID(Element p ) {
	
			Element PMID = p.element("MedlineCitation").element("PMID");
			return PMID.getText();
		
	}
	
	
	public String getTitle(Element p ) {
		if(p.element("MedlineCitation").element("Article")!= null) { //check null pointer 
			Element title = p.element("MedlineCitation").element("Article").element("ArticleTitle");
			return title.getText();
		}
		return "";
	}

	public String getKeywords(Element p) {
		Element keywordlist = p.element("MedlineCitation").element("KeywordList");
		String keywords = new String();
		if (keywordlist != null) {
			// iterate through keyword elements of keywordlist
			for (Iterator<Element> keyword = keywordlist.elementIterator(); keyword.hasNext();) {
				Element k = keyword.next();
				keywords = keywords + "" + k.getText();
			}
		}
		//System.out.println(keywords);
		return keywords;
	}
	public String getID(Element p) {
		
			Element id = p.element("MedlineCitation").element("PMID");
			return id.getText();
	}
	
	
	public String getYear(Element p) {
		if(p.element("MedlineCitation").element("Article").element("ArticleDate")!= null) {
			Element year = p.element("MedlineCitation").element("Article").element("ArticleDate").element("Year");
			return year.getText();
		}	
		return "";
	}
	public String getDate(Element p) {
		if(p.element("MedlineCitation").element("Article").element("ArticleDate")!= null) {
			Element articleDate = p.element("MedlineCitation").element("Article").element("ArticleDate");
			Element year =	articleDate.element("Year");
			Element month =	articleDate.element("Month");
			Element day =	articleDate.element("Day");
			return year.getText()+'-'+month.getText()+'-'+day.getText();
		}	
		return "";
	}
	public String getVenue(Element p) {
		if(p.element("MedlineCitation").element("MedlineJournalInfo")!= null) {
			Element venue = p.element("MedlineCitation").element("MedlineJournalInfo").element("Country");
			return venue.getText()+"\n";
		}
		return "";
	}
}
