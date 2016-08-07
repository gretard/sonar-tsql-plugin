package org.sonar.plugins.tsql.rules;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;

public class AbsolutePathCaseInsensitivePredicate implements FilePredicate {

	private final String path;

	public AbsolutePathCaseInsensitivePredicate(String path) {
		this.path = path.replace('\\', '/');
	}

	@Override
	public boolean apply(InputFile inputFile) {
		return this.path.equalsIgnoreCase(inputFile.absolutePath().replace('\\', '/'));
	}

}
