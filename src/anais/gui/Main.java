/*
 * Main.java
 *
 * Created on 18 octobre 2005, 21:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 *
 * Look and feel par defaut:UIManager.getSystemLookAndFeel()
 * Look and Feel de la plateforme sous-jacente:UIManager.getCrossPlatformLookAndFeelClassName()
 * Look and feel Windows:com.sun.java.swing.plaf.windows.WindowsLookAndFeel
 * Look and feel CDE/Motif:com.sun.java.swing.plaf.motif.MotifLookAndFeel
 * Look and feel GTK+:com.sun.java.swing.plaf.gtk.GTKLookAndFeel
 * Look and feel metal:javax.swing.plaf.metal.MetalLookAndFeel
 * Look and feel Macintosh:it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel
 * look and feel Kunststoff:com.incors.plaf.kunststoff.KunststoffLookAndFeel
 */
package anais.gui;

/*import java.io.File;
import org.apache.log4j.Logger;
import javax.swing.UIManager;*/

import java.io.IOException;

import anais.gene.GeneVariable;
import anais.world.Environment;
import anais.world.Grammar;
import anais.world.Individual;

public class Main extends Thread{
	/** Creates a new instance of Main */
	public Main() {
		/*File log4jFile = new File("log4j.xml");
		Logger logger;
		*/
	}
	/**
	 * @param args the command line arguments
	 * @return 
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CloneNotSupportedException 
	 */

	public void execute() throws IOException {
		try {

		/*	try {
			UIManager.setLookAndFeel("UIManager.getSystemLookAndFeel()");
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					//new Gui().setVisible(true);
				}
			});
		}catch (Exception e) {e.printStackTrace();}
		 */
		//new Gui().setVisible(true);s
		//for(int i=0;i<10000;i++) {
			//System.out.println("**************** i="+i);
			Environment environment = new Environment();
			System.out.println("grammar =");
			Grammar grammar = new Grammar(true, environment.getMatrix(0));
	    	for (GeneVariable variable : grammar.getVariables()) {
	    		System.out.println(variable.getSymbol()+" = "+variable.get(3));
	    	}
	
			Individual adam = new Individual(grammar, environment);
			System.out.println("adam        ="+adam);
			Individual clone = new Individual(adam);
			System.out.println("clone       ="+clone);
			clone.mutate(grammar);
			System.out.println("clone mutant="+clone);
			Individual eve =  new Individual(grammar, environment);
			System.out.println("eve         ="+eve);
			eve.crossover(adam, grammar);
			System.out.println("son         ="+eve);
		} catch (InstantiationException e) {
			System.out.println("InstantiationException:"+e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException:"+e);
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, CloneNotSupportedException, IOException {
		(new Main()).execute();
	}
	public void run(Gui gui) throws IOException {
		(new Main()).execute();
	}
}