package org.sonar.plugins.tsql.coverage;

public class NameNormalizer {
	public String normalize(String name) {
		if (name == null) {
			return "";
		}
		return name.replace("[", "").replace("]", "").replace(" ", "").toLowerCase();
	}
}
