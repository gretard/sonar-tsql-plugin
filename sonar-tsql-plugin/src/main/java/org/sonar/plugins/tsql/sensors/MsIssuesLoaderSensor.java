package org.sonar.plugins.tsql.sensors;

import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.issues.MsIssuesProvider;

public class MsIssuesLoaderSensor extends BaseTsqlExternalSensor {

	public MsIssuesLoaderSensor(final Settings settings) {
		super(new MsIssuesProvider(settings), Constants.PLUGIN_SKIP_MS, Constants.MS_REPO_KEY);
	}
}
