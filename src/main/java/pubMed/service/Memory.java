package pubMed.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memory {
	Map<String,List<String>> map = new HashMap<>();
	public void createMemory(String keyword) {
		/**
		 * This method is to store the search keyword and timestamps by using a hash map.
		 */
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-mm-dd:hh:mm:ss");
		
		// There is no search record. A pair of key and value will be created.
		if(map.get(keyword)==null) { 
			List<String> al = new ArrayList<String>(); // use ArrayList to store value in the map. 
			map.put(keyword,al);
			al.add(sdf.format(new Date())); // get time stamp.
		} else {
		//There is a search record. The value of this key will be updated.
			map.get(keyword).add(sdf.format(new Date()));
		} 
	}
	
	
	public void printMemory(){
		/**
		 * This method is to print the number of search terms, their timestamps, and frequency
		 */
		System.out.println("\n**** Memory Print ***\n");
		System.out.println("Total number of search keywords:" + map.size());
		for(String key: map.keySet())
			System.out.println("Keyword: "+key+" , "+ "Frequency: "+map.get(key).size()+" , "+"Timestamps: "+map.get(key)) ;
	}
}
