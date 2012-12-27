package eugene.utils;

import java.io.UnsupportedEncodingException;

import eugene.config.Config;

public class StringUtils {
	private static final String CAMEL_CASE_REGEX = String.format("%s|%s|%s",
			"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])");

	private StringUtils() {
	}

	public static String camelCaseToPlainString(String camelCaseString) {
		return camelCaseString.replaceAll(CAMEL_CASE_REGEX, " ");
	}

	public static String camelCaseToUnderscoreLowerCase(String camelCaseString) {
		return camelCaseToUnderscore(camelCaseString).toLowerCase();
	}

	public static String camelCaseToUnderscoreUpperCase(String camelCaseString) {
		return camelCaseToUnderscore(camelCaseString).toUpperCase();
	}

	public static String camelCaseToUnderscore(String camelCaseString) {
		return camelCaseString.replaceAll(CAMEL_CASE_REGEX, "_");
	}

	public static String underscoreToCamelCaseFirstCapital(String lowercaseUnderscoreString) {
		String[] words = lowercaseUnderscoreString.split("_");
		StringBuilder result = new StringBuilder();
		for (String word : words) {
			if (word.isEmpty()) {
				continue;
			}
			result.append(Character.toUpperCase(word.charAt(0)));
			if (word.length() > 1) {
				result.append(word.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	public static String decode(String input) {
		// Init start
		int inputLength = input.length();
		char[] inputChars = new char[inputLength];
		input.getChars(0, inputLength, inputChars, 0);
		byte[] newBytes = new byte[inputLength];
		// Init end

		// Quick-detect a unicode string being passed by looking at middle
		// character
		if (inputLength == 0 || (((int) input.charAt(inputLength / 2)) | 0xff) != 0xff) {
			return input;
		}

		// Smart detect init start
		boolean smartDetectAdditionalLatin = Config.INSTANCE.isSmartDetectAdditionalLatin();
		boolean smartDetectFinished = false;
		float maximumNonAsciiCharacterPercentInWord = 0f;
		// consider non-ascii symbols dominating the word if beyond threshold
		float nonAsciiCharacterPercentInWordThreshold = 0.5f;
		int latinCount = 0;
		int nonAsciiCount = 0;
		int latinCountCurrentWord = 0;
		int nonAsciiCountCurrentWord = 0;
		// Smart detect init end

		for (int i = 0; i < inputLength; ++i) {
			int charNumericValue = (int) inputChars[i];
			newBytes[i] = (byte) charNumericValue;

			// verify it's not a two-byte character
			if ((charNumericValue | 0xff) != 0xff) {
				// the string is not in 1-byte encoding - abort decoding
				return input;
			}

			// Smart detect processing start
			if (smartDetectAdditionalLatin && !smartDetectFinished) {
				if ((65 <= charNumericValue && charNumericValue <= 90)
						|| (97 <= charNumericValue && charNumericValue <= 122)) {
					++latinCount;
					++latinCountCurrentWord;
				} else if ((charNumericValue & 0x80) == 0x80) {
					++nonAsciiCount;
					++nonAsciiCountCurrentWord;
				} else {
					// consider current word ended
					float currentWordRatio = ((float) nonAsciiCountCurrentWord / (float) latinCountCurrentWord);
					latinCountCurrentWord = 0;
					nonAsciiCountCurrentWord = 0;

					if (currentWordRatio > maximumNonAsciiCharacterPercentInWord) {
						maximumNonAsciiCharacterPercentInWord = currentWordRatio;
						if (maximumNonAsciiCharacterPercentInWord > nonAsciiCharacterPercentInWordThreshold) {
							smartDetectFinished = true;
						}
					}
				}
			}
			// Smart detect processing end
		}
		try {
			if (!smartDetectAdditionalLatin
					|| nonAsciiCount > latinCount
					|| maximumNonAsciiCharacterPercentInWord > nonAsciiCharacterPercentInWordThreshold) {
				return new String(newBytes, Config.INSTANCE.getDefaultCharset());
			} else {
				// Latin characters dominate - treat remaining as iso-8859-1
				// chars - no need to convert
				return input;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
