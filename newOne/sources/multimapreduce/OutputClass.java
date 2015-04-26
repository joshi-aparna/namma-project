package multimapreduce;

public class OutputClass {
	double cost,budget,exetime;
	int outputsize;
	double submissiontime;

	public OutputClass(double c,double b,double t, double subtime,int outsize ){
		 cost=c; budget=b;exetime=t;
		 this.outputsize=outsize;
		 this.submissiontime=subtime;
	}
	 public void setcost(double c)
	 {     cost=c;}
	 public double getcost()
	 { return cost; }
	 public void setbudget(double b)
	 {     budget=b;}
	 public double getbudget()
	 { return budget; }
	 public void settime(double t)
	 {     exetime=t;}
	 public double gettime()
	 { return exetime; }
	 public int getOutputSize()
	 { return outputsize; }
	 public double getsubtime(){
		 return submissiontime;
	 }
}
