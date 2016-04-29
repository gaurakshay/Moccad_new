package edu.ou.cs.moccad_new;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import edu.ou.cs.cacheprototypelibrary.core.cache.Cache;
import edu.ou.cs.cacheprototypelibrary.core.cachemanagers.CacheReplacementManager;
import edu.ou.cs.cacheprototypelibrary.provider.ProcessedQueryDbHelper;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;

public class Display_cache extends AppCompatActivity {

    private QueryAdapter mProcessedQueryAdapter = null;
    private List<Query> mQueries = null;
    private TextView mEmptyTextView = null;
    private ProcessedQueryDbHelper mDBHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cache);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDBHelper = new ProcessedQueryDbHelper(this.getApplication().getApplicationContext());
        mQueries = mDBHelper.getAllProcessedQueries();
        mProcessedQueryAdapter = new QueryAdapter(this.getApplication().getApplicationContext(),R.layout.activity_display_cache, mQueries);
        //setListAdapter(mProcessedQueryAdapter);

        MOCCAD mApplication = (MOCCAD)getApplicationContext();
        if(((MOCCAD) this.getApplication()).getQueryCache() == null) {
            System.out.println("Creating cache");
            ((MOCCAD) this.getApplication()).setCacheManager("1");
        }
        final Cache<Query, QuerySegment> cache = ((MOCCAD) this.getApplication()).getQueryCache();

        CacheReplacementManager crm = cache.getCacheReplacementManager();
        Iterator<Query> itr = cache.getCacheContentManager().getEntrySet().iterator();

        int i = 0;
        System.out.println("Cache contents: ");
        while(itr.hasNext()) {
            i++;
            Query q = itr.next();
            System.out.println("Query_" + i + " relation: " + q.getRelation());
        }



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

}
