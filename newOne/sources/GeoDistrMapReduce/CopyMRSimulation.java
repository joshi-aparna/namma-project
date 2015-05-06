package GeoDistrMapReduce;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ex.mapreduce.Configuration;
import org.cloudbus.cloudsim.ex.mapreduce.Experiment;
import org.cloudbus.cloudsim.ex.mapreduce.Properties;
import org.cloudbus.cloudsim.ex.mapreduce.Simulation;
import org.cloudbus.cloudsim.ex.mapreduce.Workload;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.MapTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.ReduceTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;

import xmlHandler.ExperimentParser;
import xmlHandler.WriteExperiment;
import xmlHandler.WriteJob;

public class CopyMRSimulation {

	private static String currentID;
	
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
	 
	public static void main(){
		double subtime=0;
		int mapsize=0;
		int reducesize=0;
		int reducemips=0;
		int mapmips=0;
		int outputsize=0;
		String policy=null;
		String cdm=null;
		String dsname=null;
		Map<UserClass, Double> ucp=null;
		Map<String,Double> ucpString=new HashMap<String,Double>();
		double deadline = 0;
		double budget=0;
		UserClass userclass=null;
		DatacenterConfig selectedDC=GroupManager.getDatacenterconfiglist().get(0);
		for(DatacenterConfig dc: GroupManager.getDatacenterconfiglist()){
			Configuration.loadProperties(dc.getID());
			String experimentfile=Properties.EXPERIMENT.getProperty(); 
			Experiment experiment=ExperimentParser.getExperiment("experiments/"+experimentfile);
			for(Workload w:experiment.workloads){
					if(dc.getID().equals(selectedDC.getID())){
					
					policy=w.getPolicy();
					cdm=w.getCDM();
					ucp=w.getUCP();
					
					}
					for(Request r: w.getRequests().requests){
						if(r.submissionTime>subtime)
							subtime=r.submissionTime;
						if(r.deadline>deadline)
							deadline=r.deadline;
						if(r.budget>budget)
							budget=r.budget;
						if(dc.getID().equals(selectedDC.getID())){
							userclass=r.userClass;
							dsname=r.job.dataSourceName;
						}
						for(MapTask m:r.job.mapTasks){
							mapsize+=m.dSize;
							subtime+=mapsize/(GroupManager.getDataTransferSpeed(selectedDC, dc)/8);
							mapmips=m.mi;
							for(String i:m.intermediateData.keySet())
								reducesize+=m.intermediateData.get(i);
						}
						for(ReduceTask rd:r.job.reduceTasks){
							if(rd.mi>reducemips)
								reducemips=rd.mi;
							outputsize+=rd.outmb;
						}
					}
				}
			}
		cloneJob cj=new cloneJob();
		cj.DatasourceName=dsname;
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("reduce1",String.valueOf(reducesize));
		cj.addmap("1", String.valueOf(mapsize),String.valueOf( mapmips), hm);
		cj.addreduce("reduce", String.valueOf(reducemips), String.valueOf(outputsize));
		new WriteJob(cj,"inputs/profiles/CopyMapReduce.xml");
		for(UserClass uc:ucp.keySet()){
			ucpString.put(uc.name(), ucp.get(uc));
		}
		new WriteExperiment(policy,cdm,ucpString,subtime,deadline,budget,"CopyMapReduce.xml",userclass.name());
		
		 String cloudfile=null;
			try {
				Configuration.loadProperties(selectedDC.getID());
				for (Properties property : Properties.values()) {
					Log.printLine("= " + property + ": " + property.getProperty());
					if(property.toString().equals("CLOUD")){
						cloudfile=property.getProperty();
					}
				    
				}
				OutputStream output = new FileOutputStream("CopyMapReduce.properties");
		 
				// set the properties value
				java.util.Properties prop = new java.util.Properties();
				prop.setProperty("cloud.file", cloudfile);
				prop.setProperty("experiment.files", "intermediate.xml");
					
				// save properties to project root folder
				prop.store(output, null);
				
			}catch(Exception e){
				System.out.println("copymrsimulation writing properties file e=");
				e.printStackTrace();
			}
			String id=selectedDC.getID();
			double inmbps=selectedDC.getInMbps();
			double outmbps=selectedDC.getOutMbps();
			GroupManager.datacenterconfiglist.remove(selectedDC);
			GroupManager.datacenterconfiglist.add(new DatacenterConfig(id,"CopyMapReduce.properties",inmbps,outmbps));
			System.out.println("STARTING COPY MAPREDUCE PHASE");
			try {
				currentID=id;
				Simulation.main();
			} catch (Exception e) {
				System.out.println("simulation.main threw exception for copymapreduce");
				e.printStackTrace();
			}
		 
		}
}
