package anais.gene;

import java.util.ArrayList;

import anais.world.Grammar;
import anais.world.Individual;

public abstract class Gene {
	private Long fitness;
	private int depth;
 	
	public Gene() {
		this.fitness = 1L;
		this.setDepth(1);
	}
	
	public Long getFitness() {
		return fitness;
	}
	public void setFitness(Long fitness) {
		this.fitness = fitness;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public abstract int size();
	public abstract Gene copy() throws InstantiationException, IllegalAccessException;
	public abstract void copy(Gene gene)  throws InstantiationException, IllegalAccessException;
	public abstract String toString();
	public abstract double evaluate();
	public abstract boolean isValue();
	public abstract boolean isOperator();
	public abstract boolean isPreoperator();
	public boolean isSameAs(Gene gene) {
		return gene.getClass().getSimpleName().equalsIgnoreCase(
				this.getClass().getSimpleName());
	}
	public boolean isSameTypeAs(Gene gene) {
		return gene.getClass().getSuperclass().getSimpleName().equalsIgnoreCase(
				this.getClass().getSuperclass().getSimpleName());
	}
	public abstract Gene getGeneNumber(int target, int state);
	public abstract boolean replaceGeneNumberBy(int random, int init, Gene randomGene) throws InstantiationException, IllegalAccessException;
	public abstract boolean replaceGeneNumberBy(int target, int init, Individual individual) throws InstantiationException, IllegalAccessException;
	public abstract void getChildren(Grammar backusNaur) throws InstantiationException, IllegalAccessException;

	public void addto(ArrayList<GeneVariable> variableList) {
		//Do nothing except in GeneVariable
	}
}
