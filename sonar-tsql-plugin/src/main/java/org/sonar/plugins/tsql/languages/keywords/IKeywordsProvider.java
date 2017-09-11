package org.sonar.plugins.tsql.languages.keywords;

public interface IKeywordsProvider {
	boolean isKeyword(final String name);
}
