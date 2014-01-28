package anais.gene;

public class GeneSinus extends GenePreoperator {
	public double evaluate(){
		return Math.sin(right.evaluate());
	}
	public String toString(){
		return "sin("+right.toString()+")";
	}
}
