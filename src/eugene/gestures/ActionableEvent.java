package eugene.gestures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eugene.config.Config;
import eugene.preferences.SerializableToPreferences;

public class ActionableEvent implements Comparable<ActionableEvent>,
		SerializableToPreferences {
	protected static final String SETTINGS_KEY_PREFIX = "gesture_";
	private static final Map<String, ActionableEvent> fromSettingsNameMap = new HashMap<String, ActionableEvent>();
	private static List<String> settingsKeys;

	public static final ActionableEvent TAP = new ActionableEvent("Tap", "s");
	public static final ActionableEvent LONG_TAP = new ActionableEvent(
			"Long Tap", "ss");
	public static final ActionableEvent SHAKE = new ActionableEvent("Shake",
			"z");

	protected transient String toStringValue; // caches it's text representation
	protected transient String toSettingsStringValue; // caches it's text
														// representation

	public ActionableEvent(String displayName, String settingsName) {
		toStringValue = displayName;
		toSettingsStringValue = settingsName;
		if (settingsName != null) {
			fromSettingsNameMap.put(settingsName, this);
		}
	}

	@Override
	public String toString() {
		if (toStringValue == null) {
			throw new IllegalStateException("toStringValue == null");
		}
		return toStringValue;
	}

	@Override
	public String toSettingsString() {
		if (toSettingsStringValue == null) {
			throw new IllegalStateException("toSettingsStringValue == null");
		}
		return SETTINGS_KEY_PREFIX + toSettingsStringValue;
	}

	public static ActionableEvent fromSettingsString(String s) {
		s = s.substring(SETTINGS_KEY_PREFIX.length());
		ActionableEvent event = fromSettingsNameMap.get(s);
		return event != null ? event : Gesture.valueOf(s);
	}

	public static List<String> getAllSettingsKeys() {
		if (settingsKeys == null) {
			settingsKeys = new ArrayList<String>(Arrays.asList(
					SETTINGS_KEY_PREFIX + "s", SETTINGS_KEY_PREFIX + "ss",
					SETTINGS_KEY_PREFIX + "z"));
			List<String> strokes = new ArrayList<String>(Arrays.asList("u",
					"d", "l", "r"));
			fillGestureSettingKeys("",
					Config.INSTANCE.getMaximumStrokesInGesture(), settingsKeys,
					strokes);
		}
		return settingsKeys;
	}

	private static void fillGestureSettingKeys(String startingFrom,
			int maxLength, List<String> result, List<String> directions) {
		if (!startingFrom.isEmpty()) {
			result.add(SETTINGS_KEY_PREFIX + startingFrom);
		}
		if (startingFrom.length() >= maxLength) {
			return;
		}
		for (String direction : directions) {
			if (startingFrom.endsWith(direction)) {
				continue;
			}
			fillGestureSettingKeys(startingFrom + direction, maxLength, result,
					directions);
		}
	}

	@Override
	public int compareTo(ActionableEvent o) {
		return toSettingsStringValue.compareTo(o.toSettingsStringValue);
	}
}
