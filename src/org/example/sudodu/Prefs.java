package org.example.sudodu;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.PreferenceFragment;


public class Prefs extends PreferenceFragment {
	private static final String OPT_MUSIC = "music";
	private static final String OPT_HINTS = "hints";
	private static final boolean OPT_MUSIC_DEFAULT = true;
	private static final boolean OPT_HINTS_DEFAULT = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName("app_prefs");
		addPreferencesFromResource(R.xml.settings);
	}

	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEFAULT);
	}

	public static boolean getHints(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS, OPT_HINTS_DEFAULT);
	}
}