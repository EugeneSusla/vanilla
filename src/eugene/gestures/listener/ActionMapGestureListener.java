package eugene.gestures.listener;

import java.util.concurrent.ConcurrentHashMap;

import eugene.config.Config;
import eugene.gestures.BasicGesture;
import eugene.gestures.action.Action;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;

public class ActionMapGestureListener implements GestureListener {

	private ConcurrentHashMap<BasicGesture, Action> actionMap = new ConcurrentHashMap<BasicGesture, Action>();
	private ShoutNotificationImpl midwayGestureNotification = new ShoutNotificationImpl("");

	/**
	 * @return whether the gesture was recognised
	 */
	@Override
	public boolean onGesture(BasicGesture gesture) {
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
	 * @return whether the gesture was recognised
	 */
	@Override
	public boolean onMidwayGesture(BasicGesture gesture) {
		Action action = actionMap.get(gesture);
		if (action != null) {
			midwayGestureNotification.setMessage(action.getDisplayName());
			midwayGestureNotification.displayNotification();
			return true;
		} else {
			Config.INSTANCE.getUnrecognisedGestureNotification().displayNotification();
			return false;
		}
	}

	@Override
	public void registerActionOnGesture(BasicGesture gesture, Action action) {
		if (gesture == null) {
			throw new IllegalArgumentException("Registering action for gesture null");
		}
		if (action == null) {
			throw new IllegalArgumentException("Registering a null action for gesture " + gesture);
		}
		actionMap.put(gesture, action);
	}

	@Override
	public void registerActionOnMidwayGesture(BasicGesture gesture,
			Action action) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
