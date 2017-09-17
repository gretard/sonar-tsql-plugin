package org.sonar.plugins.tsql.rules.parsers;

import java.io.File;

public interface IIssuesParser<T> {
	T[] parse(final File file);
}
