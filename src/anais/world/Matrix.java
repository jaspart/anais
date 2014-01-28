package anais.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import anais.gene.GeneVariable;

/**
 * 
 * @author arnaud
 *
 *	dtheta=(2*Math.PI)/(double)(n-1);
 *	dr=R/(double)(m-1);
 *
 *	double r=Math.sqrt(x*x+y*y);
 *	double theta=Math.atan2(y,x);
 *	double x=((double)i-(double)(m))/(double)(m);
 *	double y=((double)j-(double)(m))/(double)(m);
 *	double r=Math.sqrt(x*x+y*y);
 *	double theta=Math.atan2(y,x);
 *	int ix=(int)(r*(double)m);
 *	int iy=(int)((theta+Math.PI)/dtheta);
 *
 */

public class Matrix {
	double matrix[][];
	int length;
	String split = "\t";
	private int[] dimension;
	
	public Matrix(Matrix m) {
		dimension = new int[2];
		dimension[0]=m.matrix.length;
		dimension[1]=m.matrix[0].length;
		matrix = new double[dimension[0]][dimension[1]];
		this.length = m.length;
		System.out.println("init x="+dimension[0]+" y="+dimension[1]+" s="+length);
	}
	
	/** Build a matrix from a file/bufferReader */
	public Matrix(BufferedReader reader) throws IOException {
		try {
		    // read the lines of the matrix as Strings
		    String line = null;
		    do {
		    	line = reader.readLine();
		    } while ((line != null && line.trim().equals("")) ||
		    		line.startsWith(";") || line.contains("="));
	
		    ArrayList<String> lines = new ArrayList<String>();
		    while (line != null && (!line.trim().equals(""))) {
		    	lines.add( line );
				line = reader.readLine();
		    }
		    
		    // split the lines in double numbers, implement the matrix
			if (lines.size() > 0) {
				double sum = 0;
			    String current = null;
			    matrix = new double[lines.size()][];
			    try {
					for (int i = 0; i < lines.size(); i++) {
					    String[] splitter = lines.get(i).split(split);
					    matrix[i] = new double[ splitter.length ];
					    for (int j = 0; j < splitter.length; j++) {
					    	current = splitter[j];
							matrix[i][j] = Double.parseDouble( splitter[j] );
							sum += matrix[i][j];
					    }
					}
					System.out.println("Total sum = "+sum);
			    }
			    catch (NumberFormatException e) {
			    	System.out.println("Could not convert \"" + current + "\" to floating point");
			    }
			}
			dimension = new int[2];
			dimension[0]=matrix.length;
			dimension[1]=matrix[0].length;
		}
		catch (IOException e) {
		    System.out.println("readMatrix I/O error: " + e );
		}
		length = matrix.length*matrix[0].length;
		System.out.println("read x="+dimension[0]+" y="+dimension[1]+" s="+length);
	}
	
	public Matrix(Matrix m, Individual individual, Grammar grammar) {
		dimension = new int[2];
		dimension[0]=m.matrix.length;
		dimension[1]=m.matrix[0].length;
		matrix = new double[m.matrix.length][m.matrix[0].length];
		this.length = m.length;
		boolean incrementNextValue = true;
		for(int i=0 ; i<this.length ; i++) {
	        for (GeneVariable variable : grammar.getVariables()) {
	        	double value = individual.getPhenotype().evaluate();
	        	this.set(grammar.getVariables(), value);
	        	incrementNextValue = variable.increment(incrementNextValue);
	        }
		}
		System.out.println("copy x="+dimension[0]+" y="+dimension[1]+" s="+length);
	}

	/** Smooth a Matrix */
	void smoother(){
		double[][] Save=new double[matrix.length][matrix[0].length];
		for (int i=1;i<matrix.length-1;i++){
			for (int j=1;j<matrix[0].length-1;j++){
				Save[i][j]=matrix[i][j];
			}
		}
		for (int i=1;i<matrix.length-1;i++){
			for (int j=1;j<matrix[0].length-1;j++){
				matrix[i][j]=(4*Save[i][j]+Save[i-1][j]+
						Save[i+1][j]+Save[i][j-1]+Save[i][j+1])/8;
			}
		}
	}
	
	void set(ArrayList<GeneVariable> variables, double value) {
		if((new Double(value)).isInfinite() || (new Double(value)).isNaN()) {
			value = World.MAX_VALUE;
		}
		matrix[variables.get(0).getIndex()][variables.get(1).getIndex()] = value;
	}

	double get(ArrayList<GeneVariable> variables) {
		return matrix[variables.get(0).getIndex()][variables.get(1).getIndex()];
	}
	double difference(Individual individual, Grammar grammar) throws IOException, InstantiationException, IllegalAccessException {
		boolean incrementNextValue = true;
		double error=0;
		for(int i=0 ; i< this.length ; i++) {
	        for (GeneVariable variable : grammar.getVariables()) {
	        	double value = individual.getPhenotype().evaluate();
	        	if((new Double(value)).isInfinite() || (new Double(value)).isNaN()) {
	    			value = World.MAX_VALUE;
	    		}
	        	error += Math.abs(value-this.get(grammar.getVariables()));
	        	incrementNextValue = variable.increment(incrementNextValue);
	        }
		}
		return error;
	}

	/** Display the matrix */
	public String toString(){
		String string = "";
		DecimalFormat format = new DecimalFormat ("####0.00");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				string += format.format(matrix[i][j]) + "\t";
		    }
			string += "\n";
		}
		return string;
	}

	public int getDimension(int i) {
		return dimension[i];
	}
}