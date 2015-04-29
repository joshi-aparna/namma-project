package multimapreduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.cloudbus.cloudsim.ex.mapreduce.Properties;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ex.mapreduce.Configuration;
import org.cloudbus.cloudsim.ex.mapreduce.Experiment;
import org.cloudbus.cloudsim.ex.mapreduce.Simulation;
import GeoDistrMapReduce.GroupManager;
import xmlHandler.ExperimentParser;
import xmlHandler.WriteExperiment;
import xmlHandler.WriteJob;

public class MultiSimulation {
	
	private static String currentID;
	private static ArrayList<OutputClass> outputclasslist=new ArrayList<OutputClass>();
	public static String getCurrentID(){
		return currentID;
	}

	
	 public static String getWorkFile(String id){
		  for(DatacenterConfig job:GroupManager.getDatacenterconfiglist()){
			  if((job.getID()).equals(id)){
				  return job.getWorkFile();
			  }
		  }
		  return null;
	  }
	  public static void main()   {
		  
		  for(final DatacenterConfig job:GroupManager.getDatacenterconfiglist()){
			  try{
				  currentID=job.getID();
				  System.out.println("STARTING JOB FOR DATACENTER ID="+currentID);
			  outputclasslist.addAll(Simulation.main());
			  
			  
			  }
			  catch(Exception e){
				  System.out.println("Simulation. main threw e="+e);
				  e.printStackTrace();
			  }
		  }
		  if(GroupManager.getNumberOfDatacenters()>1){
		  
			 
			  Map<String, Double> classmap=new HashMap<String,Double>();
			  classmap.put("GOLD",100.0);
			  classmap.put("SILVER",60.0);
			  classmap.put("BRONZE",00.0);
			  double subtime=0;
			  for(OutputClass i:outputclasslist){
				  if(i.gettime()>subtime){
					  subtime= i.gettime();
				  }
			  }
			  DatacenterConfig selectedDc=GroupManager.getDatacenterconfiglist().get(0);
			  for(DatacenterConfig d:GroupManager.getDatacenterconfiglist()){
				  if(!(d.getID()).equals(selectedDc.getID())){
					  System.out.println("Transferring from "+d.getID()+" to "+selectedDc.getID());
					  double in=selectedDc.getInMbps();
					  double out=d.getOutMbps();
					  double speed=(in<out)?in:out;
					  double dsize=0;
					  for(OutputClass oc:outputclasslist){
						  if((oc.getDcId()).equals(d.getID())){
							  dsize+=oc.getOutputSize();
						  }
					  }
					  subtime+=dsize / (speed / 8.0);
				  }
			  }
			  //---------------------------
			  java.util.Properties properties = System.getProperties();
			  try {
				properties.load(new FileInputStream(new File(selectedDc.getWorkFile())));
			} catch (Exception e1) {
				System.out.println("in multisimulation loading props for selected dc");
				e1.printStackTrace();
			}
			  String exFileNameSelectedDc=null;
			  
			  for (Properties property : Properties.values()) {
				    Log.printLine("= " + property + ": " + property.getProperty());
				    if((property.toString()).equals("EXPERIMENT")){
				    	exFileNameSelectedDc=property.getProperty();
				    	break;
				    }
			  }
			  
			  //---------------Reading experiment for the selected dc-----------
			  Experiment experiment =null;
			    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			    try {
			        SAXParser saxParser = saxParserFactory.newSAXParser();
			        ExperimentParser handler = new ExperimentParser();
			        saxParser.parse(new File("experiments/"+exFileNameSelectedDc), handler);
			        experiment = handler.getExperiment();
			    }catch(Exception e){
			    	System.out.println("reading experiment file in multisimulation.java e=");
			    	e.printStackTrace();
			    }
			    String policy=experiment.workloads.get(0).getPolicy();
			    String cdm=experiment.workloads.get(0).getCDM();
			    System.out.println("=================policy="+policy+"  cdm="+cdm);
			  //-----------------------------
			  
			  System.out.println("reducephasetwo, submission time="+subtime);
			  new WriteExperiment (policy, cdm,classmap,subtime,4000,10.5,"reduceFinal.xml","GOLD");
			  //-------------------------------------------------*/
			  cloneJob cj=new cloneJob();
			  cj.DatasourceName="S3-sensors";
		
			  int sum=0;
			   for(OutputClass i:outputclasslist){
				  sum+=i.getOutputSize();;
			   }
			  cj.addreduce("reduce", String.valueOf(10000), String.valueOf(sum));
			  new WriteJob(cj,"inputs/profiles/reduceFinal.xml");
			  //---------------------------------------------------*/
			  
			
			  writePropertiesFile("reducephasetwo.properties");
			  DatacenterConfig datacenterconfig=new DatacenterConfig();
			  datacenterconfig.setID("ReducePhaseTwo");
			  datacenterconfig.setWorkFile("reducephasetwo.properties");
			  GroupManager.getDatacenterconfiglist().add(datacenterconfig);
			  System.out.println("NOW the number of datacenters="+GroupManager.getNumberOfDatacenters());
			  currentID="ReducePhaseTwo";// Important!! donot change this name. accessed in mapreduceengine
			  try {
				Simulation.main();
			  } catch (Exception e) {
			// 	TODO Auto-generated catch block
				  e.printStackTrace();
			  }
		  }
	  }
	 
	  public static void writePropertiesFile(String file){
		  java.util.Properties prop = new java.util.Properties();
			OutputStream output = null;
		 String cloudfile=null;
			try {
				Configuration.loadProperties("BangaloreCenter");
				for (Properties property : Properties.values()) {
					Log.printLine("= " + property + ": " + property.getProperty());
					if(property.toString().equals("CLOUD")){
						cloudfile=property.getProperty();
					}
				    
				}
				

				System.out.println("cloudfile="+cloudfile);
				output = new FileOutputStream(file);
		 
				// set the properties value
				prop.setProperty("cloud.file", cloudfile);
				prop.setProperty("experiment.files", "intermediate.xml");
					
				// save properties to project root folder
				prop.store(output, null);
		 
			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		 
			}
		  }
	  
	  
}
