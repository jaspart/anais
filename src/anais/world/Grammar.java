package anais.world;

import java.util.ArrayList;
import java.util.Random;

import anais.gene.*;

public class Grammar extends ArrayList<Gene>{
	private static final long serialVersionUID = -1210982846205233574L;
	public Grammar(boolean prefilled, Matrix matrix){
		super();
		if(prefilled){
			this.add(new GeneVariable('x',-9,9,0,matrix.getDimension(0)));
			this.add(new GeneVariable('y',-9,9,1,matrix.getDimension(1)));
			this.add(new GeneConstant('M',11.13));
			this.add(new GeneConstant('0',0.0));
			this.add(new GeneConstant('π',Math.PI));
			this.add(new GeneAddition());
			this.add(new GeneSubstraction());
			this.add(new GeneMultiplication());
			this.add(new GeneDivision());
			this.add(new GeneCosinus());
	//		this.add(new GeneExp());
	//		this.add(new GeneLog());
			this.add(new GeneSquare());
			this.add(new GeneSqrt());
			this.add(new GeneDirac());
			/*this.add(new GeneVariable('ϴ',-180,180,360));
			this.add(new GeneVariable('r',0,1,100));
			this.add(new GeneVariable('h',0,1,10));*/
		}
	}
	public Gene getRandomGene(int depth) throws InstantiationException, IllegalAccessException {
		Random generator = new Random();
		Gene randomGene = this.get(generator.nextInt(this.size()));
		if ( depth > World.MAX_CELL_LENGTH ) {
			while( ! randomGene.isValue()){
				randomGene = this.get(generator.nextInt(this.size()));
			}
		}
		if(! randomGene.isValue()){
			randomGene = randomGene.getClass().newInstance();
		}
		randomGene.setDepth(depth+1);
		randomGene.getChildren(this);
		return randomGene;
	}
	public Gene getOtherLike(Gene gene) throws InstantiationException, IllegalAccessException {
		Random generator = new Random();
		Gene randomGene = null;
		try {
			do {
				randomGene = this.get(generator.nextInt(this.size()));
			} while( ! gene.isSameTypeAs(randomGene) || gene.isSameAs(randomGene) );
		}
		catch(Exception e) {
			System.out.println(e+" gene.isNull="+(gene==null)+" random.isNull="+(randomGene==null));
		}
		return randomGene;
	}
	public ArrayList<GeneVariable> getVariables(){
		ArrayList<GeneVariable> variableList = new ArrayList<GeneVariable>();
		for(Gene gene : this) {
			gene.addto(variableList);
		}
		return variableList;
	}
}