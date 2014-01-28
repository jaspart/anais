package anais.gene;

public class GeneLog extends GenePreoperator {
	public double evaluate(){
		return Math.log(right.evaluate());
	}
	public String toString(){
		return "log("+right.toString()+")";
	}
}
