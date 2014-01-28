package anais.historical;

import anais.environment.Environment;
import anais.environment.Variable;
import anais.gene.Gene;
import anais.world.Grammar;


/** Individuals of the World*/
public class Individual{
    Gene [] genotype;
    Gene phenotype;
    double [] being;
    double fitness;

    public Individual(Grammar grammar, Environment environment, Roulette wheel) {
    	
    }
    
   /**
    * do a individual copy from the daddy
    */
    public Individual(Individual dady, Grammar grammar, Environment environment){
        fitness=dady.fitness;
        genotype=new Gene[dady.genotype.length];
        for(int i=0;i<dady.genotype.length;i++){
        	genotype[i]=dady.genotype[i];
        }
        being=new double[environment.getLenght()];
        for(int i=0;i<environment.getLenght();i++){
            being[i]=dady.being[i];
        }
    }

/**
    * Evaluation of an individual in the world
    */
    public void Evaluate(Environment environment,double eps){
        being=new double[environment.getLenght()];
        Variable X=new Variable(0,0);
        for(int i=0;i<environment.getLenght();i++){
            X.valeur=environment.x[i];
            X.index=0;
            if((Double.isNaN(being[i]))||(Double.isInfinite(being[i]))){
                being[i]=Double.POSITIVE_INFINITY;
            }
        }
    }
    
	/**
     * Write the phenotype in functional representation
     */
    public String toString() {
        return phenotype.toString();
    }
    
}
