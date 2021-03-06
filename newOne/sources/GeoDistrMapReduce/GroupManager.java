package GeoDistrMapReduce;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.SAXException;

import GeoDistrMapReduce.DatacenterConfig;
import GeoDistrMapReduce.MultiMRSimulation;
import GeoDistrMapReduce.OutputClass;

import xmlHandler.DatacenterConfigReader;

public class GroupManager {
	static List<DatacenterConfig> datacenterconfiglist;
	public final static int COPY=1;
	public final static int MULTI=2;
	public final static int GEO=3;
	
	
	  public static int type;
	  
	  public static String getWorkFile(String id){
		  for(DatacenterConfig job:getDatacenterconfiglist()){
			  if((job.getID()).equals(id)){
				  return job.getWorkFile();
			  }
		  }
		  return null;
	  }
	  
	  public static void readDataconfigFile(String DatacenterConfigFile) {
		    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		    try {
		        SAXParser saxParser = saxParserFactory.newSAXParser();
		        DatacenterConfigReader handler = new DatacenterConfigReader();
		        saxParser.parse(new File(DatacenterConfigFile), handler);
		        //Get Employees list
		        datacenterconfiglist = handler.getDataconfigList();
		        //print employee information
		        for(DatacenterConfig job : datacenterconfiglist){
		        	System.out.println("datacenter:");
		            System.out.println(job);
		        }
		    } catch (ParserConfigurationException | SAXException | IOException e) {
		        e.printStackTrace();
		    }
	}
	
	 
	  public static int getNumberOfDatacenters(){
		  return datacenterconfiglist.size();
	  }
	  public static List<DatacenterConfig> getDatacenterconfiglist(){
		  return datacenterconfiglist;
	  }
	  public static double getDataTransferTime(DatacenterConfig selectedDc, ArrayList<OutputClass> outputclasslist){
		 double time=0;
		  for(DatacenterConfig d:getDatacenterconfiglist()){
			  if(!(d.getID()).equals(selectedDc.getID())){
				 double speed=getDataTransferSpeed(selectedDc, d);
				  double dsize=0;
				  for(OutputClass oc:outputclasslist){
					  if((oc.getDcId()).equals(d.getID())){
						  dsize+=oc.getOutputSize();
					  }
				  }
				  time+=dsize / (speed / 8.0);
			  }
		  }
		  return time;
		  
	  }
	  public static double getDataTransferSpeed(DatacenterConfig into,DatacenterConfig outof){
		  System.out.println("Transferring from "+outof.getID()+" to "+into.getID());
		  double in=into.getInMbps();
		  double out=outof.getOutMbps();
		  return (in<out)?in:out;
	  }
	  public static int getTypeOfMR(){
		  if(type==MULTI)
			  return MULTI;
		  else if(type==COPY)
			  return COPY;
		  else if(type==GEO)
			  return GEO;
		  return 0;
	  }
	  
	  public static void main(String[] args){
		  readDataconfigFile("DatacenterConfigFile.xml");
		  type=DecisionAlgorithm.getSuitableMRType(datacenterconfiglist);
		  if(type==MULTI){
		  System.out.println("for multimr simulation, number of datacenters found="+getNumberOfDatacenters());
			MultiMRSimulation.main();
		  }
		  else if(type==GEO){
			  System.out.println("for geo simulation, number of datacenters found="+getNumberOfDatacenters());
				GeoMRSimulation.main();
		  }
		  else if(type==COPY){
			  System.out.println("for copy simulation, number of datacenters found="+getNumberOfDatacenters());
				CopyMRSimulation.main();
		  }
			
		}
}
