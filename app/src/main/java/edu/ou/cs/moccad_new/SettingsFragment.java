package edu.ou.cs.moccad_new;

import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import edu.ou.cs.cacheprototypelibrary.connection.DataAccessProvider;

/**
 * Created by Ryan K. on 5/1/2016.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MOCCAD mApplication = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mApplication = (MOCCAD) getActivity().getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        initSummary(getPreferenceScreen());
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void initSummary(Preference p)
    {
        if(p instanceof PreferenceGroup)
        {
            PreferenceGroup pGroup = (PreferenceGroup) p;
            for(int i = 0; i < pGroup.getPreferenceCount(); i++)
            {
                initSummary(pGroup.getPreference(i));
            }
        }
        else
        {
            updatePreferenceSummary(p);
        }
    }

    public void updatePreferenceSummary(Preference p)
    {
        if(p instanceof EditTextPreference)
        {
            EditTextPreference editTextPref = (EditTextPreference) p;
            String key = p.getKey();
            if(key.equals(SettingsActivity.KEY_PREF_IP_ADDRESS))
            {
                p.setSummary(editTextPref.getText());
            }
            else if(key.equals(SettingsActivity.KEY_PREF_PORT))
            {
                p.setSummary(editTextPref.getText());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        //Do different things here depending on whether it is a ListPreference (select an item from a list)
        // an EditTextPreference (entering in your own IP address, for example), or a CheckBoxPreference
        if (pref instanceof ListPreference)
        {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());

            if (key.equals(SettingsActivity.KEY_PREF_DATA_ACCESS_PROVIDER))
            {
                int currentValue = Integer.parseInt(sharedPreferences.getString(key, "0"));
                if (currentValue != 0)
                {
                    //StatisticsManager.createFileWriter();
                    //update DataAccessProvider
                    //(new SetDataAccessProviderTask()).execute();

                    System.out.println("DEBUG: Data access provider set to value: " + currentValue);
                }
            }
            else if (key.equals(SettingsActivity.KEY_PREF_MAX_QUERY_CACHE_NUMBER_SEGMENT))
            {
                if (mApplication.getQueryCache() != null)
                {
                    int currentValue = Integer.parseInt(sharedPreferences.getString(key, "0"));
                    if (currentValue == 0)
                    {
                        mApplication.getQueryCache().setMaxCount(Integer.MAX_VALUE);
                    }
                    else
                    {
                        mApplication.getQueryCache().setMaxCount(currentValue);
                    }
                }
            }
            else if(key.equals(SettingsActivity.KEY_PREF_MAX_QUERY_CACHE_SIZE))
            {
                if(mApplication.getQueryCache() != null)
                {
                    int currentValue = Integer.parseInt(sharedPreferences.getString(key, "100000000"));
                    mApplication.getQueryCache().setMaxSize(currentValue);
                }
            }
        }

        if(pref instanceof EditTextPreference)
        {
            if(key.equals(SettingsActivity.KEY_PREF_IP_ADDRESS)
                    || key.equals(SettingsActivity.KEY_PREF_PORT))
            {
                SharedPreferences settings = getActivity().getSharedPreferences(DataAccessProvider.PREF_METADATA, 0);
                if(settings.contains(DataAccessProvider.PREF_METADATA))
                {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove(DataAccessProvider.PREF_METADATA);
                    editor.commit();
                }

                updatePreferenceSummary(findPreference(key));

                //(new SetDataAccessProviderTask()).execute();
                System.out.println("DEBUG: Preferences for data access provider updated!");
            }
        }

        if(pref instanceof CheckBoxPreference)
        {
            if(key.equals(SettingsActivity.KEY_PREF_USE_REPLACEMENT))
            {
                boolean useReplacement = sharedPreferences.getBoolean(key, true);
                mApplication.setUseReplacement(useReplacement);
            }
        }
    }
        /*
        private class SetCacheTypeTask extends AsyncTask<Void, Void, Void>
        {
            ProgressDialog mProgressDialog = null;

            @Override
            protected void onPreExecute() {
                mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.set_cache_type_message),false);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                ((MOCCAD) getActivity().getApplication()).setCacheManager();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mProgressDialog.dismiss();
                super.onPostExecute(result);
            }

        }
        */
}