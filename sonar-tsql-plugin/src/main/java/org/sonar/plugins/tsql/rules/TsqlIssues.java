package org.sonar.plugins.tsql.rules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Problems")
@XmlAccessorType(XmlAccessType.FIELD)
public class TsqlIssues {
	public TsqlIssue[] getIssues() {
		return issues;
	}

	public void setIssues(TsqlIssue[] issues) {
		this.issues = issues;
	}

	@XmlElement(name = "Problem")
	private TsqlIssue[] issues;
}
