package multimapreduce;

public class DatacenterConfig {
	private String id;
	private String workfile;
	private double inmbps;
	private double outmbps;
	public String getID(){
		return id;
	}
	public String getWorkFile(){
		return workfile;
	}
	public void setID(String id){
		this.id=id;
	}
	public void setWorkFile(String w){
		this.workfile=w;
	}
	public void setInMbps(double value){
		this.inmbps=value;
	}
	public void setOutMbps(double value){
		this.outmbps=value;
	}
	public double getInMbps(){
		return this.inmbps;
	}
	public double getOutMbps(){
		return this.outmbps;
	}
	@Override
	public String toString(){
		return "Datacenter id="+ id+"  workfile="+workfile+" in mbps="+inmbps+" out mbps="+outmbps;
	}
}
