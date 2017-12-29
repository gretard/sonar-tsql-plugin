package org.sonar.plugins.tsql.antlr.visitors;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser.Dml_clauseContext;
import org.antlr.tsql.TSqlParser.Function_callContext;
import org.antlr.tsql.TSqlParser.Join_partContext;
import org.antlr.tsql.TSqlParser.Order_by_expressionContext;
import org.antlr.tsql.TSqlParser.Search_condition_notContext;
import org.antlr.tsql.TSqlParser.Select_list_elemContext;
import org.antlr.tsql.TSqlParser.Sql_unionContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.metrics.TSQLMetrics;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;

public class ComplexityVisitor implements IParseTreeItemVisitor, ISensorFiller {
	int complexity = 0;

	private static final Logger LOGGER = Loggers.get(ComplexityVisitor.class);

	@Override
	public void visit(final ParseTree tree) {
		final Class<? extends ParseTree> classz = tree.getClass();
		if (Sql_unionContext.class.equals(classz)) {
			this.complexity++;
		}
		if (Function_callContext.class.isAssignableFrom(classz)) {
			this.complexity++;
		}
		if (Join_partContext.class.equals(classz)) {
			this.complexity++;
		}
		if (Order_by_expressionContext.class.equals(classz)) {
			this.complexity++;
		}
		if (Select_list_elemContext.class.equals(classz)) {
			this.complexity++;
		}
		if (Search_condition_notContext.class.equals(classz)) {
			this.complexity++;
		}
		if (Dml_clauseContext.class.equals(classz)) {
			this.complexity++;
		}

	}

	@Override
	public void fill(SensorContext sensorContext, FillerRequest fillerRequest) {
		final InputFile file = fillerRequest.getFile();
		synchronized (sensorContext) {
			try {
				sensorContext.<Integer>newMeasure().on(file).forMetric(TSQLMetrics.CODE_COMPLEXITY)
						.withValue(complexity).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding cyclomatic complexity measures on file %s", file.absolutePath()),
						e);
			}
		}

	}

}
