package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class FilesProvider {
	private static final Logger LOGGER = Loggers.get(FilesProvider.class);

	public File[] getFiles(String prefix, String actualValue, String baseDir) {
		final List<File> foundFiles = new LinkedList<>();
		try {
			final File temp = new File(actualValue);
			// actual file specified
			if (temp.exists() && temp.isFile()) {
				foundFiles.add(temp);
			}

			// directory specified

			if (temp.exists() && temp.isDirectory()) {
				final IReportsProvider reportsProvider = new BaseReportsProvider(prefix);
				final File[] files = reportsProvider.get(temp.getAbsolutePath());
				foundFiles.addAll(Arrays.asList(files).stream().filter(File::isFile).collect(Collectors.toList()));
			}

			// relative or search path specified
			if (!temp.exists()) {
				final IReportsProvider reportsProvider = new BaseReportsProvider(actualValue);
				final File[] files = reportsProvider.get(baseDir);
				foundFiles.addAll(Arrays.asList(files).stream().filter(File::isFile).collect(Collectors.toList()));
			}

		} catch (final Throwable e) {

			LOGGER.warn(
					"Unexpected error was thrown while searching for files: prefix {} actualValue {} baseDir {}. Exception was: {} ",
					prefix, actualValue, baseDir, e);
		}
		return foundFiles.stream().distinct().collect(Collectors.toList()).toArray(new File[0]);
	}

}
