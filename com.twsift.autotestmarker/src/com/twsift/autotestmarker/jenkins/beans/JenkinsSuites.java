package com.twsift.autotestmarker.jenkins.beans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JenkinsSuites
{
	Collection<JenkinsCases> cases;

	public Collection<JenkinsCases> getCases()
	{
		return cases;
	}

	public void setCases(Collection<JenkinsCases> cases)
	{
		this.cases = cases;
	}
}
