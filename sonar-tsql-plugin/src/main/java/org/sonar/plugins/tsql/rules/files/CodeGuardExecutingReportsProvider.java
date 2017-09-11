package org.sonar.plugins.tsql.rules.files;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public class CodeGuardExecutingReportsProvider implements IReporsProvider {

	private static final String cgCommandLine = "\"%s\" -source \"%s\" -out \"%s\" -config \"%s\"";

	private static final Logger LOGGER = Loggers.get(CodeGuardExecutingReportsProvider.class);

	private final TempFolder folder;

	private Settings settings;

	public CodeGuardExecutingReportsProvider(final Settings settings, final TempFolder folder) {
		this.settings = settings;
		this.folder = folder;
	}

	private File[] getCGANalysisFiles(String file) {
		return new CodeGuardIssuesFilesProvider(this.settings).get(file);
	}

	@Override
	public File[] get(String baseDir) {
		final String cgPath = this.settings.getString(Constants.CG_APP_PATH);

		if (StringUtils.isEmpty(cgPath) || !new File(cgPath).exists()) {
			LOGGER.info(String.format("SQL Code guard path is empty, trying to search directories instead"));
			return getCGANalysisFiles(baseDir);
		}

		if (!new File(cgPath).exists()) {
			LOGGER.info(String.format("SQL Code guard not found at %s, trying to search directories instead", cgPath));
			return getCGANalysisFiles(baseDir);
		}

		final File sourceDir = new File(baseDir);
		final File configFile = folder.newFile("temp", "config.xml");
		final File tempResultsFile = folder.newFile("temp", "results.xml");
		try {
			FileUtils.copyURLToFile(getClass().getResource("/config/sqlcodeguardsettings.xml"), configFile);
		} catch (final Throwable e1) {
			LOGGER.warn("Was not able to copy sql code guard config settings, trying to search directories instead",
					e1);
			return getCGANalysisFiles(baseDir);

		}

		final String command = String.format(cgCommandLine, cgPath, sourceDir.getAbsolutePath(),
				tempResultsFile.getAbsolutePath(), configFile.getAbsolutePath());

		try {
			LOGGER.debug(String.format("Running command: %s", command));
			final Process process = new ProcessBuilder(command).start();
			process.waitFor();
			LOGGER.debug("Running command finished");
		} catch (final Throwable e) {
			LOGGER.warn("Error executing SQL code guard tool, trying to search directories instead", e);
			return getCGANalysisFiles(baseDir);
		}

		return new File[] { tempResultsFile };
	}

}
