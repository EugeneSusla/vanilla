package eugene.utils;

import java.io.File;
import java.util.List;

import eugene.config.Config;

import android.database.DatabaseUtils;

public class FolderFilteringUtils {
	private FolderFilteringUtils() {
	}
	
	public static boolean accept(File dir, String filename) {
		String fullName = dir.getAbsolutePath() + File.separator + filename;
		
		// Show only user-included files
		boolean isInIncludedFolders = false;
		List<String> includeFolders = Config.INSTANCE.getIncludeFolders();
		for (String term : includeFolders) {
			if (term.contains(fullName) || fullName.contains(term)) {
				isInIncludedFolders = true;
				break;
			}
		}
		if (!isInIncludedFolders && isListNotEmpty(includeFolders)) {
			return false;
		}

		// Hide user-excluded files
		for (String term : Config.INSTANCE.getExcludeFolders()) {
			if (fullName.contains(term)) {
				return false;
			}
		}
		
		return true;
	}

	public static String getFolderFilterSQLPart() {
		List<String> includeFolders = Config.INSTANCE.getIncludeFolders();
		List<String> excludeFolders = Config.INSTANCE.getExcludeFolders();

		StringBuilder result = new StringBuilder();

		boolean includeFoldersPresent = isListNotEmpty(includeFolders);
		if (includeFoldersPresent) {
			addFolderFilterToQuery(result, includeFolders, "OR", "GLOB");
		}

		if (isListNotEmpty(excludeFolders)) {
			if (includeFoldersPresent) {
				result.append(" AND ");
			}
			addFolderFilterToQuery(result, excludeFolders, "AND", "NOT GLOB");
		}

		return result.toString();
	}

	private static boolean isListNotEmpty(List<?> list) {
		return list != null && !list.isEmpty();
	}

	private static void addFolderFilterToQuery(StringBuilder query,
			List<String> folderList, String operandBetween, String mainOperand) {
		String operandBetweenWithSpaces = " " + operandBetween + " ";
		query.append("(");
		for (String term : folderList) {
			query.append("_data " + mainOperand + " ");
			DatabaseUtils.appendEscapedSQLString(query, term + "*");
			query.append(operandBetweenWithSpaces);
		}
		int queryLength = query.length();
		query.delete(queryLength - operandBetweenWithSpaces.length(),
				queryLength);
		query.append(")");
	}
}
