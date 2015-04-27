package multimapreduce;

public class OutputClass {
	double cost,budget,finishtime;
	int outputsize;

	public OutputClass(double c,double b,double ft,int outsize ){
		 cost=c; budget=b;finishtime=ft;
		 this.outputsize=outsize;
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
	 {     finishtime=t;}
	 public double gettime()
	 { return finishtime; }
	 public int getOutputSize()
	 { return outputsize; }
	 
}
