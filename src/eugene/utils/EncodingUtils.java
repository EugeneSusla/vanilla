package eugene.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EncodingUtils {

	public static final Set<String> iso8859_1compatibleCharsets = new HashSet<String>(
			Arrays.asList("ISO-8859-1", "ISO-IR-100", "CSISOLATIN1", "LATIN1",
					"L1", "IBM819", "CP819", "ASCII", "UTF-8", "UTF8", "UTF-16", "UTF16"));

	private EncodingUtils() {
	}

	public static boolean shouldConvertFromEncoding(String charset) {
		return !iso8859_1compatibleCharsets.contains(charset.toUpperCase());
	}
}
