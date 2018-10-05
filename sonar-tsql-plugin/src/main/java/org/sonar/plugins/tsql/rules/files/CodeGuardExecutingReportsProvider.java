package org.sonar.plugins.tsql.rules.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public class CodeGuardExecutingReportsProvider implements IReportsProvider {

	private static final Logger LOGGER = Loggers.get(CodeGuardExecutingReportsProvider.class);

	private final TempFolder folder;

	private final Settings settings;
	private final FilesProvider filesProvide = new FilesProvider();

	public CodeGuardExecutingReportsProvider(final Settings settings, final TempFolder folder) {
		this.settings = settings;
		this.folder = folder;
	}

	private File[] getCGANalysisFiles(String baseDir) {
		return filesProvide.getFiles(Constants.CG_REPORT_FILE_DEFAULT_VALUE,
				this.settings.getString(Constants.CG_REPORT_FILE), baseDir);
	}

	@Override
	public File[] get(String baseDir) {
		final String cgPath = this.settings.getString(Constants.CG_APP_PATH);

		if (StringUtils.isEmpty(cgPath) || !new File(cgPath).exists()) {
			LOGGER.info(String.format("SQL Code guard path is empty, trying to search directories instead"));
			return getCGANalysisFiles(baseDir);
		}

		final File sourceDir = new File(baseDir);
		final File tempResultsFile = folder.newFile("temp", "results.xml");

		final String[] args = new String[] { cgPath, "-source", sourceDir.getAbsolutePath(), "-out",
				tempResultsFile.getAbsolutePath(), "/include:all" };
		try {

			LOGGER.debug(String.format("Running command: %s", String.join(" ", args)));

			final Process process = new ProcessBuilder(args).start();
			final int result = process.waitFor();
			if (result != 0 || new File(tempResultsFile.getAbsolutePath()).length() == 0) {
				LOGGER.warn("SQL Code Guard finished with errors: {}. Output was: {}", read(process.getErrorStream()),
						read(process.getInputStream()));
				read(process.getErrorStream());
				return getCGANalysisFiles(baseDir);
			}
			LOGGER.debug("Running command finished");
		} catch (final Throwable e) {
			LOGGER.warn("Error executing SQL code guard tool, trying to search directories instead", e);
			return getCGANalysisFiles(baseDir);
		}

		return new File[] { tempResultsFile };
	}

	protected static String read(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		String result = builder.toString();
		return result;
	}

}
