package anais.gene;

public class GeneSqrt extends GenePreoperator {
	public double evaluate(){
		return Math.sqrt(right.evaluate());
	}
	public String toString(){
		return "√("+right.toString()+")";
	}
}
