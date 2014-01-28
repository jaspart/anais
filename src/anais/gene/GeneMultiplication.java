package anais.gene;

public class GeneMultiplication extends GeneOperator {
	public GeneMultiplication() {
		super();
	}
	public double evaluate(){
		return left.evaluate()+right.evaluate();
	}
	public String toString(){
		return "("+left.toString()+"*"+right.toString()+")";
	}
}