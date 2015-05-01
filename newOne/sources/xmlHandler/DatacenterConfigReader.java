package xmlHandler;

import java.util.ArrayList;
import java.util.List;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import GeoDistrMapReduce.DatacenterConfig;

public class DatacenterConfigReader extends DefaultHandler {

	private List<DatacenterConfig> datacenterconfiglist=null;
	private DatacenterConfig datacenterconfig=null;
	public List<DatacenterConfig> getDataconfigList(){
		return datacenterconfiglist;
	}
	boolean bid=false;
	boolean bworkfile=false;
	boolean binmbps=false;
	boolean boutmbps=false;
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
        } else if (qName.equalsIgnoreCase("Inmbps")) {
            binmbps = true;
        } else if (qName.equalsIgnoreCase("OutMbps")) {
            boutmbps = true;
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
        else if (binmbps) {
            datacenterconfig.setInMbps(Double.parseDouble(new String(ch, start, length)));
            binmbps = false;
        }else if (boutmbps) {
        	datacenterconfig.setOutMbps(Double.parseDouble(new String(ch, start, length)));
        	boutmbps=false;
        }
    }
		


}
