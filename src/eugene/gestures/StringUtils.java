package eugene.gestures;

public class StringUtils {
	private StringUtils() {
	}

	public static String camelCaseToPlainString(String camelCaseString) {
		return camelCaseString.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
}
