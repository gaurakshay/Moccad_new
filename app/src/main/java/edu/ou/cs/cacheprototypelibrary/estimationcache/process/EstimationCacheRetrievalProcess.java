package edu.ou.cs.cacheprototypelibrary.estimationcache.process;

import edu.ou.cs.cacheprototypelibrary.core.cache.Cache;
import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.estimationcache.Estimation;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;


public class EstimationCacheRetrievalProcess implements Process<Query, Estimation> {

	private Cache<Query, Estimation> mCache = null;
	
	public EstimationCacheRetrievalProcess(Cache<Query, Estimation> cacheContentManager)
	{
		mCache = cacheContentManager;
	}
	
	@Override
	public Estimation run(Query query) {
		return mCache.get(query);
	}

}
