package eugene.gestures.listener;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eugene.config.Config;
import eugene.gestures.BasicGesture;
import eugene.gestures.Stroke;
import eugene.gestures.action.Action;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;

public class ActionMapGestureListener implements GestureListener {

	private ConcurrentHashMap<BasicGesture, Action> actionMap = new ConcurrentHashMap<BasicGesture, Action>();
	private ConcurrentHashMap<BasicGesture, Action> midwayGestureActionMap = new ConcurrentHashMap<BasicGesture, Action>();
	private ShoutNotificationImpl midwayGestureNotification = new ShoutNotificationImpl(
			"");

	/**
	 * @return whether the gesture was recognized
	 */
	@Override
	public boolean onGesture(BasicGesture gesture) {
		return invokeActionFromMap(gesture, actionMap);
	}

	private boolean invokeActionFromMap(BasicGesture gesture,
			Map<BasicGesture, Action> actionMap) {
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
	public boolean onMidwayGesture(BasicGesture gesture) {
		if (invokeActionFromMap(gesture, midwayGestureActionMap)) {
			return true;
		}

		Action action = actionMap.get(gesture);
		if (action != null) {
			if (!gesture.equals(BasicGesture.valueOf(Stroke.STATIC))) {
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
	public void registerActionOnGesture(BasicGesture gesture, Action action) {
		BasicGesture oldGesture = registerGestureActionPairOnMap(gesture, action, actionMap);
		if (action instanceof AssignmentAwareAction) {
			((AssignmentAwareAction) action).onActionAssignment(gesture, oldGesture, this);
		}
	}

	@Override
	public void registerActionOnMidwayGesture(BasicGesture gesture,
			Action action) {
		registerGestureActionPairOnMap(gesture, action, midwayGestureActionMap);
	}

	public BasicGesture registerGestureActionPairOnMap(BasicGesture gesture,
			Action action, Map<BasicGesture, Action> actionMap) {
		if (gesture == null) {
			throw new IllegalArgumentException(
					"Registering action for gesture null");
		}
		if (action == null) {
			throw new IllegalArgumentException(
					"Registering a null action for gesture " + gesture);
		}
		
		BasicGesture oldGesture = null;
		if (actionMap == this.actionMap) {
			for (Entry<BasicGesture, Action> entry : actionMap.entrySet()) {
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
	public void unRegisterGesture(BasicGesture gesture) {
		actionMap.remove(gesture);
	}

	@Override
	public void unRegisterMidwayGesture(BasicGesture gesture) {
		midwayGestureActionMap.remove(gesture);
	}

}
