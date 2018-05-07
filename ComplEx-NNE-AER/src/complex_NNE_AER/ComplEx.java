package complex_NNE_AER;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import struct.*;
import util.NegativeTripleGenerator;

public class ComplEx {
	public TripleSet m_TrainTriples;
	public TripleSet m_ValidTriples;
	public TripleSet m_TestTriples;
	public TripleDict m_AllTriples;
	public RuleSet m_lstRules;
	public Matrix m_Real_MatrixE;
	public Matrix m_Real_MatrixR;
	public Matrix m_Imag_MatrixE;
	public Matrix m_Imag_MatrixR;
	public Matrix m_Real_MatrixEGradient;
	public Matrix m_Real_MatrixRGradient;
	public Matrix m_Imag_MatrixEGradient;
	public Matrix m_Imag_MatrixRGradient;
	public Matrix m_Real_MatrixEGSquare;
	public Matrix m_Real_MatrixRGSquare;
	public Matrix m_Imag_MatrixEGSquare;
	public Matrix m_Imag_MatrixRGSquare;

	public int m_NumRelation;
	public int m_NumEntity;
	public String m_MatrixE_prefix = "";
	public String m_MatrixR_prefix = "";

	public int m_NumFactor = 50;
	public double m_Lambda = 0.001;
	public double m_Gamma = 0.1;
	public double m_Mu = 0.1;
	public double m_pca = 0.8;
	public int m_NumNegative = 10;
	public int m_NumBatch = 100;
	public int m_NumIteration = 1000;
	public int m_OutputIterSkip = 50;

	java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.######");

	public ComplEx() {
	}

	public void initialization(
			String strNumRelation, String strNumEntity,
			String fnTrainTriples, String fnValidTriples, String fnTestTriples,
			String fnAllTriples,String fnRules) throws Exception {
		m_NumRelation = Integer.parseInt(strNumRelation);
		m_NumEntity = Integer.parseInt(strNumEntity);
		m_MatrixE_prefix = "model/MatrixE-k" + m_NumFactor
				+ "-lmbda" + decimalFormat.format(m_Lambda)
				+ "-gamma" + decimalFormat.format(m_Gamma)
                + "-mu" + decimalFormat.format(m_Mu)
				+ "-neg" + m_NumNegative
				+ "-min_pca" + decimalFormat.format(m_pca);
		m_MatrixR_prefix = "model/MatrixR-k" + m_NumFactor
				+ "-lmbda" + decimalFormat.format(m_Lambda)
				+ "-gamma" + decimalFormat.format(m_Gamma)
                + "-mu" + decimalFormat.format(m_Mu)
				+ "-neg" + m_NumNegative
			    + "-min_pca" + decimalFormat.format(m_pca);

		System.out.println("\nLoading train, valid, test, and all triples");
		m_TrainTriples = new TripleSet(m_NumEntity, m_NumRelation);
		m_ValidTriples = new TripleSet(m_NumEntity, m_NumRelation);
		m_TestTriples = new TripleSet(m_NumEntity, m_NumRelation);
		m_lstRules = new RuleSet();
		m_TrainTriples.load(fnTrainTriples, -1);
		m_ValidTriples.load(fnValidTriples, 1000);
		m_TestTriples.load(fnTestTriples, -1);
		m_AllTriples = new TripleDict();
		m_AllTriples.load(fnAllTriples);
		m_lstRules.load(fnRules);
		System.out.println("# train triples: " + m_TrainTriples.triples());
		System.out.println("# valid triples: " + m_ValidTriples.triples());
		System.out.println("# test triples: " + m_TestTriples.triples());
		System.out.println("# all triples: " + m_AllTriples.tripleDict().size());
		System.out.println("# all rules: " + m_lstRules.rules().size());
		System.out.println("Success.");


		System.out.println("\nInitializing (real/imaginary) matrix E and matrix R");
		m_Real_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		m_Real_MatrixE.initializeGaussian();
		m_Real_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		m_Real_MatrixR.initializeGaussian();
		m_Imag_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		m_Imag_MatrixE.initializeGaussian();
		m_Imag_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		m_Imag_MatrixR.initializeGaussian();
		m_Real_MatrixE.truncate(0.0, 1.0);
		m_Imag_MatrixE.truncate(0.0, 1.0);
		System.out.println("Success.");

		System.out.println("\nInitializing gradients/gradient squares of matrix E and matrix R");
		m_Real_MatrixEGradient = new Matrix(m_NumEntity, m_NumFactor);
		m_Real_MatrixRGradient = new Matrix(m_NumRelation, m_NumFactor);
		m_Imag_MatrixEGradient = new Matrix(m_NumEntity, m_NumFactor);
		m_Imag_MatrixRGradient = new Matrix(m_NumRelation, m_NumFactor);
		m_Real_MatrixEGSquare = new Matrix(m_NumEntity, m_NumFactor);
		m_Real_MatrixRGSquare = new Matrix(m_NumRelation, m_NumFactor);
		m_Imag_MatrixEGSquare = new Matrix(m_NumEntity, m_NumFactor);
		m_Imag_MatrixRGSquare = new Matrix(m_NumRelation, m_NumFactor);
		System.out.println("Success.");

	}

