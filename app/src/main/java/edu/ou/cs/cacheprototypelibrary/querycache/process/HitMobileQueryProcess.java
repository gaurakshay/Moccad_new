package edu.ou.cs.cacheprototypelibrary.querycache.process;

import edu.ou.cs.cacheprototypelibrary.core.cache.Cache;
import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;
import edu.ou.cs.cacheprototypelibrary.utils.StatisticsManager;


public class HitMobileQueryProcess implements Process<Query,QuerySegment> {

	public Cache<Query,QuerySegment> mCache = null;
	
	public HitMobileQueryProcess(Cache<Query,QuerySegment> cache)
	{
		mCache = cache;
	}
	
	@Override
	public QuerySegment run(Query q) {
		
		QuerySegment result = null;

        //<editor-fold desc="LOG START MOBILE PROCESS">
        long startMobileProcessTime = StatisticsManager.startMobileProcess();
        //</editor-fold>
		
		result = mCache.get(q);

        //<editor-fold desc="LOG STOP MOBILE PROCESS">
        StatisticsManager.stopMobileProcess(startMobileProcessTime);
        //</editor-fold>
        //<editor-fold desc="LOG newQueryProcessedOnMobile">
        StatisticsManager.newQueryProcessedOnMobile();
        //</editor-fold>
		
		return result;
	}

}
