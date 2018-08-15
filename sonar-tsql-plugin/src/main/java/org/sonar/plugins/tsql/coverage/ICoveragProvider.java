package org.sonar.plugins.tsql.coverage;

import java.util.Map;

import org.sonar.api.ExtensionPoint;

public interface ICoveragProvider {
	Map<String, HitLines> getHitLines();
}
