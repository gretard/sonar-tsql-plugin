package org.sonar.plugins.tsql.sensors;

import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public abstract class BaseTsqlSensor implements org.sonar.api.batch.sensor.Sensor {

	protected static final Logger LOGGER = Loggers.get(BaseTsqlSensor.class);
	private final String sensorName;

	public BaseTsqlSensor(final String sensorName) {
		this.sensorName = sensorName;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

	@Override
	public void execute(final org.sonar.api.batch.sensor.SensorContext context) {

		final Settings settings = context.settings();
		final boolean skipAnalysis = settings.getBoolean(Constants.PLUGIN_SKIP);

		if (skipAnalysis) {
			LOGGER.debug(String.format("Skipping plugin as skip flag is set: %s", Constants.PLUGIN_SKIP));
			return;
		}
		final boolean skipSensor = settings.getBoolean(sensorName);

		if (skipSensor) {
			LOGGER.debug(String.format("Skipping sensor as skip flag is set: %s", sensorName));
			return;
		}
		innerExecute(context);

	}

	protected abstract void innerExecute(final org.sonar.api.batch.sensor.SensorContext context);
}
