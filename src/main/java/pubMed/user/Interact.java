package pubMed.user;

import java.util.Scanner;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import pubMed.conversion.Parse;
import pubMed.conversion.Write;
import pubMed.db.MongoDB;
import pubMed.db.MySQL;
import pubMed.lucene.Lucene;
import pubMed.service.Decompress;
import pubMed.service.Memory;
import pubMed.service.Merge;
import pubMed.service.Search;

public class Interact {

	public void interact() throws Exception {
		/*
		 * this method is to ask user for basic operation.
		 */
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("Please enter the operation: \n Merge = m \n Search = s \n SearchbyIndex = i  \n Timerecord = t \n MySQL = y \n MongoDB =g \n Exit = e");
			String b = sc.nextLine(); 
			if(b.equalsIgnoreCase("m")) {
				interactMerge(sc);//Merge
			} else if (b.equalsIgnoreCase("S")) {
				//interactSearch(sc);//Search
			} else if (b.equalsIgnoreCase("i")){
				interactSearchbyIndex(sc);
			} else if (b.equalsIgnoreCase("t")) {
				timeStatistics(sc);
			} else if (b.equalsIgnoreCase("y")) {
				interactMySql(sc);
			} else if (b.equalsIgnoreCase("g")) {
				interactMongoDB(sc);
				
			}	else if(b.equalsIgnoreCase("E")){
				sc.close();
				break;
			}else {
				System.out.println("Input Error!");//Error
				sc.close();
			}
		}
	}
	public void interactMerge(Scanner sc) {
		/*
		 * this method is to ask users info about merge operation  
		 */
		System.out.println("Please enter the file path:");       // C:\\Users\\Zeyu\\Desktop\\CS622\\HW2\\pubmed
		String filepath =sc.nextLine();
		System.out.println("Please enter the decompressed file path:"); //C:\\Users\\Zeyu\\Desktop\\CS622\\HW2\\pubmedxml
		String decompressedpath =sc.nextLine();
		System.out.println("Please enter the merged file path:");// C:\\Users\\Zeyu\\Desktop\\CS622\\HW2\\mergedfile.xml
		String mergedpath =sc.nextLine();
		
		Decompress.decompress(filepath, decompressedpath);
		try {
			Document rootDoc = Merge.merge(decompressedpath);
			Write w = new Write();
			w.write(rootDoc,mergedpath);
		} catch(DocumentException de) {
			de.printStackTrace();
		}
	}
	public void interactSearch(String keyword) throws DocumentException {
		/*
		 * this method is to ask users info about search operation  
		 */
		String mergedpath = "mergedfile.xml";
		Memory m = new Memory();
		//List<Long> time = new ArrayList<>();
		//long[] l = new long[];
		while(true) {
			//String keyword = enterKeyword(sc);
			//int num = enterNum(sc);
			int num = 5;
			Long start = System.currentTimeMillis();
			Parse p = new Parse();
			Document doc = p.parse(mergedpath);


			//Search.searchKeyword(doc,keyword);
			Search.searchKeyword(doc,keyword,num);
			Long end = System.currentTimeMillis(); 
			Long dur = end - start;
			System.out.println("\n Response time: " + dur); 
			
			
			m.createMemory(keyword); // create record
//			if (enterContinue(sc).equalsIgnoreCase("n") ){
//				break;
//			}
			
			//print search records 
			m.printMemory();
			
		}
	}

	public void interactSearchbyIndex(Scanner sc) throws Exception {
		/*
		 * this method is to ask users info about lucene operation  
		 */
		String xmldir = enterFilePath(sc);
		String indexdir =enterIndex(sc);
		Lucene l = new Lucene();
		FSDirectory index = l.createIndex(indexdir, xmldir);

		while (true) {
			String keyword = enterKeyword(sc);
			int num = enterNum(sc);
			long start = System.currentTimeMillis();
			//l.search(keyword, indexdir, num);
			long end = System.currentTimeMillis();
			System.out.println("\n Response time: " + (end - start));
			if (enterContinue(sc).equalsIgnoreCase("n") ){
				break;
			} 
		}
	}
	
	
	public void timeStatistics(Scanner sc) throws Exception {
		/*
		 * this method is to ask users info about search operation, and record the response time  
		 */
		String xmldir = enterFilePath(sc);
		String keyword = enterKeyword(sc);
		String indexdir = enterIndex(sc);
		//TODO user input the int array
		int[] num= {1, 5, 10, 20, 30 ,40, 50 ,60, 70, 80, 90, 100};
		int size =num.length;
		long[] bf = new long[size];  //record the response time for brute force
		long[] lc = new long[num.length]; // for lucene
		
		Parse p = new Parse();
		Document doc = p.parse(xmldir);
		//Document doc = p.parse(xmldir);
		for (int i = 0;i<size;i++) {
				long start = System.currentTimeMillis(); 
				Search.searchKeyword(doc,keyword,num[i],false);	
				long end = System.currentTimeMillis(); 
				long dur = end - start;
			bf[i] = dur;
			System.out.println((i+1) +":"+dur);
		}
		
		
		Lucene l = new Lucene();
		
		for (int i = 0;i<size;i++) {
			FSDirectory index = l.createIndex(indexdir, xmldir);
			long start = System.currentTimeMillis();
			l.search(keyword, index, num[i] , false);
			long end = System.currentTimeMillis();
			long dur = end - start;
			lc[i] = dur;
			System.out.println((i+1) + ":"  + dur);
		}
		/*
		for(long dur:bf)
			System.out.println(dur);
		System.out.println("!");
		for(long dur:lc)
			System.out.println(dur);
			*/
	}
	
	public void interactMySql(Scanner sc) throws Exception {
			//MySQL mysql = new Mysql();
			// transfer("pubmed20n1020.xml");
				
				String str = queryinDb(sc);
				if(str.equalsIgnoreCase("a")) {
					String dir = enterFilePath(sc);
					MySQL.transfer(dir);
				} else if(str.equalsIgnoreCase("b")) {
					String keyword = enterKeyword(sc);
					MySQL.countPerYear(keyword);
				} else if(str.equalsIgnoreCase("c")) {
					String keyword = enterKeyword(sc);
					String start = enterStartDate(sc);
					String end = enterEndDate(sc);
					MySQL.rangeQuery(keyword,start,end);
				} else {
					System.out.println("Error");
				}
	}
	
	public void interactMongoDB(Scanner sc) throws Exception {
		//MongoDB mg = new MongoDB();
		String str = queryinDb(sc);
		if(str.equalsIgnoreCase("a")) {
			String dir = enterFilePath(sc);
			MongoDB.transferMongo(dir);
		} else if(str.equalsIgnoreCase("b")) {
			String keyword = enterKeyword(sc);
			MongoDB.countPerYearMongo(keyword);
		} else if(str.equalsIgnoreCase("c")) {
			String keyword = enterKeyword(sc);
			String start = enterStartDate(sc);
			String end = enterEndDate(sc);
			MongoDB.rangeQueryMongo(keyword,start,end);
		} else {
			System.out.println("Error");
		}
	}
	public  String enterFilePath(Scanner sc) {
		System.out.println("Please enter the file path:"); // pubmed20n1020.xml
		return sc.nextLine();
	}
	public  String enterKeyword(Scanner sc) {
		System.out.println("Please enter the keyword to be searched:");
		return sc.nextLine();
	}
	public  int enterNum(Scanner sc) {
		System.out.println("Please enter the number of articles:");
		return Integer.parseInt(sc.nextLine());
	}
	public String enterContinue(Scanner sc) {
		System.out.println("Continue searching? YES = y or NO = n");
		return sc.nextLine();

	}
	public String enterIndex(Scanner sc) {
		System.out.println("Please enter the index document path:");
		return  sc.nextLine();
	}
	public String enterStartDate(Scanner sc) {
		System.out.println("Please enter the start month 'yyyy-mm':");
		return  sc.nextLine();
	}
	public String enterEndDate(Scanner sc) {
		System.out.println("Please enter the end date 'yyyy-mm':");
		return  sc.nextLine();
	}
	public String queryinDb(Scanner sc) {
		System.out.println("transfer xml data into MySQL: a \n"
			+ "count the number of given keywords per year: b \n"
			+ "search for a given keyword in a given from the start month to end month: c ");
		return  sc.nextLine();
	}
}
