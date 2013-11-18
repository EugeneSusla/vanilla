package eugene.gestures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Gesture implements Cloneable, Comparable<Gesture> {
	public static final Gesture TAP = new Gesture(
			Collections.unmodifiableList(Arrays.asList(Stroke.STATIC)));
	public static final Gesture LONG_TAP = new Gesture(
			Collections.unmodifiableList(Arrays.asList(Stroke.STATIC,
					Stroke.STATIC)));

	private static ConcurrentHashMap<Stroke, Gesture> mCache = new ConcurrentHashMap<Stroke, Gesture>();

	private List<Stroke> gestureStrokes = new ArrayList<Stroke>(
			Collections.singletonList(Stroke.STATIC));
	private transient String toStringValue; // caches it's text representation
	private transient String toSettingsStringValue; // caches it's text
													// representation

	public Gesture() {
	}

	public Gesture(List<Stroke> gestureStrokes) {
		this.gestureStrokes = new ArrayList<Stroke>(gestureStrokes);
	}

	public boolean addStroke(Stroke stroke) {
		if (gestureStrokes.isEmpty()
				|| (stroke != gestureStrokes.get(gestureStrokes.size() - 1) && stroke != Stroke.STATIC)) {
			if ((gestureStrokes.size() == 1 && gestureStrokes.get(0) == Stroke.STATIC)
					|| (gestureStrokes.size() == 2 && gestureStrokes.get(1) == Stroke.STATIC)) {
				gestureStrokes.clear();
			}
			gestureStrokes.add(stroke);
			return true;
		}
		return false;
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
			if (this.equals(TAP)) {
				toStringValue = "Tap";
			} else if (this.equals(LONG_TAP)) {
				toStringValue = "Long Tap";
			} else {
				StringBuilder result = new StringBuilder();
				for (Stroke stroke : gestureStrokes) {
					result.append(stroke);
				}
				toStringValue = result.toString();
			}
		}
		return toStringValue;
	}

	public String toSettingsString() {
		if (toSettingsStringValue == null) {
			StringBuilder result = new StringBuilder();
			for (Stroke stroke : gestureStrokes) {
				result.append(stroke.toSettingsString());
			}
			toSettingsStringValue = result.toString();
		}
		return toSettingsStringValue;
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
	public Gesture clone() {
		return new Gesture(gestureStrokes);
	}

	@Override
	public int compareTo(Gesture o) {
		if (gestureStrokes.contains(Stroke.STATIC)
				&& !o.gestureStrokes.contains(Stroke.STATIC)) {
			return -1;
		}
		if (!gestureStrokes.contains(Stroke.STATIC)
				&& o.gestureStrokes.contains(Stroke.STATIC)) {
			return 1;
		}
		int sizeDiff = gestureStrokes.size() - o.gestureStrokes.size();
		if (sizeDiff != 0) {
			return sizeDiff;
		}
		for (int i = 0; i < gestureStrokes.size(); i++) {
			int strokeDiff = gestureStrokes.get(i).compareTo(
					o.gestureStrokes.get(i));
			if (strokeDiff != 0) {
				return strokeDiff;
			}
		}
		return 0;
	}
}
