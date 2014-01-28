package anais.historical;

import java.io.BufferedWriter;
import java.io.IOException;

/** Environment of the World*/
public class Environment {
    double [] x;
    double h=(double)1/(WorldDarwinPrime.N-1);

    
    public Environment (){
        x=new double[WorldDarwinPrime.N];
        double xi=0;
        for(int i=0;i<WorldDarwinPrime.N;i++){
            x[i]=xi;
            xi+=h;
        }
    }
    /** 
    * Display the solution of the Environment in the WorldDarwinPrime
    */
    public void Display(BufferedWriter bw) throws IOException{
        
    }
    
    private double [] Dplus(double [] v,double vo,double vn){
        double [] dv=new double [WorldDarwinPrime.N];
        for(int i=0;i<WorldDarwinPrime.N-1;i++){
            dv[i]=(v[i+1]-v[i])/h;
        }
        dv[WorldDarwinPrime.N-1]=(vn-v[WorldDarwinPrime.N-2])/h;
        return dv;
    }
    private double [] Dless(double [] v,double vo,double vn){
        double [] dv=new double [WorldDarwinPrime.N];
        for(int i=1;i<WorldDarwinPrime.N;i++){
            dv[i]=(v[i]-v[i-1])/h;
        }
        dv[0]=(v[1]-vo)/h;
        return dv;
    }
    private double [] Dcntr(double [] v,double vo,double vn){
        double [] dv=new double [WorldDarwinPrime.N];
        for(int i=1;i<WorldDarwinPrime.N-1;i++){
            dv[i]=(v[i+1]-v[i-1])/(2*h);
        }
        dv[0]=(v[1]-vo)/h;
        v[WorldDarwinPrime.N-1]=(vn-v[WorldDarwinPrime.N-1])/h;
        return dv;
    }
    /**
     * test the capacity of an individual in the WorldDarwinPrime
     */
     static double TEST(Environment envir,double [] vector,double eps){
        double errorI=0;
        double error1=0;
        double diff=Double.POSITIVE_INFINITY;
        double [] dvector=new double[WorldDarwinPrime.N];
        double [] d2vector=new double[WorldDarwinPrime.N];
        dvector=envir.Dcntr(vector,1,1);
        d2vector=envir.Dplus(vector,1,1);
        d2vector=envir.Dless(d2vector,1,1);
        int inc=0;
        for(int i=1;i<=WorldDarwinPrime.N-1;i++){
            if(!Double.isInfinite(vector[i])){
                diff=Math.abs(dvector[i]-Math.pow(vector[i],2)/Math.pow(envir.x[i],2));
              //diff=vector[i]+2*d2vector[i]-2;
                inc++;
                error1+=diff;
                if(diff>=errorI) errorI=diff;
            }

        }
        error1+=2*Math.abs(vector[0]);
        error1+=2*Math.abs(vector[WorldDarwinPrime.N-1]-2);
        inc+=4;
	return 1/(1+(error1/inc));
    }
}