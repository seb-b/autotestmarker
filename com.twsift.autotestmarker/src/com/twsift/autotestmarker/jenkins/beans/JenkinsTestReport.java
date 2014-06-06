package com.twsift.autotestmarker.jenkins.beans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JenkinsTestReport
{
	private String failCount;
	private String skipCount;
	private String passCount;
	private Collection<JenkinsSuites> suites;

	public Collection<JenkinsSuites> getSuites()
	{
		return suites;
	}

	public void setSuites(Collection<JenkinsSuites> suites)
	{
		this.suites = suites;
	}

	public String getFailCount()
	{
		return failCount;
	}

	public void setFailCount(String failCount)
	{
		this.failCount = failCount;
	}

	public String getSkipCount()
	{
		return skipCount;
	}

	public void setSkipCount(String skipCount)
	{
		this.skipCount = skipCount;
	}

	public String getPassCount()
	{
		return passCount;
	}

	public void setPassCount(String passCount)
	{
		this.passCount = passCount;
	}

}
