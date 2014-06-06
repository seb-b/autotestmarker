package com.twsift.autotestmarker.marker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;

import com.twsift.autotestmarker.jenkins.JenkinsUtils;
import com.twsift.autotestmarker.jenkins.beans.JenkinsCases;

@SuppressWarnings("nls")
public class MarkerHelper
{

	public static String MARKER_ID = "com.tswift.autotestmarker.id";
	private static String STACKTRACE = "stacktrace";

	public void addMarker(IResource resource, JenkinsCases testCase, IWorkspace workspace)
	{
		try
		{
			IMarker marker = resource.createMarker(MARKER_ID);
			marker.setAttribute(IMarker.LOCATION, testCase.getName());
			marker.setAttribute(STACKTRACE, testCase.getErrorStackTrace());
			marker.setAttribute(
				IMarker.MESSAGE,
				"Autotest Failed\nScreenshot: " + JenkinsUtils.getScreenshotUrl(testCase) + "\nStackTrace:"
					+ testCase.getErrorStackTrace());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);

			IFile file = workspace.getRoot().getFile(resource.getFullPath());
			String contentString = convertStreamToString(file.getContents());
			if( contentString.indexOf(testCase.getName()) > 0 )
			{
				marker.setAttribute(IMarker.CHAR_START, contentString.indexOf(testCase.getName()));
				marker.setAttribute(IMarker.CHAR_END, contentString.indexOf(testCase.getName())
					+ testCase.getName().length());
			}
			else
			{
				// look for class name instead
				String classString = resource.getName().replace("." + resource.getFileExtension(), "");
				marker.setAttribute(IMarker.CHAR_START, contentString.indexOf(classString));
				marker.setAttribute(IMarker.CHAR_END, contentString.indexOf(classString) + classString.length());
			}
		}
		catch( IOException | CoreException e )
		{
			e.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream contents) throws IOException
	{
		Scanner s = new Scanner(contents).useDelimiter("\\A");
		String reurnString = s.hasNext() ? s.next() : "";
		s.close();
		contents.close();
		return reurnString;
	}

	public void deleteAutotestMarkers(IResource resource)
	{

		for( IMarker marker : findAutotestMarkers(resource, true) )
		{
			try
			{
				marker.delete();
			}
			catch( CoreException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static IMarker[] findAutotestMarkers(IResource resource, boolean recursive)
	{
		IMarker[] markers = {};
		try
		{
			markers = resource.findMarkers(MARKER_ID, true, recursive ? IResource.DEPTH_INFINITE : IResource.DEPTH_ONE);
		}
		catch( CoreException e )
		{
			e.printStackTrace();
		}
		return markers;
	}

}
