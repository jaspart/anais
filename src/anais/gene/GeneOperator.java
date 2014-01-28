package anais.gene;

import anais.world.Grammar;
import anais.world.Individual;

abstract class GeneOperator extends Gene {
	protected Gene left;
	protected Gene right;
	public GeneOperator() {
		super();
	}
	public int size() {
		return right.size()+left.size()+1;
	}
	public GeneOperator copy() throws InstantiationException, IllegalAccessException {
		GeneOperator newGene;
		newGene=this.getClass().newInstance();
		newGene.setDepth(this.getDepth());
		newGene.setFitness(this.getFitness());
		newGene.left=this.left.copy();
		newGene.right=this.right.copy();
		return newGene;
	}
	public void copy(Gene gene) throws InstantiationException, IllegalAccessException {
		GeneOperator baseGene = (GeneOperator) gene;
		this.setDepth(baseGene.getDepth());
		this.setFitness(baseGene.getFitness());
		this.left = baseGene.left.copy();
		this.right = baseGene.right.copy();
	}
	public boolean isValue() {return false;}
	public boolean isOperator(){return true;}
	public boolean isPreoperator() {return false;}
	public void getChildren(Grammar grammar) throws InstantiationException, IllegalAccessException {
		left=grammar.getRandomGene(this.getDepth());
		right=grammar.getRandomGene(this.getDepth());
	}
	public Gene getGeneNumber(int target, int state) {
		Gene solution=null;
		if(target == state) {
			solution = this;
		}
		else {
			state=state+1;
			if(left.getGeneNumber(target, state) != null) {
				solution = left.getGeneNumber(target, state);
			}
			state+=left.size();
			if(right.getGeneNumber(target, state) != null) {
				solution = right.getGeneNumber(target, state);
			}
		}
		return solution;
	}
	public boolean replaceGeneNumberBy(int target, int state, Gene gene) throws InstantiationException, IllegalAccessException {
		if(target == state) {
			((GeneOperator)gene).left=this.left.copy();
			((GeneOperator)gene).right=this.right.copy();
			return true;
		}
		else {
			state=state+1;
			if(left.replaceGeneNumberBy(target, state, gene)) {
				left=gene.copy();
			}
			left.replaceGeneNumberBy(target, state, gene);
			
			state+=left.size();
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
			if(left.replaceGeneNumberBy(target, state, individual)) {
				left=individual.getPhenotype().copy();
			}
			left.replaceGeneNumberBy(target, state, individual);
			
			state+=left.size();
			if(right.replaceGeneNumberBy(target, state, individual)) {
				right=individual.getPhenotype().copy();
			}
			right.replaceGeneNumberBy(target, state, individual);
		}
		return false;
	}
}
