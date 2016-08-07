package org.sonar.plugins.tsql.rules;

import java.io.File;
import java.util.List;

public interface IReporsProvider {
	List<File> get(String baseDir);
}
