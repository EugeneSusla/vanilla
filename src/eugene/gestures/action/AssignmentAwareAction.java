package eugene.gestures.action;

import eugene.gestures.BasicGesture;
import eugene.gestures.listener.GestureListener;

public interface AssignmentAwareAction extends Action {
	void onActionAssignment(BasicGesture gesture, BasicGesture oldGesture, GestureListener gestureListener);
}
