/**
 * WorldDarwinPrime.java
 *
 * Created on 07 July 2003, 16:40
 */

/**
 *
 * @author  arnaud.jaspart
 */
package anais.historical;

import anais.environment.Environment;
import anais.gene.*;

public class World extends Thread{
	BackusNaur grammar;
	Environment environment;
	Individual [] individuals;
	Roulette wheel;
	public World() {
		grammar = new BackusNaur(false);
		grammar.add(new GeneAddition());
		grammar.add(new GeneSubstraction());
		grammar.add(new GeneMultiplication());
		grammar.add(new GeneDivision());
		grammar.add(new GeneConstant('1',1.0));
		grammar.add(new GeneConstant('e',1.0/3000));
		grammar.add(new GeneConstant('N',55.0));
		
		wheel = new Roulette(grammar);
		
		for(Individual individual : individuals){
			individual = new Individual(grammar, environment, wheel);
		}
    }
}
