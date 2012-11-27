package eugene.gestures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import eugene.gestures.notification.Shouter;

public class BasicGesture implements Cloneable {
	public static final BasicGesture TAP = new BasicGesture(
			Collections.unmodifiableList(Arrays.asList(Stroke.STATIC)));
	public static final BasicGesture LONG_TAP = new BasicGesture(
			Collections.unmodifiableList(Arrays.asList(Stroke.STATIC,
					Stroke.STATIC)));


	private static ConcurrentHashMap<Stroke, BasicGesture> mCache = new ConcurrentHashMap<Stroke, BasicGesture>();

	private List<Stroke> gestureStrokes = new ArrayList<Stroke>(
			Collections.singletonList(Stroke.STATIC));

	public BasicGesture() {
	}

	public BasicGesture(List<Stroke> gestureStrokes) {
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

	public static BasicGesture valueOf(Stroke... strokes) {
		if (strokes.length > 1 || strokes.length == 0) {
			return createNewGesture(strokes);
		}
		BasicGesture cachedGesture = mCache.get(strokes[0]);
		if (cachedGesture == null) {
			cachedGesture = createNewGesture(strokes);
			mCache.putIfAbsent(strokes[0], cachedGesture);
		}
		return cachedGesture;
	}

	private static BasicGesture createNewGesture(Stroke... strokes) {
		return new BasicGesture(Arrays.asList(strokes));
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Stroke stroke : gestureStrokes) {
			result.append(Character.toUpperCase(stroke.name().charAt(0)));
		}
		return result.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BasicGesture)) {
			return false;
		}
		BasicGesture other = (BasicGesture) o;
		return gestureStrokes.equals(other.gestureStrokes);
	}

	@Override
	public int hashCode() {
		return gestureStrokes.hashCode();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new BasicGesture(gestureStrokes);
	}
}
