package util;

import java.util.HashSet;

import struct.Triple;

public class NegativeTripleGenerator {
	public Triple PositiveTriple;
	public int iNumberOfEntities;
	public int iNumberOfRelation;
	
	public NegativeTripleGenerator(Triple inPositiveTriple,
			int inNumberOfEntities, int inNumberOfRelation) {
		PositiveTriple = inPositiveTriple;
		iNumberOfEntities = inNumberOfEntities;
		iNumberOfRelation = inNumberOfRelation;
	}
	
	public HashSet<Triple> generateHeadNegTriple(int m_NumNeg) throws Exception {
		int iPosHead = PositiveTriple.head();
		int iPosTail = PositiveTriple.tail();
		int iPosRelation = PositiveTriple.relation();
		
		
		HashSet<Triple> NegativeTripleSet = new HashSet<Triple>();
		while (NegativeTripleSet.size() < m_NumNeg) {
			int iNegHead = iPosHead;
			Triple NegativeTriple = new Triple(iNegHead, iPosTail, iPosRelation);
			while (iNegHead == iPosHead) {
				iNegHead = (int)(Math.random() * iNumberOfEntities);
				NegativeTriple = new Triple(iNegHead, iPosTail, iPosRelation);
			}
			NegativeTripleSet.add(NegativeTriple);	
		}
		return NegativeTripleSet;
	}
	
	public HashSet<Triple> generateTailNegTriple(int m_NumNeg) throws Exception {
		int iPosHead = PositiveTriple.head();
		int iPosTail = PositiveTriple.tail();
		int iPosRelation = PositiveTriple.relation();
		
		
		HashSet<Triple> NegativeTripleSet = new HashSet<Triple>();
		
		while (NegativeTripleSet.size() < m_NumNeg) {
			int iNegTail = iPosTail;
			Triple NegativeTriple = new Triple(iPosHead, iNegTail, iPosRelation);
			while (iNegTail == iPosTail) {
				iNegTail = (int)(Math.random() * iNumberOfEntities);	
				NegativeTriple = new Triple(iPosHead, iNegTail, iPosRelation);
			}
			NegativeTripleSet.add(NegativeTriple);		
		}

		return NegativeTripleSet;
	}
	
	public Triple generateRelNegTriple() throws Exception {
		int iPosHead = PositiveTriple.head();
		int iPosTail = PositiveTriple.tail();
		int iPosRelation = PositiveTriple.relation();
		
		int iNegRelation = iPosRelation;
		Triple NegativeTriple = new Triple(iPosHead, iPosTail, iNegRelation);
		while (iNegRelation == iPosRelation) {
			iNegRelation = (int)(Math.random() * iNumberOfRelation);
			NegativeTriple = new Triple(iPosHead, iPosTail, iNegRelation);
		}
		return NegativeTriple;
	}
}
