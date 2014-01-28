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
import java.lang.Character;
import java.io.*;
import java.util.HashMap;

import anais.gui.Gui;

public class WorldDarwin extends Thread{
    WorldDarwinData data = new WorldDarwinData(21, 15, 800, 100, 100, 400, 100, 1, false,
			true);
	/** Creates a new instance of World */
    public WorldDarwin() {

    }

    /**
     * test the capacity of an individual in the world
     */
     double TEST(Environnement envir,double [] vector,double eps){
        double errorI=0;
        double error1=0;
        double diff=Double.POSITIVE_INFINITY;
        double [] dvector=new double[data.N];
        double [] d2vector=new double[data.N];
        dvector=envir.Dcntr(vector,1,1);
        d2vector=envir.Dplus(vector,1,1);
        d2vector=envir.Dless(d2vector,1,1);
        int inc=0;
        for(int i=1;i<=data.N-1;i++){
            if(!Double.isInfinite(vector[i])){
                diff=Math.abs(dvector[i]-Math.pow(vector[i],2)/Math.pow(envir.x[i],2));
              //diff=vector[i]+2*d2vector[i]-2;
                inc++;
                error1+=diff;
                if(diff>=errorI) errorI=diff;
            }

        }
        error1+=2*Math.abs(vector[0]);
        error1+=2*Math.abs(vector[data.N-1]-2);
        inc+=4;
	return 1/(1+(error1/inc));
    }

    /** Object Variable, to stock caracteristic & temporary indice*/
    class Variable {
        double valeur;
        int index;
        int oldop;
        public Variable (double val, int ind){
            valeur = val;
            index = ind;
        }
    }
    /** Environment of the World*/
    class Environnement {
        double [] x;
        double h=(double)1/(data.N-1);

        
        public Environnement (){
            x=new double[data.N];
            double xi=0;
            for(int i=0;i<data.N;i++){
                x[i]=xi;
                xi+=h;
            }
        }
        /** 
        * Display the solution of the Environment in the World
        */
        public void Display(BufferedWriter bw) throws IOException{
            
        }
        
        private double [] Dplus(double [] v,double vo,double vn){
            double [] dv=new double [data.N];
            for(int i=0;i<data.N-1;i++){
                dv[i]=(v[i+1]-v[i])/h;
            }
            dv[data.N-1]=(vn-v[data.N-2])/h;
            return dv;
        }
        private double [] Dless(double [] v,double vo,double vn){
            double [] dv=new double [data.N];
            for(int i=1;i<data.N;i++){
                dv[i]=(v[i]-v[i-1])/h;
            }
            dv[0]=(v[1]-vo)/h;
            return dv;
        }
        private double [] Dcntr(double [] v,double vo,double vn){
            double [] dv=new double [data.N];
            for(int i=1;i<data.N-1;i++){
                dv[i]=(v[i+1]-v[i-1])/(2*h);
            }
            dv[0]=(v[1]-vo)/h;
            v[data.N-1]=(vn-v[data.N-1])/h;
            return dv;
        }
    }
    
    /** Codon object
     * Possible values :
     * expressions  : v<var>,o<exp><op><exp>,p<preop><exp>,w<var><op><exp>,r<pre-op><var>
     * operators    : +, -, *, /, ^
     * preoperators : e<exp>, l<log>, s<sin>, t<tan>, q<sqrt>
     * variables    : 0, 1, x, y, z
     * constantes   : a, b, c, d, e
     */
    class Codon{
        int type;
        char value;
        
        Codon(){
            int i=Math.round((float)Math.random()*(float)(data.BNFMap.size()+1));
            value=(Character)data.BNFMap.get(i);
        }
    }
    
    /** Individuals of the World*/
    class Individual{
        int [] genotype;
        int [] phenotype;
        int [] BNF;
        double [] being;
        double fitness;
        int exp;

        public Individual(int size){
            genotype=null;
            phenotype=new int[size];
            for(int i=0;i<size;i++){
                phenotype[i]=-1;
            }
            BNF=new int[data.Ngrammar];
            being=null;
        }

       /**
        * do a individual copy from the dady
        */
        public Individual(Individual dady){
            exp=dady.exp;
            fitness=dady.fitness;
            genotype=null;
            phenotype=new int[dady.phenotype.length];
            for(int i=0;i<dady.phenotype.length;i++){
                phenotype[i]=dady.phenotype[i];
            }
            BNF=new int[data.Ngrammar];
            for(int i=0;i<BNF.length;i++){
                BNF[i]=dady.BNF[i];
            }
            being=new double[data.N];
            for(int i=0;i<data.N;i++){
                being[i]=dady.being[i];
            }
        }

