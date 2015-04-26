package multimapreduce;

import java.util.ArrayList;
import java.util.HashMap;
public class cloneJob {
	 public String DatasourceName;

	
	public ArrayList<clonemap> maplist=new ArrayList<clonemap>();
	public ArrayList<clonereduce> reducelist=new ArrayList<clonereduce>();
	
	public void addmap(String numberoftask, String datasize, String milinst, HashMap<String,String> inter){
		clonemap m=new clonemap();
		m.numberoftask=numberoftask;
		m.datasize=datasize;
		m.milinstr=milinst;
		m.inter=inter;
		maplist.add(m);
	}
	public void addreduce(String name, String milinstr, String outsize){
		clonereduce r=new clonereduce();
		r.name=name;
		r.milinstr=milinstr;
		r.outsize=outsize;
		reducelist.add(r);
	}

}
