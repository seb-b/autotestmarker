package com.twsift.autotestmarker.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.twsift.autotestmarker.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class JenkinsPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public JenkinsPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for the autotest marker plugin");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.JENKINS_URL, "&Jenkins URL:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.JENKINS_JOB, "&Job:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.JENKINS_USERNAME, "&Username:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.JENKINS_KEY, "&API Key:", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.AUTOTEST_PROJECT, "&Autotest Project Name:",
			getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}
	
}