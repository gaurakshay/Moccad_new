package edu.ou.cs.cacheprototypelibrary.core.cachemanagers;

import java.net.ConnectException;

import edu.ou.cs.cacheprototypelibrary.core.cache.Sizeable;
import edu.ou.cs.cacheprototypelibrary.core.process.Process;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.JSONParserException;


/**
 * @author Mikael Perrin
 * @since 1.0
 * The Resolution manager for the estimation cache
 */
public interface CacheResolutionManager<K extends Sizeable,T extends Sizeable> {

	/**
	 * Method used to process the cache in order to return the result Items
	 * @return the result
	 * @throws java.net.ConnectException
	 * @throws DownloadDataException 
	 * @throws JSONParserException 
	 */
	public T process() throws ConnectException, DownloadDataException, JSONParserException;
	
	public boolean addProcess(K key, Process<K, T> process);
	
}
