package org.sonar.plugins.tsql.antlr.visitors;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;

public interface ISensorFiller {
	void fill(final SensorContext sensorContext, final FillerRequest fillerRequest);
}
