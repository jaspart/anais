package anais.historical;

import java.util.ArrayList;

import anais.gene.Gene;

public class Grammar {
	private ArrayList<Gene> GeneList;
	public Grammar(){
		GeneList = new ArrayList<Gene> ();
	}
	public Gene getRandomGene(){
		int index = (int) (Math.random() * GeneList.size());
		return GeneList.get(index);
	}
	public boolean isComplete(){
		return !GeneList.isEmpty();
	}
	public void add(Gene gene){
		GeneList.add(gene);
	}
	public ArrayList<Gene> get(){
		return GeneList;
	}
	public int size() {
		return GeneList.size();
	}
}