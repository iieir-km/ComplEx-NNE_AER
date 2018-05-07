package struct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TripleDict {
	public HashMap<String, Boolean> pTripleDict = null;
	
	public TripleDict() {
	}
	
	public HashMap<String, Boolean> tripleDict() {
		return pTripleDict;
	}
	
	public void load(String fnInput) throws Exception {
		pTripleDict = new HashMap<String, Boolean>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			pTripleDict.put(line.trim(), true);
		}
		reader.close();
	}
}
