package complex_NNE_AER;

import struct.Matrix;
import struct.TripleSet;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Evaluation {
	public TripleSet lstTestTriples;
	public HashMap<String, Boolean> lstAllTriples;
	public Matrix Real_MatrixE;
	public Matrix Real_MatrixR;
	public Matrix Imag_MatrixE;
	public Matrix Imag_MatrixR;
	public double dMRR;
	public double dMeanRank;
	public double dMedian;
	public double dHits1;
	public double dHits3;
	public double dHits5;
	public double dHits10;
	
	public Evaluation(TripleSet inLstTestTriples,
			HashMap<String, Boolean> inLstAllTriples,
			Matrix in_Real_MatrixE,
			Matrix in_Real_MatrixR,
			Matrix in_Imag_MatrixE,
			Matrix in_Imag_MatrixR) {
		lstTestTriples = inLstTestTriples;
		lstAllTriples = inLstAllTriples;
		Real_MatrixE = in_Real_MatrixE;
		Real_MatrixR = in_Real_MatrixR;
		Imag_MatrixE = in_Imag_MatrixE;
		Imag_MatrixR = in_Imag_MatrixR;
	}
	
	public void calculateMetrics() throws Exception {

		int iNumberOfEntities = Real_MatrixE.rows();
		int iNumberOfFactors = Real_MatrixE.columns();
		List<Double> iList = new ArrayList<Double>();
				
		int iCnt = 0;
		double avgMRR = 0.0;
		double avgMeanRank = 0.0;
		int avgHits1=0, avgHits3=0, avgHits5=0, avgHits10 = 0;
		for (int iID = 0; iID < lstTestTriples.triples(); iID++) {
			int iRelation = lstTestTriples.get(iID).relation();
			int iHead = lstTestTriples.get(iID).head();
			int iTail = lstTestTriples.get(iID).tail();
			double dTargetValue = 0.0;
			for (int p = 0; p < iNumberOfFactors; p++) {
				dTargetValue += Real_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
						      - Imag_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
						      + Real_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p)
						      + Imag_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);
			}
			
			int iLeftRank = 1;
			int iLeftIdentical = 0;
			for (int iLeftID = 0; iLeftID < iNumberOfEntities; iLeftID++) {
				double dValue = 0.0;
				String negTiple = iLeftID + "\t" + iRelation + "\t" +iTail;
				if(!lstAllTriples.containsKey(negTiple)){
					for (int p = 0; p < iNumberOfFactors; p++) {
						dValue += Real_MatrixE.get(iLeftID, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
							    - Imag_MatrixE.get(iLeftID, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
							    + Real_MatrixE.get(iLeftID, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p)
							    + Imag_MatrixE.get(iLeftID, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);
					}
					if (dValue > dTargetValue) {
						iLeftRank++;
					}
					if (dValue == dTargetValue) {
						iLeftIdentical++;
					}
				}
			}
			double dLeftRank = (double)(2.0 * iLeftRank + iLeftIdentical) / 2.0;
			int iLeftHits1=0, iLeftHits3=0, iLeftHits5=0, iLeftHits10 = 0;
			if (dLeftRank <= 1.0) {
				iLeftHits1 = 1;
			}
			if (dLeftRank <= 3.0) {
				iLeftHits3 = 1;
			}
			if (dLeftRank <= 5.0) {
				iLeftHits5 = 1;
			}
			if (dLeftRank <= 10.0) {
				iLeftHits10 = 1;
			}
			avgMRR += 1.0/(double)dLeftRank;
			avgMeanRank += dLeftRank;
			avgHits1 += iLeftHits1;
			avgHits3 += iLeftHits3;
			avgHits5 += iLeftHits5;
			avgHits10 += iLeftHits10;
			iList.add(dLeftRank);
			iCnt++;
			
			int iRightRank = 1;
			int iRightIdentical = 0;
			for (int iRightID = 0; iRightID < iNumberOfEntities; iRightID++) {
				double dValue = 0.0;
				String negTiple = iHead + "\t" + iRelation + "\t" + iRightID;
				if(!lstAllTriples.containsKey(negTiple)){
					for (int p = 0; p < iNumberOfFactors; p++) {
						dValue += Real_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iRightID, p)
							    - Imag_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iRightID, p)
							    + Real_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iRightID, p)
							    + Imag_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iRightID, p);
					}
					if (dValue > dTargetValue) {
						iRightRank++;
					}
					if (dValue == dTargetValue) {
						iRightIdentical++;
					}
				}
			}
			double dRightRank = (double)(2.0 * iRightRank + iRightIdentical) / 2.0;
			int iRightHits1=0, iRightHits3=0, iRightHits5=0, iRightHits10 = 0;
			if (dRightRank <= 1.0) {
				iRightHits1 = 1;
			}
			if (dRightRank <= 3.0) {
				iRightHits3 = 1;
			}
			if (dRightRank <= 5.0) {
				iRightHits5 = 1;
			}
			if (dRightRank <= 10.0) {
				iRightHits10 = 1;
			}
			avgMRR += 1.0/(double)dRightRank;
			avgMeanRank += dRightRank;
			avgHits1 += iRightHits1;
			avgHits3 += iRightHits3;
			avgHits5 += iRightHits5;
			avgHits10 += iRightHits10;
			iList.add(dRightRank);
			iCnt++;
		}
		dMRR = avgMRR / (double)iCnt;
		dMeanRank = avgMeanRank / (double)iCnt;
		dHits1 = (double)avgHits1 / (double)iCnt;
		dHits3 = (double)avgHits3 / (double)iCnt;
		dHits5 = (double)avgHits5 / (double)iCnt;
		dHits10 = (double)avgHits10 / (double)iCnt;
		
		Collections.sort(iList);
		int indx=iList.size()/2;
		if (iList.size()%2==0) {
			dMedian = (iList.get(indx-1)+iList.get(indx))/2.0;
		}
		else {
			dMedian = iList.get(indx);
		}
	}
}
