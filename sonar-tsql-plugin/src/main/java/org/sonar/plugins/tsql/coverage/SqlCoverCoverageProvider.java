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
import org.sonar.plugins.tsql.rules.files.IReportsProvider;

public class SqlCoverCoverageProvider implements ICoveragProvider {

	private static final Logger LOGGER = Loggers.get(SqlCoverCoverageProvider.class);

	private final Settings settings;

	private final FileSystem fileSystem;

	private final NameNormalizer nameNormalizer = new NameNormalizer();

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
		final String prefix = settings.getString(Constants.COVERAGE_FILE);
		String coverageFile = prefix;
		File temp = new File(coverageFile);
		// try find coverage file
		if (temp == null || !temp.exists()) {
			final String baseDir = fileSystem.baseDir().getAbsolutePath();
			final IReportsProvider reportsProvider = new BaseReportsProvider(prefix);
			final File[] files = reportsProvider.get(baseDir);
			if (files.length != 1) {
				LOGGER.info("Found not 1, but {} coverage files matching {} path at {}", files.length, prefix, baseDir);
				return lines;
			}
			coverageFile = files[0].getAbsolutePath();
		}

		try (final FileInputStream stream = new FileInputStream(coverageFile)) {
			final List<org.opencover.Class> data = read(stream);
			for (final org.opencover.Class classz : data) {
				final CoveredLinesReport.LineInfo[] hitLines = classz.getMethods().getMethod().getSequencePoints()
						.getSequencePoint().stream().map(point -> new CoveredLinesReport.LineInfo(point.getSl(), point.getVc()))
						.toArray(CoveredLinesReport.LineInfo[]::new);
				final String fileName = classz.getFullName();
				lines.put(nameNormalizer.normalize(fileName), new CoveredLinesReport(fileName, hitLines));
			}
		} catch (Throwable e) {
			LOGGER.warn("Error occured while reading coverage file {}", coverageFile);
		}
		return lines;
	}

}
