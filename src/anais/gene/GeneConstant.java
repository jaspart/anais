package anais.gene;

public class GeneConstant extends GeneValue {
	public GeneConstant(){
		super();
	}
	public GeneConstant(char symbol, double value){
		this.value=value;
		this.symbol=symbol;
	}
	public double evaluate(){
		return value;
	}
	public String toString(){
		return ""+symbol;
	}
}
//168 star wars
//9432 syfy