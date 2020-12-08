package pubMed.service;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Merge {
	public static Document merge(String decompressedpath) throws DocumentException {
		/**
		 * This method is to merge multiple XML files to a site. 
		 * @param rootXml: one of these files is defined as the root file 
		 * @param filepath: The package path of files needs to be merged  
		 * @return a document including all files 
		 * @throws DocumentException
		 */
		File file = new File(decompressedpath);
		//File[] files = file.listFiles();
		File rootfile = file.listFiles()[0]; //select the first file as the root file 
		//File otherfile= files[1:];
		SAXReader saxReader = new SAXReader();
		Document rootDoc = saxReader.read(rootfile);
		Element parent = rootDoc.getRootElement(); 
        if (file.exists()) {
        	// merge all files in the package, except for the root file.
            File[] otherfiles = file.listFiles(new FileFilter() {  
                public boolean accept(File f) {
                	return !f.getAbsoluteFile().equals(rootfile);
                }
            });
            
            //iterate through files
            for (File f : otherfiles) {
                Document read = saxReader.read(f);
                List<Element> elements = read.getDocument().getRootElement().elements();
                //iterate through all the elements in the file  
                for (Element e : elements) {
                	parent.add(e.detach());
                }
            }
        }
        System.out.println("The files has been merged.");
        return rootDoc;  
      
	}
	
	
}
