package xmlHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;


public class WriteExperiment {
	String policy;
	String clouddeploymentmodel;
	Map<String, Double> ucap;
	double subtime;
	double deadline;
	double budget;
	String jobFile;
	String Userclass;
	
	public WriteExperiment(String policy, String cdm, Map<String,Double> map, double sub, double d, double b, String j,String c){
		this.policy=policy;
		this.clouddeploymentmodel=cdm;
		this.ucap=map;
		this.subtime=sub;
		this.deadline=d;
		this.budget=b;
		this.jobFile=j;
		this.Userclass=c;
		write();
	}
	private void write(){
		 try {
	         StringWriter stringWriter = new StringWriter();

	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();	
	         XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
	   
	         xMLStreamWriter.writeStartDocument();
	         xMLStreamWriter.writeStartElement("Experiment");
	         xMLStreamWriter.writeStartElement("WorkLoad");
	         
	         
	         xMLStreamWriter.writeStartElement("Policy");
	         xMLStreamWriter.writeCharacters(policy);
	         xMLStreamWriter.writeEndElement();
	         
	         xMLStreamWriter.writeStartElement("CloudDeploymentModel");
	         xMLStreamWriter.writeCharacters(clouddeploymentmodel);
	         xMLStreamWriter.writeEndElement();
	         
	         xMLStreamWriter.writeStartElement("UserClassAllowedPercentage");
	         for(String key:ucap.keySet()){
	        	 xMLStreamWriter.writeStartElement("Class");			
	             xMLStreamWriter.writeAttribute("value", String.valueOf(ucap.get(key)));
	             xMLStreamWriter.writeCharacters(key);
	             xMLStreamWriter.writeEndElement();
	         }
	         xMLStreamWriter.writeEndElement();
	         
	         xMLStreamWriter.writeStartElement("Requests");
	         xMLStreamWriter.writeStartElement("Request");
	         
	         
	         xMLStreamWriter.writeStartElement("SubmissionTime");
	         xMLStreamWriter.writeCharacters(String.valueOf(subtime));
	         xMLStreamWriter.writeEndElement();
	         
	         xMLStreamWriter.writeStartElement("Deadline");
	         xMLStreamWriter.writeCharacters(String.valueOf(deadline));
	         xMLStreamWriter.writeEndElement();
	         
	         
	         xMLStreamWriter.writeStartElement("Budget");
	         xMLStreamWriter.writeCharacters(String.valueOf(budget));
	         xMLStreamWriter.writeEndElement();
	         
	         
	         xMLStreamWriter.writeStartElement("JobFile");
	         xMLStreamWriter.writeCharacters(jobFile);
	         xMLStreamWriter.writeEndElement();
	         
	         xMLStreamWriter.writeStartElement("UserClass");
	         xMLStreamWriter.writeCharacters(Userclass);
	         xMLStreamWriter.writeEndElement();
	         

	         xMLStreamWriter.writeEndElement();//request
	         xMLStreamWriter.writeEndElement();//requests
	         
	         xMLStreamWriter.writeEndElement();//workload
	         xMLStreamWriter.writeEndElement();//experiment
	         
	         xMLStreamWriter.writeEndDocument();

	         xMLStreamWriter.flush();
	         xMLStreamWriter.close();

	         String xmlString = stringWriter.getBuffer().toString();

	         stringWriter.close();

	         System.out.println(xmlString);
	         
	         
	         
	         File file = new File("experiments/intermediate.xml");
	         
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(xmlString);
				bw.close();
	   
		 }
		 catch(Exception e){
			 System.out.println("write experiment failed ");
			 e.printStackTrace();
		 }
	}

}
