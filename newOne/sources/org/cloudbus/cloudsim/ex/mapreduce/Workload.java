package org.cloudbus.cloudsim.ex.mapreduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;
import org.cloudbus.cloudsim.ex.mapreduce.policy.Policy.CloudDeploymentModel;

public class Workload
{
    String policy;
    CloudDeploymentModel cloudDeploymentModel = CloudDeploymentModel.Hybrid;
    Map<UserClass, Double> userClassAllowedPercentage = new HashMap<UserClass, Double>();
    Requests requests;
    //--------------------------
    public String getPolicy(){
    	return policy;
    }
    public String getCDM(){
    	return cloudDeploymentModel.toString();
    }
    public Map<UserClass,Double> getUCP(){
    	return userClassAllowedPercentage;
    }
    public Requests getRequests(){
    	return requests;
    }
    //------------------------*/
    public Workload(String policy, String cloudDeploymentModel, Map<String, Double> userClassAllowedPercentage,
	    ArrayList<Request> requests)
    {
    System.out.println("!!!!!!!in workload.java:  creating new workload with num of requests="+requests.size());
    
	this.policy = policy;

	try
	{
	    this.cloudDeploymentModel = CloudDeploymentModel.valueOf(cloudDeploymentModel);
	} catch (Exception ex)
	{
	    Log.printLine(ex.getMessage());
	    Log.printLine("CloudDeploymentModel.Hybrid will be used");
	}

	for (Map.Entry<String, Double> userClassMap : userClassAllowedPercentage.entrySet()) {
		String key=userClassMap.getKey();
		System.out.println("in workload.java: finding userclass for key="+key);
	    UserClass userClass = UserClass.valueOf(key);
	    this.userClassAllowedPercentage.put(userClass, userClassMap.getValue());
	}
	this.requests = new Requests(requests);

	for (Request request : this.requests.requests)
	{
	    request.policy = policy;
	    request.setCloudDeploymentModel(CloudDeploymentModel.valueOf(cloudDeploymentModel));
	}
    }
}
