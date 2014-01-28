package anais.gene;

public class GeneDirac extends GenePreoperator {
	public double evaluate(){
		double value = 0.0;
		if(right.evaluate()>0.0) value = right.evaluate();
		return value;
	}
	public String toString(){
		return "δ("+right.toString()+")";
	}
}
