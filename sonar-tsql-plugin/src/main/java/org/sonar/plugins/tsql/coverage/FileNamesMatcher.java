package org.sonar.plugins.tsql.coverage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

public class FileNamesMatcher {
	private final NameNormalizer nameNormalizer = new NameNormalizer();
	
	public CoveredLinesReport[] match(String fileName, String parentDirName, Map<String, CoveredLinesReport> coverage) {
		final String name = FilenameUtils.removeExtension(fileName);
		final String normalizedName = nameNormalizer.normalize(name);
		String schema = nameNormalizer.normalize(parentDirName);
		String objName = normalizedName;
		
		if (normalizedName.contains(".")) {
			String[] names = name.split("\\.");
			objName = names[names.length-1];
			schema = names[names.length-2];
		}
		List<CoveredLinesReport> possibleMatches = new LinkedList<CoveredLinesReport>();
		for (Entry<String, CoveredLinesReport> info : coverage.entrySet()) {
			if (info.getKey().equals(normalizedName)) {
				possibleMatches.add(info.getValue());
				break;
			}
			if (info.getKey().equals(schema+"."+objName)) {
				possibleMatches.add(info.getValue());
				break;	
			}
			if (info.getKey().contains(objName)) {
				possibleMatches.add(info.getValue());
			}
			
		}
		return possibleMatches.toArray(new CoveredLinesReport[0]);
	}
}
