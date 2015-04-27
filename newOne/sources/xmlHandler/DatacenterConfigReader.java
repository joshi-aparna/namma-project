package xmlHandler;

import java.util.ArrayList;
import java.util.List;

import multimapreduce.DatacenterConfig;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DatacenterConfigReader extends DefaultHandler {

	private List<DatacenterConfig> datacenterconfiglist=null;
	private DatacenterConfig datacenterconfig=null;
	public List<DatacenterConfig> getDataconfigList(){
		return datacenterconfiglist;
	}
	boolean bid=false;
	boolean bworkfile=false;
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("Datacenter")) {
            //create a new datacenterconfig and put it in Map
            //initialize datacenterconfigClass object 
            datacenterconfig = new DatacenterConfig();
            if (datacenterconfiglist == null)
                datacenterconfiglist = new ArrayList<>();
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
            //add datacenterconfig object to list
            datacenterconfiglist.add(datacenterconfig);
        }
    }
 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
 
        if (bid) {
            //id element, set datacenter id
            datacenterconfig.setID(new String(ch, start, length));
            bid = false;
        } else if (bworkfile) {
            datacenterconfig.setWorkFile(new String(ch, start, length));
            bworkfile = false;
        }
    }
		


}
