package multimapreduce;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.cloudbus.cloudsim.ex.mapreduce.Simulation;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

public class MultiSimulation {
	static List<JobClass> jobList;
	private static String currentID;
	private static ArrayList<Integer> outputsize=new ArrayList<Integer>();
	public static String getCurrentID(){
		return currentID;
	}
	  public static void main(String JobConfigFile) {
		    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		    try {
		        SAXParser saxParser = saxParserFactory.newSAXParser();
		        JobFileParser handler = new JobFileParser();
		        saxParser.parse(new File(JobConfigFile), handler);
		        //Get Employees list
		        jobList = handler.getJobList();
		        //print employee information
		        for(JobClass job : jobList){
		        	System.out.println("datacenter:");
		            System.out.println(job);
		        }
		    } catch (ParserConfigurationException | SAXException | IOException e) {
		        e.printStackTrace();
		    }
		    }
	  public static String getWorkFile(String id){
		  for(JobClass job:jobList){
			  if((job.getID()).equals(id)){
				  return job.getWorkFile();
			  }
		  }
		  return null;
	  }
	  public static int getNumberOfDatacenters(){
		  return jobList.size();
	  }
	  public static void main(String args[])   {
		  main("JobFile.xml");
		  System.out.println("number of datacenters found="+getNumberOfDatacenters());
		  for(final JobClass job:jobList){
			  try{
				  currentID=job.getID();
				  System.out.println("STARTING JOB FOR DATACENTER ID="+currentID);
			  outputsize.addAll(Simulation.main());
			  System.out.println("now the outsputsize="+outputsize);
			  
			  }
			  catch(Exception e){
				  System.out.println("Simulation. main threw e="+e);
				  e.printStackTrace();
			  }
		  }
		/*  if(getNumberOfDatacenters()>1){
		  
			  createIntermediateFile(); 
			  writePropertiesFile("reducephasetwo.properties");
			  JobClass job=new JobClass();
			  job.setID("1");
			  job.setWorkFile("reducephasetwo.properties");
			  jobList.add(job);
			  System.out.println("NOW the number of datacenters="+getNumberOfDatacenters());
			  currentID=job.getID();
			  try {
				  System.out.println("Phase two simulation complete!!!! final output is of size:"+Simulation.main());
			  } catch (Exception e) {
			// 	TODO Auto-generated catch block
				  e.printStackTrace();
			  }
		  }*/
	  }
	  public static void createIntermediateFile() {
		  //--------------------------------------
		  String content = "!!org.cloudbus.cloudsim.ex.mapreduce.models.request.Job\n";
		  
			File file = new File("inputs/profiles/reduceFinal.yaml");
			FileWriter fw = null;
			try {
				fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			


		  //-------------------------------------------*/

			//--------------------------------------
		   Map<String, Object> data = new HashMap<String, Object>();
		  
		   ArrayList<String[]> reducejobs=new ArrayList<String[]>();
		   int sum=0;
		   for(int i:outputsize){
			  sum+=i;
		   }
		   String[] n={"reduce","1000",String.valueOf(sum)};
			  reducejobs.add(n); 
		   data.put("reduceTasks", reducejobs);
		   data.put("dataSourceName", "S3-sensors");
		   Yaml yaml = new Yaml();
		   
		try {
			fw = new FileWriter(file.getAbsoluteFile(),true);
			  yaml.dump(data, fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		   //--------------------------------------------*/
				
		}
	  public static void writePropertiesFile(String file){
		  Properties prop = new Properties();
			OutputStream output = null;
		 
			try {
		 
				output = new FileOutputStream(file);
		 
				// set the properties value
				prop.setProperty("cloud.file", "Cloud.yaml");
				prop.setProperty("experiment.files", "intermediate.yaml");
					
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
