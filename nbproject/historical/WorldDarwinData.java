package anais.historical;

import java.util.Vector;

public class WorldDarwinData {
	public int N;
	public int Ngrammar;
	public int Npop;
	public int Nbestpop;
	public int Nbesttmp;
	public int Ncros;
	public int Nmuta;
	public int Generation;
	public Vector BNFMap;
	public int[] GeneFitness = new int[Ngrammar];
	public double[] BestWheel = new double[Nbestpop];
	public double[] GeneWheel = new double[Ngrammar];
	public boolean Lamark;
	public boolean SyntaxAuto;

	public WorldDarwinData(int n, int ngrammar, int npop, int nbestpop,
			int nbesttmp, int ncros, int nmuta, int generation, boolean lamark,
			boolean syntaxAuto) {
		N = n;
		Ngrammar = ngrammar;
		Npop = npop;
		Nbestpop = nbestpop;
		Nbesttmp = nbesttmp;
		Ncros = ncros;
		Nmuta = nmuta;
		Generation = generation;
		Lamark = lamark;
		SyntaxAuto = syntaxAuto;
	}
}