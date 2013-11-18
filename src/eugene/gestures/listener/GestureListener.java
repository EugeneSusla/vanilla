package eugene.gestures.listener;

import eugene.gestures.Gesture;
import eugene.gestures.action.Action;

public interface GestureListener {
	void registerActionOnGesture(Gesture gesture, Action action);
	void registerActionOnMidwayGesture(Gesture gesture, Action action);
	void unRegisterGesture(Gesture gesture);
	void unRegisterMidwayGesture(Gesture gesture);
	
	boolean onGesture(Gesture gesture);
	boolean onMidwayGesture(Gesture gesture);
}
