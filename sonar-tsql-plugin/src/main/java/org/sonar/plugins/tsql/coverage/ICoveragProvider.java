package org.sonar.plugins.tsql.coverage;

import java.util.Map;

public interface ICoveragProvider {
	Map<String, CoveredLinesReport> getHitLines();
}
