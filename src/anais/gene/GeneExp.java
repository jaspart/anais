package anais.gene;

public class GeneExp extends GenePreoperator {
	public double evaluate(){
		return Math.exp(right.evaluate());
	}
	public String toString(){
		return "exp("+right.toString()+")";
	}
}
