package org.sonar.plugins.tsql.sensors.antlr4;

public interface IAntlrFiller {
	void fill(org.sonar.api.batch.sensor.SensorContext context, FillerRequest file);
}
