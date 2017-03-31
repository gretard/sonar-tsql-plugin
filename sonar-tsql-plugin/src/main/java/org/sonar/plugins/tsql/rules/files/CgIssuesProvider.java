package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class CgIssuesProvider implements IReporsProvider {

	private static final String cgPath = "C:\\Program Files (x86)\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe";

	private static final String cgCommandLine = "\"%s\" -source \"%s\" -out \"%s\" -config \"%s\"";

	protected static final String CG_EXE = "sonar.tsql.cg.exe";

	private static final Logger LOGGER = Loggers.get(CgIssuesProvider.class);

	private final FileSystem fileSystem;

	private final TempFolder folder;

	private Settings settings;

	public CgIssuesProvider(final Settings settings, final FileSystem fileSystem, final TempFolder folder) {
		this.settings = settings;
		this.fileSystem = fileSystem;
		this.folder = folder;
	}

	
	private File[] getCGANalysisFiles() {
		return new CgIssuesFilesProvider(this.settings, this.fileSystem).get();
	}
	
	@Override
	public File[] get() {
		
		if (!new File(cgPath).exists()) {
			LOGGER.info(String.format("SQL Code guard not found at %s, trying to search directories instead", cgPath));
			return getCGANalysisFiles();
		}

		final File sourceDir = fileSystem.baseDir().toPath().toFile();
		final File configFile = folder.newFile("temp", "config.xml");
		final File tempResultsFile = folder.newFile("temp", "results.xml");
		try {
			FileUtils.copyURLToFile(getClass().getResource("/config/sqlcodeguardsettings.xml"), configFile);
		} catch (IOException e1) {
			LOGGER.warn("Was not able to copy sql code guard config settings, trying to search directories instead", e1);
			return getCGANalysisFiles();

		}

		final String command = String.format(cgCommandLine, cgPath, sourceDir.getAbsolutePath(),
				tempResultsFile.getAbsolutePath(), configFile.getAbsolutePath());

		try {
			final Process process = new ProcessBuilder(command).start();
			process.waitFor();

		} catch (Throwable e) {
			LOGGER.warn("Error executing SQL code guard tool, trying to search directories instead", e);
			return getCGANalysisFiles();
		}

		return new File[] { tempResultsFile };
	}

}
