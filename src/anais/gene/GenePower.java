package anais.gene;

public class GenePower extends GeneOperator {
	public GenePower() {
		super();
	}
	public double evaluate(){
		return Math.pow(left.evaluate(),right.evaluate());
	}
	public String toString(){
		return "("+left.toString()+")"+"^"+"("+right.toString()+")";
	}
}
