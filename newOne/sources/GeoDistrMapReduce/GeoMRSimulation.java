package GeoDistrMapReduce;

import java.util.ArrayList;

import multimapreduce.OutputClass;

import org.cloudbus.cloudsim.ex.mapreduce.models.request.MapTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;

public class GeoMRSimulation {
	

	static String currentId;
	public static String getCurrentId(){
		return currentId;
	}
	public static void main(){
		
	}


	public static void createOutputClass(Requests requests) {
		ArrayList<OutputClass> outputclasslist=new ArrayList<OutputClass>();
		for(Request r:requests.requests){
			for(MapTask m:r.job.mapTasks){
				int outputsize=0;
				for(String name:m.intermediateData.keySet()){
					outputsize+=m.intermediateData.get(name);
				}
				outputclasslist.add(new OutputClass(m.dataTransferCostFromTheDataSource(),r.budget,m.getTaskExecutionTimeInSeconds(),outputsize,getCurrentId()));
			}
		}
	}

}