package eugene.gestures.listener;

import eugene.gestures.BasicGesture;

public interface GestureListener {
	boolean onGesture(BasicGesture gesture);
	boolean onMidwayGesture(BasicGesture gesture);
}
