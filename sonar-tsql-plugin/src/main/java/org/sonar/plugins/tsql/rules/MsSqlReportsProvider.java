package org.sonar.plugins.tsql.rules;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MsSqlReportsProvider implements IReporsProvider {

	private final String name;

	public MsSqlReportsProvider(final String name) {
		this.name = name;
	}

	@Override
	public List<File> get(String baseDir) {
		List<File> res = new ArrayList<>();
		List<File> files = listf(baseDir);
		for (File f : files) {

			if (f.getName().toLowerCase().endsWith(this.name)) {
				res.add(f);
			}
		}
		return res;
	}

	private static List<File> listf(String directoryName) {
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		if (!directory.exists()) {
			return resultList;
		}
		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (File file : fList) {
			if (file.isDirectory()) {
				resultList.addAll(listf(file.getAbsolutePath()));
			}
		}
		return resultList;
	}

}
