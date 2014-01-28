package anais.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** Environment of the World*/
public class Environment {
	ArrayList<Matrix> matrix = new ArrayList<Matrix>();
	ArrayList<String> files = new ArrayList<String>();
	public Environment() throws IOException {
		matrix = new ArrayList<Matrix>();
		files = new ArrayList<String>();
		files.add("/home/arnaud/workspace/anais/cal0327.txt");
		matrix.add(new Matrix(getFileReader(files.get(0))));

	}
	public Environment(String [] fileList) throws IOException {
		matrix = new ArrayList<Matrix>();
		files = new ArrayList<String>();
		for(String file : fileList) {
			files.add(file);
			matrix.add(new Matrix(getFileReader(file)));
		}
	}
	public ArrayList<Matrix> getMatrix() {
		return matrix;
	}
	public Matrix getMatrix(int i) {
		return matrix.get(i);
	}
	public void setMatrix(ArrayList<Matrix> matrix) {
		this.matrix = matrix;
	}
	public ArrayList<String> getFiles() {
		return files;
	}
	public void setFiles(ArrayList<String> files) {
		this.files = files;
	}

    public static BufferedReader getFileReader(String fileName) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader( fileName ));
		}
		catch (FileNotFoundException e1) {
		    System.out.println("File " + fileName + " not found : "+e1);
		}
		return reader;
    }
    public double mesure(Individual individual, Grammar grammar) throws IOException, InstantiationException, IllegalAccessException {
		double error = 0;
		for(Matrix m : getMatrix()) {
			error += m.difference(individual, grammar);
		}
    	return error;
    }
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
    	Environment environment = new Environment();
    	Grammar grammar = new Grammar(true, environment.getMatrix(0));
		Individual adam = new Individual(grammar, environment);
		Matrix adamMatrix = new Matrix(environment.getMatrix().get(0), adam, grammar);
		System.out.println(adam+"  fitness="+adam.getFitness()+"\n"+adamMatrix);
    }
}