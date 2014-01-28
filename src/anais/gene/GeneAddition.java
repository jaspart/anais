package anais.gene;

public class GeneAddition extends GeneOperator {
	public GeneAddition() {
		super();
	}
	public double evaluate(){
		return left.evaluate()+right.evaluate();
	}
	public String toString(){
		return "("+left.toString()+"+"+right.toString()+")";
	}
}
