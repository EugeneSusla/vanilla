package eugene.gestures.action;

import eugene.gestures.notification.ShoutNotification;

public interface Action {
	ShoutNotification invoke();
	String getDisplayName();
	/**
	 * Used to store action in settings
	 */
	String getSettingsName();
}
