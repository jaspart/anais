package anais.gene;

import java.util.ArrayList;

public final class GeneVariable extends GeneValue {
	private double min;
	private double max;
	private int index;
	private int N;
	private int level;
	public GeneVariable(){
		super();
	}
	public GeneVariable(char symbol, double min, double max, int level, int n) {
		super(symbol, min);
		this.max=max;
		this.min=min;
		this.N=n;
		this.level=level;
		this.index=0;
	}
	public double evaluate() {
		get();
		return getValue();
	}
	public String toString() {
		return ""+symbol;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	public double get() {
		setValue(min+(max-min)*(double)this.index/(double)this.N);
		return getValue();
	}
	public double get(int index) {
		this.index = index;
		setValue(min+(max-min)*(double)this.index/(double)this.N);
		return getValue();
	}
	@Override
	public void addto(ArrayList<GeneVariable> variableList) {
		variableList.add(this);
	}
	public boolean increment(boolean inc) {
		int preindex = index;
		inc = (level == 0) || inc;
		if(inc) {
			index=(index+1)%N;
		}
		return (index == 0) && (preindex != index);
	}
}
