package eugene.gestures.listener;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eugene.config.Config;
import eugene.gestures.Gesture;
import eugene.gestures.Stroke;
import eugene.gestures.action.Action;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;

public class ActionMapGestureListener implements GestureListener {

	private ConcurrentHashMap<Gesture, Action> actionMap = new ConcurrentHashMap<Gesture, Action>();
	private ConcurrentHashMap<Gesture, Action> midwayGestureActionMap = new ConcurrentHashMap<Gesture, Action>();
	private ShoutNotificationImpl midwayGestureNotification = new ShoutNotificationImpl(
			"");

	/**
	 * @return whether the gesture was recognized
	 */
	@Override
	public boolean onGesture(Gesture gesture) {
		return invokeActionFromMap(gesture, actionMap);
	}

	private boolean invokeActionFromMap(Gesture gesture,
			Map<Gesture, Action> actionMap) {
		Action action = actionMap.get(gesture);
		if (action == null) {
			return false;
		}
		ShoutNotification resultNotification = action.invoke();
		if (resultNotification != null) {
			resultNotification.displayNotification();
		}
		return true;
	}

	/**
	 * @return whether the gesture was recognized
	 */
	@Override
	public boolean onMidwayGesture(Gesture gesture) {
		if (invokeActionFromMap(gesture, midwayGestureActionMap)) {
			return true;
		}

		Action action = actionMap.get(gesture);
		if (action != null) {
			if (!gesture.equals(Gesture.valueOf(Stroke.STATIC))) {
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
	public void registerActionOnGesture(Gesture gesture, Action action) {
		Gesture oldGesture = registerGestureActionPairOnMap(gesture, action, actionMap);
		if (action instanceof AssignmentAwareAction) {
			((AssignmentAwareAction) action).onActionAssignment(gesture, oldGesture, this);
		}
	}

	@Override
	public void registerActionOnMidwayGesture(Gesture gesture,
			Action action) {
		registerGestureActionPairOnMap(gesture, action, midwayGestureActionMap);
	}

	public Gesture registerGestureActionPairOnMap(Gesture gesture,
			Action action, Map<Gesture, Action> actionMap) {
		if (gesture == null) {
			throw new IllegalArgumentException(
					"Registering action for gesture null");
		}
		if (action == null) {
			throw new IllegalArgumentException(
					"Registering a null action for gesture " + gesture);
		}
		
		Gesture oldGesture = null;
		if (actionMap == this.actionMap) {
			for (Entry<Gesture, Action> entry : actionMap.entrySet()) {
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
	public void unRegisterGesture(Gesture gesture) {
		actionMap.remove(gesture);
	}

	@Override
	public void unRegisterMidwayGesture(Gesture gesture) {
		midwayGestureActionMap.remove(gesture);
	}

}
