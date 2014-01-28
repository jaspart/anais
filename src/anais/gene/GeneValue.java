package anais.gene;

import anais.world.Grammar;
import anais.world.Individual;

abstract class GeneValue extends Gene {
	protected double value;
	protected char symbol;
	public GeneValue() {
		super();
	}
	public GeneValue(char symbol, double value) {
		this.setDepth(0);
		this.setFitness(1L);
		this.setSymbol(symbol);
		this.setValue(value);
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public char getSymbol() {
		return symbol;
	}
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	public int size() {
		return 1;
	}
	/*public GeneValue copy() {
		GeneValue newGene;
		newGene=this.getClass().newInstance();
		newGene.setDepth(this.getDepth());
		newGene.setFitness(this.getFitness());
		newGene.symbol=this.symbol;
		newGene.value=this.value;
		return newGene;
	}*/
	public GeneValue copy() {
		return this;
	}
	@Override
	public void copy(Gene gene) throws InstantiationException, IllegalAccessException {
		
	}
/*	public void copy(Gene gene) {
		GeneValue baseGene = (GeneValue) gene;
		this.setDepth(baseGene.getDepth());
		this.setFitness(baseGene.getFitness());
	}*/
	public boolean isSameAs(Gene gene) {
		return (gene.isValue() && (symbol == ((GeneValue)gene).symbol));
	}
	public boolean isValue() {return true;}
	public boolean isOperator(){return false;}
	public boolean isPreoperator() {return false;}
	public void getChildren(Grammar grammar) { }
	public Gene getGeneNumber(int target, int state) {
		Gene solution=null;
		if(state == target) {
			solution = this;
		}
		return solution;
	}
	public boolean replaceGeneNumberBy(int target, int state, Gene gene) {
		if(target == state) {
			return true;
		}
		return false;
	}
	public boolean replaceGeneNumberBy(int target, int state, Individual individual) {
		if(target == state) {
			return true;
		}
		return false;
	}
}