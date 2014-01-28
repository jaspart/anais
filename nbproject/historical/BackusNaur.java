package anais.historical;

import java.util.Random;

import anais.gene.Gene;

public class BackusNaur {
	public Gene getRandomGene(int depth) throws InstantiationException, IllegalAccessException{
		Random generator = new Random();
		Gene randomValue = this.get(generator.nextInt(this.size()));;
		if (depth > 5) {
			while( ! randomValue.getClass().getSuperclass().getSimpleName().startsWith("GeneValue"));{
				randomValue = this.get(generator.nextInt(this.size()));
			}
		}
		if(! randomValue.getClass().getSuperclass().getSimpleName().startsWith("GeneValue")){
			randomValue = randomValue.getClass().newInstance();
		}
		randomValue.setDepth(depth+1);
		//randomValue.getChildren(this);
		return randomValue;
	}

	private Gene get(int nextInt) {
		// TODO Auto-generated method stub
		return null;
	}

	private int size() {
		return 0;
	}

	public void add(Gene gene) {
		// TODO Auto-generated method stub
		
	}

	public void remove(Gene gene) {
		// TODO Auto-generated method stub
		
	}
}

