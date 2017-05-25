package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.sensors.HighlightingSensor;

public class HighlightingSensorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testAdd() throws Throwable {

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/TestTable.sql"), baseFile);

		DefaultFileSystem fs = new DefaultFileSystem(folder.getRoot());

		DefaultInputFile ti = new DefaultInputFile("test", "test.sql");
		ti.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));
		ti.setLanguage("tsql");
		fs.add(ti);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		ctxTester.setFileSystem(fs);
		System.out.println("AAAaa");
		HighlightingSensor sensor = new HighlightingSensor(new Settings());
		sensor.execute(ctxTester);
		/*
		 * CharStream stream = CharStreams.fromString("select 2"); tsqlLexer
		 * lexer = new tsqlLexer(stream); CommonTokenStream tokens = new
		 * CommonTokenStream(lexer); tsqlParser parser = new tsqlParser(tokens);
		 * Tsql_fileContext ctx = parser.tsql_file(); ParseTreeWalker walker =
		 * new ParseTreeWalker(); tsqlListener listener = new BaseListener();
		 * walker.walk(listener, ctx); System.out.println(ctx.getChildCount());
		 * for (ParseTree tree : ctx.children) { System.out.println("AA: " +
		 * tree + " " + tree.getText() + " " + tree.getChildCount()); }
		 */
	}

}
