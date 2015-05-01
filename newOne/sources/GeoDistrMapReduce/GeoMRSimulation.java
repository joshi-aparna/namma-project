package GeoDistrMapReduce;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ex.mapreduce.Experiment;
import org.cloudbus.cloudsim.ex.mapreduce.Properties;
import org.cloudbus.cloudsim.ex.mapreduce.Simulation;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.MapTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import xmlHandler.ExperimentParser;
import xmlHandler.WriteExperiment;
import xmlHandler.WriteJob;

import GeoDistrMapReduce.OutputClass;

public class GeoMRSimulation {
	
	static ArrayList<OutputClass> outputclasslist=new ArrayList<OutputClass>();
	static String currentID;
	public static String getCurrentID(){
		return currentID;
	}
	public static void main(){
		 for(final DatacenterConfig job:GroupManager.getDatacenterconfiglist()){
			  try{
				  currentID=job.getID();
				  System.out.println("STARTING JOB FOR DATACENTER ID="+currentID);
			  Simulation.main();
			  
			  
			  }
			  catch(Exception e){
				  System.out.println("Simulation. main threw e="+e);
				  e.printStackTrace();
			  }
		  }
		 startFinalReducePhase();
	}


	public static void createOutputClass(Request request) {
		
		
			for(MapTask m:request.job.mapTasks){
				int outputsize=0;
				for(String name:m.intermediateData.keySet()){
					outputsize+=m.intermediateData.get(name);
				}
				outputclasslist.add(new OutputClass(m.dataTransferCostFromTheDataSource(),request.budget,m.getTaskExecutionTimeInSeconds(),outputsize,getCurrentID()));
			
		}
		//startFinalReducePhase();
	}
	static void startFinalReducePhase(){
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
	subtime+=GroupManager.getDataTransferTime(selectedDc, outputclasslist);
		  //---------------------------
		  java.util.Properties properties = System.getProperties();
		  try {
			properties.load(new FileInputStream(new File(selectedDc.getWorkFile())));
		} catch (Exception e1) {
			System.out.println("in geosimulation loading props for selected dc");
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
		  
		
		  MultiMRSimulation.writePropertiesFile("reducephasetwo.properties");
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