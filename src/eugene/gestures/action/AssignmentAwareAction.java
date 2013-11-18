package eugene.gestures.action;

import eugene.gestures.Gesture;
import eugene.gestures.listener.GestureListener;

public interface AssignmentAwareAction extends Action {
	void onActionAssignment(Gesture gesture, Gesture oldGesture, GestureListener gestureListener);
}