	public void learn() throws Exception {
		String PATHLOG = "log/log-k" + m_NumFactor
				+ "-lmbda" + decimalFormat.format(m_Lambda)
				+ "-gamma" + decimalFormat.format(m_Gamma)
				+ "-neg" + m_NumNegative
				+ "-mu"+decimalFormat.format(m_Mu)
				+ "-min_pca" + decimalFormat.format(m_pca)
				+ ".txt";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(PATHLOG), "UTF-8"));

		HashMap<Integer, ArrayList<Triple>> lstPosTriples = new HashMap<Integer, ArrayList<Triple>>();
		HashMap<Integer, ArrayList<Triple>> lstHeadNegTriples = new HashMap<Integer, ArrayList<Triple>>();
		HashMap<Integer, ArrayList<Triple>> lstTailNegTriples = new HashMap<Integer, ArrayList<Triple>>();

		int iCurIter = 0;
		double dCurMRR =  0.0;
		int iBestIter = -1;
		double dBestMRR =  -1.0;
		while (iCurIter < m_NumIteration) {
			m_TrainTriples.randomShuffle();
			for (int iIndex = 0; iIndex < m_TrainTriples.triples(); iIndex++) {
				Triple PosTriple = m_TrainTriples.get(iIndex);
				NegativeTripleGenerator negTripGen = new NegativeTripleGenerator(
						PosTriple, m_NumEntity, m_NumRelation);
				HashSet<Triple> headNegTripleSet = negTripGen.generateHeadNegTriple(m_NumNegative/2);
				HashSet<Triple> tailNegTripleSet = negTripGen.generateTailNegTriple(m_NumNegative/2);

				int iID = iIndex % m_NumBatch;
				if (!lstPosTriples.containsKey(iID)) {
					ArrayList<Triple> tmpPosLst = new ArrayList<Triple>();
					ArrayList<Triple> tmpHeadNegLst = new ArrayList<Triple>();
					ArrayList<Triple> tmpTailNegLst = new ArrayList<Triple>();
					tmpPosLst.add(PosTriple);
					tmpHeadNegLst.addAll(headNegTripleSet);
					tmpTailNegLst.addAll(tailNegTripleSet);
					lstPosTriples.put(iID, tmpPosLst);
					lstHeadNegTriples.put(iID, tmpHeadNegLst);
					lstTailNegTriples.put(iID, tmpTailNegLst);
				} else {
					lstPosTriples.get(iID).add(PosTriple);
					lstHeadNegTriples.get(iID).addAll(headNegTripleSet);
					lstTailNegTriples.get(iID).addAll(tailNegTripleSet);
				}
			}

			for (int iID = 0; iID < m_NumBatch; iID++) {
				AdaGrad adagrad = new AdaGrad(
						lstPosTriples.get(iID),
						lstHeadNegTriples.get(iID),
						lstTailNegTriples.get(iID),
						m_lstRules.rules(),
						m_Real_MatrixE,
						m_Real_MatrixR,
						m_Imag_MatrixE,
						m_Imag_MatrixR,
						m_Real_MatrixEGradient,
						m_Real_MatrixRGradient,
						m_Imag_MatrixEGradient,
						m_Imag_MatrixRGradient,
						m_Real_MatrixEGSquare,
						m_Real_MatrixRGSquare,
						m_Imag_MatrixEGSquare,
						m_Imag_MatrixRGSquare,
						m_Gamma,
						m_Lambda,
						m_Mu);
				adagrad.gradientDescent();
			}

			lstPosTriples = new HashMap<Integer, ArrayList<Triple>>();
			lstHeadNegTriples = new HashMap<Integer, ArrayList<Triple>>();
			lstTailNegTriples = new HashMap<Integer, ArrayList<Triple>>();
			iCurIter++;
			System.out.println("Complete iteration #" + iCurIter);

			if (iCurIter % m_OutputIterSkip == 0) {
				writer.write("Complete iteration #" + iCurIter + ":\n");
				Evaluation eval = new Evaluation(
						m_ValidTriples,
						m_AllTriples.tripleDict(),
						m_Real_MatrixE,
						m_Real_MatrixR,
						m_Imag_MatrixE,
						m_Imag_MatrixR);
				eval.calculateMetrics();
				dCurMRR = eval.dMRR;
				writer.write("------Current iteration #" + iCurIter + "\t" + dCurMRR + "\t" + eval.dHits10 + "\n");
				if (dCurMRR > dBestMRR) {
					m_Real_MatrixE.output(m_MatrixE_prefix + ".real");
					m_Real_MatrixR.output(m_MatrixR_prefix + ".real");
					m_Imag_MatrixE.output(m_MatrixE_prefix + ".imag");
					m_Imag_MatrixR.output(m_MatrixR_prefix + ".imag");
					dBestMRR = dCurMRR;
					iBestIter = iCurIter;
				}
				writer.write("------Best iteration #" + iBestIter + "\t" + dBestMRR + "\n");
				writer.flush();
			}
		}

		m_Real_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		m_Real_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		m_Imag_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		m_Imag_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		m_Real_MatrixE.load(m_MatrixE_prefix + ".real");
		m_Real_MatrixR.load(m_MatrixR_prefix + ".real");
		m_Imag_MatrixE.load(m_MatrixE_prefix + ".imag");
		m_Imag_MatrixR.load(m_MatrixR_prefix + ".imag");
		Evaluation testEval = new Evaluation(
				m_TestTriples,
				m_AllTriples.tripleDict(),
				m_Real_MatrixE,
				m_Real_MatrixR,
				m_Imag_MatrixE,
				m_Imag_MatrixR);
		testEval.calculateMetrics();
		writer.write("################################################\n");
		writer.write("MRR:\t" + testEval.dMRR + "\n");
		writer.write("MeanRank:\t" + testEval.dMeanRank + "\n");
		writer.write("Median:\t" + testEval.dMedian + "\n");
		writer.write("Hits@1:\t" + testEval.dHits1 + "\n");
		writer.write("Hits@3:\t" + testEval.dHits3 + "\n");
		writer.write("Hits@5:\t" + testEval.dHits5 + "\n");
		writer.write("Hits@10:\t" + testEval.dHits10 + "\n");
		writer.close();
	}
}
