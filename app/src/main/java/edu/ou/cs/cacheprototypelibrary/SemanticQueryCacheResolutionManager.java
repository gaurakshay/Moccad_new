package edu.ou.cs.cacheprototypelibrary;

import java.net.ConnectException;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Pair;
import edu.ou.cs.cacheprototypelibrary.core.cachemanagers.CacheResolutionManager;
import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.JSONParserException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;

/**
 * @author Mikael Perrin
 * @since 1.0
 * ResolutionManager used to process the QueryCache if it is less expensive do so, 
 * or on the cloud if the data are not available in cache, or if it is less expensive
 * to be processed on the cloud.
 */
public class SemanticQueryCacheResolutionManager implements CacheResolutionManager<Query,QuerySegment>{

	private Queue<Pair<Query,Process<Query,QuerySegment>>> mProcessQueue = new LinkedList<Pair<Query,Process<Query,QuerySegment>>>();
	
	@Override
	public QuerySegment process() throws ConnectException, DownloadDataException, JSONParserException {
		QuerySegment resultSegment = new QuerySegment();
		QuerySegment curSegment = null;
		Pair<Query,Process<Query,QuerySegment>> curP = null;
		
		while(!mProcessQueue.isEmpty())
		{
			curP = mProcessQueue.poll();
			curSegment = curP.second.run(curP.first);
			resultSegment.addAllTuples(curSegment);
		}

		return resultSegment;
	}

	@Override
	public boolean addProcess(Query key, Process<Query, QuerySegment> process) {
		return mProcessQueue.add(new Pair<Query,Process<Query,QuerySegment>>(key,process));
	}
	
}
