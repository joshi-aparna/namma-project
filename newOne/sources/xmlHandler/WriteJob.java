package xmlHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import multimapreduce.cloneJob;
import multimapreduce.clonemap;
import multimapreduce.clonereduce;

public class WriteJob {

	String filename;
	public WriteJob(cloneJob cj,String filename){
	
		this.filename=filename;
		write(cj);
	}
	
	private void write(cloneJob cj){
		try{
			 StringWriter stringWriter = new StringWriter();

	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();	
	         XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
	   
	         xMLStreamWriter.writeStartDocument();
	         xMLStreamWriter.writeStartElement("Job");
	         
	         xMLStreamWriter.writeStartElement("DataSourceName");
	         xMLStreamWriter.writeCharacters(cj.DatasourceName);
	         xMLStreamWriter.writeEndElement();
	         if(cj.maplist.size()>0){
	         xMLStreamWriter.writeStartElement("MapTasks");
	         for(clonemap m:cj.maplist){
	        	 xMLStreamWriter.writeStartElement("MapTask");
	        	 
	        	 xMLStreamWriter.writeStartElement("NumberOfTasks");
		         xMLStreamWriter.writeCharacters(m.numberoftask);
		         xMLStreamWriter.writeEndElement();
		         
		         xMLStreamWriter.writeStartElement("DataSize");
		         xMLStreamWriter.writeCharacters(m.datasize);
		         xMLStreamWriter.writeEndElement();
		         
		         xMLStreamWriter.writeStartElement("MillionInstructions");
		         xMLStreamWriter.writeCharacters(m.milinstr);
		         xMLStreamWriter.writeEndElement();
		         
		         
		         for(String key:m.inter.keySet()){
		        	 xMLStreamWriter.writeStartElement("RTaskId");			
		        	 xMLStreamWriter.writeAttribute("dsize", String.valueOf(m.inter.get(key)));
		        	 xMLStreamWriter.writeCharacters(key);
		        	 xMLStreamWriter.writeEndElement();
		         }
	         
	        	 xMLStreamWriter.writeEndElement();//maptask
	         }
	         xMLStreamWriter.writeEndElement();//maptasks
	         }
	         else{
	        	 System.out.println("writejob maplist is empty");
	         }
	        if(cj.reducelist.size()>0){
	        	xMLStreamWriter.writeStartElement("ReduceTasks");
	        	for(clonereduce c:cj.reducelist){
	        	xMLStreamWriter.writeStartElement("ReduceTask");			
	        	 xMLStreamWriter.writeAttribute("name",c.name);
	        	 
	        	 xMLStreamWriter.writeStartElement("ReduceMillionInstructions");
		         xMLStreamWriter.writeCharacters(c.milinstr);
		         xMLStreamWriter.writeEndElement();
		         
		         xMLStreamWriter.writeStartElement("OutputDataSize");
		         xMLStreamWriter.writeCharacters(c.outsize);
		         xMLStreamWriter.writeEndElement();
		         
	        	 xMLStreamWriter.writeEndElement();//reducetask
	        	}
	        	xMLStreamWriter.writeEndElement();//reducetasks
	        }
	        else{
	        	System.out.println("writejob reducelist is empty");
	        }
	         
	         xMLStreamWriter.writeEndElement();//job
	         
	         xMLStreamWriter.writeEndDocument();

	         xMLStreamWriter.flush();
	         xMLStreamWriter.close();

	         String xmlString = stringWriter.getBuffer().toString();

	         stringWriter.close();

	         System.out.println(xmlString);
	         
	         
	         
	         File file = new File(filename);
	         
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
			System.out.println("Write job failed to write ");
			e.printStackTrace();
		}
	}
	
}
