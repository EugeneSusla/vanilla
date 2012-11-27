package eugene.gestures.listener;

import eugene.gestures.BasicGesture;
import eugene.gestures.action.Action;

public interface GestureListener {
	void registerActionOnGesture(BasicGesture gesture, Action action);
	void registerActionOnMidwayGesture(BasicGesture gesture, Action action);
	void unRegisterGesture(BasicGesture gesture);
	void unRegisterMidwayGesture(BasicGesture gesture);
	
	boolean onGesture(BasicGesture gesture);
	boolean onMidwayGesture(BasicGesture gesture);
}
