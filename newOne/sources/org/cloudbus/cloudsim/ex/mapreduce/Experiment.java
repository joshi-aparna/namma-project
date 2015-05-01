package org.cloudbus.cloudsim.ex.mapreduce;

import java.util.ArrayList;
import java.util.List;



import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;
import org.cloudbus.cloudsim.ex.util.CustomLog;

import GeoDistrMapReduce.GeoMRSimulation;
import GeoDistrMapReduce.GroupManager;
import GeoDistrMapReduce.MultiMRSimulation;

public class Experiment
{
    public List<Workload> workloads = new ArrayList<Workload>();
    public static String currentExperimentName;
    static int type=GroupManager.getTypeOfMR();
    
    @Override
    public String toString(){
    	String ret=currentExperimentName;
    	return ret;
    }
    public static void logExperimentsHeader(Requests requests)
    {
	// Experiments Plotting

        String id = null;
		if(type==GroupManager.MULTI)
        	 id=MultiMRSimulation.getCurrentID();
        else if(type==GroupManager.GEO)
        	id=GeoMRSimulation.getCurrentID();
        
	CustomLog.redirectToFile("output/plots/" + Experiment.currentExperimentName +id+ ".csv");

	if (Experiment.currentExperimentName.equals("Algorithm_Cost_Deadline")
		|| Experiment.currentExperimentName.equals("test1")
		|| Experiment.currentExperimentName.equals("test2")
		|| Experiment.currentExperimentName.equals("Algorithm_AlgoTime_Deadline"))
	{
	    String header = "Deadline,";
	    for (Request request : requests.requests)
		header += request.getDeadline() + ",";
	  CustomLog.printLine(header);
	}

	if (Experiment.currentExperimentName.equals("Algorithm_ExecutionTime_Budget"))
	{
	    String header = "Budget,";
	    for (Request request : requests.requests)
		header += request.getBudget() + ",";
	    CustomLog.printLine(header);
	}
    }

    public static void logExperimentsData(Requests requests)
    {
    	String id = null;
		if(type==GroupManager.MULTI)
        	 id=MultiMRSimulation.getCurrentID();
        else if(type==GroupManager.GEO)
        	id=GeoMRSimulation.getCurrentID();
        
	CustomLog.redirectToFile("output/plots/" + Experiment.currentExperimentName +id+ ".csv", true);

	if (Experiment.currentExperimentName.equals("Algorithm_Cost_Deadline")
		|| Experiment.currentExperimentName.equals("test1"))
	{
	    String plottingValue = requests.requests.get(0).getPolicy() + ",";
	    for (Request request : requests.requests)
		if (!request.getIsBudgetViolatedBoolean() && !request.getIsDeadlineViolatedBoolean())
		    plottingValue += request.getCost() + ",";
		else
		    plottingValue += "-1,";
	    CustomLog.printLine(plottingValue);
	}

	if (Experiment.currentExperimentName.equals("Algorithm_AlgoTime_Deadline")
		|| Experiment.currentExperimentName.equals("test2"))
	{
	    String plottingValue = requests.requests.get(0).getPolicy() + ",";
	    for (Request request : requests.requests)
		if (!request.getIsBudgetViolatedBoolean() && !request.getIsDeadlineViolatedBoolean()
			&& request.getAlgoFirstSoulationFoundedTime() != null)
		    plottingValue += request.getAlgoFirstSoulationFoundedTime() + ",";
		else
		    plottingValue += request.getAlgorithRunningTime() + ",";
	    CustomLog.printLine(plottingValue);
	}

	if (Experiment.currentExperimentName.equals("Algorithm_ExecutionTime_Budget"))
	{
	    String plottingValue = requests.requests.get(0).getPolicy() + ",";
	    for (Request request : requests.requests)
		if (!request.getIsBudgetViolatedBoolean() && !request.getIsDeadlineViolatedBoolean())
		    plottingValue += request.getExecutionTime() + ",";
		else
		    plottingValue += "-1,";
	    CustomLog.printLine(plottingValue);
	}
    }

}
