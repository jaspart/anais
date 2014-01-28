package anais.gene;

public class GeneSubstraction extends GeneOperator {
	public GeneSubstraction() {
		super();
	}
	public double evaluate(){
		return left.evaluate()-right.evaluate();
	}
	public String toString(){
		return "("+left.toString()+"-"+right.toString()+")";
	}
}
