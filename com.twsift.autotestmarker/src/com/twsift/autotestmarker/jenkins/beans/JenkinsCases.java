package com.twsift.autotestmarker.jenkins.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JenkinsCases
{
	private String className;
	private String errorDetails;
	private String errorStackTrace;
	private boolean skipped;
	private String skippedMessage;
	private String status;
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String name)
	{
		this.className = name;
	}

	public String getErrorDetails()
	{
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails)
	{
		this.errorDetails = errorDetails;
	}

	public String getErrorStackTrace()
	{
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace)
	{
		this.errorStackTrace = errorStackTrace;
	}

	public String getSkippedMessage()
	{
		return skippedMessage;
	}

	public void setSkippedMessage(String skippedMessage)
	{
		this.skippedMessage = skippedMessage;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public boolean isSkipped()
	{
		return skipped;
	}

	public void setSkipped(boolean skipped)
	{
		this.skipped = skipped;
	}
}
