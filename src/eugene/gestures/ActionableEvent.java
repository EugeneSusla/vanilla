package eugene.gestures;

import java.util.HashMap;
import java.util.Map;

import eugene.preferences.SerializableToPreferences;

public class ActionableEvent implements Cloneable, Comparable<ActionableEvent>, SerializableToPreferences {
	private static Map<String, ActionableEvent> fromSettingsNameMap = new HashMap<String, ActionableEvent>();
	public static final ActionableEvent TAP = new ActionableEvent("Tap", "s");
	public static final ActionableEvent LONG_TAP = new ActionableEvent("Long Tap", "ss");
	public static final ActionableEvent SHAKE = new ActionableEvent("Shake", "z");
	
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
		return toSettingsStringValue;
	}
	
	public static ActionableEvent fromSettingsString(String s) {
		ActionableEvent event = fromSettingsNameMap.get(s);
		return event != null ? event : Gesture.valueOf(s);
	}
	
	@Override
	public int compareTo(ActionableEvent o) {
		return toSettingsStringValue.compareTo(o.toSettingsStringValue);
	}
}
