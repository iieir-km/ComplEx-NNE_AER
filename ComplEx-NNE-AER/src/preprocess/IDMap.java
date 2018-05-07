package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

public class IDMap {
	public HashMap<String, Boolean> m_lstEntities = null;
	public HashMap<String, Boolean> m_lstRelations = null;
	
	public void LoadEntRel(String fnInput) throws Exception {
		m_lstEntities = new HashMap<String, Boolean>();
		m_lstRelations = new HashMap<String, Boolean>();
		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		
		String line = "";
		while ((line = sr.readLine()) != null)
		{
			String[] tokens = line.split("\t");
			String strHead = tokens[0];
			String strRelation = tokens[1];
			String strTail = tokens[2];
			if (!m_lstEntities.containsKey(strHead))
			{
				m_lstEntities.put(strHead, true);
			}
			if (!m_lstEntities.containsKey(strTail))
			{
				m_lstEntities.put(strTail, true);
			}
			if (!m_lstRelations.containsKey(strRelation))
			{
				m_lstRelations.put(strRelation, true);
			}
		}
		sr.close();
	}
	
	public void OutputIDMap(String fnEntityMap, String fnRelationMap) throws Exception {
		BufferedWriter sw1 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnEntityMap), "UTF-8"));
		BufferedWriter sw2 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnRelationMap), "UTF-8"));
		
		int iID = 0;
		Iterator<String> iterEnt = m_lstEntities.keySet().iterator();
		while (iterEnt.hasNext()) {
			sw1.write(iID + "\t" + iterEnt.next() + "\n");
			iID++;
		}
		iID = 0;
		Iterator<String> iterRel = m_lstRelations.keySet().iterator();
		while (iterRel.hasNext()) {
			sw2.write(iID + "\t" + iterRel.next() + "\n");
			iID++;
		}
		sw1.close();
		sw2.close();
	}
	
	public static void main(String[] args) throws Exception {
		String fnInput = "D:\\MyExperiments\\ComplExWithRules\\WN18\\wordnet-mlj12-train.txt";
		String fnOutput1 = "D:\\MyExperiments\\ComplExWithRules\\WN18\\EntityIDMap.tsv";
		String fnOutput2 = "D:\\MyExperiments\\ComplExWithRules\\WN18\\RelationIDMap.tsv";
		IDMap mapper = new IDMap();
		mapper.LoadEntRel(fnInput);
		mapper.OutputIDMap(fnOutput1, fnOutput2);
	}
}