       /** 
        * make an randomised individual
        */
        public Individual (Environnement envir){
            boolean bad=true;
            double eps=0.1;
            fitness=0;
            while(bad){
                bad=MakeGenotype();
            }
            MakePhenotype();
            Evaluate(envir,eps);
            fitness+=TEST(envir,being,eps);
            fitness/=4;
        }

       /** 
        * make the genotype of an individual
        */
        public boolean MakeGenotype (){
            int nb_gene,gene;
            int continu=1;
            int codon;
            int i;

            BNF=new int[data.Ngrammar];
            nb_gene = (int)(30 + 50*Math.random());
            genotype=new int[nb_gene];
            for(i=0;i<nb_gene;i++){
                if((data.GeneWheel[data.Ngrammar-1]!=0)&&(data.Lamark==true)){
                    gene=Search(data.GeneWheel,0,data.GeneWheel.length);
                }
                else {
                    gene=(int)(Math.random()*data.Ngrammar);
                }
                genotype[i]=gene;
                BNF[gene]++;
            }
            return(SumBtwin(BNF,0,3)<1);
        }

       /** 
        * make the phenotype of an individual
        */
        public void MakePhenotype() {
            boolean continu=true;
            int gene,genetmp;
            int i;
            i=4*(8*BNF[0]+4*BNF[1]+6*BNF[2]);
            phenotype=new int[i];
            for(i=0;i<phenotype.length;i++) {
                phenotype[i]=-1;
            }
            gene=IndexMax(BNF, 0, 3);
            exp=1;i=0;BNF[gene]--;
            phenotype[i]=gene;
            if(gene== 0) exp++;
            while(continu){
                i++;
                if     (gene==0) {gene=IndexMax(BNF,4,7);exp++;}
                else if(gene==1) {gene=IndexMax(BNF,8,11);}
                else if(gene==2) {gene=IndexMax(BNF,4,7);exp++;}
                else if(gene==3) {gene=IndexMax(BNF,12,14);}
                else if(gene>=12){gene=IndexMax(BNF,0,3);exp--;}
                else if((gene>3)&&(gene<12)) {gene=IndexMax(BNF,0,3);}

                if(BNF[gene]>0) {phenotype[i]=gene;BNF[gene]--;}
                else continu=false;
                if(exp==0) continu=false;
            }
            if(phenotype[i-1]<4) {phenotype[i-1]=-1;i--;}

            while((exp>0)&&(i+2<phenotype.length)&&data.SyntaxAuto){
                phenotype[i]=3;
                phenotype[i+1]=(int)(Math.random()*3+12);
                i=i+2;
                exp--;
            }
        }

       /** 
        * Display the phenotype an the results in the World => individual
        */
        public BufferedWriter Display(BufferedWriter bw)  throws IOException{
            Variable X=new Variable(0,0);
            double eps=0.1;
            X.valeur=0.5;
            X.index=0;
            int op=0;
            if(phenotype.length>1){
                WriteFct(phenotype,op,X,eps,bw);
                bw.newLine();bw.write("graph = (fitness)"+fitness);bw.newLine();
                bw.write("____________________");
                bw.newLine();
            }
            return bw;
        }

