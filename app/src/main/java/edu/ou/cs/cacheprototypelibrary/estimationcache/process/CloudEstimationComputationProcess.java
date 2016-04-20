package edu.ou.cs.cacheprototypelibrary.estimationcache.process;

import java.net.ConnectException;

import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.estimationcache.CloudEstimationComputationManager;
import edu.ou.cs.cacheprototypelibrary.estimationcache.Estimation;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.JSONParserException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;

public class CloudEstimationComputationProcess implements Process<Query, Estimation>{

	CloudEstimationComputationManager mCloudEstimationComputationManager = null;
	
	public CloudEstimationComputationProcess(CloudEstimationComputationManager clEstiCompManager)
	{
		mCloudEstimationComputationManager = clEstiCompManager;
	}
	
	@Override
	public Estimation run(Query query) throws ConnectException, JSONParserException, DownloadDataException {
		return mCloudEstimationComputationManager.estimate(query);
	}

}
