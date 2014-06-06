package com.twsift.autotestmarker.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.twsift.autotestmarker.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.JENKINS_URL, "http://dev-builds.equella.com/");
		store.setDefault(PreferenceConstants.JENKINS_JOB, "EQUELLA-AutomatedTests%20-%20Local");
		store.setDefault(PreferenceConstants.AUTOTEST_PROJECT, "TLE Automated Tests");
	}

}
