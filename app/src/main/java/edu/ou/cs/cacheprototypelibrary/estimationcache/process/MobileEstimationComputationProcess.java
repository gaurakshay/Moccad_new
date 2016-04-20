package edu.ou.cs.cacheprototypelibrary.estimationcache.process;

import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.estimationcache.Estimation;
import edu.ou.cs.cacheprototypelibrary.estimationcache.MobileEstimationComputationManager;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;


public class MobileEstimationComputationProcess implements Process<Query, Estimation> {

	MobileEstimationComputationManager mMobileEstimationComputationManager = null;
	
	public MobileEstimationComputationProcess(MobileEstimationComputationManager mobileEstiCompManager)
	{
		mMobileEstimationComputationManager = mobileEstiCompManager;
	}
	
	@Override
	public Estimation run(Query query) {
		return mMobileEstimationComputationManager.estimate(query);
	}

}
