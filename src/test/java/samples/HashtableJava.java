package samples;

import java.util.Hashtable;

//import java.util.Hashtable;

public class HashtableJava {
	
	public static void main(String[] args) {
		
		Hashtable<String, String> table = new Hashtable<String, String> ();
		table.put("Country", "India");
		table.put("Continent", "Asia");
		
		System.out.println(table.get("Country"));
		
	}

}
