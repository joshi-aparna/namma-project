package org.cloudbus.cloudsim.ex.mapreduce.models.request;


public class ReduceTask extends Task {

	private int outmb;
    public ReduceTask(String name, int mi, int mb) {
	// reduce task dSize is 0 for now, and it will be updated in Request
	// constractor, after creating the job
	super(name, 0, mi);
	outmb=mb;
    }

    public void updateDSize(Request request)
    {
    	try{
    		for (MapTask mapTask : request.job.mapTasks)
    			if (mapTask.intermediateData.containsKey(name))
    				dSize += mapTask.intermediateData.get(name);
    	}
    	catch(NullPointerException e){
    		System.out.println("ReduceTask encountered null pointer exception while calculating dSize. setting dSize to "+outmb);
    		dSize=outmb;
    	}
    }

    public String getTaskType()
    {
	return "Reduce";
    }
    public int getOutputMB(){
    	return outmb;
    }
}
