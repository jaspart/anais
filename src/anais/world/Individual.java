package anais.world;

import java.io.IOException;
import java.util.ArrayList;

import anais.gene.Gene;
	
public class Individual {
	private Gene phenotype;
	private int size;
	private double fitness;
	public Individual(Grammar grammar, Environment environment) throws InstantiationException, IllegalAccessException, IOException {
		do {
			phenotype=grammar.getRandomGene(0);
			this.setSize();
		} while( this.getSize() < 2 && this.getSize() > 15);
		fitness = mesure(environment.mesure(this, grammar));
	}
	public Individual(Individual individual) throws InstantiationException, IllegalAccessException {
		this.phenotype = individual.phenotype.copy();
		this.size = individual.size;
		this.fitness = individual.fitness;
	}
	private Individual(Gene pickedGene) throws InstantiationException, IllegalAccessException {
		this.phenotype = pickedGene.copy();
		this.setSize();
	}
	private void setSize() {
		this.size = phenotype.size();
	}
	public int getSize() {
		return size;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public Gene getPhenotype() {
		return phenotype;
	}
	public void setPhenotype(Gene phenotype) {
		this.phenotype = phenotype;
	}
	public String toString(){
		return phenotype.toString();
	}
	double mesure(double error) {
		return 1.0/(1.0+error);
	}
	private Gene pickGene() {
		int random = (int) (Math.random()*size)+1;
		return phenotype.getGeneNumber(random, 1);
	}
	public boolean existIn(ArrayList<Individual> population) {
		boolean exist = false;
		for(Individual individual : population) {
			exist = exist || this.toString().equals(individual);
		}
		return exist;
	}
	public Individual mutate(Grammar grammar, Environment environment) throws InstantiationException, IllegalAccessException, IOException  {
		int random = (int) (Math.random()*size)+1;
		Gene randomGene = grammar.getOtherLike(phenotype.getGeneNumber(random, 1));
		phenotype.replaceGeneNumberBy(random, 1, randomGene);
		if(random==1) {
			phenotype = randomGene.copy();
		}
		fitness = mesure(environment.mesure(this, grammar));
		return this;
	}
	public Individual crossover(Individual individual, Grammar grammar, Environment environment) throws InstantiationException, IllegalAccessException {
		do{
			Gene gene = individual.pickGene();
			try{
				Individual subindiv = new Individual(gene);
				int random = (int) (Math.random()*Math.min(size, 10))+1;
				/*System.out.println("crossover replaceGeneNumberBy "+"" +
						" phenotype.random="+phenotype.getGeneNumber(random, 1)+
						" subindiv="+subindiv+
						" phenotype="+phenotype);*/
				phenotype.replaceGeneNumberBy(random, 1, subindiv);
				//System.out.println("crossover 4");
				this.setSize();
				this.fitness = mesure(environment.mesure(this, grammar));
			}catch (Exception e) {
				System.out.println("WARN: "+e+" : "+(gene==null));
				System.out.println("WARN: gene="+gene);
			}
		} while(size > 30);
		this.setSize();
		return this;
	}
}