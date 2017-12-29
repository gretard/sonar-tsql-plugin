package org.sonar.plugins.tsql.lines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class SourceLinesProvider {
	private static final Logger LOGGER = Loggers.get(SourceLinesProvider.class);

	public SourceLine[] getLines(final InputStream file, final Charset charset) {
		final List<SourceLine> sourceLines = new ArrayList<>();

		try {
			final char bom = '\ufeff';
			int totalLines = 1;
			int global = 0;
			int count = 0;
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file, charset));
			int currentChar;
			while ((currentChar = bufferedReader.read()) != -1) {
				if (currentChar == bom) {
					continue;
				}
				global++;
				count++;
				if (currentChar == 10) {
					sourceLines.add(new SourceLine(totalLines, count, global - count, global));
					totalLines++;
					count = 0;
				}

			}
			sourceLines.add(new SourceLine(totalLines, count, global - count, global));

			file.close();
			bufferedReader.close();

		} catch (final Throwable e) {
			LOGGER.warn("Error occured reading file", e);
		}

		return sourceLines.toArray(new SourceLine[0]);
	}

}
