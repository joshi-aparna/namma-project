package multimapreduce;

public class JobClass {
	private String id;
	private String workfile;
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
	@Override
	public String toString(){
		return "Datacenter id="+ id+"  workfile="+workfile;
	}
}
