package pubMed.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Decompress {
	private static void decompressHelper(String gzFile, String xmlFile) {
		/**
		 * This method is to de-compress a GZIP file.
		 * @param gzFile
		 * @param xmlFile
		 * @throws IOException
		 */
		try {
			GZIPInputStream gis =  new GZIPInputStream(new FileInputStream(gzFile));
			FileOutputStream fos = new FileOutputStream(xmlFile);
			byte[] buffer = new byte[1024];
			int length;
			while((length = gis.read(buffer)) > 0){
				fos.write(buffer,0,length);
			}
			fos.close();
			gis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	public static void decompress(String filepath, String decompressedpath) {
		/**
		 * This method is to de-compress all GZIP files from a file path.
		 * @param filepath
		 * @param decompressedpath
		 */
		File file = new File(filepath);
		File[] files = file.listFiles();
		for(File f: files) {
			String gzFile = f.getAbsolutePath();
			String name = f.getName();  //pubmed20n1042.xml.gz
			String xmlFile = decompressedpath +"\\"+ name.substring(0, name.length()-3); //pubmed20n1042.xml
			//System.out.println(xmlFile); 
			decompressHelper(gzFile, xmlFile);
		}
		System.out.println("The files has been decompressed.");
	}
}
