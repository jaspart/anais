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

import java.lang.Double;
import java.io.*;

import anais.environment.Variable;
import anais.gui.Gui;
import anais.historical.Environment;

public class WorldDarwinPrime extends Thread{
    static int N	=	21;
    static int Ngrammar=	15;
    int Npop	=	800;
    int Nbestpop=	100;
    int Nbesttmp=	100;
    int Ncros	=	400;
    int Nmuta	=	100;
    int Generation=	30;
    int [] GeneFitness=new int[Ngrammar];
    double [] BestWheel=new double[Nbestpop];
    static double [] GeneWheel=new double[Ngrammar];
    static boolean Lamark=	false;
    static boolean SyntaxAuto=	true;
    /** Creates a new instance of World */
    public WorldDarwinPrime() {

    }

   /**
    * Do the caracteristic of an individual by his phenotype
    */
    static double Eval(int t[], int op, Variable var,double eps) {
        if(op>=t.length) return 0;
        if(t[op]==-1) return 0;
        if(var.index+2<t.length) var.index=var.index+2;

        if(t[op]==3) {
            if(t[op+1]==14) return 1;
            if(t[op+1]==13) return eps;
            if(t[op+1]==12) return var.valeur;
        }
        if(t[op]==1) {
            if(t[op+1]==11) return Math.log(Eval(t, op+2,var,eps));
            if(t[op+1]==10) return Math.exp(Eval(t, op+2,var,eps));
            if(t[op+1]== 9) return Math.sin(Eval(t, op+2,var,eps));
            if(t[op+1]== 8) return Math.cos(Eval(t, op+2,var,eps));
        }
        if((t[op]==0)||(t[op]==2)) {
            if(t[op+1]== 7) return Eval(t, op+2,var,eps)/Eval(t, var.index,var,eps);
            if(t[op+1]== 6) return Eval(t, op+2,var,eps)*Eval(t, var.index,var,eps);
            if(t[op+1]== 5) return Eval(t, op+2,var,eps)-Eval(t, var.index,var,eps);
            if(t[op+1]== 4) return Eval(t, op+2,var,eps)+Eval(t, var.index,var,eps);
        }
        return 0;
    }

