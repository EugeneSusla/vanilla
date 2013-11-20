package eugene.gestures.listener;

import eugene.gestures.ActionableEvent;
import eugene.gestures.action.Action;

public interface GestureListener {
	void registerActionOnGesture(ActionableEvent gesture, Action action);
	void registerActionOnMidwayGesture(ActionableEvent gesture, Action action);
	void unRegisterGesture(ActionableEvent gesture);
	void unRegisterMidwayGesture(ActionableEvent gesture);

	/**
	 * @return whether the gesture was recognized
	 */
	boolean onGesture(ActionableEvent gesture);

	/**
	 * Invoked when the used is in the middle of esture, and current gesture
	 * changes, i.e. gets new stroke
	 * 
	 * @return whether the gesture was recognized
	 */
	boolean onMidwayGesture(ActionableEvent gesture);
}
