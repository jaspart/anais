package anais.gene;

import anais.world.Grammar;
import anais.world.Individual;

abstract class GenePreoperator extends Gene {
	protected Gene right;
	public GenePreoperator() {
		super();
	}
	public int size() {
		return right.size()+1;
	}
	public GenePreoperator copy() throws InstantiationException, IllegalAccessException {
		GenePreoperator newGene;
		newGene = this.getClass().newInstance();
		newGene.setDepth(this.getDepth());
		newGene.setFitness(this.getFitness());
		newGene.right=this.right.copy();
		return newGene;
	}
	public void copy(Gene gene) throws InstantiationException, IllegalAccessException {
		GenePreoperator baseGene = (GenePreoperator) gene;
		this.setDepth(baseGene.getDepth());
		this.setFitness(baseGene.getFitness());
		this.right = baseGene.right.copy();
	}
	public boolean isValue() {return false;}
	public boolean isOperator(){return false;}
	public boolean isPreoperator() {return true;}
	public void getChildren(Grammar grammar) throws InstantiationException, IllegalAccessException {
		right=grammar.getRandomGene(this.getDepth());
	}
	public Gene getGeneNumber(int target, int state) {
		Gene solution=null;
		if(target == state) {
			solution = this;
		}
		else {
			state=state+1;
			if(right.getGeneNumber(target, state) != null) {
				solution = right.getGeneNumber(target, state);
			}
		}
		return solution;
	}
	public boolean replaceGeneNumberBy(int target, int state, Gene gene) throws InstantiationException, IllegalAccessException {
		if(target == state) {
			((GenePreoperator)gene).right=this.right.copy();
			return true;
		}
		else {
			state=state+1;
			if(right.replaceGeneNumberBy(target, state, gene)) {
				right=gene.copy();
			}
			right.replaceGeneNumberBy(target, state, gene);
		}
		return false;
	}
	public boolean replaceGeneNumberBy(int target, int state, Individual individual) throws InstantiationException, IllegalAccessException {
		if(target == state) {
			return true;
		}
		else {
			state=state+1;
			if(right.replaceGeneNumberBy(target, state, individual)) {
				right=individual.getPhenotype().copy();
			}
			right.replaceGeneNumberBy(target, state, individual);
		}
		return false;
	}
}
