package com.twsift.autotestmarker.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.twsift.autotestmarker.Activator;
import com.twsift.autotestmarker.jenkins.JenkinsUtils;
import com.twsift.autotestmarker.jenkins.beans.JenkinsCases;
import com.twsift.autotestmarker.marker.MarkerHelper;
import com.twsift.autotestmarker.preferences.PreferenceConstants;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */

@SuppressWarnings("nls")
public class ButtonAction implements IWorkbenchWindowActionDelegate
{
	/**
	 * The constructor.
	 */
	public ButtonAction()
	{
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@Override
	public void run(IAction action)
	{
		JenkinsUtils jkUtils = new JenkinsUtils();
		List<JenkinsCases> badTests = (List<JenkinsCases>) jkUtils.getLatestTestData();

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
			.getProject(store.getString(PreferenceConstants.AUTOTEST_PROJECT));
		MarkerHelper markers = new MarkerHelper();
		markers.deleteAutotestMarkers(project);
		for( JenkinsCases testCase : badTests )
		{
			String absolutToPath = testCase.getClassName().replace(".", "/");
			IResource failedTest = project.findMember("src/" + absolutToPath + ".java");
			if( failedTest != null )
			{
				markers.addMarker(failedTest, testCase, ResourcesPlugin.getWorkspace());
			}
			else
			{
				System.out.println("RESOURCE NOT FOUND: " + absolutToPath);
			}
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose()
	{
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(IWorkbenchWindow window)
	{

	}
}