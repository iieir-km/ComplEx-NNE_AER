package complex_NNE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import struct.Matrix;
import struct.Triple;

public class AdaGrad {
	public ArrayList<Triple> lstPosTriples;
	public ArrayList<Triple> lstHeadNegTriples;
	public ArrayList<Triple> lstTailNegTriples;
	public Matrix Real_MatrixE;
	public Matrix Real_MatrixR;
	public Matrix Imag_MatrixE;
	public Matrix Imag_MatrixR;
	public Matrix Real_MatrixEGradient;
	public Matrix Real_MatrixRGradient;
	public Matrix Imag_MatrixEGradient;
	public Matrix Imag_MatrixRGradient;
	public Matrix Real_MatrixEGSquare;
	public Matrix Real_MatrixRGSquare;
	public Matrix Imag_MatrixEGSquare;
	public Matrix Imag_MatrixRGSquare;
	public double dGamma;
	public double dLambda;
	
	public AdaGrad(
			ArrayList<Triple> inLstPosTriples,
			ArrayList<Triple> inLstHeadNegTriples,
			ArrayList<Triple> inLstTailNegTriples,
			Matrix in_Real_MatrixE,
			Matrix in_Real_MatrixR,
			Matrix in_Imag_MatrixE,
			Matrix in_Imag_MatrixR,
			Matrix in_Real_MatrixEGradient,
			Matrix in_Real_MatrixRGradient,
			Matrix in_Imag_MatrixEGradient,
			Matrix in_Imag_MatrixRGradient,
			Matrix in_Real_MatrixEGSquare,
			Matrix in_Real_MatrixRGSquare,
			Matrix in_Imag_MatrixEGSquare,
			Matrix in_Imag_MatrixRGSquare,
			double inGamma,
			double inLambda) {
		lstPosTriples = inLstPosTriples;
		lstHeadNegTriples = inLstHeadNegTriples;
		lstTailNegTriples = inLstTailNegTriples;
		Real_MatrixE = in_Real_MatrixE;
		Real_MatrixR = in_Real_MatrixR;
		Imag_MatrixE = in_Imag_MatrixE;
		Imag_MatrixR = in_Imag_MatrixR;
		Real_MatrixEGradient = in_Real_MatrixEGradient;
		Real_MatrixRGradient = in_Real_MatrixRGradient;
		Imag_MatrixEGradient = in_Imag_MatrixEGradient;
		Imag_MatrixRGradient = in_Imag_MatrixRGradient;
		Real_MatrixEGSquare = in_Real_MatrixEGSquare;
		Real_MatrixRGSquare = in_Real_MatrixRGSquare;
		Imag_MatrixEGSquare = in_Imag_MatrixEGSquare;
		Imag_MatrixRGSquare = in_Imag_MatrixRGSquare;
		dGamma = inGamma;
		dLambda = inLambda;
	}
	
