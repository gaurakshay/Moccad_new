package edu.ou.cs.moccad_new;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;
import java.util.Set;

import edu.ou.cs.cacheprototypelibrary.connection.DataAccessProvider;

public class SettingsActivity extends Activity
{
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    //public static final String PREFERENCES_FILE_NAME = "preferences.xml";
    public static final String KEY_PREF_MAX_QUERY_CACHE_SIZE = "pref_max_query_cache_size";
    public static final String KEY_PREF_IMPORTANT_PARAMETER = "pref_important_parameter";
    public static final String KEY_PREF_TIME_CONSTRAINT = "pref_time_contraint";
    public static final String KEY_PREF_MONEY_CONSTRAINT = "pref_money_contraint";
    public static final String KEY_PREF_ENERGY_CONSTRAINT = "pref_energy_contraint";
    public static final String KEY_PREF_MAX_MOBILE_ESTIMATION_CACHE_SIZE = "pref_max_mobile_estimation_cache_size";
    public static final String KEY_PREF_MAX_CLOUD_ESTIMATION_CACHE_SIZE = "pref_max_cloud_estimation_cache_size";
    public static final String KEY_PREF_MAX_QUERY_CACHE_NUMBER_SEGMENT = "pref_max_query_cache_number_segment";
    public static final String KEY_PREF_MAX_MOBILE_ESTIMATION_CACHE_NUMBER_SEGMENT = "pref_max_mobile_estimation_cache_number_segment";
    public static final String KEY_PREF_MAX_CLOUD_ESTIMATION_CACHE_NUMBER_SEGMENT = "pref_max_cloud_estimation_cache_number_segment";
    public static final String KEY_PREF_DATA_ACCESS_PROVIDER = "pref_data_access_provider";
    public static final String KEY_PREF_CACHE_TYPE = "pref_cache_type";
    public static final String KEY_PREF_IP_ADDRESS = "pref_ip_address";
    public static final String KEY_PREF_PORT = "pref_port";
    public static final String KEY_PREF_NB_QUERIES_TO_PROCESS = "pref_nb_queries_to_process";
    public static final String KEY_PREF_USE_REPLACEMENT = "pref_use_replacement";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


}