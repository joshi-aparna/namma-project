package GeoDistrMapReduce;

import java.util.List;

import org.cloudbus.cloudsim.ex.mapreduce.Configuration;
import org.cloudbus.cloudsim.ex.mapreduce.Experiment;
import org.cloudbus.cloudsim.ex.mapreduce.Properties;
import org.cloudbus.cloudsim.ex.mapreduce.Workload;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.MapTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.ReduceTask;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;

import xmlHandler.ExperimentParser;

/*----------------
 * COPY---if input data is smaller than output or any intermediate data

GEO---intermediate data size is smaller than both input and output size

MULTI---otherwise...

 */

public class DecisionAlgorithm {

	static int getSuitableMRType(DatacenterConfig dc){
		int input=0;
		int inter=0;
		int output=0;
		Configuration.loadProperties(dc.getID());
		String experimentfile=Properties.EXPERIMENT.getProperty(); 
		Experiment experiment=ExperimentParser.getExperiment("experiments/"+experimentfile);
		for(Workload w:experiment.workloads){
			for(Request r: w.getRequests().requests){
				for(MapTask m:r.job.mapTasks){
					input+=m.dSize;
					for(String i:m.intermediateData.keySet())
						inter+=m.intermediateData.get(i);
				}
				for(ReduceTask rd:r.job.reduceTasks){
					for(MapTask m:r.job.mapTasks){
						if(m.intermediateData.containsKey(rd.name))
							output+=rd.outmb;
					}
				}
			}
		}
		if(input<inter && input<output)
			return GroupManager.COPY;
		else if(inter<input && inter<output)
			return GroupManager.GEO;
		
		return GroupManager.MULTI;
	}
	public static int getSuitableMRType(List<DatacenterConfig> dclist){
		int copy=0;
		int geo=0;
		int multi=0;
		for(DatacenterConfig dc:dclist){
			int retval=getSuitableMRType(dc);
			if(retval==GroupManager.COPY)
				copy++;
			else if(retval==GroupManager.GEO)
				geo++;
			else if(retval==GroupManager.MULTI)
				multi++;
		}
		if(copy>multi && copy>geo)
			return GroupManager.COPY;
		else if(geo>copy && geo>multi)
			return GroupManager.GEO;
		return GroupManager.MULTI;
	}
}
