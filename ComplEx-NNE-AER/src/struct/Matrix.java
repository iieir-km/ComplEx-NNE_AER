package struct;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import util.StringSplitter;

public class Matrix {
	private double[][] pData = null;
	private int iNumberOfRows;
	private int iNumberOfColumns;
	
	public Matrix() {
	}
	
	public Matrix(int iRows, int iColumns) {
		pData = new double[iRows][];
		for (int i = 0; i < iRows; i++) {
			pData[i] = new double[iColumns];
			for (int j = 0; j < iColumns; j++) {
				pData[i][j] = 0.0;
			}
		}
		iNumberOfRows = iRows;
		iNumberOfColumns =  iColumns;
	}
	
	public int rows() {
		return iNumberOfRows;
	}
	
	public int columns() {
		return iNumberOfColumns;
	}
	
	public double get(int i, int j) throws Exception {
		if (i < 0 || i >= iNumberOfRows) {
			throw new Exception("get error in Matrix: RowID out of range");
		}
		if (j < 0 || j >= iNumberOfColumns) {
			throw new Exception("get error in Matrix: ColumnID out of range");
		}
		return pData[i][j];
	}
	
	public void set(int i, int j, double dValue) throws Exception {
		if (i < 0 || i >= iNumberOfRows) {
			throw new Exception("set error in Matrix: RowID out of range");
		}
		if (j < 0 || j >= iNumberOfColumns) {
			throw new Exception("set error in Matrix: ColumnID out of range");
		}
		pData[i][j] = dValue;
	}
	
	public void setToValue(double dValue) {
		for (int i = 0; i < iNumberOfRows; i++) {
			for (int j = 0; j < iNumberOfColumns; j++) {
				pData[i][j] = dValue;
			}
		}
	}
	
	public void add(int i, int j, double dValue) throws Exception {
		if (i < 0 || i >= iNumberOfRows) {
			throw new Exception("add error in Matrix: RowID out of range");
		}
		if (j < 0 || j >= iNumberOfColumns) {
			throw new Exception("add error in Matrix: ColumnID out of range");
		}
		pData[i][j] += dValue;
	}
	
	public void initializeUnif() {
		Random rd = new Random(123);
		for (int i = 0; i < iNumberOfRows; i++) {
			for (int j = 0; j < iNumberOfColumns; j++) {
				double dValue = rd.nextDouble();
				pData[i][j] = 2.0 * dValue - 1.0;
			}
		}
	}
	
	public void initializeNunif() {
		Random rd = new Random(123);
		double dBnd = Math.sqrt(6.0) / Math.sqrt(iNumberOfRows + iNumberOfColumns);
		for (int i = 0; i < iNumberOfRows; i++) {
			for (int j = 0; j < iNumberOfColumns; j++) {
				double dValue = rd.nextDouble();
				pData[i][j] = (2.0 * dValue - 1.0) * dBnd;
			}
		}
	}
	
	public void initializeGaussian() {
		Random rd = new Random(123);
		for (int i = 0; i < iNumberOfRows; i++) {
			for (int j = 0; j < iNumberOfColumns; j++) {
				double dValue = rd.nextGaussian();
				pData[i][j] = dValue;
			}
		}
	}
	
	public void rescaleByRow() {
		for (int i = 0; i < iNumberOfRows; i++) {
			double dNorm = 0.0;
			for (int j = 0; j < iNumberOfColumns; j++) {
				dNorm += pData[i][j] * pData[i][j];
			}
			dNorm = Math.sqrt(dNorm);
			if (dNorm != 0.0) {
				for (int j = 0; j < iNumberOfColumns; j++) {
					pData[i][j] *= Math.min(1.0, 1.0/dNorm);
				}
			}
		}
	}
	
	public void truncate(double dLower, double dUpper) {
		for (int i = 0; i < iNumberOfRows; i++) {
			for (int j = 0; j < iNumberOfColumns; j++) {
				double dValue = pData[i][j];
				if (pData[i][j] < dLower) {
					dValue = dLower;
				}
				if (pData[i][j] > dUpper) {
					dValue = dUpper;
				}
				pData[i][j] = dValue;
			}
		}
	}

	public void truncate_row(double dLower, double dUpper, int i) {
		for (int j = 0; j < iNumberOfColumns; j++) {
			double dValue = pData[i][j];
			if (pData[i][j] < dLower) {
				dValue = dLower;
			}
			if (pData[i][j] > dUpper) {
				dValue = dUpper;
			}
			pData[i][j] = dValue;
		}
	}
	
	public boolean load(String fnInput) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		
		String line = "";
		line = reader.readLine();
		String[] first_line = StringSplitter.RemoveEmptyEntries(StringSplitter
				.split(":; ", line));
		if (iNumberOfRows != Integer.parseInt(first_line[1]) || 
				iNumberOfColumns != Integer.parseInt(first_line[3])) {
			throw new Exception("load error in Matrix: row/column number incorrect");
		}
		
		int iRowID = 0;
		while ((line = reader.readLine()) != null) {
			String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter
					.split("\t ", line));
			if (iRowID < 0 || iRowID >= iNumberOfRows) {
				throw new Exception("load error in Matrix: RowID out of range");
			}
			if (tokens.length != iNumberOfColumns) {
				throw new Exception("load error in Matrix: ColumnID out of range");
			}
			for (int iColumnID = 0; iColumnID < tokens.length; iColumnID++) {
				pData[iRowID][iColumnID] = Double.parseDouble(tokens[iColumnID]);
			}
			iRowID++;
		}
		
		reader.close();
		return true;
	}
	
	public void output(String fnOutput) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnOutput), "UTF-8"));
		
		writer.write("iNumberOfRows: " + iNumberOfRows + "; iNumberOfColumns: " + iNumberOfColumns + "\n");
		for (int i = 0; i < iNumberOfRows; i++) {
			writer.write((pData[i][0] + " ").trim());
			for (int j = 1; j < iNumberOfColumns; j++) {
				writer.write("\t" + (pData[i][j] + " ").trim());
			}
			writer.write("\n");
		}
		
		writer.close();
	}
	
	public void releaseMemory() {
		for (int i = 0; i < iNumberOfRows; i++) {
			pData[i] = null;
		}
		pData = null;
		iNumberOfRows = 0;
		iNumberOfColumns = 0;
	}
}
