package GeoDistrMapReduce;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import multimapreduce.DatacenterConfig;
import multimapreduce.MultiSimulation;

import org.xml.sax.SAXException;

import xmlHandler.DatacenterConfigReader;

public class GroupManager {
	static List<DatacenterConfig> datacenterconfiglist;
	public final static int COPY=1;
	public final static int MULTI=2;
	public final static int GEO=3;
	
	
	  public static int type=MULTI;
	  
	  
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
	  public static void main(String[] args){
		  readDataconfigFile("DatacenterConfigFile.xml");
		
		  if(type==MULTI){
		  System.out.println("number of datacenters found="+getNumberOfDatacenters());
			MultiSimulation.main();
		  }
			
		}
}
