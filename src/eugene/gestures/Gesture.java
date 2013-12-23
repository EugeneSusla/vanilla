package eugene.gestures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Gesture extends ActionableEvent {
	private static ConcurrentHashMap<Stroke, Gesture> mCache = new ConcurrentHashMap<Stroke, Gesture>();

	protected List<Stroke> gestureStrokes = new ArrayList<Stroke>(
			Collections.singletonList(Stroke.STATIC));

	public Gesture(List<Stroke> gestureStrokes) {
		super(null, null);
		this.gestureStrokes = new ArrayList<Stroke>(gestureStrokes);
	}

	public static Gesture valueOf(String strokes) {
		List<Stroke> strokeObjects = new ArrayList<Stroke>();
		for (int i = 0; i < strokes.length(); i++) {
			strokeObjects.add(Stroke.fromChar(strokes.charAt(i)));
		}
		return valueOf(strokeObjects.toArray(new Stroke[strokeObjects.size()]));
	}

	public static Gesture valueOf(Stroke... strokes) {
		if (strokes.length > 1 || strokes.length == 0) {
			return createNewGesture(strokes);
		}
		Gesture cachedGesture = mCache.get(strokes[0]);
		if (cachedGesture == null) {
			cachedGesture = createNewGesture(strokes);
			mCache.putIfAbsent(strokes[0], cachedGesture);
		}
		return cachedGesture;
	}

	private static Gesture createNewGesture(Stroke... strokes) {
		return new Gesture(Arrays.asList(strokes));
	}

	@Override
	public String toString() {
		if (toStringValue == null) {
			StringBuilder result = new StringBuilder();
			for (Stroke stroke : gestureStrokes) {
				result.append(stroke);
			}
			toStringValue = result.toString();
		}
		return toStringValue;
	}

	@Override
	public String toSettingsString() {
		if (toSettingsStringValue == null) {
			StringBuilder result = new StringBuilder();
			for (Stroke stroke : gestureStrokes) {
				result.append(stroke.toSettingsString());
			}
			toSettingsStringValue = result.toString();
		}
		return SETTINGS_KEY_PREFIX + toSettingsStringValue;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Gesture)) {
			return false;
		}
		Gesture other = (Gesture) o;
		return gestureStrokes.equals(other.gestureStrokes);
	}

	@Override
	public int hashCode() {
		return gestureStrokes.hashCode();
	}

	@Override
	public int compareTo(ActionableEvent o) {
		if (!(o instanceof Gesture)) {
			return 1;
		}
		
		Gesture g = (Gesture) o;
		int sizeDiff = gestureStrokes.size() - g.gestureStrokes.size();
		if (sizeDiff != 0) {
			return sizeDiff;
		}
		for (int i = 0; i < gestureStrokes.size(); i++) {
			int strokeDiff = gestureStrokes.get(i).compareTo(
					g.gestureStrokes.get(i));
			if (strokeDiff != 0) {
				return strokeDiff;
			}
		}
		return 0;
	}
}
