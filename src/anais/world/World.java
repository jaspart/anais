/**
 * 
 */
package anais.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author arnaud
 */
public class World {
	public static final double MAX_VALUE = 1000.0;
	public static final int MAX_CELL_LENGTH = 5;
	Grammar grammar;
	Environment environment;
	ArrayList<Individual> population;
	ArrayList<Individual> best;
	int size = 50;//500
	private int sizeBest = size/10;//50
	private int sizeRandom = size/10;//50
	private int sizeMutant = size/10;//50
	private int sizeOffspring = size/2;//250
	private int sizeRest = size - sizeBest - sizeRandom - sizeMutant - sizeOffspring;//100
	private int sizeGeneration = 5000;

	public World() throws IOException, InstantiationException, IllegalAccessException {
		environment = new Environment();
		grammar = new Grammar(true, environment.getMatrix(0));
		best = new ArrayList<Individual>();
		population = new ArrayList<Individual>();
		for (int i = 0 ; i < size ; i++) {
			population.add(new Individual(grammar, environment));
		}
	}
	private double getBestFitness() {
		double bestFitness = 0;
		for(Individual individual : population) {
			if(individual.getFitness() > bestFitness) {
				bestFitness = individual.getFitness();
			}
		}
		return bestFitness;
	}
	private Individual getBestIndividual() {
		double bestFitness = 0;
		Individual bestIndividual = null;
		for(Individual individual : population) {
			if(individual.getFitness() > bestFitness) {
				bestFitness = individual.getFitness();
				bestIndividual = individual;
			}
		}
		return bestIndividual;
	}
	private void selectBestIndividuals() throws InstantiationException, IllegalAccessException {
		for(int i=0 ; i<sizeBest  ; i++) {
			best.add(new Individual(getBestIndividual()));
			population.remove(getBestIndividual());
		}
		population.addAll(best);
	}
	private double calculateSumFitness() {
		double sumFitness = 0;
		for(Individual individual : population) {
			sumFitness += individual.getFitness();
		}
		return sumFitness;
	}
	private double calculateBestSumFitness() {
		double sumFitness = 0;
		for(Individual individual : best) {
			sumFitness += individual.getFitness();
		}
		return sumFitness;
	}
	private Individual turnRouletteWheel() {
		double target = (new Random()).nextDouble()*calculateSumFitness();
		double cumul = 0;
		int index = 0;
		while( (cumul+= population.get(index).getFitness()) < target ) {
			cumul += population.get(index).getFitness();
			index ++ ;
		}
		return population.get(index);
	}
	private Individual turnBestRouletteWheel() {
		double target = (new Random()).nextDouble()*calculateBestSumFitness();
		double cumul = 0;
		int index = 0;
		while( (cumul+= best.get(index).getFitness()) < target ) {
			index ++ ;
		}
		return best.get(index);
	}
	private void addIndividualsInBest() throws InstantiationException, IllegalAccessException {
		for(int i=0 ; i<sizeRandom  ; i++) {
			Individual individual = null;
			do {
				individual = new Individual(turnRouletteWheel());
			}while(individual.existIn(best));
			best.add(individual);
		}
	}
	private void addMutatedIndividualsInBest() throws InstantiationException, IllegalAccessException, IOException {
		Individual individual = null;
		for(int i=0 ; i<sizeMutant  ; i++) {
			do {
				individual = new Individual(turnBestRouletteWheel().mutate(grammar, environment));
			}while(individual.existIn(best));
			best.add(individual);
		}
	}
	private void addCrossoverIndividualsInBest() throws InstantiationException, IllegalAccessException {
		for(int i=0 ; i<sizeOffspring  ; i++) {
			Individual child = null;
			do {
				Individual dad = turnRouletteWheel();
				Individual mom = turnBestRouletteWheel();
				child = mom.crossover(dad, grammar, environment);
			}while(child.existIn(best));
			best.add(new Individual(child));
		}
	}
	private void addRest() throws IOException, InstantiationException, IllegalAccessException {
		for(Individual individual : best) {
			if(individual.existIn(best)) {
				best.remove(individual);
			}
		}
		for(int i=best.size() ; i<size  ; i++) {
			Individual individual = null;
			do {
				individual = new Individual(grammar, environment);
			}while(individual.existIn(best));
			best.add(individual);
		}
	}
	private void replaceWithBest() {
		population.removeAll(population);
		population.addAll(best);
		best.removeAll(best);
		for(Individual individual:population) {
			if(individual == null || individual.getPhenotype()==null) {
				System.out.println("ERR: there is a wolf in the chelter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}
	}
	public String toString() {
		String text = 	" size = "+size+
						" sizeBest = "+sizeBest+
						" sizeRandom = "+sizeRandom+
						" sizeMutant = "+sizeMutant+
						" sizeOffspring = "+sizeOffspring+
						" sizeRest = "+sizeRest+
						" sizeGeneration = "+sizeGeneration+"\n";
		for(Individual individual : best) {
			text += individual.toString()+"\n";
		}
		Individual bestIndividual = getBestIndividual();
		Matrix bestMatrix = new Matrix(environment.getMatrix(0), bestIndividual, grammar);
		text += "\n"+bestMatrix;
		return text;
	}
	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		World world;
		do{
			System.out.println("===new world===");
			world = new World();
			for(int i=0 ; i<world.sizeGeneration ; i++) {
				//System.out.println("selectBestIndividuals");
				world.selectBestIndividuals();
				//System.out.println("addIndividualsInBest");
				world.addIndividualsInBest();
				//System.out.println("addMutatedIndividualsInBest");
				world.addMutatedIndividualsInBest();
				//System.out.println("addCrossoverIndividualsInBest");
				world.addCrossoverIndividualsInBest();
				//System.out.println("addRest");
				world.addRest();
				//System.out.println("replaceWithBest");
				world.replaceWithBest();
				System.out.println("fit="+(1.0+1.0/world.getBestIndividual().getFitness())+" best="+world.getBestIndividual());
			}
			System.out.println("coucou\n");
			System.out.println(world);
		} while(world.getBestFitness() < 0.3);
		System.out.println("+++BestWorld="+world);
	}
}
