package anais.gene;

public class GeneSquare extends GenePreoperator {
	public double evaluate(){
		return Math.pow(right.evaluate(),2);
	}
	public String toString(){
		return "("+right.toString()+")²";
	}
}
