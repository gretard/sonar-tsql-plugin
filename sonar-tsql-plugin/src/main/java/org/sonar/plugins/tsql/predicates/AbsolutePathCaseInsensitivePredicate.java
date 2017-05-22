package org.sonar.plugins.tsql.predicates;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;

public class AbsolutePathCaseInsensitivePredicate implements FilePredicate {

	private final String path;

	public AbsolutePathCaseInsensitivePredicate(final String path) {
		this.path = path.replace('\\', '/');
	}

	@Override
	public boolean apply(final InputFile inputFile) {
		return this.path.equalsIgnoreCase(inputFile.absolutePath().replace('\\', '/'));
	}

}
