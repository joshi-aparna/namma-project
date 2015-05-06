package xmlHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.cloudbus.cloudsim.ex.mapreduce.Experiment;
import org.cloudbus.cloudsim.ex.mapreduce.Workload;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExperimentParser {

	public static Experiment getExperiment(String file){
		Experiment experiment =null;
	    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	    try {
	        SAXParser saxParser = saxParserFactory.newSAXParser();
	        Parser handler = new Parser();
	        saxParser.parse(new File(file), handler);
	        experiment = handler.getExperiment();
	        return experiment;
	    }catch(Exception e){
	    	System.out.println("reading experiment file in simulation.java e=");
	    	e.printStackTrace();
	    }
	    return null;
		
	}
}
	
	class Parser extends DefaultHandler{
	ArrayList<Workload> workloadlist;
	Map<String, Double> ucap ;
	Requests rs;
	String policy;
	String cdm;
	ArrayList<Request> reqlist=new ArrayList<Request>();
	double value;
	double submissiontime;
	double deadline;
	double budget;
	String jobfile;
	UserClass userclass;
	String mclass;
	
	boolean bworkload=false;
	boolean bpolicy=false;
	boolean bcdm=false;//clouddeploymentmodel
	boolean bucap=false;//userclassallowedpercentage
	boolean bclass=false;
	boolean brequests=false;
	boolean brequest=false;
	boolean bsubtime=false;
	boolean bdeadline=false;
	boolean bbudget=false;
	boolean bjobfile=false;
	boolean buserclass=false;
	
	Experiment ex=new Experiment();

	public Experiment getExperiment(){
		return ex;
	}
	
	
	  @Override
	    public void startElement(String uri, String localName, String qName, Attributes attributes)
	            throws SAXException {
	 
		  if (qName.equalsIgnoreCase("Workload")) {
			  bworkload=true;
			  if(workloadlist==null){
				  workloadlist=new ArrayList<Workload>();
			  }
			  
		  }
		  if (qName.equalsIgnoreCase("Policy")) {
			  bpolicy=true;
		  }
		  if (qName.equalsIgnoreCase("CloudDeploymentModel")) {
			  bcdm=true;
		  }
		  if (qName.equalsIgnoreCase("UserClassAllowedPercentage")) {
			  bucap=true;
			  ucap= new HashMap<String, Double>();
		  }
		  if (qName.equalsIgnoreCase("Class")) {
			  value = Double.parseDouble(attributes.getValue("value"));
			  bclass=true;
		  }
		  if (qName.equalsIgnoreCase("Requests")) {
			  brequests=true;
			  if(reqlist==null){
				  reqlist=new ArrayList<Request>();
			  }
		  }
		  if (qName.equalsIgnoreCase("Request")) {
			  brequest=true;
		  }
		  if (qName.equalsIgnoreCase("SubmissionTime")) {
			  bsubtime=true;
		  }
		  if (qName.equalsIgnoreCase("Deadline")) {
			  bdeadline=true;
		  }
		  if (qName.equalsIgnoreCase("Budget")) {
			  bbudget=true;
		  }
		  if (qName.equalsIgnoreCase("JobFile")) {
			  bjobfile=true;
		  }
		  if (qName.equalsIgnoreCase("UserClass")) {
			  buserclass=true;
		  }
		 
	  }
	  @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        if (qName.equalsIgnoreCase("Experiment")) {
	        	System.out.println("in experimentparser, completed parsing. number of workloads found="+workloadlist.size());
	            ex.workloads=workloadlist;
	            
	        }
	        if (qName.equalsIgnoreCase("Workload")) {
	        	System.out.println("in experiment parser: ucap values:");
	        	for(String key:ucap.keySet()){
	        		System.out.println(key+":"+ucap.get(key));
	        	}
	        	Workload w=new Workload(policy, cdm, ucap, reqlist);
	        	
	        	workloadlist.add(w);
	        }
	        if (qName.equalsIgnoreCase("Request")) {
	        	Request req=new Request(submissiontime,deadline,budget,jobfile,userclass);
	        	reqlist.add(req);
	        }
	        if (qName.equalsIgnoreCase("Class")) {
	        	System.out.println("in experimentparser:inserting into ucap "+mclass+":"+value);
	        	ucap.put(mclass, value);
	        	
	        }
	        
	    }
	  @Override
	    public void characters(char ch[], int start, int length) throws SAXException {
	 
	        if (bpolicy) {
	            
	            policy=new String(ch,start,length);
	            bpolicy = false;
	        } else if (bcdm) {
	            cdm=new String(ch,start,length);
	            bcdm = false;
	        } else if (bsubtime) {
	            submissiontime=Double.parseDouble(new String(ch,start,length));
	            bsubtime = false;
	        } else if (bdeadline) {
	            deadline=Double.parseDouble(new String(ch,start,length));
	            bdeadline = false;
	        }else if (bbudget) {
	            budget=Double.parseDouble(new String(ch,start,length));
	            bbudget = false;
	        }else if (bjobfile) {
	            jobfile=new String(ch,start,length);
	            bjobfile = false;
	        }else if (buserclass) {
	        	String userclassname=new String(ch,start,length);
	        	System.out.println("in experiment parser: userclassname="+userclassname);
	            userclass=UserClass.valueOf(userclassname);
	            buserclass = false;
	        }else if(bclass){
	        	mclass=new String(ch,start,length);
	        }
	        
	        
	    }	
}

