package complex;

import struct.Matrix;
import struct.Triple;

public class Gradients {
	public Triple curTriple;
	public double dLabel;
	public Matrix Real_MatrixE;
	public Matrix Real_MatrixR;
	public Matrix Imag_MatrixE;
	public Matrix Imag_MatrixR;
	public Matrix Real_MatrixEGradient;
	public Matrix Real_MatrixRGradient;
	public Matrix Imag_MatrixEGradient;
	public Matrix Imag_MatrixRGradient;
	public double dLambda;
	public double dSize;
	
	public Gradients(
			Triple inTriple,
			double inLabel,
			Matrix in_Real_MatrixE,
			Matrix in_Real_MatrixR,
			Matrix in_Imag_MatrixE,
			Matrix in_Imag_MatrixR,
			Matrix in_Real_MatrixEGradient,
			Matrix in_Real_MatrixRGradient,
			Matrix in_Imag_MatrixEGradient,
			Matrix in_Imag_MatrixRGradient,
			double inLambda,
			double inSize) {
		curTriple = inTriple;
		dLabel = inLabel;
		Real_MatrixE = in_Real_MatrixE;
		Real_MatrixR = in_Real_MatrixR;
		Imag_MatrixE = in_Imag_MatrixE;
		Imag_MatrixR = in_Imag_MatrixR;
		Real_MatrixEGradient = in_Real_MatrixEGradient;
		Real_MatrixRGradient = in_Real_MatrixRGradient;
		Imag_MatrixEGradient = in_Imag_MatrixEGradient;
		Imag_MatrixRGradient = in_Imag_MatrixRGradient;
		dLambda = inLambda;
		dSize = inSize;
	}
	
	public double sigmoid(double x){
		double y = 0.0;
		if(x > 10.0){
			y = 1.0;
		}
		else if (x < -10.0){
			y = 0.0;
		}
		else{
			y = 1.0 / (1.0 + Math.exp(-x));
		}
		return y;	
	}
		
	
	public void calculateGradients() throws Exception {
		int iNumberOfFactors = Real_MatrixE.columns();
		int iHead = curTriple.head();
		int iTail = curTriple.tail();
		int iRelation = curTriple.relation();		
		double dEta = 0.0;
		for (int p = 0; p < iNumberOfFactors; p++) {
			dEta += Real_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
				  - Imag_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
				  + Real_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p)
				  + Imag_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);  
		}
		double dPartial = -dLabel * sigmoid(-dLabel * dEta);
		
		for (int p = 0; p < iNumberOfFactors; p++) {
			double dRealHead = 0.0;			
			double dRealTail = 0.0;
			double dRealRel  = 0.0;
			double dImagHead = 0.0;			
			double dImagTail = 0.0;
			double dImagRel  = 0.0;
			
			dRealHead = Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p) + Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);			
			dRealTail = Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iHead, p) - Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iHead, p);
			dRealRel  = Real_MatrixE.get(iHead, p) * Real_MatrixE.get(iTail, p) + Imag_MatrixE.get(iHead, p) * Imag_MatrixE.get(iTail, p);
			dImagHead = Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p) - Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p);			
			dImagTail = Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iHead, p) + Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iHead, p);
			dImagRel  = Real_MatrixE.get(iHead, p) * Imag_MatrixE.get(iTail, p) - Imag_MatrixE.get(iHead, p) * Real_MatrixE.get(iTail, p);
						
			Real_MatrixEGradient.add(iHead, p, (dPartial * dRealHead  + 2.0 * (dLambda / iNumberOfFactors) * Real_MatrixE.get(iHead, p)) / dSize);
			Real_MatrixEGradient.add(iTail, p, (dPartial * dRealTail + 2.0 * (dLambda / iNumberOfFactors) * Real_MatrixE.get(iTail, p))/ dSize);
			Real_MatrixRGradient.add(iRelation, p, (dPartial * dRealRel + 2.0 * (dLambda / iNumberOfFactors) * Real_MatrixR.get(iRelation, p))/ dSize);
			Imag_MatrixEGradient.add(iHead, p, (dPartial * dImagHead + 2.0 * (dLambda / iNumberOfFactors) * Imag_MatrixE.get(iHead, p))/ dSize);
			Imag_MatrixEGradient.add(iTail, p, (dPartial * dImagTail + 2.0 * (dLambda / iNumberOfFactors) * Imag_MatrixE.get(iTail, p))/ dSize);
			Imag_MatrixRGradient.add(iRelation, p, (dPartial * dImagRel + 2.0 * (dLambda / iNumberOfFactors) * Imag_MatrixR.get(iRelation, p))/ dSize);
		}
	}
}
