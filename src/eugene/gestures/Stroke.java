package eugene.gestures;

import java.util.HashMap;
import java.util.Map;

import eugene.config.Config;
import eugene.preferences.SerializableToPreferences;

public enum Stroke implements SerializableToPreferences {
	STATIC("\u2022"),
	UP("\u2191"),
	DOWN("\u2193"),
	LEFT("\u2190"),
	RIGHT("\u2192");
	
	Stroke(String toString) {
		this.toString = toString;
		settingsChar = Character.toLowerCase(name().charAt(0));
	}
	private String toString;
	private char settingsChar;
	
	private static Map<Character, Stroke> charMap = new HashMap<Character, Stroke>();
	
	@Override
	public String toString() {
		return toString;
	}
	
	@Override
	public String toSettingsString() {
		return Character.toString(settingsChar);
	}
	
	public static Stroke fromChar(char c) {
		if (charMap.isEmpty()) {
			for (Stroke s : values()) {
				charMap.put(s.settingsChar, s);
				charMap.put(Character.toLowerCase(s.toString.charAt(0)), s);
			}
		}
		return charMap.get(Character.toLowerCase(c));
	}
	
	public static Stroke fromDeltas(float deltaX, float deltaY) {
		float deltaXAbs = Math.abs(deltaX);
		float deltaYAbs = Math.abs(deltaY);
		
		int strokeMinPixelThreshold = Config.INSTANCE.getGestureStrokeMinPixelThreshold();
		if (deltaXAbs < strokeMinPixelThreshold && deltaYAbs < strokeMinPixelThreshold) {
			return STATIC;
		}
		
		if (deltaXAbs > deltaYAbs) {
			return (deltaX < 0 ? RIGHT : LEFT);
		} else {
			return (deltaY < 0 ? DOWN : UP);
		}
	}
}
