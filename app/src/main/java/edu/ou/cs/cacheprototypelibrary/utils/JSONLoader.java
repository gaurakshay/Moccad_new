package edu.ou.cs.cacheprototypelibrary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.DownloadDataException;

public class JSONLoader {
	
	private static DefaultHttpClient httpClient = null;
	private static HttpGet httpGet = null;
	
	public static void abort()
	{
		if (httpClient != null)
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		if (httpGet != null)
		{
			httpGet.abort();
		}
	}
	
	public static InputStream getJSONInputStreamFromUrl(String url) throws DownloadDataException {
		 
		InputStream json = null;
		
		
    	// affichage de l'url dans les logs
    	Log.i("URL", url);
    	
        // fabrication de la requte http
        try {
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();     
            json = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
            throw new DownloadDataException();
        } catch (ClientProtocolException e) {
        	throw new DownloadDataException();   
        } catch (IOException e) {
        	throw new DownloadDataException();
        }
 
        // return JSON String
        return json;
 
    }
	
}
