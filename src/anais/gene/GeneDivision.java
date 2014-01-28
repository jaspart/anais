package anais.gene;

public class GeneDivision extends GeneOperator {
	public GeneDivision() {
		super();
	}
	public double evaluate(){
		return left.evaluate()/right.evaluate();
	}
	public String toString(){
		return "("+left.toString()+")"+"/"+"("+right.toString()+")";
	}
}
