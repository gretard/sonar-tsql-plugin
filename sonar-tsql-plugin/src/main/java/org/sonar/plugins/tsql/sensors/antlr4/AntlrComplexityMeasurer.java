package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.tsql.TSqlParser.Select_statementContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class AntlrComplexityMeasurer implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrComplexityMeasurer.class);
	boolean debugEnabled = LOGGER.isDebugEnabled();

	@Override
	public void work(SensorContext context, AntrlFile antrlFile) {

		final InputFile file = antrlFile.getFile();
		int cmp = 0;
		final TSqlParser parser = new TSqlParser(antrlFile.getStream());

		// parser.removeErrorListeners();
		final ParseTree root = parser.tsql_file();
		new ComplVisitor().visit(root);
		root.accept(new ComplVisitor());
		synchronized (context) {
			try {
				context.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMPLEXITY).withValue(cmp).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding nloc measures on file %s", file.absolutePath()), e);
			}

		}
	}

	public static class ComplVisitor extends AbstractParseTreeVisitor<Object> {
		int c = 0;

		@Override
		public Object visit(final ParseTree tree) {
			System.out.println(tree.getClass());
			final int n = tree.getChildCount();
			System.out.println("AA: " + tree.getText());
			System.out.println(n);
			for (int i = 0; i < n; i++) {
				final ParseTree c = tree.getChild(i);
				visit(c);
			}
			if (Select_statementContext.class.equals(tree.getClass())) {

			}
			return null;

		}
	}

}
