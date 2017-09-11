package org.sonar.plugins.tsql.rules.files;

import java.io.File;

public interface IReporsProvider {
	File[] get(String file);
}
