package eugene.gestures.listener;

import android.util.Log;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eugene.config.Config;
import eugene.gestures.ActionableEvent;
import eugene.gestures.Stroke;
import eugene.gestures.action.Action;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.MutableTextNotification;

public class ActionMapGestureListener implements GestureListener {
    private static final String TAG = ActionMapGestureListener.class.getSimpleName();

	private ConcurrentHashMap<ActionableEvent, Action> actionMap = new ConcurrentHashMap<ActionableEvent, Action>();
	private ConcurrentHashMap<ActionableEvent, Action> midwayGestureActionMap = new ConcurrentHashMap<ActionableEvent, Action>();
	private MutableTextNotification midwayGestureNotification = new MutableTextNotification(
			"");

	@Override
	public boolean onGesture(ActionableEvent gesture) {
		return invokeActionFromMap(gesture, actionMap);
	}

	private boolean invokeActionFromMap(ActionableEvent gesture,
			Map<ActionableEvent, Action> actionMap) {
        Log.i(TAG, "Received gesture: " + gesture);
        Action action = actionMap.get(gesture);
        if (action == null) {
            Log.i(TAG, "No action to invoke");
            return false;
        }
        Log.i(TAG, "Invoking action: " + action);
		ShoutNotification resultNotification = action.invoke();
		if (resultNotification != null) {
			resultNotification.displayNotification();
		}
		return true;
	}

	@Override
	public boolean onMidwayGesture(ActionableEvent gesture) {
        Log.i(TAG, "Incoming midway gesture: " + gesture);
		if (invokeActionFromMap(gesture, midwayGestureActionMap)) {
            return true;
        }

        Log.i(TAG, "No action specified, looking up midway notification...");
        Action action = actionMap.get(gesture);
		if (action != null) {
			if (!gesture.equals(ActionableEvent.TAP)) {
				midwayGestureNotification.setMessage(action.getDisplayName());
				midwayGestureNotification.displayNotification();
			}
			return true;
		} else {
			Config.INSTANCE.getUnrecognisedGestureNotification()
					.displayNotification();
			return false;
		}
	}

	@Override
	public void registerActionOnGesture(ActionableEvent gesture, Action action) {
		ActionableEvent oldGesture = registerGestureActionPairOnMap(gesture, action, actionMap);
		if (action instanceof AssignmentAwareAction) {
			((AssignmentAwareAction) action).onActionAssignment(gesture, oldGesture, this);
		}
	}

	@Override
	public void registerActionOnMidwayGesture(ActionableEvent gesture,
			Action action) {
		registerGestureActionPairOnMap(gesture, action, midwayGestureActionMap);
	}

	public ActionableEvent registerGestureActionPairOnMap(ActionableEvent gesture,
			Action action, Map<ActionableEvent, Action> actionMap) {
		if (gesture == null) {
			throw new IllegalArgumentException(
					"Registering action for gesture null");
		}
		if (action == null) {
			throw new IllegalArgumentException(
					"Registering a null action for gesture " + gesture);
		}
		
		ActionableEvent oldGesture = null;
		if (actionMap == this.actionMap) {
			for (Entry<ActionableEvent, Action> entry : actionMap.entrySet()) {
				if (entry.getValue().equals(action)) {
					oldGesture = entry.getKey();
					break;
				}
			}
		}
		actionMap.put(gesture, action);
		return oldGesture;
	}

	@Override
	public void unRegisterGesture(ActionableEvent gesture) {
		actionMap.remove(gesture);
	}

	@Override
	public void unRegisterMidwayGesture(ActionableEvent gesture) {
		midwayGestureActionMap.remove(gesture);
	}

}
