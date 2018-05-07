package struct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import util.StringSplitter;

public class TripleSet {
	private int iNumberOfEntities;
	private int iNumberOfRelations;
	private int iNumberOfTriples;
	public ArrayList<Triple> pTriple = null;
	
	public TripleSet() {
	}
	
	public TripleSet(int iEntities, int iRelations) throws Exception {
		iNumberOfEntities = iEntities;
		iNumberOfRelations = iRelations;
	}
	
	public int entities() {
		return iNumberOfEntities;
	}
	
	public int relations() {
		return iNumberOfRelations;
	}
	
	public int triples() {
		return iNumberOfTriples;
	}
	
	public Triple get(int iID) throws Exception {
		if (iID < 0 || iID >= iNumberOfTriples) {
			throw new Exception("getTriple error in TripleSet: ID out of range");
		}
		return pTriple.get(iID);
	}
	
	public void load(String fnInput, int iSize) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		pTriple = new ArrayList<Triple>();
		
		String line = "";
		int iCnt = 0;
		while ((line = reader.readLine()) != null) {
			String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter
					.split("\t ", line));
			if (tokens.length != 3) {
				throw new Exception("load error in TripleSet: data format incorrect");
			}
			int iHead = Integer.parseInt(tokens[0]);
			int iTail = Integer.parseInt(tokens[2]);
			int iRelation = Integer.parseInt(tokens[1]);
			if (iHead < 0 || iHead >= iNumberOfEntities) {
				throw new Exception("load error in TripleSet: head entity ID out of range");
			}
			if (iTail < 0 || iTail >= iNumberOfEntities) {
				throw new Exception("load error in TripleSet: tail entity ID out of range");
			}
			if (iRelation < 0 || iRelation >= iNumberOfRelations) {
				throw new Exception("load error in TripleSet: relation ID out of range");
			}
			pTriple.add(new Triple(iHead, iTail, iRelation));
			iCnt++;
			if (iSize != -1 && iCnt >= iSize) {
				break;
			}
		}
		iNumberOfTriples = pTriple.size();
		reader.close();
	}
	
	public void randomShuffle() {
		TreeMap<Double, Triple> tmpMap = new TreeMap<Double, Triple>();
		for (int iID = 0; iID < iNumberOfTriples; iID++) {
			int i = pTriple.get(iID).head();
			int j = pTriple.get(iID).tail();
			int k = pTriple.get(iID).relation();
			tmpMap.put(Math.random(), new Triple(i, j, k));
		}
		
		pTriple = new ArrayList<Triple>();
		Iterator<Double> iterValues = tmpMap.keySet().iterator();
		while (iterValues.hasNext()) {
			double dRand = iterValues.next();
			Triple trip = tmpMap.get(dRand);
			pTriple.add(new Triple(trip.head(), trip.tail(), trip.relation()));
		}
		iNumberOfTriples = pTriple.size();
		tmpMap.clear();
	}
}
