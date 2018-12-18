package org.sonar.plugins.tsql.sensors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.tsql.antlr.PluginHelper;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class MainSensor extends BaseTsqlSensor {

	public MainSensor() {
		super("test");
	}

	@Override
	protected void innerExecute(SensorContext context) {
		final ExecutorService executorService = Executors.newWorkStealingPool();

		final org.sonar.api.batch.fs.FileSystem fs = context.fileSystem();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
		files.forEach(f -> {
			if (fileTooLarge()) {
				return;
			}
			executorService.execute(() -> {
				try {
					ParseTree root = PluginHelper.read(f, Charset.defaultCharset());
					CustomTreeVisitor visitor = new CustomTreeVisitor();
					visitor.visit(root);
					visitor.fill(context, root, f);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		});
	}

	private static boolean fileTooLarge() {
		return false;
	}

	private static void analyzeFile(InputFile file) {

	}
}