	public void gradientDescent() throws Exception {
		Real_MatrixEGradient.setToValue(0.0);
		Real_MatrixRGradient.setToValue(0.0);
		Imag_MatrixEGradient.setToValue(0.0);
		Imag_MatrixRGradient.setToValue(0.0);
		
		int iSize = lstPosTriples.size() + lstHeadNegTriples.size() + lstTailNegTriples.size();
		HashSet eIDs = new HashSet();
		for (int iID = 0; iID < lstPosTriples.size(); iID++) {
			Triple PosTriple = lstPosTriples.get(iID);
			eIDs.add(PosTriple.head());
			eIDs.add(PosTriple.tail());
			Gradients posGradients = new Gradients(
					PosTriple,
					1.0,
					Real_MatrixE,
					Real_MatrixR,
					Imag_MatrixE,
					Imag_MatrixR,
					Real_MatrixEGradient,
					Real_MatrixRGradient,
					Imag_MatrixEGradient,
					Imag_MatrixRGradient,
					dLambda,
					(double)iSize);
			posGradients.calculateGradients();
		}
		for (int iID = 0; iID < lstHeadNegTriples.size(); iID++) {
			Triple HeadNegTriple = lstHeadNegTriples.get(iID);
            eIDs.add(HeadNegTriple.head());
            eIDs.add(HeadNegTriple.tail());
			Gradients headGradients = new Gradients(
					HeadNegTriple,
					-1.0,
					Real_MatrixE,
					Real_MatrixR,
					Imag_MatrixE,
					Imag_MatrixR,
					Real_MatrixEGradient,
					Real_MatrixRGradient,
					Imag_MatrixEGradient,
					Imag_MatrixRGradient,
					dLambda,
					(double)iSize);
			headGradients.calculateGradients();
		}
		for (int iID = 0; iID < lstTailNegTriples.size(); iID++) {
			Triple TailNegTriple = lstTailNegTriples.get(iID);
            eIDs.add(TailNegTriple.head());
            eIDs.add(TailNegTriple.tail());
			Gradients tailGradients = new Gradients(
					TailNegTriple,
					-1.0,
					Real_MatrixE,
					Real_MatrixR,
					Imag_MatrixE,
					Imag_MatrixR,
					Real_MatrixEGradient,
					Real_MatrixRGradient,
					Imag_MatrixEGradient,
					Imag_MatrixRGradient,
					dLambda,
					(double)iSize);
			tailGradients.calculateGradients();
		}
		Real_MatrixEGradient.rescaleByRow();
		Real_MatrixRGradient.rescaleByRow();
		Imag_MatrixEGradient.rescaleByRow();
		Imag_MatrixRGradient.rescaleByRow();

		for (int i = 0; i < Real_MatrixE.rows(); i++) {
			for (int j = 0; j < Real_MatrixE.columns(); j++) {
				double dG = Real_MatrixEGradient.get(i, j);
				Real_MatrixEGSquare.add(i, j, dG * dG);
				double dH = Math.sqrt(Real_MatrixEGSquare.get(i, j)) + 1e-8;
				Real_MatrixE.add(i, j, -1.0 * dGamma * dG / dH);	
			}
		}

		for (int i = 0; i < Real_MatrixR.rows(); i++) {
			for (int j = 0; j < Real_MatrixR.columns(); j++) {
				double dG = Real_MatrixRGradient.get(i, j);
				Real_MatrixRGSquare.add(i, j, dG * dG);
				double dH = Math.sqrt(Real_MatrixRGSquare.get(i, j)) + 1e-8;
				Real_MatrixR.add(i, j, -1.0 * dGamma * dG  / dH);
			}
		}
		
		for (int i = 0; i < Imag_MatrixE.rows(); i++) {
			for (int j = 0; j < Imag_MatrixE.columns(); j++) {
				double dG = Imag_MatrixEGradient.get(i, j);
				Imag_MatrixEGSquare.add(i, j, dG * dG);
				double dH = Math.sqrt(Imag_MatrixEGSquare.get(i, j)) + 1e-8;
				Imag_MatrixE.add(i, j, -1.0 * dGamma * dG  / dH);
				
			}
		}
		
		for (int i = 0; i < Imag_MatrixR.rows(); i++) {
			for (int j = 0; j < Imag_MatrixR.columns(); j++) {
				double dG = Imag_MatrixRGradient.get(i, j);
				Imag_MatrixRGSquare.add(i, j, dG * dG);
				double dH = Math.sqrt(Imag_MatrixRGSquare.get(i, j)) + 1e-8;
				Imag_MatrixR.add(i, j, -1.0 * dGamma * dG  / dH);
			}
		}
		//Real_MatrixE.truncate(0.0, 1.0);
		//Imag_MatrixE.truncate(0.0, 1.0);
        Iterator it = eIDs.iterator();
		while (it.hasNext()){
		    int ent = (Integer) it.next();
		    Real_MatrixE.truncate_row(0,1,ent);
		    Imag_MatrixE.truncate_row(0,1,ent);
        }
	}
}
