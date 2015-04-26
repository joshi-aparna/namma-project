package org.cloudbus.cloudsim.ex.mapreduce.models.request;

import java.util.List;

public class Job {
    public String dataSourceName;
    public List<MapTask> mapTasks;
    public List<ReduceTask> reduceTasks;

    public void setDatasourceName(String n){
    	System.out.println("in job.java: setting datasourcename="+n);
    	dataSourceName=n;
    }
    public Task getTask(int taskId)
    {
	for (MapTask mapTask : mapTasks) {
	    if (mapTask.getCloudletId() == taskId)
		return mapTask;
	}

	for (ReduceTask reduceTask : reduceTasks) {
	    if (reduceTask.getCloudletId() == taskId)
		return reduceTask;
	}

	return null;
    }
    @Override
    public String toString(){
    	String ret=dataSourceName+"\n";
    	if(mapTasks!=null){
    	for(MapTask m:mapTasks){
    		ret+=m;
    	}
    	}
    	
    	return ret;
    }
}
