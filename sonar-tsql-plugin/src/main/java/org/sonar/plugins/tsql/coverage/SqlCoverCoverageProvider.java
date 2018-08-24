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
import org.sonar.plugins.tsql.rules.files.FilesProvider;

public class SqlCoverCoverageProvider implements ICoveragProvider {

	private static final Logger LOGGER = Loggers.get(SqlCoverCoverageProvider.class);

	private final Settings settings;

	private final FileSystem fileSystem;

	private final NameNormalizer nameNormalizer = new NameNormalizer();
	private final FilesProvider filesProvider = new FilesProvider();

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
	public Map<String, CoveredLinesReport> getHitLines() {
		final Map<String, CoveredLinesReport> lines = new HashMap<>();
		final String specifiedValue = settings.getString(Constants.COVERAGE_FILE);
		final File[] files = filesProvider.getFiles(Constants.COVERAGE_FILE_DEFAULT_VALUE, specifiedValue,
				fileSystem.baseDir().getAbsolutePath());
		if (files.length > 1) {
			LOGGER.info("Found multiple coverage files at  {}. Try to specify absolute path.", specifiedValue);
			return lines;
		}
		if (files.length == 0) {
			LOGGER.debug("Did not find any coverage files at {}", specifiedValue);
			return lines;
		}
		final String coverageFile = files[0].getAbsolutePath();

		LOGGER.debug("Found coverage file at {}", coverageFile);
		try (final FileInputStream stream = new FileInputStream(coverageFile)) {
			final List<org.opencover.Class> data = read(stream);
			for (final org.opencover.Class classz : data) {
				final CoveredLinesReport.LineInfo[] hitLines = classz.getMethods().getMethod().getSequencePoints()
						.getSequencePoint().stream()
						.map(point -> new CoveredLinesReport.LineInfo(point.getSl(), point.getVc()))
						.toArray(CoveredLinesReport.LineInfo[]::new);
				final String fileName = classz.getFullName();
				lines.put(nameNormalizer.normalize(fileName), new CoveredLinesReport(fileName, hitLines));
			}
		} catch (Throwable e) {
			LOGGER.warn("Unexpected error occured while reading coverage file at {}", coverageFile);
		}
		return lines;
	}

}
