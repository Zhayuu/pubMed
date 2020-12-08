package pubMed.conversion;

import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.io.XMLWriter;

public class Write {
	public static void write(Document rootDoc, String mergedpath) {
		/**
		 * This method is to write document into XML.
		 * @param rootDoc
		 * @param newfilepath
		 */
		File newfile = new File(mergedpath);
        try {
        	XMLWriter writer= new XMLWriter(new FileOutputStream(newfile));
        	writer.write(rootDoc);
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        System.out.println("The files has been written.");
	}
}
