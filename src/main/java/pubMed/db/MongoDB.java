package pubMed.db;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.bson.Document;
import org.dom4j.Element;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import pubMed.article.Article;
import pubMed.conversion.Parse;


public class MongoDB {
	
	/*
	 * this method is to transfer xml date into MongoDB
	 */
	public static void transferMongo(String dir) throws Exception {
		// initialize the client object 
				MongoClient mongoClient = new MongoClient(); 
				MongoDatabase dbObj = mongoClient.getDatabase( "pubmed" );
				MongoCollection<Document> col = dbObj.getCollection("article");
				
				Parse parser = new Parse();
				org.dom4j.Document doc = parser.parse(dir);
				Element root =  doc.getRootElement();
				Iterator<Element> pumbedarticle = root.elementIterator();
				while (pumbedarticle.hasNext()) {
					Document mydoc = new Document(); 
					Element p = (Element) pumbedarticle.next();
					Article article = new Article();
					mydoc.put("title", article.getTitle(p)); 
					java.sql.Date date = null;
					if(article.getDate(p)!="") {
						date = java.sql.Date.valueOf(article.getDate(p));
					}
					mydoc.put("date", date);
					col.insertOne( mydoc );
				}
				mongoClient.close();
	}
	/*
	 *search for a given keyword in a given from the start month to end month
	 * this method is to execute the query: 
	 * db.article.find({title:{$regex:/animal/i},date:{$gte:start,$lt:end}})
	 */
	public static void rangeQueryMongo(String keyword, String startDate, String endDate) throws SQLException {
		Date start = StrToDate(startDate);
		Date end =StrToDate(endDate);
		MongoClient mongoClient = new MongoClient(); 
		MongoDatabase dbObj = mongoClient.getDatabase( "pubmed" );
		MongoCollection<Document> collection = dbObj.getCollection("article");
        FindIterable<Document> iterable = collection.find(Filters.and(Filters.gt("date",start),
        		Filters.lt("date", end),Filters.regex("title", keyword,"i")));
        MongoCursor<Document> cursor = iterable.iterator();
        while(cursor.hasNext()){
            Document docu = cursor.next();
            System.out.println(docu.get("date")+"\t"+docu.get("title")+"\t");
        }
        mongoClient.close();
		
	} 

	/*
	 * count the number of given keywords per year
	 */
	public static void countPerYearMongo(String keyword) throws SQLException {
		MongoClient mongoClient = new MongoClient(); 
		MongoDatabase dbObj = mongoClient.getDatabase( "pubmed" );
		MongoCollection<Document> collection = dbObj.getCollection("article");
        FindIterable<Document> iterable = collection.find(Filters.and(Filters.gt("date",StrToDate("2018-01")),
        		Filters.lt("date", StrToDate("2019-01")),Filters.regex("title", keyword,"i")));
        MongoCursor<Document> cursor = iterable.iterator();
        int i =0;
        while( cursor.hasNext()){
        	Document docu = cursor.next();
            System.out.println(docu.get("date")+"\t"+docu.get("title")+"\t");
            i = i+1;
        }
        
        FindIterable<Document> iterable2 = collection.find(Filters.and(Filters.gt("date",StrToDate("2019-01")),
        		Filters.lt("date", StrToDate("2020-01")),Filters.regex("title", keyword,"i")));
        MongoCursor<Document> cursor2 = iterable2.iterator();
        int j =0;
        while( cursor2.hasNext()){
        	Document docu = cursor2.next();
            System.out.println(docu.get("date")+"\t"+docu.get("title")+"\t");
            j = j+1;
        }
        FindIterable<Document> iterable3 = collection.find(Filters.and(Filters.gt("date",StrToDate("2020-01")),
        		Filters.lt("date", StrToDate("2021-01")),Filters.regex("title", keyword,"i")));
        MongoCursor<Document> cursor3 = iterable3.iterator();
        int k =0;
        while( cursor3.hasNext()){
        	Document docu = cursor3.next();
            System.out.println(docu.get("date")+"\t"+docu.get("title")+"\t");
            k = k+1;
        }
        System.out.println("2018:"+i+"\n"+"2019:" +j +"\n"+"2020:"+k);
        mongoClient.close();
	}
	
	
	public static java.sql.Date StrToDate(String str) {
		SimpleDateFormat input = new SimpleDateFormat("yyyy-mm"); 
		SimpleDateFormat output = new SimpleDateFormat("yyyy-mm-dd"); 
		java.sql.Date  date = null; 
		   try { 

			   date =  java.sql.Date.valueOf(output.format(input.parse(str)).toString()); 
		   } catch (Exception e) { 
		    e.printStackTrace(); 
		   } 
			return date;
		}
	public static java.sql.Date StrToDatebyYear(String str) {
		SimpleDateFormat input = new SimpleDateFormat("yyyy"); 
		SimpleDateFormat output = new SimpleDateFormat("yyyy-mm-dd"); 
		java.sql.Date  date = null; 
		   try { 

			   date =  java.sql.Date.valueOf(output.format(input.parse(str)).toString()); 
		   } catch (Exception e) { 
		    e.printStackTrace(); 
		   } 
			return date;
		}
}
