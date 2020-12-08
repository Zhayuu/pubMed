package pubMed.db;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.dom4j.Document;
import org.dom4j.Element;
import pubMed.article.Article;
import pubMed.conversion.Parse;


import java.util.Iterator;

public class MySQL {
		
	/*
	 * this method is to connect to mysql database.
	 */
	public static Connection connectDb() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/met622", "root", "12345678");
			System.out.println("--->" + con.toString());
			return con;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/*
	 * this method create statements and choose the database.
	 */
	public static Statement prepareStmt(Connection con) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("use pubmed");
			return stmt;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/*
	 * this method is to transfer xml file into mysql
	 */
	public static void transfer(String dir) {
		Parse parser = new Parse();
		try {
			Document doc = parser.parse(dir);
			Connection con = connectDb();
			Statement stmt = prepareStmt(con);
			
			Element root = doc.getRootElement();
			Iterator<Element> pumbedarticle = root.elementIterator();
			while (pumbedarticle.hasNext()) {
				Element p = (Element) pumbedarticle.next();
				Article article = new Article();
				String title = article.getTitle(p);
				java.sql.Date date = null;
				if(article.getDate(p)!="") {
					date = java.sql.Date.valueOf(article.getDate(p));
				}
				System.out.println(title + date);
                //stmt.execute("CREATE DATABASE pubmed");
	                stmt.execute("use pubmed;");
				// stmt.execute("CREATE TABLE article(title VARCHAR(500), date VARCHAR(6))");
				String sql = "INSERT INTO article VALUES (?,?)";
				PreparedStatement pst = con.prepareStatement(sql);
				pst.setString(1, title);
				pst.setDate(2, date);
				int resultSet = pst.executeUpdate();
				if (resultSet > 0) {
					System.out.println("Sucess");
				} else {
					System.out.println("Failure");
				}
			}	
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	
/*
 * Write a query to count the number of given keywords per year, e.g. �flu�, �obesity" keywords,
 *  for at least three years.
 *  SELECT COUNT(title) AS NumberofArticle, YEAR(date) AS year from article where title like '%Animal%' group by year;
 */
	public static void countPerYear(String keyword) throws SQLException {
		
		Connection con = connectDb();
		Statement stmt = prepareStmt(con);
		
		String sql = "SELECT COUNT(title) AS NumberofArticle, YEAR(date) AS year from article where title like '%"+keyword+"%' group by year";
		ResultSet rs= stmt.executeQuery(sql);
		System.out.println("ROW - YEAR -NumberofArticle");
		for(int i =1; rs.next();i++) { 
			System.out.println( i+":"+rs.getString(2)+"  "+ rs.getString(1));
		}
		
	}
	
/*
 * Write a range queries that search for a given keyword in a given from the start month to end
 *  month, like from Jan 2017 to Mar 2018.	
 *  select * FROM article where title like '%Animal%' AND date between '2018-11' and '2019-12';
 */
	public static void rangeQuery(String keyword, String startDate, String endDate) throws SQLException {
		Connection con = connectDb();
		Statement stmt = prepareStmt(con);
		Date start = StrToDate(startDate);
		Date end =StrToDate(endDate);
		String sql = "select * FROM article where title like '%"+keyword+"%' AND date between '"+start+"' and '"+end+"'";
		ResultSet rs= stmt.executeQuery(sql);
		for(int i =1; rs.next();i++) { 
			System.out.println( i+":"+rs.getString(2)+"  "+ rs.getString(1));
		}
	} 
	
	/*
	 * this method is convert the date of string type to Date type
	 */
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
}
