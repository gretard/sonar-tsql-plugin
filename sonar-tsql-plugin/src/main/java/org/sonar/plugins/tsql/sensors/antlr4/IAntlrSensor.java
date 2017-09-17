package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;

public interface IAntlrSensor {
	void work(org.sonar.api.batch.sensor.SensorContext context, CommonTokenStream stream,
			InputFile file);
}
