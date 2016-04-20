package edu.ou.cs.cacheprototypelibrary.querycache.trimming;

import java.util.HashSet;
import java.util.Set;

import edu.ou.cs.cacheprototypelibrary.querycache.query.Predicate;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;

public class RenEtAlQueryCacheQueryTrimmer implements QueryCacheQueryTrimmer{

	@Override
	public QueryTrimmingResult evaluate(Query inputQuery, Query segmentQuery) {
		QueryTrimmingResult result = new QueryTrimmingResult();
		GuoEtAlPredicateAnalyzer geapanalyzer = new GuoEtAlPredicateAnalyzer();
		
		// if all the input query attributes are found in the segment query attributes
		if(segmentQuery.containsAttributes(inputQuery.getPredicateAttributes()))
		{
			Set<Predicate> allPredicates = new HashSet<Predicate>(inputQuery.getPredicates());
			allPredicates.addAll(segmentQuery.getPredicates());
			if(segmentQuery.equals(inputQuery)) // exact hit
			{
				result.type = QueryTrimmingType.CACHE_HIT;
				result.probeQuery = inputQuery;
			}
			else if (geapanalyzer.respectsImplicationIntegerDomain(inputQuery.getPredicates(), segmentQuery.getPredicates())) // extended hit
			{
				GuoEtAlPredicateAnalyzer eqGeapanalyzer = new GuoEtAlPredicateAnalyzer();
				if (eqGeapanalyzer.respectsImplicationIntegerDomain(segmentQuery.getPredicates(), inputQuery.getPredicates()))// equivalent
				{
					result.type = QueryTrimmingType.CACHE_EXTENDED_HIT_EQUIVALENT;
				}
				else
				{
					result.type = QueryTrimmingType.CACHE_EXTENDED_HIT_INCLUDED;
				}
				result.entryQuery = segmentQuery;
				result.probeQuery = inputQuery;
				
			}
			else if (geapanalyzer.respectsSatifiabilityIntegerDomain(allPredicates)) //partial hit
			{
				result.type = QueryTrimmingType.CACHE_PARTIAL_HIT;
				result.entryQuery = segmentQuery;
				result.probeQuery = new Query(segmentQuery.getRelation());
				result.probeQuery.addPredicates(inputQuery.getPredicates());
				result.remainderQuery = new Query(inputQuery.getRelation());
				result.remainderQuery.addPredicates(inputQuery.getPredicates());
				result.remainderQuery.addExcludedPredicates(segmentQuery.getPredicates());
			}
			else
			{
				result.type = QueryTrimmingType.CACHE_MISS;
				result.remainderQuery = inputQuery;
			}
		}
		else
		{
			result.type = QueryTrimmingType.CACHE_MISS;
			result.remainderQuery = inputQuery;
		}
		
		return result;
	}

	
	
	
}
