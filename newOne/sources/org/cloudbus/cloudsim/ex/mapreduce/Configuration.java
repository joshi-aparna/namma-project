package org.cloudbus.cloudsim.ex.mapreduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import GeoDistrMapReduce.MultiMRSimulation;



/**
 * This class implements the properties manager. Here the name of the properties
 * file is defined. Moreover, when properties are to be read, this is the class
 * that is called.
 * 
 */
public enum Configuration {

    INSTANCE;

    private static Properties properties = System.getProperties();

    
    public static  void loadProperties(String id){
	loadPropertiesFromFile(MultiMRSimulation.getWorkFile(id));
    }

    public String getProperty(String key) {
	return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
	properties.setProperty(key, value);
    }

    private  static void loadPropertiesFromFile(String file) {
	File propertiesFile = new File(file);
	try {
	    properties.load(new FileInputStream(propertiesFile));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
