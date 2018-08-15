package org.sonar.plugins.tsql.coverage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.opencover.CoverageSession;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.files.BaseReportsProvider;
import org.sonar.plugins.tsql.rules.files.IReporsProvider;

public class SqlCoverCoverageProvider implements ICoveragProvider {
	private final Settings settings;
	private FileSystem fileSystem;

	private static final Logger LOGGER = Loggers.get(SqlCoverCoverageProvider.class);

	public SqlCoverCoverageProvider(final Settings settings, final FileSystem fileSystem) {
		this.settings = settings;
		this.fileSystem = fileSystem;

	}

	private static List<org.opencover.Class> read(final InputStream stream) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(CoverageSession.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final CoverageSession coverageSession = (CoverageSession) jaxbUnmarshaller.unmarshal(stream);
		final List<org.opencover.Class> data = coverageSession.getModules().getModule().getClasses().getClazz();
		return data;
	}

	@Override
	public Map<String, HitLines> getHitLines() {
		final Map<String, HitLines> lines = new HashMap<>();
		final String prefix = settings.getString(Constants.COVERAGE_FILE);
		String coverageFile = prefix;
		File temp = new File(coverageFile);
		// try find coverage file
		if (temp == null || !temp.exists()) {

			final String baseDir = fileSystem.baseDir().getAbsolutePath();
			final IReporsProvider reportsProvider = new BaseReportsProvider(prefix);
			final File[] files = reportsProvider.get(baseDir);
			if (files.length != 1) {
				LOGGER.warn("Found not 1, but {} coverage files matching {} path at {}", files.length, prefix, baseDir);
				return lines;
			}
			coverageFile = files[0].getAbsolutePath();
		}

		try (final FileInputStream stream = new FileInputStream(coverageFile)) {
			final List<org.opencover.Class> data = read(stream);
			for (final org.opencover.Class classz : data) {
				final HitLines.LineInfo[] hitLines = classz.getMethods().getMethod().getSequencePoints()
						.getSequencePoint().stream().map(point -> new HitLines.LineInfo(point.getSl(), point.getVc()))
						.toArray(HitLines.LineInfo[]::new);
				final String fileName = classz.getFullName();
				lines.put(fileName, new HitLines(fileName, hitLines));
			}
		} catch (Throwable e) {
			LOGGER.warn("Error occured while reading coverage file {}", coverageFile);
		}
		return lines;
	}

}