   /**
    * Do the caracteristic of an individual by his phenotype
    */
    static double getBeing(int t[], int op, Variable var,double eps,BufferedWriter bw) throws IOException{
        if(op>=t.length) return 0;
        if(t[op]==-1) return 0;
        if(var.index+2<t.length) var.index=var.index+2;

        if(t[op]==3) {
            if(t[op+1]==14) {bw.write("1");return 1;}
            if(t[op+1]==13) {bw.write("e");return eps;}
            if(t[op+1]==12) {bw.write("x");return var.valeur;}
        }
        if(t[op]==1) {
            if(t[op+1]==11) {bw.write("log("); Math.log(getBeing(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]==10) {bw.write("exp("); Math.exp(getBeing(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]== 9) {bw.write("sin("); Math.sin(getBeing(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]== 8) {bw.write("cos("); Math.cos(getBeing(t, op+2,var,eps,bw));bw.write(")");return 0;}
        }
        if((t[op]==0)||(t[op]==2)) {
		    bw.write("(");
		    if(t[op+1]== 7) {bw.write(")/(");getBeing(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 6) {bw.write(")*(");getBeing(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 5) {bw.write(")-(");getBeing(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 4) {bw.write(")+(");getBeing(t,var.index,var,eps,bw);bw.write(")");return 0;}
        }
        return 0;
    }

   /**
    * Search the indice of the maximum in an array t between a and b
    */
    static int IndexMax (int t[], int a, int b) {
    	int aux = t[a];
        int maxi=a;
        for (int i=a ; i <= b ; i++) {
            if (t[i] > aux) {
                aux = t[i];
                maxi = i;
            }
        }
        return maxi;
    }

    /**
    * Do the sum of a piece of array t between a and b
    */
    static int SumBtwin (int t[], int a, int b) {
		int sum=0;
		for (int i=a ; i < b ; i++) {
            sum=sum+t[i];
        }
		return sum ;
    }


    /**
    *
    */
    int ChoixGene(int t[]){
		boolean endsg=false;
		int codon=0;
		int gene=0;
		while(!endsg){
	        boolean preop=(((int)Math.random()*2)==1);
	        if(preop) gene=Search(GeneWheel,8,11);
	        else      gene=Search(GeneWheel,4,7);
	        System.out.println(gene);
	        int j=0;
	        while((t[j]!=-1)&&(gene>3)&&(!endsg)){
	            if(t[j]==gene){
	                codon=j;
	                endsg=true;
	            }
	            j++;
	        }
		}
		System.out.println("gene "+t[codon]+" agene"+codon);
		if(gene<3) return -1;
		else return codon;
    }

   /**
    * print the solution in a file with the name given in parameter
    */    
    void PrintFile(Environment envir,Individual [] population,Individual [] best, String name) throws IOException {
        try{
            FileWriter fw = new FileWriter(name);
            BufferedWriter bw= new BufferedWriter(fw);
            bw.write("solu: ");
            envir.Display(bw);
		    Variable X=new Variable(0,0);
		    double eps=0.1;
		    X.valeur=0.5;
	        X.index=0;
		    int op=0;
	
		    for(int i=0;i<Nbestpop;i++){
	            bw.write("wheel["+i+"] = "+BestWheel[i]);bw.newLine();
	        }
	        for(int i=0;i<Npop;i++){
	            bw.write("population["+i+"] = ");population[i].Display(bw);
	        }
	        for(int i=0;i<Nbestpop;i++){
	            bw.write("best["+i+"] = ");best[i].Display(bw);
	        }
	        getBeing(best[0].phenotype,op,X,eps,bw);
		    bw.newLine();
		    int j=0;
		    while(j<best[0].phenotype.length){
		    	if(best[0].phenotype[j]>=0){
			    	bw.write(java.lang.Integer.toString(best[0].phenotype[j]));
		    	}
		    	j++;
		    }
		    for(int i=0;i<=100;i++){
		    	double s=0;
		    	X.valeur=(double)i/100;
		    	X.index=0;
	        	if(best[0].phenotype[1]>=0) s=Eval(best[0].phenotype,0,X,eps);
	        	if((Double.isNaN(s))||(Double.isInfinite(s))){
	            	s=Double.POSITIVE_INFINITY;
	        	}
	        	bw.write(java.lang.Double.toString(s));bw.newLine();
		    }
	        bw.close();
        }catch (Exception e) {System.out.println("erreur : "+e);}
    }
    
   /**
    * select a randomized point in t from a to b
    */    
    static int Search(double t[], int a, int b) {
        double var=Math.random();
        int hub=0;b--;
        boolean noFind=true;
        while(noFind){
            if(var>t[hub]) hub++;
            else noFind=false;
        }
        return hub;
    }
    
   /**
    * Initialise the population 0
    */
     void Initial(Individual [] population, anais.historical.Environment envir){
         if(Lamark==true){
             int sum=0;
             for(int i=0;i<Ngrammar;i++){
                 if(i<11) {GeneFitness[i]=50;sum+=50;}
                 else {GeneFitness[i]=100;sum+=100;}
             }
             GeneWheel[0]=(double)GeneFitness[0]/sum;
             for(int i=1;i<Ngrammar;i++){
                 GeneWheel[i]=(double)GeneFitness[i]/sum+GeneWheel[i-1];
             }
         }
         for(int i=0;i<population.length;i++){
             population[i]=new Individual(envir);
         }
     }
     
     boolean AlreadySeen(Individual [] pop, int j, int p){
         boolean seen=false;
         for(int k=0;k<j;k++){
             double diff=0;
             for(int l=0;l<pop[p].being.length;l++){
                 diff+=(pop[k].being[l]-pop[p].being[l]);
             }
             seen=((diff==0)||(seen==true));
         }
         return seen;
     }
     
   /**
    * select the best individual from a individual population : 
    * Greedy Selection of J.R.Koza for big population
    */    
     double Selection(Individual [] population, Individual [] best) {
        double sum=0;
        double TheMax;
        int iOfTheMax;
        int j=0;
        boolean finish=false;

        while(!finish){
            TheMax=0;
            iOfTheMax=0;
            for(int i=0;i<Npop;i++){
                if(population[i].fitness>TheMax){
                    TheMax=population[i].fitness;
                    iOfTheMax=i;
                }
            }
            if(TheMax==0) finish=true;
            if(!AlreadySeen(population,j,iOfTheMax)){
                best[j]=new Individual(population[iOfTheMax]);
                sum+=best[j].fitness;
                j++;
            }
            if(j==Nbestpop) finish=true;
            population[iOfTheMax].fitness=0;
        }
        /*CAUTION*/ Nbestpop=j; /*IMPORTANT*/
        return sum;
    }

   /**
    * calcul the roulette wheel from the fitness of individuals
    */    
    void Statistic(Individual [] best, double sum){
        double cumul=0;
        for(int i=0;i<Nbestpop;i++) BestWheel[i]=0;
        for(int i=0;i<Nbestpop;i++){
            cumul+=best[i].fitness;
            BestWheel[i]=cumul/sum;
        }
        if(Lamark==true){
            /** GeneFitness foundation*/
            for(int i=0;i<Nbestpop;i++){
                int j=0;
                while(j<best[i].phenotype.length){
                    if(best[i].phenotype[j]>=0){
                        GeneFitness[best[i].phenotype[j]]++;
                    }
                    j++;
                }
            }
            /** GeneWheel fundation*/
            GeneWheel[0]=(double)(GeneFitness[0])/SumBtwin(GeneFitness,0,GeneFitness.length);
            for(int j=1;j<GeneFitness.length;j++){
                GeneWheel[j]=(double)(GeneFitness[j])/SumBtwin(GeneFitness,0,GeneFitness.length)+GeneWheel[j-1];
            }
        }
    }
    
   /**
    * copy the best Individual of pop[n-1] in new pop[n]
    */    
    int Copy(Individual [] best, Individual [] population){
        for(int i=0;i<Nbestpop;i++){
            population[i]=new Individual(best[i]);
        }
        return Nbestpop;
    }

    /**
    * Do a crossover of the phenotypes from 2 Individuals
    */
     void Crossover(Individual dad, Individual mom, Individual c1, Individual c2, Environment envir,int dle,int mle) {
         int sd,sm;
         int p1,p2,pm,pd,gd,gm;
                  
         /* Find 2 points of crossover 'stopdad' and 'stopmom'*/
         if(dad.phenotype[0]==3) sd=0;
         else do{ sd=(int)(Math.random()*dle);
         } while(dad.phenotype[sd]>=3);
         if(mom.phenotype[0]==3) sm=0;
         else do{ sm=(int)(Math.random()*mle);
         } while(mom.phenotype[sm]>=3);
         
         /* Copy before Pcross*/
         p1=0;pd=0;
         while(pd<sd) {c1.phenotype[p1]=dad.phenotype[pd];p1++;pd++;}
         p2=0;pm=0;
         while(pm<sm) {c2.phenotype[p2]=mom.phenotype[pm];p2++;pm++;}
         
         /* Insert expression*/
         gd=1;
         while((gd!=0)&&(pd<dle)) {
             c2.phenotype[p2]=dad.phenotype[pd];
             if((c2.phenotype[p2]>=4)&&(c2.phenotype[p2]<=7)) gd++;
             if(c2.phenotype[p2]>=12) gd--;
             p2++;pd++;
         }
         gm=1;
         while((gm!=0)&&(pm<mle)) {
             c1.phenotype[p1]=mom.phenotype[pm];
             if((c1.phenotype[p1]>=4)&&(c1.phenotype[p1]<=7)) gm++;
             if(c1.phenotype[p1]>=12) gm--;
             p1++;pm++;
         }
         
         /* Finish expression*/
         while(pd<dle) {c1.phenotype[p1]=dad.phenotype[pd];p1++;pd++;}
         while(pm<mle) {c2.phenotype[p2]=mom.phenotype[pm];p2++;pm++;}
     }
    
    /**
    * Put the crossover of the phenotypes from 2 Individuals
    */
    int PutCrossover(Individual [] best,Individual [] population,int a,int b,Environment envir){
        int i=a,j;
        int k,l,dl,ml;
        while(i<a+b){
            /* Search best Individual in avoiding
            blood relations crossover*/
            k=Search(BestWheel, 0, Nbestpop);
            if(Nbestpop<2) {
                System.out.println("Nbest<2!");
                return 0;
            }
            do {
                l=(int)(Math.random()*Nbestpop);
            }while(k==l);
            
            /* Initialize the offspring*/
            j=0;dl=0;
            while(j<best[k].phenotype.length){
                if(best[k].phenotype[dl]!=-1) dl++;
                j++;
            }
            j=0;ml=0;
            while(j<best[l].phenotype.length){
                if(best[l].phenotype[ml]!=-1) ml++;
                j++;
            }
            population[i]=null;
            population[i+1]=null;
            population[i]=new Individual(dl+ml);
            population[i+1]=new Individual(dl+ml);
 
            Crossover(best[k],best[l],population[i],population[i+1],envir,dl,ml);
            i=i+2;
        }
        return a+b;
    }
    
    /**
    * Do the mutation of the phenotype from 1 Individuals
    */
    void Mutation(Individual dad,Environment envir,int dl){
        int i;
        int codon1=0,codon2;
	/* Choose a point of nutation*/
        if((dad.phenotype[0]==3)||(dl<3)) i=1;
        else
		if(Lamark) i=ChoixGene(dad.phenotype);
		else do{i=(int)(Math.random()*dl);
        	} while(dad.phenotype[i]<=3);
	if(i<0) codon1=0;
	else codon1=dad.phenotype[i];
        codon2=codon1;
        /* Search a new randomized codon different from old codon*/
        if(codon1>4) while(codon1==codon2){
            if((codon1>=4)&&(codon1<=7)) codon2=(int)(Math.random()*4+4);//operator
            if((codon1>=8)&&(codon1<=11)) codon2=(int)(Math.random()*4+8);//preoperator
            if(codon1>=12) codon2=(int)(Math.random()*3+12);//variable
        }
        /* Integration of the codon in the phenotype*/
        dad.phenotype[i]=codon2;
    }

    /**
    * Put the mutation of the phenotype from 1 Individuals
    */
    int PutMutations(Individual [] best,Individual [] population,int a,int b, Environment envir){
        int i=a;
        int k=0;
        while(i<a+b){
	    int j=0;
            while(j<3){
	    	k=Search(BestWheel,0,Nbestpop);
            	while(best[k].phenotype[j]!=-1) j++;
	    	}
	    population[i]=null;
            population[i]=new Individual(best[k]);
            Mutation(population[i],envir,j);
            i++;
        }
        return a+b;
    }
    
    /**
    * Finish the population with new randomized individuals
    */
    void Finish(Individual [] population,int a, Environment envir){
        for(int i=a; i<population.length;i++){
            population[i]=new Individual(envir);
        }
    }
    
    void Performance(Individual [] pop, Environment envir){
        double eps=0.1;
        for(int i=0;i<pop.length;i++){
            pop[i].Evaluate(envir,eps);
            pop[i].fitness=Environment.TEST(envir,pop[i].being,eps);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public void run(Gui ag) {
        double sumbest=0;
        ag.FillFitnessTextField("!!!START!!!");
        try{
            FileWriter fw = new FileWriter("convexp1.dat");
            BufferedWriter bw= new BufferedWriter(fw);
            boolean fini=false;
              Environment envir=new Environment();
              Individual [] population=new Individual[Npop];
              Individual [] best=new Individual[Nbestpop];
              int monde=0;
              while(!fini){
                monde++;
                System.out.println("monde numero"+monde);
                ag.FillFitnessTextField("monde numero"+monde);
                Initial(population, envir);
                sumbest=Selection(population,best);
                Statistic(best,sumbest);
                //PrintFile(envir,population,best,"c:\\temp\\avant2.txt");
                int k=0;
                while ((k<Generation)&&(!fini)){
                    bw.write(best[0].fitness+"	"+sumbest/Nbestpop);bw.newLine();
                    ag.FillFitnessTextField(best[0].fitness+"\t"+sumbest/Nbestpop);
                    System.out.println(best[0].fitness+"\t"+sumbest/Nbestpop);
                    int i=0;
                    i=Copy(best,population);
                    i=PutCrossover(best,population,i,Ncros,envir);
                    i=PutMutations(best,population,i,Nmuta,envir);
                    Finish(population,i,envir);
                    Performance(population,envir);
                    sumbest=Selection(population,best);
                    Statistic(best,sumbest);
                    k++;
                }
                bw.newLine();
        //        if(bestfitness>=0.9) PrintFile(envir,population,best,"ODEprexp1.dat");
        //        if(bestfitness==1) System.out.println("EXACT SOLUTION");
                Performance(population,envir);
                envir=null;envir=new Environment();
                population=null;population =new Individual[Npop];
                best=null;best=new Individual[Nbestpop];
                if(monde==3) fini=true;
              }
              //PrintFile(envir,population,best,"c:\\temp\\apres3.txt");
              bw.close();
        }catch(Exception e){System.out.println(e);}
    }

	public void run(Gui gui) {
		// TODO Auto-generated method stub
		
	}
 }
