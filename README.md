# PubMed
The project supports users to merge mutiple xml files into one large file, search for a keyword in a file, and check the search records. The data is downloaded from the PubMed, a institution collecting all scientific papers written about medical medicines. 
## Class and Methods
### Article 
Get the content of different field in a article element. 
- **getID(Element p)**
- **getKeywords(Element p)** get the content of a list of keywords 
- **getTitle(Element p)**
- **getVenue(Element p)**
- **getYear(Element p)**
### Parse
- **parse(String xmlfile):** parse a XML file into a document.
### Write
- **write(Document rootDoc, String mergedpath):** write a document into a XML file.
### Decompress
Decompress GZIP files
- **decompress(String filepath, String decompressedpath):**  decompress all GZIP files from a file path.
- **decompresshelper(String gzFile, String xmlFile):**  decompress a GZIP file.
### Merge
Merge XML files
- **merge(String decompressedpath):** merge multiple XML files to a site.
### Search
Search for a given keywords in a file and present the result.
- **searchKeyword(Document doc,String givenword):** search a given keyword in a document.
### Memory
Store search records and present it.
- **createMemory(String keyword):** store the search keyword and timestamps by using a hash map.
- **printMemory():** print the number of search terms, their timestamps, and frequency.
### Execution
Execute the application (the main function).
### Interact 
- **interact()** ask user to choose the basic operations.
- **interactMerge(Scanner sc)** ask users info about merge operation
- **interactSearch(Scanner sc)** ask users info about search operation 
- **interactSearchbyIndex(Scanner sc)** ask users info about lucene operation  
- **timeStatistics(Scanner sc)** ask users info about search operation, and record the response time 
###  Lucene
- **createIndex(String indexdir, String xmldir)** create index for a xml file.
- **query(String str)** tokenize and parse the query 
- **search(String str,FSDirectory index, int hitsPerPage) ** search by using index 
- **search(String str,FSDirectory index, int hitsPerPage, boolean ifDisplay)** search by using index without showing results
- **present(Document d, int num)** presents the venue, the title and the year of search results
-  **addDoc(IndexWriter w, String keyword, String title, String year, String venue):** add field into the index document
###  Mysql
- **transfer(String dir)** transfer xml file into mysql
- **countPerYear(String keyword)** count the number of given keywords per year
- **rangeQuery(String keyword, String startDate, String endDate)** search for a given keyword in a given from the start month to end
- **StrToDate(String str)** convert the date of string type to Date type
###  MongoDB
- **transfer(String dir)** transfer xml file into mongodb
- **countPerYearMongo(String keyword)** count the number of given keywords per year
- **rangeQueryMongo(String keyword, String startDate, String endDate)** search for a given keyword in a given from the start month to end

## ChangeLog
09/22/2020
- Support keeping the track of what has been searched.
- Change mutiple main() to one main() for the whole process.
- Delete hard coding.

10/15/2020
- Support Lucene search.
- Record the time response of two ways to searching.
- Add Article class in order to get info from a article easily.
- Break down the previous main class, and put the operations about interactions with user  into Interact class. 
11/03/2020
- add db operation