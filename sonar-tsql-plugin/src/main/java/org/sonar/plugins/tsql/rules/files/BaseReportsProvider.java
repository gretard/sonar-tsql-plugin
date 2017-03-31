package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseReportsProvider implements IReporsProvider {

	private final String baseDir;

	private final String searchName;

	public BaseReportsProvider(final String baseDir, final String searchName) {
		this.baseDir = baseDir;
		this.searchName = searchName.toLowerCase();
	}

	@Override
	public File[] get() {
		final List<File> res = new ArrayList<>();
		final List<File> files = listf(this.baseDir);
		for (final File f : files) {
			if (f.getName().toLowerCase().endsWith(this.searchName)) {
				res.add(f);
			}
		}
		return res.toArray(new File[0]);
	}

	private static List<File> listf(final String directoryName) {
		final File directory = new File(directoryName);

		final List<File> resultList = new ArrayList<File>();

		if (!directory.exists()) {
			return resultList;
		}

		final File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (final File file : fList) {
			if (file.isDirectory()) {
				resultList.addAll(listf(file.getAbsolutePath()));
			}
		}
		return resultList;
	}

}
