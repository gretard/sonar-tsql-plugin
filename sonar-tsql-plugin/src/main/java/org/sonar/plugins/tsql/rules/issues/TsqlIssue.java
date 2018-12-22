package org.sonar.plugins.tsql.rules.issues;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.plugins.tsql.predicates.AbsolutePathCaseInsensitivePredicate;

public class TsqlIssue {

	private String type;

	private String description;
	private boolean isExternal;
	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	private String filePath;
	private int line;
	private String repositoryKey;
	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public int getLine() {
		return line;
	}

	public void setLine(final int line) {
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
