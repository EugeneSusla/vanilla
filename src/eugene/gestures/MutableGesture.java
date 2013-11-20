package eugene.gestures;

import java.util.Collections;

public class MutableGesture extends Gesture {
	
	public MutableGesture() {
		super(Collections.<Stroke> emptyList());
	}
	
	public MutableGesture(Gesture gesture) {
		super(gesture.gestureStrokes);
	}
	
	public static MutableGesture formActionableEvent(ActionableEvent e) {
		return new MutableGesture(Gesture.valueOf(e.toSettingsStringValue));
	}
	
	public boolean addStroke(Stroke stroke) {
		if (gestureStrokes.isEmpty()
				|| (stroke != gestureStrokes.get(gestureStrokes.size() - 1) && stroke != Stroke.STATIC)) {
			if ((gestureStrokes.size() == 1 && gestureStrokes.get(0) == Stroke.STATIC)
					|| (gestureStrokes.size() == 2 && gestureStrokes.get(1) == Stroke.STATIC)) {
				gestureStrokes.clear();
			}
			gestureStrokes.add(stroke);
			toStringValue = null;
			toSettingsStringValue = null;
			return true;
		}
		return false;
	}
}
