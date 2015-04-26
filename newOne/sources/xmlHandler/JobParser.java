package xmlHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.ex.mapreduce.models.request.Job;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.MapTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.ReduceTask;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JobParser extends DefaultHandler {
	Job job;

	String datasourcename;
	int numberoftasks;
	int datasize;
	int interdatasize;
	String intername;
	int milinstr;
	Map<String, Integer> inter=new HashMap<String,Integer> ();
	int reducemilinstr;
	int outputdatasize;
	String reducename;
	String rname;
	ArrayList<MapTask> maptasks;
	ArrayList<ReduceTask> reducetasks;
	
	
	boolean bdatasourcename=false;
	boolean bnumberoftasks=false;
	boolean bdatasize=false;
	boolean binterdatasize=false;
	boolean bintername=false;
	boolean bmilinstr=false;
	boolean breducemilinstr=false;
	boolean boutputsize=false;
	boolean brname=false;
	
	public Job getJob(){
		return job;
	}
	
	  @Override
	    public void startElement(String uri, String localName, String qName, Attributes attributes)
	            throws SAXException {
	 
		  if (qName.equalsIgnoreCase("DatasourceName")) {
			  System.out.println("datasourcename tag open");
			  bdatasourcename=true;
			  
		  }
		  if (qName.equalsIgnoreCase("Maptasks")) {
			  	maptasks=new ArrayList<MapTask>();
		  }
		  if (qName.equalsIgnoreCase("NumberOfTasks")) {
			  bnumberoftasks=true;
		  }
		  if (qName.equalsIgnoreCase("DataSize")) {
			  bdatasize=true;
		  }
		  if (qName.equalsIgnoreCase("MillionInstructions")) {
			  bmilinstr=true;
		  }
		  if (qName.equalsIgnoreCase("RTaskId")) {
			  System.out.println("intermediate data size found="+attributes.getValue("dsize"));
			  interdatasize = Integer.parseInt(attributes.getValue("dsize"));
			  bintername=true;
		  }
		  if (qName.equalsIgnoreCase("ReduceTasks")) {
			  reducetasks=new ArrayList<ReduceTask>();
			  
		  }
		  if (qName.equalsIgnoreCase("ReduceTask")) {
			  rname=attributes.getValue("name");
			  
		  }
		  if (qName.equalsIgnoreCase("ReduceMillionInstructions")) {
			  breducemilinstr=true;
		  }
		  if (qName.equalsIgnoreCase("OutputDataSize")) {
			  boutputsize=true;
		  }
		  
		  
	  }
	  
	  
	  @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        if (qName.equalsIgnoreCase("Job")) {
	        	job=new Job();
	        	job.dataSourceName=datasourcename;
	        	job.setDatasourceName(datasourcename);
	        	System.out.println("xml closing job element datasourcename="+datasourcename);
	        	job.mapTasks=maptasks;
	        	job.reduceTasks=reducetasks;
	        	System.out.println("xml job end element job created="+job);
	        	
	        }
	        if (qName.equalsIgnoreCase("ReduceTask")) {
	        	ReduceTask r=new ReduceTask(rname,reducemilinstr,outputdatasize);
	        	reducetasks.add(r);
	        }
	        if (qName.equalsIgnoreCase("MapTask")) {
	        	MapTask m=new MapTask(numberoftasks, datasize, milinstr, inter);
	        	m.dataSourceName=datasourcename;
	        	maptasks.add(m);
	        }
	        if (qName.equalsIgnoreCase("RTaskId")) {
	        	inter.put(reducename, interdatasize);
	        }
	     /*   if (qName.equalsIgnoreCase("RTaskId")) {
	        	bintername=false;
	        }
	        if (qName.equalsIgnoreCase("MillionInstructions")) {
	        	bmilinstr=false;
	        }
	        if (qName.equalsIgnoreCase("DataSize")) {
	        	bdatasize=false;
	        }
	        if (qName.equalsIgnoreCase("NumberOfTasks")) {
	        	bnumberoftasks=false;
	        }
	        if (qName.equalsIgnoreCase("Datasourcename")) {
				  bdatasourcename=false;
				  
			  }*/
	  }
	  public void characters(char ch[], int start, int length) throws SAXException {
			 
	        if (bdatasourcename) {
	            
	            datasourcename=new String(ch,start,length);
	            System.out.println("datasourcename found in xml:"+datasourcename);
	            bdatasourcename = false;
	        } else if (bnumberoftasks) {
	            numberoftasks=Integer.parseInt(new String(ch,start,length));
	            bnumberoftasks = false;
	        }
	        else if (bdatasize) {
	            datasize=Integer.parseInt(new String(ch,start,length));
	            bdatasize = false;
	        } else if (bmilinstr) {
	            milinstr=Integer.parseInt(new String(ch,start,length));
	            bmilinstr = false;
	        } else if (bintername) {
	            reducename=new String(ch,start,length);
	            bintername = false;
	        } else if (breducemilinstr) {
	            reducemilinstr=Integer.parseInt(new String(ch,start,length));
	            breducemilinstr = false;
	        } else if (boutputsize) {
	            outputdatasize=Integer.parseInt(new String(ch,start,length));
	            boutputsize = false;
	        }
	  }
}
