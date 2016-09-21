package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.List;

public interface IReporsProvider {
	List<File> get();
}
