package pubMed.conversion;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Parse {
	public static Document parse(String xmlfile) throws DocumentException {
		/**
		 * This method is to convert a xml file into a Document.
		 * @param	xmlfile
		 * @throws  DocumentException
		 */
		SAXReader reader = new SAXReader();
		Document doc = reader.read(xmlfile);
		System.out.print("The xml file has been parsed.");
		return doc;
	}
}
