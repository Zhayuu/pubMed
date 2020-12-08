package pubMed.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import pubMed.article.Article;
import pubMed.conversion.Parse;

import java.util.Iterator;

public class BruteForce {

    public static String bruteForce(String xmlfile, String keyword) throws DocumentException {
        /**
         * This method is to search a given keyword.
         *
         * @param doc
         * @param givenword
         */
        Long start = System.currentTimeMillis();
        Document doc = Parse.parse(xmlfile);
        Element root = doc.getRootElement();
        StringBuilder res = new StringBuilder();
        int hitsPerPage = 10;
        int num = 0;
        // iterate through child elements of root
        for (Iterator<Element> pumbedarticle = root.elementIterator(); pumbedarticle.hasNext() && num < hitsPerPage;) {
            Element p = pumbedarticle.next();
            Article a = new Article();
            a.getTitle(p);
            if (a.getTitle(p).toLowerCase().contains(keyword.toLowerCase())) {
                num++;
                res.append(a.getTitle(p)+"\n");
            }
        }
        res.append("total:"+num);
        Long end = System.currentTimeMillis();
        Long dur = end - start;
        System.out.println("\n Response time: " + dur);
        return res.toString();
    }

    public static void main(String[] args) throws DocumentException {
        String s = bruteForce("C:\\Users\\Zeyu\\eclipse-workspace\\pubMed\\src\\main\\resources\\static\\xml\\testfile.xml","heart");
        System.out.println(s);
    }
}