       /**
        * Evaluation of an individual in the world
        */
        public void Evaluate(Environnement envir,double eps){
            being=new double[data.N];
            Variable X=new Variable(0,0);
            for(int i=0;i<data.N;i++){
                X.valeur=envir.x[i];
                X.index=0;
                if(phenotype[1]>=0) being[i]=Eval(phenotype,0,X,eps);
                if((Double.isNaN(being[i]))||(Double.isInfinite(being[i]))){
                    being[i]=Double.POSITIVE_INFINITY;
                }
            }
        }
    }
    
    
   /**
    * Do the caracteristic of an individual by his phenotype
    */
    double Eval(int t[], int op, Variable var,double eps) {
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
    double WriteFct(int t[], int op, Variable var,double eps,BufferedWriter bw) throws IOException{
        if(op>=t.length) return 0;
        if(t[op]==-1) return 0;
        if(var.index+2<t.length) var.index=var.index+2;

        if(t[op]==3) {
            if(t[op+1]==14) {bw.write("1");return 1;}
            if(t[op+1]==13) {bw.write("e");return eps;}
            if(t[op+1]==12) {bw.write("x");return var.valeur;}
        }
        if(t[op]==1) {
            if(t[op+1]==11) {bw.write("log("); Math.log(WriteFct(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]==10) {bw.write("exp("); Math.exp(WriteFct(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]== 9) {bw.write("sin("); Math.sin(WriteFct(t, op+2,var,eps,bw));bw.write(")");return 0;}
            if(t[op+1]== 8) {bw.write("cos("); Math.cos(WriteFct(t, op+2,var,eps,bw));bw.write(")");return 0;}
        }
        if((t[op]==0)||(t[op]==2)) {
	    bw.write("(");
	    double first=WriteFct(t, op+2,var,eps,bw);
            if(t[op+1]== 7) {bw.write(")/(");WriteFct(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 6) {bw.write(")*(");WriteFct(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 5) {bw.write(")-(");WriteFct(t,var.index,var,eps,bw);bw.write(")");return 0;}
            if(t[op+1]== 4) {bw.write(")+(");WriteFct(t,var.index,var,eps,bw);bw.write(")");return 0;}
        }

        return 0;
    }
    /**
    * Write the phenotype in fonctional representation
    */
    void DispFct(int t[]) {
        int op=0;
        while(op<t.length){
            switch(t[op]){
                case  4:System.out.println("+");  break;
                case  5:System.out.println("-");  break;
                case  6:System.out.println("*");  break;
                case  7:System.out.println("/");  break;
                case  8:System.out.println("cos");break;
                case  9:System.out.println("sin");break;
                case 10:System.out.println("exp");break;
                case 11:System.out.println("log");break;
                case 12:System.out.println("x");  break;
                case 13:System.out.println("e");  break;
                case 14:System.out.println("1");  break;
                case  3:System.out.println("V");  break;
                //case -1:bw.write("o");break;
                default:System.out.println(" ");
            }
            op++;
        }
    }

   /**
    * Search the indice of the maximum in an array t between a and b
    */
    int IndexMax (int t[], int a, int b) {
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
    int SumBtwin (int t[], int a, int b) {
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
            if(preop) gene=Search(data.GeneWheel,8,11);
            else      gene=Search(data.GeneWheel,4,7);
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
    void PrintFile(Environnement envir,Individual [] population,Individual [] best, String name) throws IOException {
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

	    for(int i=0;i<data.Nbestpop;i++){
                bw.write("wheel["+i+"] = "+data.BestWheel[i]);bw.newLine();
            }
            for(int i=0;i<data.Npop;i++){
                bw.write("population["+i+"] = ");population[i].Display(bw);
            }
            for(int i=0;i<data.Nbestpop;i++){
                bw.write("best["+i+"] = ");best[i].Display(bw);
            }
            WriteFct(best[0].phenotype,op,X,eps,bw);
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
    int Search(double t[], int a, int b) {
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
     void Initial(Individual [] population, Environnement envir){
         if(data.Lamark==true){
             int sum=0;
             for(int i=0;i<data.Ngrammar;i++){
                 if(i<11) {data.GeneFitness[i]=50;sum+=50;}
                 else {data.GeneFitness[i]=100;sum+=100;}
             }
             data.GeneWheel[0]=(double)data.GeneFitness[0]/sum;
             for(int i=1;i<data.Ngrammar;i++){
                 data.GeneWheel[i]=(double)data.GeneFitness[i]/sum+data.GeneWheel[i-1];
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
            for(int i=0;i<data.Npop;i++){
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
            if(j==data.Nbestpop) finish=true;
            population[iOfTheMax].fitness=0;
        }
        /*CAUTION*/ data.Nbestpop=j; /*IMPORTANT*/
        return sum;
    }

   /**
    * calcul the roulette wheel from the fitness of individuals
    */    
    void Statistic(Individual [] best, double sum){
        double cumul=0;
        int cmp=0;
        for(int i=0;i<data.Nbestpop;i++) data.BestWheel[i]=0;
        for(int i=0;i<data.Nbestpop;i++){
            cumul+=best[i].fitness;
            data.BestWheel[i]=cumul/sum;
        }
        if(data.Lamark==true){
            /** GeneFitness fundation*/
            for(int i=0;i<data.Nbestpop;i++){
                int j=0;
                while(j<best[i].phenotype.length){
                    if(best[i].phenotype[j]>=0){
                        data.GeneFitness[best[i].phenotype[j]]++;
                    }
                    j++;
                }
            }
            /** GeneWheel fundation*/
            data.GeneWheel[0]=(double)(data.GeneFitness[0])/SumBtwin(data.GeneFitness,0,data.GeneFitness.length);
            for(int j=1;j<data.GeneFitness.length;j++){
                data.GeneWheel[j]=(double)(data.GeneFitness[j])/SumBtwin(data.GeneFitness,0,data.GeneFitness.length)+data.GeneWheel[j-1];
            }
        }
    }
    
   /**
    * copy the best Individual of pop[n-1] in new pop[n]
    */    
    int Copy(Individual [] best, Individual [] population){
        for(int i=0;i<data.Nbestpop;i++){
            population[i]=new Individual(best[i]);
        }
        return data.Nbestpop;
    }

    /**
    * Do a crossover of the phenotypes from 2 Individuals
    */
     void Crossover(Individual dad, Individual mom, Individual c1, Individual c2, Environnement envir,int dle,int mle) {
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
    int PutCrossover(Individual [] best,Individual [] population,int a,int b,Environnement envir){
        int i=a,j;
        int k,l,dl,ml;
        boolean ok=true;
        while(i<a+b){
            /* Search best Individual in avoiding
            blood relations crossover*/
            k=Search(data.BestWheel, 0, data.Nbestpop);
            if(data.Nbestpop<2) {
                System.out.println("Nbest<2!");
                return 0;
            }
            do {
                l=(int)(Math.random()*data.Nbestpop);
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
    void Mutation(Individual dad,Environnement envir,int dl){
        int i;
        int codon1=0,codon2;
	/* Choose a point of nutation*/
        if((dad.phenotype[0]==3)||(dl<3)) i=1;
        else
		if(data.Lamark) i=ChoixGene(dad.phenotype);
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
    int PutMutations(Individual [] best,Individual [] population,int a,int b, Environnement envir){
        int i=a,r=a+b;
        int k=0;
        while(i<a+b){
	    int j=0;
            while(j<3){
	    	k=Search(data.BestWheel,0,data.Nbestpop);
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
    void Finish(Individual [] population,int a, Environnement envir){
        for(int i=a; i<population.length;i++){
            population[i]=new Individual(envir);
        }
    }
    
    void Performance(Individual [] pop, Environnement envir){
        double eps=0.1;
        for(int i=0;i<pop.length;i++){
            pop[i].Evaluate(envir,eps);
            pop[i].fitness=TEST(envir,pop[i].being,eps);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public void run(Gui ag) {
        double Baverage=0;
        double average=0;
        double sumbest=0;
        double bestfitness;
        //BNFMap=new Vector(ag.BNFMap);
        ag.FillFitnessTextField("!!!START!!!");
        try{
            FileWriter fw = new FileWriter("convexp1.dat");
            BufferedWriter bw= new BufferedWriter(fw);
            boolean fini=false;
              Environnement envir=new Environnement();
              Individual [] population=new Individual[data.Npop];
              Individual [] best=new Individual[data.Nbestpop];
              bestfitness=0;
              int monde=0;
              while(!fini){
                monde++;
                Initial(population, envir);
                sumbest=Selection(population,best);
                Statistic(best,sumbest);
                //PrintFile(envir,population,best,"c:\\temp\\avant2.txt");
                int k=0;
                while ((k<data.Generation)&&(!fini)){
                    bw.write(best[0].fitness+"	"+sumbest/data.Nbestpop);bw.newLine();
                    ag.FillFitnessTextField(best[0].fitness+"    "+sumbest/data.Nbestpop);
                    System.out.println(best[0].fitness+"    "+sumbest/data.Nbestpop);
                    int i=0;//System.out.println("1");
                    i=Copy(best,population);//System.out.println("2");
                    i=PutCrossover(best,population,i,data.Ncros,envir);//System.out.println("3");
                    i=PutMutations(best,population,i,data.Nmuta,envir);//System.out.println("4");
                    Finish(population,i,envir);//System.out.println("5");
                    Performance(population,envir);//System.out.println("6");
                    sumbest=Selection(population,best);//System.out.println("7");
                    Statistic(best,sumbest);
                    k++;
                    if(monde==2) fini=true;
                }
                bestfitness=best[0].fitness;
                bw.newLine();
                System.out.println("monde numero"+monde);
                ag.FillFitnessTextField("monde numero"+monde);
        //        if(bestfitness>=0.9) PrintFile(envir,population,best,"ODEprexp1.dat");
        //        if(bestfitness==1) System.out.println("EXACT SOLUTION");
                Performance(population,envir);
                envir=null;envir=new Environnement();
                population=null;population =new Individual[data.Npop];
                best=null;best=new Individual[data.Nbestpop];
              }
              //PrintFile(envir,population,best,"c:\\temp\\apres3.txt");
              bw.close();
        }catch(Exception e){System.out.println(e);}
    }
 }
