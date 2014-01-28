package anais.gene;

public class GeneCosinus extends GenePreoperator {
	public double evaluate(){
		return Math.cos(right.evaluate());
	}
	public String toString(){
		return "cos("+right.toString()+")";
	}
}
