package eugene.gestures.action;

import eugene.gestures.ActionableEvent;
import eugene.gestures.listener.GestureListener;

public interface AssignmentAwareAction extends Action {
	void onActionAssignment(ActionableEvent gesture, ActionableEvent oldGesture, GestureListener gestureListener);
}
