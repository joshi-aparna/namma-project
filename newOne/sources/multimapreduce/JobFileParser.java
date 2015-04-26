package multimapreduce;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JobFileParser extends DefaultHandler {

	private List<JobClass> joblist=null;
	private JobClass job=null;
	public List<JobClass> getJobList(){
		return joblist;
	}
	boolean bid=false;
	boolean bworkfile=false;
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("Datacenter")) {
            //create a new Job and put it in Map
            //initialize JobClass object 
            job = new JobClass();
            if (joblist == null)
                joblist = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("ID")) {
            //set boolean values for fields, will be used in setting Employee variables
            bid = true;
        } else if (qName.equalsIgnoreCase("work")) {
            bworkfile = true;
        }
    }
 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Datacenter")) {
            //add Job object to list
            joblist.add(job);
        }
    }
 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
 
        if (bid) {
            //id element, set datacenter id
            job.setID(new String(ch, start, length));
            bid = false;
        } else if (bworkfile) {
            job.setWorkFile(new String(ch, start, length));
            bworkfile = false;
        }
    }
		


}
