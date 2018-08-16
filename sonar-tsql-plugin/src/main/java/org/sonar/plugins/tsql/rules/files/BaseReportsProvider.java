package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class BaseReportsProvider implements IReportsProvider {

	private final String searchName;

	public BaseReportsProvider(final String searchName) {
		this.searchName = searchName.toLowerCase();
	}

	@Override
	public File[] get(final String baseDir) {
		if (StringUtils.isEmpty(this.searchName)) {
			return new File[0];
		}

		final List<File> res = new ArrayList<>();
		final List<File> files = listf(baseDir);
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
