package multimapreduce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.cloudbus.cloudsim.ex.mapreduce.Properties;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ex.mapreduce.Configuration;
import org.cloudbus.cloudsim.ex.mapreduce.Simulation;
import org.xml.sax.SAXException;

import xmlHandler.DatacenterConfigReader;
import xmlHandler.WriteExperiment;
import xmlHandler.WriteJob;

public class MultiSimulation {
	static List<DatacenterConfig> datacenterconfiglist;
	private static String currentID;
	private static ArrayList<OutputClass> outputclasslist=new ArrayList<OutputClass>();
	public static String getCurrentID(){
		return currentID;
	}
	  public static void main(String JobConfigFile) {
		    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		    try {
		        SAXParser saxParser = saxParserFactory.newSAXParser();
		        DatacenterConfigReader handler = new DatacenterConfigReader();
		        saxParser.parse(new File(JobConfigFile), handler);
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
	  public static String getWorkFile(String id){
		  for(DatacenterConfig job:datacenterconfiglist){
			  if((job.getID()).equals(id)){
				  return job.getWorkFile();
			  }
		  }
		  return null;
	  }
	  public static int getNumberOfDatacenters(){
		  return datacenterconfiglist.size();
	  }
	  public static void main(String args[])   {
		  main("JobFile.xml");
		  System.out.println("number of datacenters found="+getNumberOfDatacenters());
		  for(final DatacenterConfig job:datacenterconfiglist){
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
		  if(getNumberOfDatacenters()>1){
		  
			 
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
			  System.out.println("reducephasetwo, submission time="+subtime);
			  new WriteExperiment ("BBDecision", "Public",classmap,subtime,4000,10.5,"reduceFinal.xml","GOLD");
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
			  datacenterconfiglist.add(datacenterconfig);
			  System.out.println("NOW the number of datacenters="+getNumberOfDatacenters());
			  currentID="ReducePhaseTwo";// Important!! donot change this name. accessed in mapreduceengine
			  try {
				  System.out.println("Phase two simulation complete!!!! final output is of size:"+Simulation.main());
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
