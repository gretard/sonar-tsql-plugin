package org.sonar.plugins.tsql.rules.files;

import java.io.File;

public interface IReportsProvider {
	File[] get(String file);
}
