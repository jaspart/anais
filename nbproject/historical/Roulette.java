package anais.historical;

import anais.gene.Gene;
import anais.world.Grammar;

public class Roulette {
	int geneWheel[];
	int typeWheel[];
	int typeNumber = 4;
	int geneNumber;
	
	public Roulette(Grammar grammar){
		typeWheel = new int [typeNumber];
		for(int i = 0 ; i < typeNumber ; i++){
			typeWheel[i]=0;
		}
		geneNumber = grammar.size();
		for(Gene g : grammar){
			System.out.println(g.getClass().getSimpleName());
		}
	}
	public int wheelTypeSize(){
		int size=0;
		for(int i = 0 ; i < typeNumber ; i++){
			size+=typeWheel[i];
		}
		return size;
	}
	public int wheelGeneSize(){
		double size=0;
		//while(geneWheel.)
		return 0;
	}
	public String run(){
		double random = Math.random();
		int i = 0;
		boolean found = false;
		double total=0.0;
		/*while(i<geneWheel.size() && !found){
			total+=geneWheel.
			found=;
		}*/
		return null;
	}
}
