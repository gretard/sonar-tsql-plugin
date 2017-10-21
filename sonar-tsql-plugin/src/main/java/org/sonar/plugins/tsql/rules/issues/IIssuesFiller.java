package org.sonar.plugins.tsql.rules.issues;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

public interface IIssuesFiller {
	void fill(String repositoryKey, SensorContext context, final InputFile file, TsqlIssue... issues);
}
