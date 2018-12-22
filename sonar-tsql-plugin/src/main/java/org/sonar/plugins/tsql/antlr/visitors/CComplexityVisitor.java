package org.sonar.plugins.tsql.antlr.visitors;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser.Search_condition_notContext;
import org.antlr.tsql.TSqlParser.Try_catch_statementContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr.AntlrContext;

public class CComplexityVisitor implements IParseTreeItemVisitor {
	int complexity = 1;

	public int getMeasure() {
		return complexity;
	}

	private static final Logger LOGGER = Loggers.get(CComplexityVisitor.class);

	@Override
	public void apply(final ParseTree tree) {
		final Class<? extends ParseTree> classz = tree.getClass();
		if (Search_condition_notContext.class.equals(classz)) {
			complexity++;
		}

		if (Try_catch_statementContext.class.equals(classz)) {
			complexity++;
		}

	}

	@Override
	public void fillContext(SensorContext sensorContext, AntlrContext antrlContext) {

		final InputFile file = antrlContext.getFile();
		synchronized (sensorContext) {
			try {
				sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMPLEXITY).withValue(complexity)
						.save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding complexity measures on file %s", file.absolutePath()), e);
			}
		}

	}

}
