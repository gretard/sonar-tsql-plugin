package org.sonar.plugins.tsql.rules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sonar.api.batch.fs.FilePredicate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TsqlIssue {

	@XmlElement(name = "Rule")
	private String type;

	@XmlElement(name = "ProblemDescription")
	private String description;

	@XmlElement(name = "SourceFile")
	private String filePath;

	@XmlElement(name = "Line")
	private int line;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return this.getType() + " " + this.getDescription() + " " + this.getFilePath() + " " + this.getLine();
	}

	public FilePredicate getPredicate() {
		return new AbsolutePathCaseInsensitivePredicate(this.getFilePath());
	}
}
