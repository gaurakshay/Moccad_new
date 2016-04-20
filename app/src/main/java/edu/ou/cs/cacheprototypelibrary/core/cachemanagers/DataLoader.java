package edu.ou.cs.cacheprototypelibrary.core.cachemanagers;

import android.content.Context;

import java.net.ConnectException;
import java.util.List;

import edu.ou.cs.cacheprototypelibrary.connection.DataAccessProvider;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.ConstraintsNotRespectedException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.JSONParserException;



/**
 * @author Mikael Perrin
 * @since 1.0
 */
public abstract class DataLoader<K,V> {
	
	protected DataAccessProvider mDataAccessProvider = null;
	
	public DataLoader(Context context, DataAccessProvider dataAccessProvider)
	{
		if (dataAccessProvider != null)
		{
			this.mDataAccessProvider = dataAccessProvider;
		}
	}
	
	public abstract List<List<String>> load(K key) throws ConstraintsNotRespectedException, ConnectException, DownloadDataException, JSONParserException;

	public void setDataAccessProvider(DataAccessProvider dataAccessProvider)
	{
		if (dataAccessProvider != null)
		{
			this.mDataAccessProvider = dataAccessProvider;
		}
	}

	public abstract void setUsingReplacement(boolean useReplacement);
}
