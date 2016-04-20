package edu.ou.cs.cacheprototypelibrary.querycache.process;

import edu.ou.cs.cacheprototypelibrary.connection.DataAccessProvider;
import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.JSONParserException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;
import edu.ou.cs.cacheprototypelibrary.utils.StatisticsManager;


public class CloudQueryProcess implements Process<Query,QuerySegment> {

	
	private DataAccessProvider mDataAccessProvider = null;
	
	public CloudQueryProcess(DataAccessProvider dataAccessProvider)
	{
		mDataAccessProvider = dataAccessProvider;
	}
	
	@Override
	public QuerySegment run(Query query) throws DownloadDataException, JSONParserException {
		
		// statistics are computed in the Cloud data access provider
		
		QuerySegment segment = mDataAccessProvider.process(query);

        StatisticsManager.newQueryProcessedOnCloud();
		
		return segment;
	}

}
