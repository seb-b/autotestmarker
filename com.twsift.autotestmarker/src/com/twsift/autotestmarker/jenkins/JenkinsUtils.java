package com.twsift.autotestmarker.jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.jface.preference.IPreferenceStore;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twsift.autotestmarker.Activator;
import com.twsift.autotestmarker.jenkins.beans.JenkinsCases;
import com.twsift.autotestmarker.jenkins.beans.JenkinsSuites;
import com.twsift.autotestmarker.jenkins.beans.JenkinsTestReport;
import com.twsift.autotestmarker.preferences.PreferenceConstants;

@SuppressWarnings("nls")
public class JenkinsUtils
{
	private static String jenkinsUrl;
	private static String jobName;
	private String reportEndPoint = "/lastSuccessfulBuild/testReport/api/json?pretty=true";
	private static String screenShotEndpoint = "/lastSuccessfulBuild/artifact/Tests/results/screenshots/";
	private String username;
	private String password;

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	static
	{
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public JenkinsUtils()
	{
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		jenkinsUrl = store.getString(PreferenceConstants.JENKINS_URL);
		jobName = store.getString(PreferenceConstants.JENKINS_JOB);
		username = store.getString(PreferenceConstants.JENKINS_USERNAME);
		password = store.getString(PreferenceConstants.JENKINS_KEY);
	}

	// Returns bad cases
	public Collection<JenkinsCases> getLatestTestData()
	{
		// Create your httpclient
		DefaultHttpClient client = new DefaultHttpClient();

		// Then provide the right credentials
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
			new UsernamePasswordCredentials(username, password));
		// Generate BASIC scheme object and stick it to the execution context
		BasicScheme basicAuth = new BasicScheme();
		BasicHttpContext context = new BasicHttpContext();
		context.setAttribute("preemptive-auth", basicAuth);

		// Add as the first (because of the zero) request interceptor
		// It will first intercept the request and preemptively initialize the
		// authentication scheme if there is not
		client.addRequestInterceptor(new PreemptiveAuth(), 0);

		// You get request that will start the build

		String getUrl = jenkinsUrl + "job/" + jobName + reportEndPoint;
		HttpGet get = new HttpGet(getUrl);

		try
		{
			// Execute your request with the given context
			HttpResponse response = client.execute(get, context);
			HttpEntity entity = response.getEntity();
			return convertResponse(EntityUtils.toString(entity));
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return null;
		}
	}


	public static String getScreenshotUrl(JenkinsCases testCase)
	{
		String screenshotFilename = testCase.getClassName() + "_" + testCase.getName() + ".png";
		screenshotFilename = screenshotFilename.replaceFirst("@.+\\s", "");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(jenkinsUrl);
		stringBuilder.append("/job/");
		stringBuilder.append(jobName);
		stringBuilder.append(screenShotEndpoint);
		stringBuilder.append(screenshotFilename);
		return stringBuilder.toString();
	}


	@SuppressWarnings("nls")
	private Collection<JenkinsCases> convertResponse(String response)
	{
		try
		{
			JenkinsTestReport testReport = jsonMapper.readValue(response, JenkinsTestReport.class);
			ArrayList<JenkinsCases> amalgamatedCases = new ArrayList<JenkinsCases>();
			for( JenkinsSuites suite : testReport.getSuites() )
			{
				amalgamatedCases.addAll(suite.getCases());
			}
			ArrayList<JenkinsCases> cases = new ArrayList<JenkinsCases>();
			for( JenkinsCases testCase : amalgamatedCases )
			{
				if( testCase.getStatus().equals("FAILED") || testCase.getStatus().equals("REGRESSION") )
				{
					cases.add(testCase);
				}
			}
			return cases;

		}
		catch( IOException e )
		{

			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Preemptive authentication interceptor.
	 *  This is rubbish but it works
	 */
	static class PreemptiveAuth implements HttpRequestInterceptor
	{

		/*
		 * (non-Javadoc)
		 * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.
		 * HttpRequest, org.apache.http.protocol.HttpContext)
		 */
		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
		{
			// Get the AuthState
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

			// If no auth scheme available yet, try to initialize it
			// preemptively
			if( authState.getAuthScheme() == null )
			{
				AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if( authScheme != null )
				{
					Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost
						.getPort()));
					if( creds == null )
					{
						throw new HttpException("No credentials for preemptive authentication");
					}
					authState.setAuthScheme(authScheme);
					authState.setCredentials(creds);
				}
			}

		}

	}
}
