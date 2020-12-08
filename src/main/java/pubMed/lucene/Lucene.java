package pubMed.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import pubMed.article.Article;
import pubMed.conversion.Parse;

public class Lucene{
	StandardAnalyzer analyzer = new StandardAnalyzer();
	
	public FSDirectory createIndex(String indexdir, String xmldir) throws Exception{
		/*
		 * this method is to create index for a xml file. 
		 */
		FSDirectory index = FSDirectory.open(Paths.get(indexdir));//"C:\\Users\\Zeyu\\eclipse-workspace\\main.java.pubMed\\index"
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		Article a = new Article();
		Parse parser = new Parse();
		Element root = parser.parse(xmldir).getRootElement(); //"C:\\Users\\Zeyu\\Desktop\\CS622\\HW2\\pubmedxml\\pubmed20n1020.xml"
		for (Iterator<Element> pumbedarticle = root.elementIterator(); pumbedarticle.hasNext();) {
			Element p = pumbedarticle.next();
			addDoc(w,a.getKeywords(p),a.getTitle(p), a.getYear(p), a.getVenue(p));// add info into the index document.
		}
		w.close();
		return index;
	}		
	
	private Query query(String str) throws ParseException {
		/*
		 * this method is to tokenize and parse the query 
		 */
		String querystr = new String(str);
		Query q = new QueryParser("keywords", analyzer).parse(querystr);
		// the "keywords" arg specifies the default field to use in the query 
		return q;
	}

	public String luncene(String str,String indexdir, int hitsPerPage) throws IOException, ParseException {
		/*
		 * this method is to search by using index
		 */
		long start = System.currentTimeMillis();
		FSDirectory index = FSDirectory.open(Paths.get(indexdir));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(query(str), hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs; 
		System.out.println("Number of article ： " + hits.length);

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			present(d, i);
		}
		long end = System.currentTimeMillis();
		System.out.println("\n Lucene Search Response time: " + (end - start));
		return null;
	}

	public void search(String str,FSDirectory index, int hitsPerPage, boolean ifDisplay) throws IOException, ParseException {
		/*
		 * this method is to search by using index without showing results
		 */
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query q = query(str);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs; 
		//System.out.println("Number of article ： " + hits.length);
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			if(ifDisplay = false) {
				present(d, i+1);
			}
		}
	}

	public void present(Document d, int num) {
		/*
		 * This method presents the venue, the title and the year of search results
		 */
		 
		
		System.out.println("------------------------------------------");
		System.out.println("Num:    " + (num ));
		Article a = new Article();
		System.out.println("Title:  " + d.get("title"));
		System.out.println("Year :  " + d.get("year"));
		System.out.println("Venue:  " + d.get("venue"));

	}
	private void addDoc(IndexWriter w, String keyword, String title, String year, String venue) throws IOException {
		/*
		 * this is method to add fields into the index document.
		 */
		Document document = new Document();
		document.add(new TextField("keywords", keyword, Field.Store.YES));
		document.add(new TextField("title", title, Field.Store.YES));
		document.add(new TextField("year", year, Field.Store.YES));
		document.add(new TextField("venue", venue, Field.Store.YES));
		w.addDocument(document);

	}

	public static void main(String[] args) throws Exception {


		String indexdir = "C:\\Users\\Zeyu\\eclipse-workspace\\pubMed\\src\\main\\resources\\static\\xml\\index";
		String xmldir = "C:\\Users\\Zeyu\\eclipse-workspace\\pubMed\\src\\main\\resources\\static\\xml\\testfile.xml";
		Lucene l = new Lucene();
		FSDirectory index = l.createIndex(indexdir, xmldir);
		//l.search("heart", indexdir, 5);
	}
	
}